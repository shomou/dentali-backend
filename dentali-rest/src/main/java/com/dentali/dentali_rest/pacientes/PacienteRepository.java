package com.dentali.dentali_rest.pacientes;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long>{
    boolean existsByCurpAndIdNot(String curp,Long id);
}
