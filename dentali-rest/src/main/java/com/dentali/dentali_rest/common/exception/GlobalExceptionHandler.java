package com.dentali.dentali_rest.common.exception;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ProblemDetail manejarNoEncontrado(RecursoNoEncontradoException ex) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problema.setTitle("Recurso no encontrado");
        return problema;
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ProblemDetail manejarReglaNegocio(ReglaNegocioException ex) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problema.setTitle("Conflicto con una regla de negocio");
        return problema;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail manejarValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errores.putIfAbsent(error.getField(), error.getDefaultMessage()));

        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "La petición tiene datos inválidos");
        problema.setTitle("Error de validación");
        problema.setProperty("errores", errores);
        return problema;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail manejarIntegridad(DataIntegrityViolationException ex) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, "La operación viola una restricción de la base de datos");
        problema.setTitle("Conflicto de datos");
        return problema;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ProblemDetail manejarArchivoGrande(MaxUploadSizeExceededException ex) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONTENT_TOO_LARGE, "El archivo excede el tamaño máximo permitido (5 MB)");
        problema.setTitle("Archivo demasiado grande");
        return problema;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail manejarCuerpoIlegible(HttpMessageNotReadableException ex) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "El cuerpo de la petición no es válido. Revisa el formato de fechas (AAAA-MM-DD) y los valores permitidos.");
        problema.setTitle("Peticion mal formada");
        return problema;
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ProblemDetail manejarOrdenInvalido(PropertyReferenceException ex) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "No se puede ordenar por '" + ex.getPropertyName() + "'");
        problema.setTitle("Parámetro de orden inválido");
        return problema;
    }

    /**
     * Errores lanzados por triggers de PostgreSQL (RAISE EXCEPTION … ERRCODE
     * 'P0001'):
     * consultas firmadas y notas aclaratorias. Son reglas de negocio → 409.
     */
    @ExceptionHandler(DataAccessException.class)
    public ProblemDetail manejarAccesoDatos(DataAccessException ex) {
        SQLException sql = buscarSqlException(ex);
        if (sql != null && "P0001".equals(sql.getSQLState())) {
            ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                    HttpStatus.CONFLICT, limpiarMensajePostgres(sql.getMessage()));
            problema.setTitle("Conflicto con una regla de negocio");
            return problema;
        }
        throw ex; // cualquier otro error de BD no es nuestro caso: que siga su camino
    }

    /** Recorre los "Caused by" hasta encontrar la SQLException original. */
    private SQLException buscarSqlException(Throwable ex) {
        Throwable actual = ex;
        while (actual != null) {
            if (actual instanceof SQLException sql) {
                return sql;
            }
            actual = actual.getCause();
        }
        return null;
    }

    /**
     * "ERROR: La consulta 5 está firmada…\n Where: PL/pgSQL…" → "La consulta 5 está
     * firmada…"
     */
    private String limpiarMensajePostgres(String mensaje) {
        if (mensaje == null) {
            return "La operación viola una regla del expediente";
        }
        String primeraLinea = mensaje.lines().findFirst().orElse(mensaje);
        return primeraLinea.replaceFirst("^ERROR:\\s*", "");
    }

}
