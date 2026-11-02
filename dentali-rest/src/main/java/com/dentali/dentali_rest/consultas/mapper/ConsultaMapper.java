package com.dentali.dentali_rest.consultas.mapper;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import com.dentali.dentali_rest.consultas.CalculadoraImc;
import com.dentali.dentali_rest.consultas.Consulta;
import com.dentali.dentali_rest.consultas.SignosVitales;
import com.dentali.dentali_rest.consultas.dto.ConsultaRequestDTO;
import com.dentali.dentali_rest.consultas.dto.ConsultaResponseDTO;
import com.dentali.dentali_rest.doctores.Doctor;

@Mapper (componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ConsultaMapper {

    // ----------- DTO -> Entidad -----------
    @Mapping (target = "id", ignore = true)
    @Mapping (target = "historiaClinica", ignore = true)
    @Mapping (target = "doctor", ignore = true)
    @Mapping (target = "fechaCreacion", ignore = true)
    Consulta toEntity(ConsultaRequestDTO dto);

    @InheritConfiguration (name = "toEntity")
    void actualizarEntidad(ConsultaRequestDTO dto, @MappingTarget Consulta consulta);

    // ---------- Entidad -> DTO -----------
    @Mapping (target = "historiaClinicaId", source = "historiaClinica.id")
    @Mapping (target = "doctorId", source = "doctor.id")
    @Mapping (target = "doctorNombre", source = "doctor", qualifiedByName = "nombreCompleto")
    @Mapping (target = "imc", source = "signosVitales", qualifiedByName = "calcularImc")
    @Mapping (target = "firmada", expression = "java(consulta.estaFirmada())")
    ConsultaResponseDTO toResponse(Consulta consulta);

    // ---------- Ayudantes -----------
    @Named ("nombreCompleto")
    default String nombreCompleto(Doctor doctor) {
        if(doctor == null){
            return null;
        }
        return Stream.of(doctor.getNombre(), doctor.getPrimerApellido(), doctor.getSegundoApellido())
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));
    }

    @Named("calcularImc")
    default BigDecimal calcularImc(SignosVitales sv) {
        return sv == null ? null : CalculadoraImc.calcular(sv.getPesoKg(), sv.getTallaM());
    }

}
