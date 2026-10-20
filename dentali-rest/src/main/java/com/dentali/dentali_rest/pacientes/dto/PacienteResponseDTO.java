package com.dentali.dentali_rest.pacientes.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import com.dentali.dentali_rest.catalogos.dto.EntidadFederativaResponseDTO;
import com.dentali.dentali_rest.pacientes.EstadoPaciente;
import com.dentali.dentali_rest.pacientes.Sexo;

public record PacienteResponseDTO(
    Long id,
    String nombre,
    String primerApellido,
    String segundoApellido,
    String curp,
    LocalDate fechaNacimiento,
    Integer edad,
    Sexo sexo,
    EntidadFederativaResponseDTO entidadNacimiento,
    String email,
    String telefono,
    String fotoUrl,
    EstadoPaciente estado,
    OffsetDateTime fechaCreacion
) {

}
