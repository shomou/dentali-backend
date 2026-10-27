package com.dentali.dentali_rest.historiaclinica;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoriaClinicaRepository extends JpaRepository<HistoriaClinica, Long>{

    Optional<HistoriaClinica> findByPacienteId(Long oacienteId);

    boolean existsByPacienteId(Long pacienteId);

}
