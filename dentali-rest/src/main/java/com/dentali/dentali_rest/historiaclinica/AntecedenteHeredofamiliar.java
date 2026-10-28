package com.dentali.dentali_rest.historiaclinica;

import com.dentali.dentali_rest.catalogos.Enfermedad;
import com.dentali.dentali_rest.catalogos.Parentesco;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Embeddable 
@Getter 
@NoArgsConstructor 
public class AntecedenteHeredofamiliar {

    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "enfermedad_id", nullable = false)
    private Enfermedad enfermedad;

    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "parentesco_id", nullable = false)
    private Parentesco parentesco;

    public AntecedenteHeredofamiliar(Enfermedad enfermedad, Parentesco parentesco){
        this.enfermedad = enfermedad;
        this.parentesco = parentesco;
    }

    @Override 
    public boolean equals(Object o){
        if (this == o) return true;
        if(!(o instanceof AntecedenteHeredofamiliar otro)) return false;
        return Objects.equals(enfermedad.getId(), otro.enfermedad.getId())
                && Objects.equals(parentesco.getId(), otro.parentesco.getId());
    }

    @Override 
    public int hashCode(){
        return Objects.hash(enfermedad.getId(), parentesco.getId());
    }
}
