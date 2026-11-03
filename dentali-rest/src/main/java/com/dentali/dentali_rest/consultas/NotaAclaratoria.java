package com.dentali.dentali_rest.consultas;

import java.time.OffsetDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;

import com.dentali.dentali_rest.doctores.Doctor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity 
@Table (name = "notas_aclaratorias")
@Immutable 
@NoArgsConstructor (access = AccessLevel.PROTECTED)
@Getter 
public class NotaAclaratoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;
    
    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "consulta_id", nullable = false)
    private Consulta consulta;
    
    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "doctor_id", nullable = false)
    private Doctor doctor;
    
    @Column(nullable = false)
    private String texto;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private OffsetDateTime fechaCreacion;

    public NotaAclaratoria(Consulta consulta, Doctor doctor, String texto) {
        this.consulta = consulta;
        this.doctor = doctor;
        this.texto = texto;
    }
}
