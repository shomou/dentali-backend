package com.dentali.dentali_rest.catalogos;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dentali.dentali_rest.catalogos.dto.CatalogoResponseDTO;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;


@RestController 
@RequestMapping ("/api/catalogos")
@RequiredArgsConstructor 
public class CatalogoController {

    private final EntidadFederativaRepository entidadFederativaRepository;
    private final EstadoCivilRepository estadoCivilRepository;

    @GetMapping("/entidades-federativas")
    public List<CatalogoResponseDTO> listar(){
        return entidadFederativaRepository.findAll(Sort.by("id")).stream()
                .map(e -> new CatalogoResponseDTO(e.getId(), e.getNombre()))
                .toList();
    }


    @GetMapping("/estados-civiles")
    public List<CatalogoResponseDTO> listarEstadosCiviles() {
        return estadoCivilRepository.findAll(Sort.by("id")).stream()
            .map(e -> new CatalogoResponseDTO(e.getId(), e.getNombre()))
            .toList();
    }

}
