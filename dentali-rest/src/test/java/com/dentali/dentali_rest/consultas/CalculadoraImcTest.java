package com.dentali.dentali_rest.consultas;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class CalculadoraImcTest {

    @Test
    void calculaImcConUnDecimal() {
        BigDecimal imc = CalculadoraImc.calcular(new BigDecimal("70"), new BigDecimal("1.75"));

        assertEquals(new BigDecimal("22.9"), imc);
    }

    @Test
    void sinTallaDevuelveNull() {
        BigDecimal imc = CalculadoraImc.calcular(new BigDecimal("70"), null);

        assertNull(imc);
    }

    @Test
    void divisionInfinitaNoExplota() {
        // 70 / (1.75 × 1.75) = 70 / 3.0625 = 22.857142857... (decimales infinitos)
        assertDoesNotThrow(
                () -> CalculadoraImc.calcular(new BigDecimal("70"), new BigDecimal("1.75")));
    }
}
