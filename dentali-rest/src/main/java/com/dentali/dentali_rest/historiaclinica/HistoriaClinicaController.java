package com.dentali.dentali_rest.historiaclinica;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.dentali.dentali_rest.historiaclinica.dto.HistoriaClinicaRequestDTO;
import com.dentali.dentali_rest.historiaclinica.dto.HistoriaClinicaResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping ("/api/pacientes/{pacienteId}/historia-clinica")
@RequiredArgsConstructor 
public class HistoriaClinicaController {

    private final HistoriaClinicaService historiaClinicaService;

    @GetMapping 
    public HistoriaClinicaResponseDTO obtener(@PathVariable  Long pacienteId){
        return historiaClinicaService.obtener(pacienteId);
    }

    @PostMapping 
    public ResponseEntity<HistoriaClinicaResponseDTO> crear (@PathVariable Long pacienteId,
                                                            @Valid @RequestBody HistoriaClinicaRequestDTO dto){
        HistoriaClinicaResponseDTO creada = historiaClinicaService.crear(pacienteId, dto);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        return ResponseEntity.created(ubicacion).body(creada);
    }

    @PutMapping
    public HistoriaClinicaResponseDTO actualizar(@PathVariable Long pacienteId,
                                             @Valid @RequestBody HistoriaClinicaRequestDTO dto) {
        return historiaClinicaService.actualizar(pacienteId, dto);
    }

}
