package com.dentali.dentali_rest.consultas;

import java.net.URI;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.dentali.dentali_rest.common.dto.PaginaResponseDTO;
import com.dentali.dentali_rest.consultas.dto.ConsultaRequestDTO;
import com.dentali.dentali_rest.consultas.dto.ConsultaResponseDTO;
import com.dentali.dentali_rest.consultas.dto.NotaAclaratoriaRequestDTO;
import com.dentali.dentali_rest.consultas.dto.NotaAclaratoriaResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping ("/api")
@RequiredArgsConstructor 
public class ConsultaController {

    private static final Sort ORDEN_CRONOLOGICO = Sort.by(Sort.Direction.DESC, "fechaAtencion");
    private final ConsultaService consultaService;

    // -------- Dentro del expediente del paciente --------
    @GetMapping ("pacientes/{pacienteId}/consultas")
    public PaginaResponseDTO<ConsultaResponseDTO> listar(@PathVariable Long pacienteId, Pageable pageable){
        Pageable cronologico = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), ORDEN_CRONOLOGICO);
        return PaginaResponseDTO.de(consultaService.listar(pacienteId, cronologico));
    }

    @PostMapping ("pacientes/{pacienteId}/consultas")
    public ResponseEntity<ConsultaResponseDTO> crear(@PathVariable Long pacienteId, @Valid @RequestBody ConsultaRequestDTO dto){
        ConsultaResponseDTO creada = consultaService.crear(pacienteId, dto);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/consultas/{id}")
                .buildAndExpand(creada.id())
                .toUri();    
        return ResponseEntity.created(ubicacion).body(creada);
    }

    // -------- Una consulta por id --------
    @GetMapping ("consultas/{id}")
    public ConsultaResponseDTO obtener(@PathVariable Long id){
        return consultaService.obtener(id);
    }

    @PutMapping ("consultas/{id}")
    public ConsultaResponseDTO actualizar(@PathVariable Long id, @Valid @RequestBody ConsultaRequestDTO dto){
        return consultaService.actualizar(id, dto);
    }

    @DeleteMapping ("consultas/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        consultaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping ("consultas/{id}/firmar")
    public ConsultaResponseDTO firmar(@PathVariable Long id){
        return consultaService.firmar(id);

    }

    // ---------- Notas aclaratorias (solo agregar y leer: nunca editar ni borrar) ----------
    @GetMapping("consultas/{consultaId}/notas-aclaratorias")
    public List<NotaAclaratoriaResponseDTO> listarNotas(@PathVariable Long consultaId) {
        return consultaService.listarNotas(consultaId);
    }

    @PostMapping("consultas/{consultaId}/notas-aclaratorias")
    public ResponseEntity<NotaAclaratoriaResponseDTO> agregarNota(@PathVariable Long consultaId,
                                                                  @Valid @RequestBody NotaAclaratoriaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(consultaService.agregarNota(consultaId, dto));
    }

}
