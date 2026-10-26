package com.dentali.dentali_rest.catalogos;


import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table (name = "entidades_federativas")
@Immutable 
@Getter 
@Setter 
@NoArgsConstructor 
public class EntidadFederativa {

    @Id 
    private Short id;

    @Column(nullable = false, length = 60, unique = true)
    private String nombre;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "abreviatura_curp", nullable = false, length = 2, unique = true)
    private String abreviaturaCurp;

}
