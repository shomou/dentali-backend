package com.dentali.dentali_rest.common.exception;

public class RecursoNoEncontradoException extends RuntimeException{

    public RecursoNoEncontradoException(String recurso, Object id) {
        super(recurso + " con id " + id + " no encontrado");
    }

    /** Mensaje libre, para casos que no son "un recurso con un id". */
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
    
}
