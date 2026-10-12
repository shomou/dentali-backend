package com.dentali.dentali_rest.doctores;

import com.dentali.dentali_rest.especialidades.Especialidad;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "doctor_especialidades")
@Getter
@Setter
@NoArgsConstructor
public class DoctorEspecialidad {

    @EmbeddedId
    private DoctorEspecialidadId id = new DoctorEspecialidadId();

    @MapsId("doctorId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @MapsId("especialidadId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "especialidad_id")
    private Especialidad especialidad;

    @Column(name = "cedula_especialidad", nullable = false, length = 20, unique = true)
    private String cedulaEspecialidad;

    public DoctorEspecialidad(Doctor doctor, Especialidad especialidad, String cedulaEspecialidad) {
        this.doctor = doctor;
        this.especialidad = especialidad;
        this.cedulaEspecialidad = cedulaEspecialidad;
    }
}
