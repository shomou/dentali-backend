package com.dentali.dentali_rest.consultas;

import java.time.OffsetDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dentali.dentali_rest.common.exception.RecursoNoEncontradoException;
import com.dentali.dentali_rest.common.exception.ReglaNegocioException;
import com.dentali.dentali_rest.consultas.dto.ConsultaRequestDTO;
import com.dentali.dentali_rest.consultas.dto.ConsultaResponseDTO;
import com.dentali.dentali_rest.consultas.mapper.ConsultaMapper;
import com.dentali.dentali_rest.doctores.Doctor;
import com.dentali.dentali_rest.doctores.DoctorRepository;
import com.dentali.dentali_rest.doctores.EstadoDoctor;
import com.dentali.dentali_rest.historiaclinica.HistoriaClinica;
import com.dentali.dentali_rest.historiaclinica.HistoriaClinicaRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
@Transactional (readOnly = true)
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final HistoriaClinicaRepository historiaClinicaRepository;
    private final DoctorRepository doctorRepository;
    private final ConsultaMapper consultaMapper;

    // --------- Consultas ---------
    public Page<ConsultaResponseDTO> listar(Long pacienteId, Pageable pageable){
        HistoriaClinica historia = buscarHisptoria(pacienteId);
        return consultaRepository.findByHistoriaClinicaId(historia.getId(), pageable)
        .map(consultaMapper::toResponse);
    }

    public ConsultaResponseDTO obtener(Long consultaId) {
        return consultaMapper.toResponse(buscarConsulta(consultaId));
    }

    // --------- Escritura ---------
    @Transactional
    public ConsultaResponseDTO crear(Long pacienteId, ConsultaRequestDTO dto){
        HistoriaClinica historia =  buscarHisptoria(pacienteId);
        Doctor doctor = buscarDoctorActivo(dto.doctorId());

        Consulta consulta = consultaMapper.toEntity(dto);
        consulta.setHistoriaClinica(historia);
        consulta.setDoctor(doctor);
        if(consulta.getFechaAtencion() == null){
            consulta.setFechaAtencion(OffsetDateTime.now());
        }

        return consultaMapper.toResponse(consultaRepository.save(consulta));
    }

    @Transactional 
    public ConsultaResponseDTO actualizar(Long consultaId, ConsultaRequestDTO dto){
        Consulta consulta = buscarConsulta(consultaId);
        verificarBorrador(consulta);
        Doctor doctor = buscarDoctorActivo(dto.doctorId());

        OffsetDateTime fechaAnterior = consulta.getFechaAtencion(); 
        consultaMapper.actualizarEntidad(dto, consulta);
        consulta.setDoctor(doctor);

        if(consulta.getFechaAtencion() == null){
            consulta.setFechaAtencion(fechaAnterior);

        }

        consultaRepository.flush(); // Asegura que los cambios se guarden antes de mapear a DTO

        return consultaMapper.toResponse(consultaRepository.save(consulta));
    }

    @Transactional 
    public void eliminar(Long consultaId){
        Consulta consulta = buscarConsulta(consultaId);
        verificarBorrador(consulta);
        consultaRepository.delete(consulta);
    }

    @Transactional
    public ConsultaResponseDTO firmar(Long consultaId){
        Consulta consulta = buscarConsulta(consultaId);
        consulta.firmar();
        consultaRepository.flush(); // Asegura que los cambios se guarden antes de mapear a DTO
        return consultaMapper.toResponse(consulta);
    }

    // --------- Ayudantes---------
    private HistoriaClinica buscarHisptoria(Long pacienteId){
        return historiaClinicaRepository.findByPacienteId(pacienteId)
        .orElseThrow(() -> new RecursoNoEncontradoException(
            "El paciente con id " + pacienteId + " no tiene historia clínica"));
    }

    private Consulta buscarConsulta(Long id){
        return consultaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Consulta", id));
    }

    private Doctor buscarDoctorActivo(Long id){
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Doctor",id));
        
        if( doctor.getEstado() != EstadoDoctor.ACTIVO){
            throw new ReglaNegocioException("El doctor con id " + id + " no está activo");
        }
        return doctor;
    }

    /** Capa de servicio del candado: mensaje claro antes de que llegue al trigger. */
    private void verificarBorrador(Consulta consulta) {
        if (consulta.estaFirmada()) {
            throw new ReglaNegocioException(
                    "La consulta con id " + consulta.getId() + " está firmada y no puede modificarse ni eliminarse");
        }
    }

}
