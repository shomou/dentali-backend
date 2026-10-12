package com.dentali.dentali_rest.doctores.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EspecialidadAsignadaRequestDTO(
    
    @NotNull (message = "La especialidad es obligatoria")
    Long especialidadId,

    @NotBlank (message = "Lacédula de especialidad es obligatoria")
    @Size (max= 20)
    String cedulaEspecialidad
) {

}
