package com.dentali.dentali_rest.doctores;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import com.dentali.dentali_rest.especialidades.Especialidad;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "doctores")
@Getter
@Setter
@NoArgsConstructor
public class Doctor {

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
    @Column(nullable = false, length = 18, unique = true)
    private String curp;

    @Column(name = "cedula_profesional", nullable = false, length = 20, unique = true)
    private String cedulaProfesional;

    @Column(nullable = false, length = 150, unique = true)
    private String email;

    @Column(length = 15)
    private String telefono;

    @Column(name = "foto_ruta", length = 255)
    private String fotoRuta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoDoctor estado = EstadoDoctor.ACTIVO;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private OffsetDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private OffsetDateTime fechaActualizacion;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DoctorEspecialidad> especialidades = new HashSet<>();


    public void agregarEspecialidad(Especialidad especialidad, String cedulaEspecialidad) {
        especialidades.add(new DoctorEspecialidad(this, especialidad, cedulaEspecialidad));
    }

    public void quitarEspecialidad(Especialidad especialidad) {
        especialidades.removeIf(de -> de.getEspecialidad().getId().equals(especialidad.getId()));
    }

}
