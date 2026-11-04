package com.dentali.dentali_rest.estudios;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import org.hibernate.annotations.CreationTimestamp;


import com.dentali.dentali_rest.catalogos.TipoEstudioImagen;
import com.dentali.dentali_rest.consultas.Consulta;
import com.dentali.dentali_rest.doctores.Doctor;
import com.dentali.dentali_rest.historiaclinica.HistoriaClinica;

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
import lombok.Setter;

@Entity 
@Table (name = "estudios_imagen")
@Getter 
@NoArgsConstructor (access = AccessLevel.PROTECTED)
public class EstudioImagen {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    // ------------- Fijos: relaciones ---------------
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "historia_clinica_id", nullable = false, updatable = false)
    private HistoriaClinica historiaClinica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "consulta_id", updatable = false)
    private Consulta consulta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "doctor_id", nullable = false, updatable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "tipo_estudio_id", nullable = false, updatable = false)
    private TipoEstudioImagen tipoEstudio;

    @Column (name = "fecha_estudio", nullable = false, updatable = false)
    private LocalDate fechaEstudio;

    // ----------- Fijos: archivo --------------
    @Column(name = "archivo_ruta", nullable = false, updatable = false, unique = true)
    private String archivoRuta;

    @Column(name = "nombre_original", nullable = false, updatable = false)
    private String nombreOriginal;

    @Column(name = "tipo_contenido", nullable = false, updatable = false)
    private String tipoContenido;

    @Column(name = "tamano_bytes", nullable = false, updatable = false)
    private Long tamanoBytes;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private OffsetDateTime fechaCreacion;

    // ----------- Editables --------------
    @Setter 
    private String descripcion;

    @Setter 
    private String interpretacion;

    // ---------- Unica puerta de entrada --------
    public EstudioImagen(HistoriaClinica historiaClinica, Consulta consulta, Doctor doctor,
                         TipoEstudioImagen tipoEstudio, LocalDate fechaEstudio,
                         String archivoRuta, String nombreOriginal, String tipoContenido, Long tamanoBytes) {
        this.historiaClinica = historiaClinica;
        this.consulta = consulta;
        this.doctor = doctor;
        this.tipoEstudio = tipoEstudio;
        this.fechaEstudio = fechaEstudio;
        this.archivoRuta = archivoRuta;
        this.nombreOriginal = nombreOriginal;
        this.tipoContenido = tipoContenido;
        this.tamanoBytes = tamanoBytes;
    }

    

}
