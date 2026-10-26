CREATE TABLE notas_aclaratorias(
    id                      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    consulta_id     BIGINT NOT NULL
        CONSTRAINT fk_consultas_notas REFERENCES consultas (id),
    doctor_id       BIGINT NOT NULL
        CONSTRAINT fk_hc_doctor_notas REFERENCES doctores(id),
    texto           TEXT    NOT NULL
     CONSTRAINT chk_notas_texto CHECK (btrim(texto) <> ''),
    fecha_creacion  TIMESTAMPTZ NOT NULL DEFAULT NOW()
    );


-- "Las notas de esta consulta, en orden cronológico"
CREATE INDEX idx_notas_aclaratorias_consulta_fecha
ON notas_aclaratorias (consulta_id, fecha_creacion);


-- ================= El candado: append-only ============

CREATE FUNCTION fn_notas_aclaratorias_inmutables() RETURNS trigger AS $$
BEGIN
    RAISE EXCEPTION 'La nota aclaratoria % no puede modificarse ni eliminarse', OLD.id
        USING ERRCODE = 'P0001';
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER trg_notas_aclaratorias_inmutables
    BEFORE UPDATE OR DELETE ON notas_aclaratorias
    FOR EACH ROW
    EXECUTE FUNCTION fn_notas_aclaratorias_inmutables();