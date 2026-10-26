package com.dentali.dentali_rest.pacientes;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import com.dentali.dentali_rest.catalogos.EntidadFederativa;
import com.dentali.dentali_rest.catalogos.EstadoCivil;

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
@Table(name = "pacientes")
@Getter
@Setter
@NoArgsConstructor
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "primer_apellido", nullable = false, length = 100)
    private String primerApellido;

    @Column(name = "segundo_apellido", length = 100)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_civil_id")
    private EstadoCivil estadoCivil;

    @Column(length = 100)
    private String ocupacion;

    /** DATO PERSONAL SENSIBLE (LFPDPPP). */
    @Column(length = 60)
    private String religion;

    @Column(name = "domicilio_calle", length = 150)
    private String domicilioCalle;

    @Column(name = "domicilio_numero_exterior", length = 10)
    private String domicilioNumeroExterior;

    @Column(name = "domicilio_numero_interior", length = 10)
    private String domicilioNumeroInterior;

    @Column(name = "domicilio_colonia", length = 100)
    private String domicilioColonia;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "domicilio_codigo_postal", length = 5)
    private String domicilioCodigoPostal;

    @Column(name = "domicilio_municipio", length = 100)
    private String domicilioMunicipio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domicilio_entidad_id")
    private EntidadFederativa domicilioEntidad;

    @Column(name = "contacto_emergencia_nombre", length = 150)
    private String contactoEmergenciaNombre;

    @Column(name = "contacto_emergencia_telefono", length = 15)
    private String contactoEmergenciaTelefono;
}
