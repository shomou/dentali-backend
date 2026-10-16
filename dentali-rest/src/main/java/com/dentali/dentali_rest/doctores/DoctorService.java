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

import com.dentali.dentali_rest.common.storage.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
@Transactional(readOnly = true)
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final EspecialidadRepository especialidadRepository;
    private final DoctorMapper doctorMapper;
    private final FileStorageService fileStorageService;

    //----------------- Consultas -----------------//
    public List<DoctorResponseDTO> listarActivos(){
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
    public DoctorResponseDTO crear(DoctorRequestDTO dto){
        validarDuplicados(dto, null);

        Doctor doctor = doctorMapper.toEntity(dto);

        sincronizarEspecialidades(doctor, dto.especialidades());

        return doctorMapper.toResponse(doctorRepository.save(doctor));

    }

    @Transactional 
    public DoctorResponseDTO actualizar(Long id, DoctorRequestDTO dto){
        Doctor doctor = buscarDoctor(id);
        validarDuplicados(dto, id);

        doctorMapper.actualizarEntidad(dto, doctor);
        sincronizarEspecialidades(doctor, dto.especialidades());

        return doctorMapper.toResponse(doctor);

    }

    @Transactional
    public void desactivar(Long id){
        buscarDoctor(id).setEstado(EstadoDoctor.DESACTIVADO);
    }

    @Transactional
    public void reactivar(Long id){
        Doctor doctor = buscarDoctor(id);
        
        if(doctor.getEstado() == EstadoDoctor.ACTIVO){
            throw new ReglaNegocioException("El doctor con id " + id + "ya está activado");
        }
        
        doctor.setEstado(EstadoDoctor.ACTIVO);
    }

    @Transactional 
    public DoctorResponseDTO actualizarFoto(Long id, MultipartFile archivo){
        Doctor doctor = buscarDoctor(id);

        String rutaAnterior = doctor.getFotoRuta();
        String rutaNueva = fileStorageService.guardarImagen(archivo, "doctores/" + id);
        doctor.setFotoRuta(rutaNueva);

        FileStorageService almacenamiento = this.fileStorageService;

        // El disco no es transaccional: decidims que borrar cuando sepamos como terminó la base de datos
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override 
            public void afterCompletion(int status){
                if(status == STATUS_COMMITTED){
                    almacenamiento.eliminar(rutaAnterior);
                }else{
                    almacenamiento.eliminar(rutaNueva);
                }

            }
        });

        return doctorMapper.toResponse(doctor);
    }


    public Resource obtenerFoto(Long id){
        Doctor doctor = buscarDoctor(id);
        if(doctor.getFotoRuta() == null){
            throw new RecursoNoEncontradoException("Foto del doctor", id);
        }
        return fileStorageService.cargar(doctor.getFotoRuta()).
                orElseThrow(() -> new RecursoNoEncontradoException("Foto del Doctor", id));
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
