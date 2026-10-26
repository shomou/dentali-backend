package com.dentali.dentali_rest.pacientes;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PacienteRepository extends JpaRepository<Paciente, Long>{
    
    boolean existsByCurpAndIdNot(String curp,Long id);
    
    @EntityGraph(attributePaths = "entidadNacimiento")    
    Page<Paciente> findByEstado(EstadoPaciente estado, Pageable pageable);

    @Query("""
        SELECT p FROM Paciente p
        WHERE p.estado = :estado
            AND (LOWER(CONCAT(p.nombre, ' ', p.primerApellido, ' ', COALESCE(p.segundoApellido,'')))
                    LIKE LOWER(CONCAT('%', :texto, '%'))
                    OR p.curp = UPPER(:texto))
        """)
    Page<Paciente> buscar(@Param("estado") EstadoPaciente estado,
                            @Param("texto") String texto,
                            Pageable pageable);

}
