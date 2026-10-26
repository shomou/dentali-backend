package com.dentali.dentali_rest.pacientes.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import com.dentali.dentali_rest.catalogos.dto.CatalogoResponseDTO;
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
    CatalogoResponseDTO entidadNacimiento,
    String email,
    String telefono,
    String fotoUrl,
    EstadoPaciente estado,
    OffsetDateTime fechaCreacion,
    CatalogoResponseDTO estadoCivil,
    String ocupacion,
    String religion,
    String domicilioCalle,
    String domicilioNumeroExterior,
    String domicilioNumeroInterior,
    String domicilioColonia,
    String domicilioCodigoPostal,
    String domicilioMunicipio,
    CatalogoResponseDTO domicilioEntidad,
    String contactoEmergenciaNombre,
    String contactoEmergenciaTelefono
) {

}
