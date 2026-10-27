package com.dentali.dentali_rest.catalogos;

import org.springframework.data.annotation.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity 
@Table (name = "enfermedades")
@Immutable 
@Getter 
@NoArgsConstructor 
public class Enfermedad {

    @Id 
    private Short id;

    @Column (nullable = false, length = 60, unique = true)
    private String nombre;

    @Column (name = "aplica_personal", nullable = false)
    private boolean aplicaPersonal;

    @Column(name = "aplica_heredofamiliar", nullable = false)
    private boolean aplicaHeredoFamiliar;

}
