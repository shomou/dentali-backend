package com.dentali.dentali_rest.consultas;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable 
@Getter 
@Setter 
@NoArgsConstructor 
public class SignosVitales {

    @Column(name = "presion_sistolica")
    private Short presionSistolica;

    @Column(name = "presion_diastolica")
    private Short presionDiastolica;

    @Column(name = "frecuencia_cardiaca")
    private Short frecuenciaCardiaca;

    @Column(name = "temperatura_c", precision = 4, scale = 1)
    private BigDecimal temperaturaC;

    @Column(name = "talla_m", precision = 3, scale = 2)
    private BigDecimal tallaM;

    @Column(name = "peso_kg", precision = 5, scale = 2)
    private BigDecimal pesoKg;

    @Column(name = "frecuencia_respiratoria")
    private Short frecuenciaRespiratoria;

    @Column(name = "saturacion_o2")
    private Short saturacionO2;

}
