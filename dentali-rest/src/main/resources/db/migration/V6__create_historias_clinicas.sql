CREATE TABLE historias_clinicas(
    id                          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    paciente_id                 BIGINT NOT NULL
        CONSTRAINT uq_hc_paciente UNIQUE
        CONSTRAINT fk_hc_paciente REFERENCES pacientes(id),
    doctor_apertura_id          BIGINT NOT NULL
        CONSTRAINT fk_hc_doctor_apertura REFERENCES doctores(id),
    fecha_apertura              TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    fecha_actualizacion         TIMESTAMPTZ,

    grupo_sanguineo             VARCHAR(3)
        CONSTRAINT chk_hc_grupo_sanguineo
        CHECK (grupo_sanguineo IN('A+','A-','B+','B-','AB+','AB-','O+','O-')),

    -- ------------------- IV. Antecedentes personales patológicos  ---------------
    enfermedades_ultimo_anio    TEXT,
    antecedente_patologico_otro TEXT,
    alergico_medicamentos       BOOLEAN,
    alergias_detalle            TEXT,
    anestesiado_previamente     BOOLEAN,
    problemas_anestesia         BOOLEAN,
    problemas_anestesia_detalle TEXT,
    hemorragias_frecuentes      BOOLEAN,
    sangrado_abundante          BOOLEAN,
    hospitalizado               BOOLEAN,
    hospitalizacion_motivo      TEXT,
    cirugias                    BOOLEAN,
    cirugias_motivo             TEXT,
    enfermedad_actual           BOOLEAN,
    enfermedad_actual_detalle   TEXT,
    toma_medicamentos           BOOLEAN,
    medicamentos_detalle        TEXT,
    bifosfonatos                BOOLEAN,
    bifosfonatos_detalle        TEXT,

    -- ------------------ VI. Antecedentes no patológicos  --------------------
    cepillados_por_dia          SMALLINT
        CONSTRAINT chk_hc_cepillado CHECK (cepillados_por_dia BETWEEN 0 AND 10),
    auxiliar_higiene_otro       VARCHAR(100),
    fuma                        BOOLEAN,
    fuma_frecuencia             VARCHAR(100),
    alcohol                     BOOLEAN,
    alcohol_frecuencia          VARCHAR(100),
    otras_sustancias            BOOLEAN,
    otras_sustancias_frecuencia VARCHAR(100),
    embarazada                  BOOLEAN,
    semanas_gestacion           SMALLINT
        CONSTRAINT chk_hc_semanas CHECK (semanas_gestacion BETWEEN 1 AND 42),
    lactando                    BOOLEAN,

    -- ---------------- VII. Exploración facial y de tejidos blandos ---------
    exploracion_cara            TEXT,
    exploracion_cuello          TEXT,
    exploracion_labios          TEXT,
    exploracion_paladar_blando  TEXT,
    exploracion_paladar_duro    TEXT,
    exploracion_glandulas_salivales TEXT,
    exploracion_carrillos           TEXT,
    exploracion_amigdalas           TEXT,
    exploracion_lengua              TEXT,
    exploracion_piso_boca           TEXT,
    atm_observaciones               TEXT,

    -- ───── IX. Plan de tratamiento ─────
    plan_tratamiento                TEXT,

    -- ───── Coherencia entre columnas ─────
    CONSTRAINT chk_hc_alergias_detalle
        CHECK (alergico_medicamentos IS NOT TRUE OR COALESCE(btrim(alergias_detalle), '') <> ''),

    CONSTRAINT chk_hc_semanas_solo_embarazo
        CHECK (embarazada IS TRUE OR semanas_gestacion IS NULL),

    CONSTRAINT chk_hc_problemas_anestesia_detalle
        CHECK (problemas_anestesia IS NOT TRUE OR COALESCE(btrim(problemas_anestesia_detalle), '') <> ''),

    CONSTRAINT chk_hc_hospitalizacion_motivo
        CHECK (hospitalizado IS NOT TRUE OR COALESCE(btrim(hospitalizacion_motivo), '') <> ''),
        
    CONSTRAINT chk_hc_cirugias_motivo
        CHECK (cirugias IS NOT TRUE OR COALESCE(btrim(cirugias_motivo), '') <> ''),
    
    CONSTRAINT chk_hc_enfermedad_actual_detalle
        CHECK (enfermedad_actual IS NOT TRUE OR COALESCE(btrim(enfermedad_actual_detalle), '') <> ''),

    CONSTRAINT chk_hc_medicamentos_detalle
        CHECK (toma_medicamentos IS NOT TRUE OR COALESCE(btrim(medicamentos_detalle), '') <> ''),
    
    CONSTRAINT chk_hc_bifosfonatos_detalle
        CHECK (bifosfonatos IS NOT TRUE OR COALESCE(btrim(bifosfonatos_detalle), '') <> ''),

    CONSTRAINT chk_hc_fuma_frecuencia
        CHECK (fuma IS NOT TRUE OR COALESCE(btrim(fuma_frecuencia), '') <> ''),

    CONSTRAINT chk_hc_alcohol_frecuencia
        CHECK (alcohol IS NOT TRUE OR COALESCE(btrim(alcohol_frecuencia), '') <> ''),
    
    CONSTRAINT chk_hc_otras_sustancias_frecuencia
        CHECK (otras_sustancias IS NOT TRUE OR COALESCE(btrim(otras_sustancias_frecuencia), '') <> '')
);