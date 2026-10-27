package com.dentali.dentali_rest.historiaclinica;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum GrupoSanguineo {

    A_POSITIVO("A+"),
    A_NEGATIVO("A-"),
    B_POSITIVO("B+"),
    B_NEGATIVO("B-"),
    AB_POSITIVO("AB+"),
    AB_NEGATIVO("AB-"),
    O_POSITIVO("O+"),
    O_NEGATIVO("O-");

    private final String codigo;

    GrupoSanguineo(String codigo) {
        this.codigo = codigo;
    }

    @JsonValue
    public String getCodigo() {
        return codigo;
    }

    @JsonCreator
    public static GrupoSanguineo desdeCodigo(String codigo) {
        if (codigo == null) {
            return null;
        }
        for (GrupoSanguineo grupo : values()) {
            if (grupo.codigo.equals(codigo)) {
                return grupo;
            }
        }
        throw new IllegalArgumentException("Grupo sanguíneo inválido: " + codigo);
    }

}
