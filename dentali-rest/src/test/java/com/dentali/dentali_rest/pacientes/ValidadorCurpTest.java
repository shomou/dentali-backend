package com.dentali.dentali_rest.pacientes;

import com.dentali.dentali_rest.common.exception.ReglaNegocioException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidadorCurpTest {

    private static final String CURP_ANA = "LOPA900101MDFPRN09";
    private static final LocalDate NACIMIENTO_ANA = LocalDate.of(1990, 1, 1);

    @Test
    void aceptaCurpCoherente() {
        assertDoesNotThrow(() ->
                ValidadorCurp.validarCoherencia(CURP_ANA, NACIMIENTO_ANA, Sexo.M, "DF"));
    }

    @Test
    void aceptaPacienteSinCurp() {
        assertDoesNotThrow(() ->
                ValidadorCurp.validarCoherencia(null, NACIMIENTO_ANA, Sexo.M, "DF"));
    }

    @Test
    void rechazaFechaDistinta() {
        assertThrows(ReglaNegocioException.class, () ->
                ValidadorCurp.validarCoherencia(CURP_ANA, LocalDate.of(1990, 1, 2), Sexo.M, "DF"));
    }

    @Test
    void rechazaSexoDistinto() {
        assertThrows(ReglaNegocioException.class, () ->
                ValidadorCurp.validarCoherencia(CURP_ANA, NACIMIENTO_ANA, Sexo.H, "DF"));
    }

    @Test
    void rechazaEntidadDistinta() {
        assertThrows(ReglaNegocioException.class, () ->
                ValidadorCurp.validarCoherencia(CURP_ANA, NACIMIENTO_ANA, Sexo.M, "MC"));
    }

    // BONUS: descomenta cuando implementes el diferenciador de siglo
    @Test
    void rechazaDigitoParaNacidosDesde2000() {
        assertThrows(ReglaNegocioException.class, () ->
                ValidadorCurp.validarCoherencia("LOPA000101MDFPRN09", LocalDate.of(2000, 1, 1), Sexo.M, "DF"));
    }
}