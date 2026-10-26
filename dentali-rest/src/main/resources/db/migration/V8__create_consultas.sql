CREATE TABLE consultas(
    id                      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    historia_clinica_id     BIGINT NOT NULL
        CONSTRAINT fk_consultas_historia REFERENCES historias_clinicas(id),
    doctor_id               BIGINT NOT NULL
        CONSTRAINT fk_consultas_doctor  REFERENCES doctores (id),
    fecha_atencion          TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    -- ------------ Nora evolución  -------------
    motivo_consulta         TEXT        NOT NULL,
    exploracion             TEXT,
    diagnostico             TEXT,
    tratamiento             TEXT,
    indicaciones            TEXT,
    pronostico              TEXT,

    -- ------------ Signos Vitales --------------
    presion_sistolica       SMALLINT
        CONSTRAINT chk_consultas_sistolica CHECK (presion_sistolica BETWEEN 50 and 300),
    presion_diastolica      SMALLINT
        CONSTRAINT chk_consultas_diastolica CHECK (presion_diastolica BETWEEN 30 AND 200),
    frecuencia_cardiaca     SMALLINT
        CONSTRAINT chk_consultas_fc         CHECK (frecuencia_cardiaca BETWEEN 20 AND 250),
    temperatura_c           NUMERIC(4,1)
        CONSTRAINT chk_consultas_temp       CHECK (temperatura_c BETWEEN 30 AND 45),

    peso_kg                 NUMERIC(5,2)
        CONSTRAINT chk_consultas_peso CHECK (peso_kg BETWEEN 1 and 400),
    talla_m                 NUMERIC(3,2)
        CONSTRAINT chk_consultas_talla CHECK (talla_m BETWEEN 0.30 and 2.50),
    frecuencia_respiratoria SMALLINT
        CONSTRAINT chk_consultas_fr CHECK (frecuencia_respiratoria BETWEEN 5 and 80),
    saturacion_o2 SMALLINT
        CONSTRAINT chk_consultas_saturacion CHECK (saturacion_o2 BETWEEN 30 and 100),
    
    -- ------------------ Firma ------------
    firmada_en              TIMESTAMPTZ,
    fecha_creacion          TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT chk_consultas_presion_completa
        CHECK ((presion_sistolica IS NULL) = (presion_diastolica IS NULL)),
    CONSTRAINT chk_consultas_presion_coherente
        CHECK (presion_sistolica IS NULL OR presion_sistolica > presion_diastolica)
);

CREATE INDEX idx_consultas_historia_fecha ON consultas (historia_clinica_id, fecha_atencion DESC);

-- =========== El candado ==============
CREATE FUNCTION fn_proteger_consulta_firmada() RETURNS trigger AS $$
BEGIN
    IF OLD.firmada_en IS NOT NULL THEN
        RAISE EXCEPTION 'La consulta % está firmada y no puede modificarse ni eliminarse', OLD.id
            USING ERRCODE = 'P0001';
    END IF;

    IF TG_OP = 'DELETE' THEN
        RETURN OLD;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_proteger_consulta_firmada
    BEFORE UPDATE OR DELETE ON consultas
    FOR EACH ROW
    EXECUTE FUNCTION fn_proteger_consulta_firmada();
