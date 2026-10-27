package com.dentali.dentali_rest.historiaclinica;

import java.time.OffsetDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.dentali.dentali_rest.doctores.Doctor;
import com.dentali.dentali_rest.pacientes.Paciente;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table (name = "historias_clinicas")
@Getter 
@Setter 
@NoArgsConstructor 
public class HistoriaClinica {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "paciente_id", nullable = false, unique = true)
    private Paciente paciente;

    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "doctor_apertura_id", nullable = false)
    private Doctor doctorApertura;

    @CreationTimestamp 
    @Column (name = "fecha_apertura", nullable = false, updatable = false)
    private OffsetDateTime fechaApertura;

    @UpdateTimestamp 
    @Column (name = "fecha_actualizacion")
    private OffsetDateTime fechaActualizacion;

    @Column (name = "grupo_sanguineo", length = 3)
    private GrupoSanguineo grupoSanguineo; //usa elñ convertidor autoApply

    // --------- Antecedentes personales patológicos  ----------------
    @Column (name = "enfermedades_ultimo_anio")
    private String enfermedadesUltimoAnio;

    @Column (name = "antecedente_patologico_otro")
    private String antecedentesPatologicoOtro;

    @Column (name = "alergico_medicamentos")
    private Boolean alergicoMedicamentos;

    @Column (name = "alergias_detalle")
    private String alergiasDetalle;

    @Column (name = "anestesiado_previamente")
    private Boolean anestesiadoPreviamente;

    @Column (name = "problemas_anestesia")
    private Boolean problemasAnestesia;

    @Column (name = "problemas_anestesia_detalle")
    private String problemasAnestesiaDetalle;

    @Column (name = "hemorragias_frecuentes")
    private Boolean hemorragiasFrecuentes;

    @Column (name = "sangrado_abundante")
    private Boolean sangradoAbundante;

    private Boolean hospitalizado;
    
    @Column (name = "hospitalizacion_motivo")
    private String hospitalizacionMotivo;

    private Boolean cirugias;

    @Column (name = "cirugias_motivo")
    private String cirugiasMotivo;

    @Column (name = "enfermedad_actual")
    private Boolean enfermedadActual;

    @Column (name = "enfermedad_actual_detalle")
    private String enfermedadActualDetalle;

    @Column (name = "toma_medicamentos")
    private Boolean tomaMedicamentos;

    @Column (name = "medicamentos_detalle")
    private String medicamentosDetalle;

    private Boolean bifosfonatos;

    @Column (name = "bifosfonatos_detalle")
    private String bifosfonatosDetalle;

    // --------- Antecedentes no patológicos ----------------
    @Column (name = "cepillados_por_dia")
    private Short cepilladosPorDia;

    @Column (name = "auxiliar_higiene_otro", length = 100)
    private String auxiliarHigieneOtro;

    private Boolean fuma;

    @Column (name = "fuma_frecuencia", length = 100)
    private String fumaFrecuancia;

    private Boolean alcohol;

    @Column (name = "alcohol_frecuencia", length = 100)
    private String alcoholFrecuencia;

    @Column (name = "otras_sustancias")
    private Boolean otrasSustancias;

    @Column (name = "otras_sustancias_frecuencia", length = 100)
    private String otrasSustanciasFrecuencia;

    private Boolean embarazada;

    @Column (name = "semanas_gestacion")
    private Short semanasGestacion;

    private Boolean lactando;

    // --------------- Exploración -----------
    @Embedded 
    private ExploracionTejidosBlandos exploracion;

    @Column(name = "atm_observaciones")
    private String observaciones;

    // --------------- Plan de tratamiento -----------
    @Column(name = "plan_tratamiento")
    private String planTratamiento;

    public ExploracionTejidosBlandos getExploracion(){
        if(exploracion == null){
            exploracion = new ExploracionTejidosBlandos();
        }
        return exploracion;
    }


}
