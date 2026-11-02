package com.dentali.dentali_rest.consultas.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record SignosVitalesDTO(

    @Min (50) @Max (300)
    Short presionSistolica,
    
    @Min (30) @Max (200)
    Short presionDiastolica,
    
    @Min (20) @Max (250)
    Short frecuenciaCardiaca,

    @DecimalMin("30.0") @DecimalMax("45.0") @Digits(integer = 3, fraction = 1)
    BigDecimal temperaturaC,
    
    @DecimalMin("1") @DecimalMax("400") @Digits(integer = 3, fraction = 2)
    BigDecimal pesoKg,
    
    @DecimalMin("0.30") @DecimalMax("2.50") @Digits(integer = 1, fraction = 2)
    BigDecimal tallaM,
    
    @Min (5) @Max (80)
    Short frecuenciaRespiratoria,
    
    @Min (30) @Max (100)
    Short saturacionO2
) {

    /** Espejo de chk_consultas_presion_completa: las dos o ninguna */
    @JsonIgnore 
    @AssertTrue (message = "Si se registra la presión arterial, deben registrarse ambos valores (sistólica y diastólica).")
    public boolean isPresionCompleta() {
        return (presionSistolica == null) == (presionDiastolica == null);
    }

    /** Espejo de chk_consultas_presion_coherente: si hay presión, debe ser coherente */
    @JsonIgnore
    @AssertTrue(message = "La presión sistólica debe ser mayor que la diastólica")
    public boolean isPresionCoherente(){
        return presionSistolica == null ||  presionDiastolica == null
                || presionSistolica > presionDiastolica;
    }
}
