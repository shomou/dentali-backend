package com.dentali.dentali_rest.doctores;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DoctorEspecialidadId implements Serializable{

    @Column(name = "doctor_id")
    private Long doctorId;

    @Column(name = "especialidad_id")
    private Long especialidadId;

}
