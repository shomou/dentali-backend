package com.dentali.dentali_rest.doctores;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.dentali.dentali_rest.doctores.dto.DoctorRequestDTO;
import com.dentali.dentali_rest.doctores.dto.DoctorResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/doctores")
@RequiredArgsConstructor 
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping 
    public List<DoctorResponseDTO> listarActivos(){
        return doctorService.listarActivos();
    }

    @GetMapping("/{id}")
    public DoctorResponseDTO obtenerPorId(@PathVariable Long id){
        return doctorService.obtenerPorId(id);
    }

    @PostMapping 
    public ResponseEntity<DoctorResponseDTO> crear(@Valid @RequestBody DoctorRequestDTO dto){
        DoctorResponseDTO creado = doctorService.crear(dto);

        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(creado.id())
            .toUri();
        
        return ResponseEntity.created(ubicacion).body(creado);
    }

    @PutMapping 
    public DoctorResponseDTO actualizar(@PathVariable  Long id, 
                                        @Valid @RequestBody DoctorRequestDTO dto){
            return doctorService.actualizar(id, dto);
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable Long id){
        doctorService.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reactivar")
    public ResponseEntity<Void> reactivar(@PathVariable Long id){
        doctorService.reactivar(id);
        return ResponseEntity.noContent().build();
    }


}
