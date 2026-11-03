package com.dentali.dentali_rest.consultas;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotaAclaratoriaRepository extends JpaRepository<NotaAclaratoria, Long>{

    @EntityGraph(attributePaths = {"doctor"})
    List<NotaAclaratoria> findByConsultaIdOrderByFechaCreacionAsc(Long consultaId);

}
