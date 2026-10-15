package com.dentali.dentali_rest.doctores;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dentali.dentali_rest.common.exception.RecursoNoEncontradoException;
import com.dentali.dentali_rest.common.exception.ReglaNegocioException;
import com.dentali.dentali_rest.doctores.dto.DoctorRequestDTO;
import com.dentali.dentali_rest.doctores.dto.DoctorResponseDTO;
import com.dentali.dentali_rest.doctores.dto.EspecialidadAsignadaRequestDTO;
import com.dentali.dentali_rest.doctores.mapper.DoctorMapper;
import com.dentali.dentali_rest.especialidades.Especialidad;
import com.dentali.dentali_rest.especialidades.EspecialidadRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
@Transactional(readOnly = true)
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final EspecialidadRepository especialidadRepository;
    private final DoctorMapper doctorMapper;

    //----------------- Consultas -----------------//
    public List<DoctorResponseDTO> listarArchivos(){
        return doctorRepository.findByEstado(EstadoDoctor.ACTIVO)
            .stream()
            .map(doctorMapper::toResponse)
            .toList();
    }

    public DoctorResponseDTO obtenerPorId(Long id){
        return doctorMapper.toResponse(buscarDoctor(id));
    }

    // ----------------- Escrituras -----------------//
    @Transactional
    public DoctorResponseDTO crear(Long id, DoctorRequestDTO dto){
        validarDuplicados(dto, id);

        Doctor doctor = doctorMapper.toEntity(dto);

        sincronizarEspecialidades(doctor, dto.especialidades());

        return doctorMapper.toResponse(doctor);

    }

    //----------------- Helpers Privados -----------------//
    private Doctor buscarDoctor(Long id){
        return doctorRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Doctor: ", id));
    }

    private void validarDuplicados(DoctorRequestDTO dto, Long idActual) {
        // Los IDs empiezan en 1, así que 0 nunca coincide: sirve para "doctor nuevo"
        Long excluir = (idActual == null) ? 0L : idActual;

        if (doctorRepository.existsByCurpAndIdNot(dto.curp(), excluir)) {
            throw new ReglaNegocioException("Ya existe un doctor con la CURP " + dto.curp());
        }
        if (doctorRepository.existsByEmailAndIdNot(dto.email(), excluir)) {
            throw new ReglaNegocioException("Ya existe un doctor con el email " + dto.email());
        }
        if (doctorRepository.existsByCedulaProfesionalAndIdNot(dto.cedulaProfesional(), excluir)) {
            throw new ReglaNegocioException("Ya existe un doctor con la cédula " + dto.cedulaProfesional());
        }
    }

    private void sincronizarEspecialidades(Doctor doctor, List<EspecialidadAsignadaRequestDTO> solicitadas){
        // 1. Pasar la lista a un mapa: especialidadID -> cédula ( y detectar repetidas)
        Map<Long, String> cedulasPorEspecialidad = new HashMap<>();

        if(solicitadas != null){
            for(EspecialidadAsignadaRequestDTO s : solicitadas){
                if(cedulasPorEspecialidad.put(s.especialidadId(), s.cedulaEspecialidad()) != null){
                    throw new ReglaNegocioException(
                        "La especialidad " + s.especialidadId() + " viene repetida");
                }
            }
        }


        // 2. Quitar las que ya no vienen (orphanRemoval las borra de la Base)
        doctor.getEspecialidades().removeIf(
            de -> !cedulasPorEspecialidad.containsKey(de.getEspecialidad().getId()));

        // 3. Actualizar las cedulas de las que quedan
        Set<Long> yaAsignadas =  new HashSet<>();

        for(DoctorEspecialidad de: doctor.getEspecialidades()){
            Long especialidadId = de.getEspecialidad().getId();
            de.setCedulaEspecialidad(cedulasPorEspecialidad.get(especialidadId));
            yaAsignadas.add(especialidadId);

        }

        // 4. Agregar las nuevas, cargando la Especialidad real de la base de dayos
        cedulasPorEspecialidad.forEach((especialidadId, cedula)-> {
            if(!yaAsignadas.contains(especialidadId)){
                Especialidad especialidad = especialidadRepository.findById(especialidadId)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Especialidad: ", especialidadId));
                    doctor.agregarEspecialidad(especialidad, cedula);
            }
        });
        
    }

}
