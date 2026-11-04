package com.dentali.dentali_rest.estudios;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstudioImagenRepository extends JpaRepository<EstudioImagen, Long>{

    @EntityGraph (attributePaths = {"tipoEstudio","doctor"})
    List<EstudioImagen> findByHistoriaClinicaIdOrderByFechaEstudioDescIdDesc(Long hisotiraClinicaId);

}
