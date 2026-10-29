package com.dentali.dentali_rest.historiaclinica.dto;

import jakarta.validation.constraints.NotNull;

public record HeredoFamiliarRequestDTO(
        @NotNull Short enfermedadId,
        @NotNull Short parentescoId
) {

}
