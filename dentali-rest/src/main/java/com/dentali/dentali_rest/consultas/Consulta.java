package com.dentali.dentali_rest.consultas;

import java.time.OffsetDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.dentali.dentali_rest.common.exception.ReglaNegocioException;
import com.dentali.dentali_rest.doctores.Doctor;
import com.dentali.dentali_rest.historiaclinica.HistoriaClinica;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
import lombok.Setter;

@Entity 
@Table (name = "consultas")
@Getter 
@Setter 
@NoArgsConstructor 
public class Consulta {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "historia_clinica_id", nullable = false)
    private HistoriaClinica historiaClinica;

    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column (name = "fecha_atencion", nullable = false)
    private OffsetDateTime fechaAtencion;

    // ---------- Nota de evolución ----------
    @Column (name = "motivo_consulta", nullable = false)
    private String motivoConsulta;

    private String exploracion;
    private String diagnostico;
    private String tratamiento;
    private String indicaciones;
    private String pronostico;

    // ---------- Signos vitales ----------
    @Embedded 
    private SignosVitales signosVitales;

    // ---------- Firma ----------
    @Column(name = "firmada_en")
    @Setter (AccessLevel.NONE)
    private OffsetDateTime firmadaEn;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private OffsetDateTime fechaCreacion;

    // ---------- Comportamiento ----------
    public boolean estaFirmada() {
        return firmadaEn != null;
    }

    public void firmar() {
        if (estaFirmada()) {
            throw new ReglaNegocioException("La consulta con id " + id + " ya está firmada");
        }
        firmadaEn = OffsetDateTime.now();
    }

    public SignosVitales getSignosVitales() {
        if (signosVitales == null) {
            signosVitales = new SignosVitales();
        }
        return signosVitales;
    }
}
