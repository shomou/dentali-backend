package com.dentali.dentali_rest.consultas.dto;

import java.time.OffsetDateTime;

public record NotaAclaratoriaResponseDTO(
    Long id,
    Long consultaId,
    Long doctorId,
    String doctorNombre,
    String texto,
    OffsetDateTime fechaCreacion
) {

}
