package com.dentali.dentali_rest.pacientes.mapper;

import java.time.LocalDate;
import java.time.Period;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import com.dentali.dentali_rest.pacientes.Paciente;
import com.dentali.dentali_rest.pacientes.dto.PacienteRequestDTO;
import com.dentali.dentali_rest.pacientes.dto.PacienteResponseDTO;

@Mapper (
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface PacienteMapper {

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "entidadNacimiento",ignore = true)
    @Mapping(target = "fotoRuta",ignore = true)
    @Mapping(target = "estado",ignore = true)
    @Mapping(target = "fechaCreacion",ignore = true)
    @Mapping(target = "fechaActualizacion",ignore = true)
    Paciente toEntity(PacienteRequestDTO dto);

    @InheritConfiguration (name = "toEntity")
    void actualizarEntidad(PacienteRequestDTO dto, @MappingTarget Paciente paciente);

    @Mapping(target = "edad", source = "fechaNacimiento", qualifiedByName = "calcularEdad")
    @Mapping(target = "fotoUrl",
             expression = "java(paciente.getFotoRuta() != null ? \"/api/pacientes/\" + paciente.getId() + \"/foto\" : null)")
    PacienteResponseDTO toResponse(Paciente paciente);

    @Named("calcularEdad")
    default Integer calcularEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            return null;
        }
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

}
