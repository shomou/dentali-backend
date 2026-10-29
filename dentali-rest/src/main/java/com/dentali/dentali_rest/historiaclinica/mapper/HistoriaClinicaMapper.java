package com.dentali.dentali_rest.historiaclinica.mapper;

import com.dentali.dentali_rest.catalogos.Enfermedad;
import com.dentali.dentali_rest.doctores.Doctor;
import com.dentali.dentali_rest.historiaclinica.AlertasClinicas;
import com.dentali.dentali_rest.historiaclinica.HistoriaClinica;
import com.dentali.dentali_rest.historiaclinica.dto.AntecedentePersonalResponseDTO;
import com.dentali.dentali_rest.historiaclinica.dto.HistoriaClinicaRequestDTO;
import com.dentali.dentali_rest.historiaclinica.dto.HistoriaClinicaResponseDTO;
import com.dentali.dentali_rest.pacientes.Paciente;
import org.mapstruct.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Mapper (
    componentModel =  MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    imports = AlertasClinicas.class
)
public interface HistoriaClinicaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "paciente", ignore = true)
    @Mapping(target = "doctorApertura", ignore = true)
    @Mapping(target = "fechaApertura", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    @Mapping(target = "antecedentesPersonales", ignore = true)
    @Mapping(target = "antecedentesHeredofamiliares", ignore = true)
    @Mapping(target = "auxiliaresHigiene", ignore = true)
    @Mapping(target = "hallazgosAtm", ignore = true)
    @Mapping(target = "estadosPeriodontales", ignore = true)    
    HistoriaClinica toEntity(HistoriaClinicaRequestDTO dto);

    @InheritConfiguration(name = "toEntity")
    void actualizarEntidad(HistoriaClinicaRequestDTO dto, @MappingTarget HistoriaClinica historia);

    @Mapping(target = "numeroExpediente", source = "id", qualifiedByName = "formatearExpediente")
    @Mapping(target = "pacienteId", source = "paciente.id")
    @Mapping(target = "pacienteNombre", source = "paciente", qualifiedByName = "nombrePaciente")
    @Mapping(target = "doctorAperturaId", source = "doctorApertura.id")
    @Mapping(target = "doctorAperturaNombre", source = "doctorApertura", qualifiedByName = "nombreDoctor")
    @Mapping(target = "alertas", expression = "java(AlertasClinicas.calcular(historia))")
    @Mapping(target = "antecedentesPersonales", qualifiedByName = "antecedentesPersonales")
    HistoriaClinicaResponseDTO toResponse(HistoriaClinica historia);

    @Named("formatearExpediente")
    default String formatearExpediente(Long id) {
        return id == null ? null : String.format("EXP-%06d", id);
    }

    @Named("nombrePaciente")
    default String nombrePaciente(Paciente p) {
        return unirNombre(p.getNombre(), p.getPrimerApellido(), p.getSegundoApellido());
    }

    @Named("nombreDoctor")
    default String nombreDoctor(Doctor d) {
        return unirNombre(d.getNombre(), d.getPrimerApellido(), d.getSegundoApellido());
    }

    @Named("antecedentesPersonales")
    default List<AntecedentePersonalResponseDTO> antecedentesPersonales(Map<Enfermedad, Boolean> mapa) {
        return mapa.entrySet().stream()
                .map(e -> new AntecedentePersonalResponseDTO(e.getKey().getId(), e.getKey().getNombre(), e.getValue()))
                .sorted(Comparator.comparing(AntecedentePersonalResponseDTO::enfermedadId))
                .toList();
    }

    private static String unirNombre(String nombre, String primerApellido, String segundoApellido) {
        return segundoApellido == null
                ? nombre + " " + primerApellido
                : nombre + " " + primerApellido + " " + segundoApellido;
    }

}
