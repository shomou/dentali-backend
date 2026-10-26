package com.dentali.dentali_rest.pacientes.dto;

import java.time.LocalDate;

import com.dentali.dentali_rest.pacientes.Sexo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PacienteRequestDTO(

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    String nombre,

    @NotBlank(message = "El primer apellido es obligatorio")
    @Size(max = 100)
    String primerApellido,

    @Size(max = 100)
    String segundoApellido,

    @Pattern(regexp = "^[A-Z]{4}\\d{6}[HMX][A-Z]{5}[A-Z0-9]\\d$",
                 message = "La CURP no tiene un formato válido")
    String curp,

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @PastOrPresent(message = "La fecha de nacimiento no puede estar en el futuro")
    LocalDate fechaNacimiento,

    @NotNull(message = "El sexo es obligatorio")
    Sexo sexo,

    @NotNull(message = "La entidad de nacimiento es obligatoria")
    Short entidadNacimientoId,

    @Email(message = "El email no tiene un formato válido")
    @Size(max = 150)
    String email,

    @Pattern(regexp = "^\\d{10}$", message = "El teléfono debe tener 10 dígitos")
    String telefono

    
) {

}
