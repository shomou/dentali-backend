package com.dentali.dentali_rest.doctores.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DoctorRequestDTO(
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    String nombre,

    @NotBlank (message = "El primer apellido es obligatorio")
    @Size(max = 100)
    String primerApellido,
    
    @Size(max = 100)
    String segundoApellido,
    
    @NotBlank (message = "La CURP es obligatoria")
    @Pattern (regexp = "^[A-Z]{4}\\d{6}[HM][A-Z]{5}[A-Z0-9]\\d$", 
            message = "La CURP no tiene un formato válido")
    String curp,
    
    @NotBlank (message = "La cédula profesional es obligatoria")
    @Size (max= 20)
    String cedulaProfesional,

    @NotBlank (message = "El correo electrónico es obligatorio")
    @Size (max= 150)
    String email,
    
    @Pattern (regexp = "^\\d{10}$", message = "El teléfono no tiene un formato válido")
    String telefono,
    
    List<@Valid EspecialidadAsignadaRequestDTO> especialidades
) {

}
