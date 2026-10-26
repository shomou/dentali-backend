package com.dentali.dentali_rest.pacientes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.dentali.dentali_rest.common.exception.ReglaNegocioException;

public final class ValidadorCurp {

    public static final DateTimeFormatter AAMMDD = DateTimeFormatter.ofPattern("yyMMdd");

      private ValidadorCurp() {
        // Clase de utilidad: no se instancia
    }

    /**
     * Verifica que la CURP coincida con los datos capturados.
     * Si la CURP es null, no hay nada que validar.
     */
    public static void validarCoherencia(String curp, LocalDate fechaNacimiento,
                                         Sexo sexo, String abreviaturaEntidad) {
        if (curp == null) {
            return;
        }

        // TODO 1: compara la fecha de la CURP con fechaNacimiento.format(AAMMDD)
        // Declara formatter
        String fechaNacimientoIn = fechaNacimiento.format(AAMMDD);
        
        if(!fechaNacimientoIn.equals(curp.substring(4,10))){
            throw new ReglaNegocioException("La CURP no coincide con la fecha de nacimiento");
        }


        // TODO 2: compara el sexo
        if(!sexo.name().equals(String.valueOf(curp.charAt(10)))){
            throw new ReglaNegocioException("La CURP no coincide con el sexo");
        }

        // TODO 3: compara la entidad
        if(!abreviaturaEntidad.equals(curp.substring(11,13))){
            throw new ReglaNegocioException("La CURP no coincide con la entidad federativa");
        }

        // BONUS: valida el diferenciador de siglo (Character.isDigit te ayudará)
        boolean esDigito = Character.isDigit(curp.charAt(16));
        boolean nacioAntesDe2000 = fechaNacimiento.getYear() < 2000;

        if (esDigito != nacioAntesDe2000) {
            throw new ReglaNegocioException("La CURP no coincide con el siglo de nacimiento");
        }


        // Si algo no coincide: throw new ReglaNegocioException("La CURP no coincide con ...");
    }
}
