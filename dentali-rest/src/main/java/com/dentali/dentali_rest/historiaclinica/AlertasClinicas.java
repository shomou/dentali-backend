package com.dentali.dentali_rest.historiaclinica;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/** Calcula las alertas clínicas a partir de la historia. No se guardan: se derivan. */
public final class AlertasClinicas {

    private static final String SIN_DETALLE = "sin detalle registrado";

    private AlertasClinicas(){}

    public static List<String> calcular(HistoriaClinica h){
        List<String> alertas = new ArrayList<>();

        if(Boolean.TRUE.equals(h.getAlergicoMedicamentos())){
            alertas.add("Alérgico a medicamentos: " + detalle(h.getAlergiasDetalle()));
        }

        if(Boolean.TRUE.equals(h.getProblemasAnestesia())){
            alertas.add("Problemas previos con anestesia: " + detalle(h.getProblemasAnestesiaDetalle()));
        }

        if(Boolean.TRUE.equals(h.getHemorragiasFrecuentes()) || Boolean.TRUE.equals(h.getSangradoAbundante())){
            alertas.add("Riesgo de sangrado: hemorragias frecuentes o sangrado abundante en heridas");
        }

        if(Boolean.TRUE.equals(h.getBifosfonatos())){
           alertas.add("Tratamiento con bifosfonatos (precaución en extracciones): " + detalle(h.getBifosfonatosDetalle()));
        }

        if(Boolean.TRUE.equals(h.getEmbarazada())){
            alertas.add(h.getSemanasGestacion() == null
                    ? "Embarazo"
                    : "Embarazo (" + h.getSemanasGestacion() + " semanas)");
        }

        // Ordenadas por id del catálogo: el HashMap no garantiza orden y las alertas
        // saldrían distintas en cada petición (y los tests serían intermitentes).
        h.getAntecedentesPersonales().entrySet().stream()
                .filter(e -> Boolean.TRUE.equals(e.getValue()))
                .map(Map.Entry::getKey)
                .sorted(Comparator.comparing(enfermedad -> enfermedad.getId()))
                .forEach(enfermedad -> alertas.add("Padece: " + enfermedad.getNombre()));

        return alertas;
    }

    /** Evita que concatenar null escriba "null" en la alerta. */
    private static String detalle(String texto){
        return (texto == null || texto.isBlank()) ? SIN_DETALLE : texto.strip();
    }

}
