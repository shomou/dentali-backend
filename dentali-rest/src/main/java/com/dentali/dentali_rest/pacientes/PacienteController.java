package com.dentali.dentali_rest.pacientes;

import org.springframework.data.domain.Sort;

import java.net.URI;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import com.dentali.dentali_rest.common.dto.PaginaResponseDTO;
import com.dentali.dentali_rest.pacientes.dto.PacienteRequestDTO;
import com.dentali.dentali_rest.pacientes.dto.PacienteResponseDTO;
import org.springframework.core.io.Resource;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor 
public class PacienteController {

    private final PacienteService pacienteService;

    @GetMapping 
    public PaginaResponseDTO<PacienteResponseDTO> buscar(
        @RequestParam (required = false) String texto,
        @PageableDefault (size = 20, sort = {"primerApellido", "segundoApellido", "nombre"},
                            direction = Sort.Direction.ASC) Pageable pageable
    ){
        return PaginaResponseDTO.de(pacienteService.buscar(texto, pageable));
    }

    @GetMapping ("/{id}")
    public PacienteResponseDTO obtenerPorId(@PathVariable Long id){
        return pacienteService.obtenerPorId(id);
    }

    @PostMapping 
    public ResponseEntity<PacienteResponseDTO> crear(@Valid @RequestBody PacienteRequestDTO dto){
        PacienteResponseDTO creado = pacienteService.crear(dto);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.id())
                .toUri();
        return ResponseEntity.created(ubicacion).body(creado);
    }

    @PutMapping ("/{id}")
    public PacienteResponseDTO actualizar(@PathVariable Long id, 
                                            @Valid @RequestBody PacienteRequestDTO dto){
        return pacienteService.actualizar(id, dto);
    }

    @PatchMapping ("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable Long id){
        pacienteService.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping ("/{id}/reactivar")
    public ResponseEntity<Void> reactivar(@PathVariable Long id){
        pacienteService.reactivar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}/foto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PacienteResponseDTO subirFoto(@PathVariable Long id,
                                        @RequestParam("archivo") MultipartFile archivo){
        return pacienteService.actualizarFoto(id, archivo);
    }

    @GetMapping("/{id}/foto")
    public ResponseEntity<Resource> obtenerFoto(@PathVariable Long id) {
        Resource foto = pacienteService.obtenerFoto(id);
        MediaType tipo = MediaTypeFactory.getMediaType(foto).orElse(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok().contentType(tipo).body(foto);
    }
}
