package com.dentali.dentali_rest.consultas.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ConsultaResponseDTO(
    Long id,
    Long historiaClinicaId,
    Long doctorId,
    String doctorNombre,
    OffsetDateTime fechaAtencion,
    String motivoConsulta,
    String exploracion,
    String diagnostico,
    String tratamiento,
    String indicaciones,
    String pronostico,
    SignosVitalesDTO signosVitales,
    BigDecimal imc,
    boolean firmada,
    OffsetDateTime firmadaEn,
    OffsetDateTime fechaCreacion

) {

}
