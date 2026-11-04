package com.dentali.dentali_rest.catalogos;

import org.hibernate.annotations.Immutable; 

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity 
@Table (name = "tipos_estudio_imagen")
@Immutable 
@Getter 
@NoArgsConstructor 
public class TipoEstudioImagen {
     @Id 
    private Short id;

    @Column(nullable = false, length = 80, unique = true)
    private String nombre;
}
