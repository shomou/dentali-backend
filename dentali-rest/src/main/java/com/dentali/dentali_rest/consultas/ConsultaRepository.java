package com.dentali.dentali_rest.consultas;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultaRepository extends JpaRepository<Consulta, Long>{
    @EntityGraph (attributePaths = {"doctor"})
    Page<Consulta> findByHistoriaClinicaId(Long historiaClinicaId, Pageable pageable);
}
