package com.dentali.dentali_rest.historiaclinica;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter (autoApply = true)
public class GrupoSanguineoConverter implements AttributeConverter<GrupoSanguineo, String> {

    @Override
    public String convertToDatabaseColumn(GrupoSanguineo grupo) {
        return grupo == null ? null : grupo.getCodigo();
    }

    @Override
    public GrupoSanguineo convertToEntityAttribute(String codigo) {
        return GrupoSanguineo.desdeCodigo(codigo);
    }

}
