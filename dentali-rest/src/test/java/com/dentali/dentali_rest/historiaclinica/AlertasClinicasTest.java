package com.dentali.dentali_rest.historiaclinica;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

public class AlertasClinicasTest {

    @Test
    void historiaVaciaNoGeneraAlertas() {
        HistoriaClinica h = new HistoriaClinica();   // todos los Boolean en null = "no preguntado"

        List<String> alertas = AlertasClinicas.calcular(h);

        assertTrue(alertas.isEmpty());
    }

    @Test
    void alergiaSinDetalleNoMuestraNull() {
        HistoriaClinica h = new HistoriaClinica();
        h.setAlergicoMedicamentos(true);             // detalle se queda en null a propósito

        List<String> alertas = AlertasClinicas.calcular(h);

        assertEquals(1, alertas.size());
        assertEquals("Alérgico a medicamentos: sin detalle registrado", alertas.get(0));
        assertFalse(alertas.get(0).contains("null"));
    }

    @Test
    void embarazoSinSemanasNoMuestraNull() {
        HistoriaClinica h = new HistoriaClinica();
        h.setEmbarazada(true);                 // semanasGestacion se queda en null a propósito

        List<String> alertas = AlertasClinicas.calcular(h);

        assertEquals(List.of("Embarazo"), alertas);
        assertFalse(alertas.get(0).contains("null"));
    }

    @Test
    void sangradoAbundanteGeneraAlerta() {
        HistoriaClinica h = new HistoriaClinica();
        h.setSangradoAbundante(true);

        List<String> alertas = AlertasClinicas.calcular(h);

        assertEquals(
            List.of("Riesgo de sangrado: hemorragias frecuentes o sangrado abundante en heridas"),
            alertas);
    }

    @Test
    void embarazoConSemanasMuestraLasSemanas() {
        HistoriaClinica h = new HistoriaClinica();
        h.setEmbarazada(true);
        h.setSemanasGestacion((short) 12);     // el campo es Short: hay que convertir el 12 a short
    
        assertEquals(List.of("Embarazo (12 semanas)"), AlertasClinicas.calcular(h));
    }
    
    @Test
    void ambasCausasDeSangradoGeneranUnaSolaAlerta() {
        HistoriaClinica h = new HistoriaClinica();
        h.setHemorragiasFrecuentes(true);
        h.setSangradoAbundante(true);
    
        assertEquals(1, AlertasClinicas.calcular(h).size());
    }

}
