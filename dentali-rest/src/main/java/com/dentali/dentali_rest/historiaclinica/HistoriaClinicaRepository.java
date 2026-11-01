package com.dentali.dentali_rest.historiaclinica;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoriaClinicaRepository extends JpaRepository<HistoriaClinica, Long>{

    @EntityGraph(attributePaths = {"paciente", "doctorApertura"})
    Optional<HistoriaClinica> findByPacienteId(Long pacienteId);

    boolean existsByPacienteId(Long pacienteId);

}
