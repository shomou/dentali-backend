package com.dentali.dentali_rest.doctores;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface DoctorRepository extends JpaRepository<Doctor, Long>{

    Optional<Doctor> findByCurp(String curp);

    boolean existsByEmail(String email);

    boolean existsByCedulaProfesional(String cedulaProfesional);

    List<Doctor> findByEstado(EstadoDoctor estado);

}
