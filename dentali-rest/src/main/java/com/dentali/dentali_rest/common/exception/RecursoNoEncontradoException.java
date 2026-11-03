package com.dentali.dentali_rest.common.exception;

public class RecursoNoEncontradoException extends RuntimeException{

    public RecursoNoEncontradoException(String recurso, Object id) {
        super("No se encontró " + recurso + " con id " + id);
    }

    /** Mensaje libre, para casos que no son "un recurso con un id". */
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
    
}
