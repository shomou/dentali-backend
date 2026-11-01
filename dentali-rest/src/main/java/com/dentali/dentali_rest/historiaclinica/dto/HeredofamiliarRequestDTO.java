package com.dentali.dentali_rest.historiaclinica.dto;

import jakarta.validation.constraints.NotNull;

public record HeredofamiliarRequestDTO(
        @NotNull Short enfermedadId,
        @NotNull Short parentescoId
) {

}
