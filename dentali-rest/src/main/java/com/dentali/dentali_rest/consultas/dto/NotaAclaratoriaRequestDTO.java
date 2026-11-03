package com.dentali.dentali_rest.consultas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotaAclaratoriaRequestDTO(
    @NotNull Long doctorId,
    @NotBlank  String texto
) {

}
