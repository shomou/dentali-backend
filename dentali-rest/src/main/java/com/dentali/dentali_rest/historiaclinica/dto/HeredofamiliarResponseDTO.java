package com.dentali.dentali_rest.historiaclinica.dto;

import com.dentali.dentali_rest.catalogos.dto.CatalogoResponseDTO;

public record HeredofamiliarResponseDTO(
    CatalogoResponseDTO enfermedad, 
    CatalogoResponseDTO parentesco
) {

}
