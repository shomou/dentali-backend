package com.dentali.dentali_rest.historiaclinica;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.dentali.dentali_rest.catalogos.HallazgoAtmRepository;
import com.dentali.dentali_rest.catalogos.Parentesco;
import com.dentali.dentali_rest.catalogos.AuxiliarHigiene;
import com.dentali.dentali_rest.catalogos.AuxiliarHigieneRepository;
import com.dentali.dentali_rest.catalogos.Enfermedad;
import com.dentali.dentali_rest.catalogos.EnfermedadRepository;
import com.dentali.dentali_rest.catalogos.EstadoPeriodontal;
import com.dentali.dentali_rest.catalogos.EstadoPeriodontalRepository;
import com.dentali.dentali_rest.catalogos.HallazgoAtm;
import com.dentali.dentali_rest.catalogos.ParentescoRepository;
import com.dentali.dentali_rest.common.exception.RecursoNoEncontradoException;
import com.dentali.dentali_rest.common.exception.ReglaNegocioException;
import com.dentali.dentali_rest.doctores.Doctor;
import com.dentali.dentali_rest.doctores.DoctorRepository;
import com.dentali.dentali_rest.doctores.EstadoDoctor;
import com.dentali.dentali_rest.historiaclinica.dto.HeredoFamiliarRequestDTO;
import com.dentali.dentali_rest.historiaclinica.dto.HistoriaClinicaRequestDTO;
import com.dentali.dentali_rest.historiaclinica.dto.HistoriaClinicaResponseDTO;
import com.dentali.dentali_rest.historiaclinica.mapper.HistoriaClinicaMapper;
import com.dentali.dentali_rest.pacientes.EstadoPaciente;
import com.dentali.dentali_rest.pacientes.Paciente;
import com.dentali.dentali_rest.pacientes.PacienteRepository;
import com.dentali.dentali_rest.pacientes.Sexo;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
@Transactional (readOnly = true)
public class HistoriaClinicaService {

    // Ids de catálogo con significado especial (ver V7). Constantes, no "números mágicos".
    private static final Short HALLAZGO_ATM_SANA = 1;
    private static final Short ESTADO_PERIODONTAL_SANO = 1;

    // --- Dependencias (inyectadas por constructor gracias a @RequiredArgsConstructor)  -------
    private final HistoriaClinicaRepository historiaClinicaRepository;
    private final PacienteRepository pacienteRepository;
    private final DoctorRepository doctorRepository;
    private final HallazgoAtmRepository hallazgoAtmRepository;
    private final EnfermedadRepository enfermedadRepository;
    private final EstadoPeriodontalRepository estadoPeriodontalRepository;
    private final AuxiliarHigieneRepository auxiliarHigieneRepository;
    private final ParentescoRepository parentescoRepository;
    private final HistoriaClinicaMapper historiaClinicaMapper;

    
    // ---------------- Consultas -------------------
    public HistoriaClinicaResponseDTO obtener(Long pacienteId) {
        HistoriaClinica historia = historiaClinicaRepository.findByPacienteId(pacienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Historia clínica del paciente", pacienteId));
        
        return historiaClinicaMapper.toResponse(historia);
    }


    // ---------------- Escrituras -------------------
    @Transactional 
    public HistoriaClinicaResponseDTO crear(Long pacienteId, HistoriaClinicaRequestDTO dto){
        // Validaciones        
        Paciente paciente = buscarPacienteActivo(pacienteId);

        if(historiaClinicaRepository.existsByPacienteId(pacienteId)){
            throw new ReglaNegocioException(
                "El paciente con id " + pacienteId + " ya tiene historia clínica");
        }

        Doctor doctor = buscarDoctorActivo(dto.doctorAperturaId());

        validarSexo(paciente, dto);

        // Campos simples con el mapper
        HistoriaClinica historia = historiaClinicaMapper.toEntity(dto);

        // Relaciones que el mapper ignora
        historia.setPaciente(paciente);
        historia.setDoctorApertura(doctor);
        llenarColecciones(historia, dto);

        return historiaClinicaMapper.toResponse(historiaClinicaRepository.save(historia));

    }

    @Transactional 
    public HistoriaClinicaResponseDTO actualizar(Long pacienteId, HistoriaClinicaRequestDTO dto){
        Paciente paciente = buscarPacienteActivo(pacienteId);
        HistoriaClinica historia = historiaClinicaRepository.findByPacienteId(pacienteId)
            .orElseThrow(() -> new RecursoNoEncontradoException("Historia clínica del paciente", pacienteId));
        
            validarSexo(paciente, dto);

            historiaClinicaMapper.actualizarEntidad(dto, historia);
            vaciarColecciones(historia);
            llenarColecciones(historia, dto);

            historiaClinicaRepository.flush();
            
            return historiaClinicaMapper.toResponse(historia);
    }

    // ----------- Métodos Privados Helpers ------------------------------
    private Paciente buscarPacienteActivo(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente", id));
        
        if(paciente.getEstado() != EstadoPaciente.ACTIVO){
            throw new ReglaNegocioException("El paciente con id "+ id + " está desactivado");
        }
        return paciente;
    }

    private Doctor buscarDoctorActivo(Long id){
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Doctor", id));
        
        if(doctor.getEstado() != EstadoDoctor.ACTIVO){
            throw new ReglaNegocioException("El doctor con id "+ id + " está desactivado");
        }

        return doctor;
    }

    private void validarSexo(Paciente paciente, HistoriaClinicaRequestDTO dto){
        if(paciente.getSexo() == Sexo.H
                && (Boolean.TRUE.equals(dto.embarazada()) || Boolean.TRUE.equals(dto.lactando()))){
            throw new ReglaNegocioException("Embarazo y lactancia no aplican para pacientes de Sexo H");
        }
    }


    // --------------- Ayudantes: colecciones ----------------
    /** Agregar las colecciones administradas (nunca setX(new ...)). Sirve también para actualizar. */
    private void llenarColecciones(HistoriaClinica historia, HistoriaClinicaRequestDTO dto){
        // Antecedentes personales Map<idEnfermedad, padece>
        Map<Short, Boolean> personales = nuloAVacio(dto.antecedentesPersonales());
        for (Enfermedad e : buscarTodos(enfermedadRepository, personales.keySet(), Enfermedad::getId, "Enfermedad")) {
            if (!e.isAplicaPersonal()) {
                throw new ReglaNegocioException("'" + e.getNombre() + "' no aplica como antecedente personal");
            }
            historia.getAntecedentesPersonales().put(e, personales.get(e.getId()));
        }

        // Heredofamiliares: pares (enfermedad, parentesco)
        List<HeredoFamiliarRequestDTO> heredo = nuloAVacio(dto.antecedentesHeredofamiliares());
        Map<Short, Enfermedad> enfermedades = porId(buscarTodos(enfermedadRepository,
                heredo.stream().map(h -> h.enfermedadId()).collect(Collectors.toSet()),
                Enfermedad::getId, "Enfermedad"), Enfermedad::getId);
        Map<Short, Parentesco> parentescos = porId(buscarTodos(parentescoRepository,
                heredo.stream().map(h -> h.parentescoId()).collect(Collectors.toSet()),
                Parentesco::getId, "Parentesco"), Parentesco::getId);
        
        
        for (HeredoFamiliarRequestDTO h : heredo) {
            Enfermedad e = enfermedades.get(h.enfermedadId());
            if (!e.isAplicaHeredofamiliar()) {
                throw new ReglaNegocioException("'" + e.getNombre() + "' no aplica como antecedente heredofamiliar");
            }
            historia.getAntecedentesHeredofamiliares()
                    .add(new AntecedenteHeredofamiliar(e, parentescos.get(h.parentescoId())));
        }

        // Seleccion multiple simple
        historia.getAuxiliaresHigiene().addAll(buscarTodos(auxiliarHigieneRepository,
                nuloAVacio(dto.auxiliaresHigieneIds()), AuxiliarHigiene::getId, "Auxiliar de higiene"));

        Set<Short> hallazgosIds = nuloAVacio(dto.hallazgosAtmIds());
        validarExclusivo(hallazgosIds, HALLAZGO_ATM_SANA, "ATM 'Sana'");
        historia.getHallazgosAtm().addAll(buscarTodos(hallazgoAtmRepository,
                hallazgosIds, HallazgoAtm::getId, "Hallazgo ATM"));

        Set<Short> periodontalIds = nuloAVacio(dto.estadosPeriodontalesIds());
        validarExclusivo(periodontalIds, ESTADO_PERIODONTAL_SANO, "Estado periodontal 'Sano'");
        historia.getEstadosPeriodontales().addAll(buscarTodos(estadoPeriodontalRepository,
                periodontalIds, EstadoPeriodontal::getId, "Estado periodontal"));
    }

    /** "Sana"/"Sano" no se puede combinar con ningún otro hallazgo. */
    private void validarExclusivo(Set<Short> ids, Short idExclusivo, String nombre) {
        if (ids.contains(idExclusivo) && ids.size() > 1) {
            throw new ReglaNegocioException(nombre + " no se puede combinar con otros hallazgos");
        }
    }


    // --------------- Ayudantes genéricos ----------------

    /** findAllById + 404 si falta alguno (pregunta 2 / mini-reto). */
    private <T> List<T> buscarTodos(JpaRepository<T, Short> repo, Collection<Short> ids,
                                    Function<T, Short> obtenerId, String recurso) {
        if (ids.isEmpty()) {
            return List.of();
        }
        List<T> encontrados = repo.findAllById(ids);
        if (encontrados.size() != ids.size()) {
            Set<Short> faltantes = new HashSet<>(ids);
            encontrados.forEach(t -> faltantes.remove(obtenerId.apply(t)));
            throw new RecursoNoEncontradoException(recurso + " no existe: ids " + faltantes);
        }
        return encontrados;
    }

    private <T> Map<Short, T> porId(List<T> lista, Function<T, Short> obtenerId) {
        return lista.stream().collect(Collectors.toMap(obtenerId, t -> t));
    }

    private static <K, V> Map<K, V> nuloAVacio(Map<K, V> m) { return m == null ? Map.of() : m; }
    private static <T> List<T> nuloAVacio(List<T> l)        { return l == null ? List.of() : l; }
    private static <T> Set<T> nuloAVacio(Set<T> s)          { return s == null ? Set.of() : s; }

    /** Vacía las colecciones administradas (sin reemplazarlas) para volver a llenarlas. */
    private void vaciarColecciones(HistoriaClinica historia) {
        historia.getAntecedentesPersonales().clear();
        historia.getAntecedentesHeredofamiliares().clear();
        historia.getAuxiliaresHigiene().clear();
        historia.getHallazgosAtm().clear();
        historia.getEstadosPeriodontales().clear();
    }

}
