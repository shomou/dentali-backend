package com.dentali.dentali_rest.doctores.dto;

import java.time.OffsetDateTime;
import java.util.List;

import com.dentali.dentali_rest.doctores.EstadoDoctor;

public record DoctorResponseDTO(
    Long id,
    String nombre,
    String primerApellido,
    String segundoApellido,
    String curp,
    String cedulaProfesional,
    String email,
    String telefono,
    EstadoDoctor estado,
    List<EspecialidadDoctorResponseDTO> especialidades,
    OffsetDateTime fechaCreacion
) {

}
