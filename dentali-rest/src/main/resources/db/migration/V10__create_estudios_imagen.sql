-- ============================================================
-- V10: Estudios de imagen (radiografías, tomografías, fotografías)
-- Archivo fijo (no se reemplaza ni se borra); solo descripcion e
-- interpretacion son editables. NOM-004: el expediente se conserva.
-- ============================================================

-- 1. Catálogo (primero: estudios_imagen lo referencia)
CREATE TABLE tipos_estudio_imagen (
    id      SMALLINT    PRIMARY KEY,
    nombre  VARCHAR(80) NOT NULL UNIQUE
);

INSERT INTO tipos_estudio_imagen (id, nombre) VALUES
    (1, 'Periapical'),
    (2, 'Aleta de mordida'),
    (3, 'Oclusal'),
    (4, 'Panorámica'),
    (5, 'Cefalométrica'),
    (6, 'Tomografía (CBCT)'),
    (7, 'Fotografía clínica'),
    (8, 'Otro');

-- 2. Estudios
CREATE TABLE estudios_imagen (
    id                   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    historia_clinica_id  BIGINT NOT NULL
        CONSTRAINT fk_estudios_historia REFERENCES historias_clinicas (id),
    consulta_id          BIGINT
        CONSTRAINT fk_estudios_consulta REFERENCES consultas (id),
    doctor_id            BIGINT NOT NULL
        CONSTRAINT fk_estudios_doctor REFERENCES doctores (id),
    tipo_estudio_id      SMALLINT NOT NULL
        CONSTRAINT fk_estudios_tipo REFERENCES tipos_estudio_imagen (id) ON DELETE RESTRICT,
    fecha_estudio        DATE NOT NULL,
    descripcion          TEXT,
    interpretacion       TEXT,
    archivo_ruta         VARCHAR(255) NOT NULL
        CONSTRAINT uq_estudios_archivo_ruta UNIQUE,
    nombre_original      VARCHAR(255) NOT NULL,
    tipo_contenido       VARCHAR(100) NOT NULL
        CONSTRAINT chk_estudios_tipo_contenido
        CHECK (tipo_contenido IN ('image/jpeg', 'image/png', 'image/webp', 'application/pdf')),
    tamano_bytes         BIGINT NOT NULL
        CONSTRAINT chk_estudios_tamano
        CHECK (tamano_bytes BETWEEN 1 AND 20971520),          -- máx. 20 MB
    fecha_creacion       TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_estudios_historia_fecha
    ON estudios_imagen (historia_clinica_id, fecha_estudio DESC);

CREATE INDEX idx_estudios_consulta
    ON estudios_imagen (consulta_id) WHERE consulta_id IS NOT NULL;

-- 3. Candado: solo descripcion e interpretacion pueden cambiar; nunca se borra
CREATE FUNCTION fn_proteger_estudio_imagen() RETURNS trigger AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        RAISE EXCEPTION 'El estudio de imagen % forma parte del expediente y no puede eliminarse', OLD.id
            USING ERRCODE = 'P0001';
    END IF;

    IF NEW.historia_clinica_id IS DISTINCT FROM OLD.historia_clinica_id
       OR NEW.consulta_id      IS DISTINCT FROM OLD.consulta_id
       OR NEW.doctor_id        IS DISTINCT FROM OLD.doctor_id
       OR NEW.tipo_estudio_id  IS DISTINCT FROM OLD.tipo_estudio_id
       OR NEW.fecha_estudio    IS DISTINCT FROM OLD.fecha_estudio
       OR NEW.archivo_ruta     IS DISTINCT FROM OLD.archivo_ruta
       OR NEW.nombre_original  IS DISTINCT FROM OLD.nombre_original
       OR NEW.tipo_contenido   IS DISTINCT FROM OLD.tipo_contenido
       OR NEW.tamano_bytes     IS DISTINCT FROM OLD.tamano_bytes
       OR NEW.fecha_creacion   IS DISTINCT FROM OLD.fecha_creacion THEN
        RAISE EXCEPTION 'Del estudio de imagen % solo pueden modificarse la descripción y la interpretación', OLD.id
            USING ERRCODE = 'P0001';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_proteger_estudio_imagen
    BEFORE UPDATE OR DELETE ON estudios_imagen
    FOR EACH ROW
    EXECUTE FUNCTION fn_proteger_estudio_imagen();