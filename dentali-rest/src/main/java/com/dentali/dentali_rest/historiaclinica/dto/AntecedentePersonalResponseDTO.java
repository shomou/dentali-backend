package com.dentali.dentali_rest.historiaclinica.dto;

public record AntecedentePersonalResponseDTO(
    Short enfermedadId, 
    String enfermedad, 
    Boolean padece
) {

}
