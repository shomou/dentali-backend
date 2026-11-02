package com.dentali.dentali_rest.consultas;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** IMC = peso(kg) / talla2(m). Se calcula no se guarda */
public final class CalculadoraImc {

    private CalculadoraImc(){}

    public static BigDecimal calcular(BigDecimal pesoKg, BigDecimal tallaM) {
        if (pesoKg == null || tallaM ==  null) {
            return null;
        }
        BigDecimal tallaAlCuadrado = tallaM.multiply(tallaM);
        return pesoKg.divide(tallaAlCuadrado, 1, RoundingMode.HALF_UP);

    }


}
