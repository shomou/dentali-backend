package com.dentali.dentali_rest.consultas.dto;

import java.time.OffsetDateTime;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record ConsultaRequestDTO(

    @NotNull 
    Long doctorId,

    @PastOrPresent 
    OffsetDateTime fechaAtencion,
    
    @NotBlank 
    String motivoConsulta,

    String exploracion,
    String diagnostico,
    String tratamiento,
    String indicaciones,
    String pronostico,

    @Valid 
    SignosVitalesDTO signosVitales

) {}
