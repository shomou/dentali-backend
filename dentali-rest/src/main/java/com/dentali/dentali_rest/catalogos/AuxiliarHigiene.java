package com.dentali.dentali_rest.catalogos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;


@Entity 
@Table (name = "auxiliares_higiene")
@Immutable 
@Getter 
@NoArgsConstructor
public class AuxiliarHigiene {

    @Id 
    private Short id;

    @Column(nullable = false, length = 40, unique = true)
    private String nombre;

}
