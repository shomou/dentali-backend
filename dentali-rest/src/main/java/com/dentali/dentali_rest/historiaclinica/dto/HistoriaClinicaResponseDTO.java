package com.dentali.dentali_rest.historiaclinica.dto;

import java.time.OffsetDateTime;
import java.util.List;

import com.dentali.dentali_rest.catalogos.dto.CatalogoResponseDTO;
import com.dentali.dentali_rest.historiaclinica.GrupoSanguineo;

public record HistoriaClinicaResponseDTO(
        Long id,
        String numeroExpediente,
        Long pacienteId,
        String pacienteNombre,
        Long doctorAperturaId,
        String doctorAperturaNombre,
        OffsetDateTime fechaApertura,
        OffsetDateTime fechaActualizacion,

        List<String> alertas,

        GrupoSanguineo grupoSanguineo,
        String enfermedadesUltimoAnio,
        String antecedentePatologicoOtro,
        Boolean alergicoMedicamentos,
        String alergiasDetalle,
        Boolean anestesiadoPreviamente,
        Boolean problemasAnestesia,
        String problemasAnestesiaDetalle,
        Boolean hemorragiasFrecuentes,
        Boolean sangradoAbundante,
        Boolean hospitalizado,
        String hospitalizacionMotivo,
        Boolean cirugias,
        String cirugiasMotivo,
        Boolean enfermedadActual,
        String enfermedadActualDetalle,
        Boolean tomaMedicamentos,
        String medicamentosDetalle,
        Boolean bifosfonatos,
        String bifosfonatosDetalle,

        Short cepilladosPorDia,
        String auxiliarHigieneOtro,
        Boolean fuma,
        String fumaFrecuencia,
        Boolean alcohol,
        String alcoholFrecuencia,
        Boolean otrasSustancias,
        String otrasSustanciasFrecuencia,
        Boolean embarazada,
        Short semanasGestacion,
        Boolean lactando,

        ExploracionTejidosBlandosDTO exploracion,
        String atmObservaciones,
        String planTratamiento,

        List<AntecedentePersonalResponseDTO> antecedentesPersonales,
        List<HeredofamiliarResponseDTO> antecedentesHeredofamiliares,
        List<CatalogoResponseDTO> auxiliaresHigiene,
        List<CatalogoResponseDTO> hallazgosAtm,
        List<CatalogoResponseDTO> estadosPeriodontales
) {

}
