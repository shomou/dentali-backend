package com.dentali.dentali_rest.historiaclinica;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable 
@Getter 
@Setter 
@NoArgsConstructor 
public class ExploracionTejidosBlandos {

    @Column(name = "exploracion_cara")
    private String cara;

    @Column(name = "exploracion_cuello")
    private String cuellos;

    @Column(name = "exploracion_labios")
    private String labios;

    @Column(name = "exploracion_paladar_blando")
    private String paladarBlando;

    @Column(name = "exploracion_paladar_duro")
    private String paladarDuro;

    @Column(name = "exploracion_glandulas_salivales")
    private String glandulasSalivales;

    @Column(name = "exploracion_carrillos")
    private String carrillos;

    @Column(name = "exploracion_amigdalas")
    private String amigdalas;

    @Column(name = "exploracion_lengua")
    private String lengua;

    @Column(name = "exploracion_piso_boca")
    private String pisoBoca;

}
