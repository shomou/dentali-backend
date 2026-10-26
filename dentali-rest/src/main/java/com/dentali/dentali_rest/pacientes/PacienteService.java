package com.dentali.dentali_rest.pacientes;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dentali.dentali_rest.catalogos.EntidadFederativa;
import com.dentali.dentali_rest.catalogos.EntidadFederativaRepository;
import com.dentali.dentali_rest.common.exception.RecursoNoEncontradoException;
import com.dentali.dentali_rest.common.exception.ReglaNegocioException;
import com.dentali.dentali_rest.common.storage.FileStorageService;
import com.dentali.dentali_rest.pacientes.dto.PacienteRequestDTO;
import com.dentali.dentali_rest.pacientes.dto.PacienteResponseDTO;
import com.dentali.dentali_rest.pacientes.mapper.PacienteMapper;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
@Transactional (readOnly = true)
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final EntidadFederativaRepository entidadFederativaRepository;
    private final PacienteMapper pacienteMapper;
    private final FileStorageService fileStorageService;

    // ------------------ Consultas ---------------------
    public Page<PacienteResponseDTO> buscar(String  texto, Pageable pageable){
        Page<Paciente> pagina = (texto == null || texto.isBlank())
        ? pacienteRepository.findByEstado(EstadoPaciente.ACTIVO, pageable)
        : pacienteRepository.buscar(EstadoPaciente.ACTIVO, texto.trim(), pageable);

        return pagina.map(pacienteMapper::toResponse);
    }

    public PacienteResponseDTO obtenerPorId(Long id){
        return pacienteMapper.toResponse(buscarPaciente(id));
    }

    // --------------- Escrituras ---------------------

    @Transactional
    public PacienteResponseDTO crear(PacienteRequestDTO dto){
        EntidadFederativa entidad = validarYObtenerEntidad(dto, null);

        Paciente paciente = pacienteMapper.toEntity(dto);
        paciente.setEntidadNacimiento(entidad);

        return pacienteMapper.toResponse(pacienteRepository.save(paciente)); 
    }

    @Transactional
    public PacienteResponseDTO actualizar(Long id, PacienteRequestDTO dto) {
        Paciente paciente = buscarPaciente(id);
        EntidadFederativa entidad = validarYObtenerEntidad(dto, id);

        pacienteMapper.actualizarEntidad(dto, paciente);
        paciente.setEntidadNacimiento(entidad);

        return pacienteMapper.toResponse(paciente);   // dirty checking
    }

    @Transactional 
    public void desactivar(Long id){
        buscarPaciente(id).setEstado(EstadoPaciente.DESACTIVADO);
    }


    @Transactional
    public void reactivar(Long id) {
        Paciente paciente = buscarPaciente(id);
        if (paciente.getEstado() == EstadoPaciente.ACTIVO) {
            throw new ReglaNegocioException("El paciente con id " + id + " ya está activo");
        }
        paciente.setEstado(EstadoPaciente.ACTIVO);
    }

    @Transactional
    public PacienteResponseDTO actualizarFoto(Long id, MultipartFile archivo){
        Paciente paciente = buscarPaciente(id);

        String rutaAnterior = paciente.getFotoRuta();
        String rutaNueva = fileStorageService.guardarImagen(archivo, "pacientes/" + id);
        paciente.setFotoRuta(rutaNueva);

        fileStorageService.limpiarAlTerminarTransaccion(rutaAnterior, rutaNueva);
        
        return pacienteMapper.toResponse(paciente);
    }

    public Resource obtenerFoto(Long id){
        Paciente paciente = buscarPaciente(id);

        if(paciente.getFotoRuta() == null){
            throw new RecursoNoEncontradoException("Foto del paciente", id);
        }

        return fileStorageService.cargar(paciente.getFotoRuta())
                .orElseThrow(()-> new RecursoNoEncontradoException("Foto del paciente", id));
    }

    // --------------- Ayudantes privados ----------------
    private Paciente buscarPaciente(Long id){
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente", id));
    }

    private EntidadFederativa validarYObtenerEntidad(PacienteRequestDTO dto, Long idActual){
        // 1. CURP única (solo si viene)
        if(dto.curp() != null){
            Long excluir = (idActual == null) ? 0L : idActual;
            if (pacienteRepository.existsByCurpAndIdNot(dto.curp(), excluir)) {
                throw new ReglaNegocioException("Ya existe un paciente con la CURP " + dto.curp());
            }
        }

        // 2. La entidad debe existir en el catálogo
        EntidadFederativa entidad = entidadFederativaRepository.findById(dto.entidadNacimientoId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Entidad federativa", dto.entidadNacimientoId()));

        // 3. La CURP debe coincidir con los datos capturados
        ValidadorCurp.validarCoherencia(
                dto.curp(), dto.fechaNacimiento(), dto.sexo(), entidad.getAbreviaturaCurp());

        return entidad;
    }

}
