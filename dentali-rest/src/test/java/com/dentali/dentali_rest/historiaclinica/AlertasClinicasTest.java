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

}
