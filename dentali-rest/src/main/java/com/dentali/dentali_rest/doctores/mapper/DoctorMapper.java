package com.dentali.dentali_rest.doctores.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.dentali.dentali_rest.doctores.Doctor;
import com.dentali.dentali_rest.doctores.DoctorEspecialidad;
import com.dentali.dentali_rest.doctores.dto.DoctorRequestDTO;
import com.dentali.dentali_rest.doctores.dto.DoctorResponseDTO;
import com.dentali.dentali_rest.doctores.dto.EspecialidadDoctorResponseDTO;

@Mapper (
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface DoctorMapper {
    
    @Mapping (target = "id", ignore = true )
    @Mapping (target = "estado", ignore = true )
    @Mapping (target = "fotoRuta", ignore = true )
    @Mapping (target = "fechaCreacion", ignore = true )
    @Mapping (target = "fechaActualizacion", ignore = true )
    @Mapping (target = "especialidades", ignore = true )
    Doctor toEntity(DoctorRequestDTO dto);

    DoctorResponseDTO toResponse(Doctor doctor);

    @Mapping (target = "especialidadId", source = "especialidad.id")
    @Mapping (target = "nombre", source = "especialidad.nombre")
    EspecialidadDoctorResponseDTO toEspecialidadResponse(DoctorEspecialidad doctorEspecialidad);

    @InheritConfiguration(name = "toEntity")
    void actualizarEntidad(DoctorRequestDTO dto, @MappingTarget Doctor doctor);
}
