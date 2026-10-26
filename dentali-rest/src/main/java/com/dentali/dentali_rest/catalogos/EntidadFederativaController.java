package com.dentali.dentali_rest.catalogos;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dentali.dentali_rest.catalogos.dto.EntidadFederativaResponseDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;


@RestController 
@RequestMapping ("/api/catalogos/entidades-federativas")
@RequiredArgsConstructor 
public class EntidadFederativaController {

    private final EntidadFederativaRepository repository;

    @GetMapping
    public List<EntidadFederativaResponseDTO> listar(){
        return repository.findAll(Sort.by("id")).stream()
                .map(e -> new EntidadFederativaResponseDTO(e.getId(), e.getNombre()))
                .toList();
    }

}
