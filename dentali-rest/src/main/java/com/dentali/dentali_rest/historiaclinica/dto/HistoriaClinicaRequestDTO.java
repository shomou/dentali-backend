package com.dentali.dentali_rest.historiaclinica.dto;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dentali.dentali_rest.historiaclinica.GrupoSanguineo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record HistoriaClinicaRequestDTO(
    
    // Temporal cuando lleguemos a Spring Security, debemos ir por el usuario Autenticado
    @NotNull (message = "El doctor que abre la historia es obligatorio")
    Long doctorAperturaId,
    GrupoSanguineo grupoSanguineo,

    // ------- Antecedentes personales patológicos ----------
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

    // No patologicos
    @Min (0) @Max (10)
    Short cepilladosPorDia,
    @Size (max = 100) String auxiliarHigieneOtro,
    Boolean fuma,
    @Size (max = 100) String fumaFrecuencia,
    Boolean alcohol,
    @Size(max = 100) String alcoholFrecuencia,
    Boolean otrasSustancias,
    @Size(max = 100) String otrasSustanciasFrecuencia,
    Boolean embarazada,
    @Min(1) @Max(42)
    Short semanasGestacion,
    Boolean lactando,

    // -------  Exploracion ---------
    @Valid ExploracionTejidosBlandosDTO exploracion,
    String atmObservaciones,

    // ───── IX. Plan de tratamiento ─────
    String planTratamiento,

    // ───── Selección múltiple (ids de catálogos) ─────
    Map<Short, Boolean> antecedentesPersonales,          // enfermedadId → padece
    List<@Valid HeredoFamiliarRequestDTO> antecedentesHeredofamiliares,
    Set<Short> auxiliaresHigieneIds,
    Set<Short> hallazgosAtmIds,
    Set<Short> estadosPeriodontalesIds

) {

    // ═══════════════ Validaciones entre campos ═══════════════
    @AssertTrue(message = "Indica a qué medicamentos es alérgico")
    public boolean isAlergiasCoherentes() {
        return requiereDetalle(alergicoMedicamentos, alergiasDetalle);
    }

    @AssertTrue(message = "Describe el problema con la anestesia")
    public boolean isAnestesiaCoherente() {
        return requiereDetalle(problemasAnestesia, problemasAnestesiaDetalle);
    }

    @AssertTrue(message = "Indica el motivo de la hospitalización")
    public boolean isHospitalizacionCoherente() {
        return requiereDetalle(hospitalizado, hospitalizacionMotivo);
    }

    @AssertTrue(message = "Indica el motivo de la cirugía")
    public boolean isCirugiasCoherentes() {
        return requiereDetalle(cirugias, cirugiasMotivo);
    }

    @AssertTrue(message = "Indica qué enfermedad padece actualmente")
    public boolean isEnfermedadActualCoherente() {
        return requiereDetalle(enfermedadActual, enfermedadActualDetalle);
    }

    @AssertTrue(message = "Especifica el medicamento, el motivo y la dosis")
    public boolean isMedicamentosCoherentes() {
        return requiereDetalle(tomaMedicamentos, medicamentosDetalle);
    }

    @AssertTrue(message = "Indica el motivo, cuándo y durante qué tiempo")
    public boolean isBifosfonatosCoherentes() {
        return requiereDetalle(bifosfonatos, bifosfonatosDetalle);
    }

    @AssertTrue(message = "Indica con qué frecuencia fuma")
    public boolean isFumaCoherente() {
        return requiereDetalle(fuma, fumaFrecuencia);
    }

    @AssertTrue(message = "Indica con qué frecuencia consume alcohol")
    public boolean isAlcoholCoherente() {
        return requiereDetalle(alcohol, alcoholFrecuencia);
    }

    @AssertTrue(message = "Indica con qué frecuencia consume otras sustancias")
    public boolean isOtrasSustanciasCoherente() {
        return requiereDetalle(otrasSustancias, otrasSustanciasFrecuencia);
    }

    @AssertTrue(message = "Las semanas de gestación solo aplican si hay embarazo")
    public boolean isSemanasCoherentes() {
        return Boolean.TRUE.equals(embarazada) || semanasGestacion == null;
    }
    
    /** "Si respondió Sí, el detalle es obligatorio." Equivale al CHECK de la V6. */
    private static boolean requiereDetalle(Boolean respuesta, String detalle) {
        return !Boolean.TRUE.equals(respuesta) || (detalle != null && !detalle.isBlank());
    }

}
