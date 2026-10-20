package com.dentali.dentali_rest.pacientes;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import com.dentali.dentali_rest.catalogos.EntidadFederativa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table (name = "pacientes")
@Getter 
@Setter 
@NoArgsConstructor 
public class Paciente {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name= "primer_apellido", nullable = false, length = 100)
    private String primerApellido;
    
    @Column(name= "segundo_apellido", length = 100)
    private String segundoApellido;

    @JdbcTypeCode(SqlTypes.CHAR) 
    @Column(unique = true)
    private String curp;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(nullable = false, length = 1)
    private Sexo sexo;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entidad_nacimiento", nullable = false)
    private EntidadFederativa entidadNacimiento;

    @Column(length = 150)
    private String email;

    @Column(length = 15)
    private String telefono;

    @Column(name = "foto_ruta", length = 255)
    private String fotoRuta;

    @Enumerated(EnumType.STRING) 
    @Column(nullable = false, length = 20)
    private EstadoPaciente estado = EstadoPaciente.ACTIVO;


    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private OffsetDateTime fechaCreacion;

    @UpdateTimestamp 
    @Column(name = "fecha_actualizacion")
    private OffsetDateTime fechaActualizacion;
}
