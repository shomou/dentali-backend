-- ================ CATÁLOGOS  ==========================
CREATE TABLE enfermedades(
    id                      SMALLINT PRIMARY KEY,
    nombre                  VARCHAR(60) NOT NULL UNIQUE,
    aplica_personal         BOOLEAN NOT NULL,
    aplica_heredofamiliar   BOOLEAN NOT NULL,
    CONSTRAINT chk_enfermedades_aplica CHECK(aplica_personal OR aplica_heredofamiliar)
);

INSERT INTO enfermedades (id, nombre, aplica_personal, aplica_heredofamiliar) VALUES
    (1,  'Diabetes',                 TRUE,  TRUE),
    (2,  'Hipertensión',             TRUE,  TRUE),
    (3,  'Hipotensión',              TRUE,  TRUE),
    (4,  'Enfermedades renales',     TRUE,  FALSE),
    (5,  'Cáncer',                   TRUE,  TRUE),
    (6,  'Asma',                     TRUE,  FALSE),
    (7,  'Anemia',                   TRUE,  FALSE),
    (8,  'Gastritis',                TRUE,  FALSE),
    (9,  'Colitis',                  TRUE,  FALSE),
    (10, 'Hepatitis',                TRUE,  FALSE),
    (11, 'Tuberculosis',             TRUE,  FALSE),
    (12, 'Herpes',                   TRUE,  FALSE),
    (13, 'Varicela',                 TRUE,  FALSE),
    (14, 'Sarampión',                TRUE,  FALSE),
    (15, 'Insuficiencia renal',      FALSE, TRUE),
    (16, 'Enfermedades del corazón', FALSE, TRUE);

CREATE TABLE parentescos (
    id      SMALLINT    PRIMARY KEY,
    nombre  VARCHAR(40) NOT NULL UNIQUE
);

INSERT INTO parentescos (id, nombre) VALUES
    (1, 'Padre'),
    (2, 'Madre'),
    (3, 'Hermano(a)'),
    (4, 'Abuelo(a) paterno(a)'),
    (5, 'Abuelo(a) materno(a)'),
    (6, 'Tío(a) paterno(a)'),
    (7, 'Tío(a) materno(a)');

CREATE TABLE auxiliares_higiene (
    id      SMALLINT    PRIMARY KEY,
    nombre  VARCHAR(40) NOT NULL UNIQUE
);

INSERT INTO auxiliares_higiene (id, nombre) VALUES
    (1, 'Cepillo de dientes'),
    (2, 'Pasta de dientes'),
    (3, 'Hilo dental'),
    (4, 'Enjuague bucal'),
    (5, 'Tabletas reveladoras'),
    (6, 'Cepillos interdentales'),
    (7, 'Palillos');

CREATE TABLE hallazgos_atm (
    id      SMALLINT    PRIMARY KEY,
    nombre  VARCHAR(80) NOT NULL UNIQUE
);

INSERT INTO hallazgos_atm (id, nombre) VALUES
    (1, 'Sana'),
    (2, 'Chasquido'),
    (3, 'Crepitación'),
    (4, 'Dificultad para abrir la boca'),
    (5, 'Dolor a la apertura o movimientos de lateralidad'),
    (6, 'Fatiga o dolor muscular'),
    (7, 'Disminución de la apertura'),
    (8, 'Desviación de la mandíbula a la apertura o cierre');

CREATE TABLE estados_periodontales (
    id      SMALLINT    PRIMARY KEY,
    nombre  VARCHAR(40) NOT NULL UNIQUE
);

INSERT INTO estados_periodontales (id, nombre) VALUES
    (1, 'Sano'),
    (2, 'Gingivitis'),
    (3, 'Periodontitis'),
    (4, 'Recesión gingival');

-- ═══════════════ TABLAS PUENTE ═══════════════

-- Antecedentes personales: Sí / No explícito (sin fila = no se preguntó)
CREATE TABLE hc_antecedentes_personales (
    historia_clinica_id  BIGINT   NOT NULL
        CONSTRAINT fk_hcap_historia REFERENCES historias_clinicas (id) ON DELETE CASCADE,
    enfermedad_id        SMALLINT NOT NULL
        CONSTRAINT fk_hcap_enfermedad REFERENCES enfermedades (id) ON DELETE RESTRICT,
    padece               BOOLEAN  NOT NULL,
    CONSTRAINT pk_hc_antecedentes_personales PRIMARY KEY (historia_clinica_id, enfermedad_id)
);

-- Para reportes: "¿cuántos pacientes padecen X?"
CREATE INDEX idx_hcap_enfermedad ON hc_antecedentes_personales (enfermedad_id) WHERE padece;

-- Heredofamiliares: una fila por enfermedad + parentesco (solo los "Sí")
CREATE TABLE hc_antecedentes_heredofamiliares (
    historia_clinica_id  BIGINT   NOT NULL
        CONSTRAINT fk_hcah_historia REFERENCES historias_clinicas (id) ON DELETE CASCADE,
    enfermedad_id        SMALLINT NOT NULL
        CONSTRAINT fk_hcah_enfermedad REFERENCES enfermedades (id) ON DELETE RESTRICT,
    parentesco_id        SMALLINT NOT NULL
        CONSTRAINT fk_hcah_parentesco REFERENCES parentescos (id) ON DELETE RESTRICT,
    CONSTRAINT pk_hc_antecedentes_heredofamiliares
        PRIMARY KEY (historia_clinica_id, enfermedad_id, parentesco_id)
);


-- Auxiliares higiene
CREATE TABLE hc_auxiliares_higiene (
    historia_clinica_id  BIGINT   NOT NULL
        CONSTRAINT fk_hcaux_historia REFERENCES historias_clinicas (id) ON DELETE CASCADE,
    auxiliar_higiene_id        SMALLINT NOT NULL
        CONSTRAINT fk_hcaux_auxiliares_higiene REFERENCES auxiliares_higiene (id) ON DELETE RESTRICT,
    CONSTRAINT pk_hc_auxiliares_higiene PRIMARY KEY (historia_clinica_id, auxiliar_higiene_id)
);

-- Hallazgo ATM
CREATE TABLE hc_hallazgos_atm (
    historia_clinica_id  BIGINT   NOT NULL
        CONSTRAINT fk_hcha_historia REFERENCES historias_clinicas (id) ON DELETE CASCADE,
    hallazgo_atm_id        SMALLINT NOT NULL
        CONSTRAINT fk_hcha_hallazgo_atm_id REFERENCES hallazgos_atm (id) ON DELETE RESTRICT,    
    CONSTRAINT pk_hc_hallazgos_atm PRIMARY KEY (historia_clinica_id, hallazgo_atm_id)
);

-- Estado Periodontales
CREATE TABLE hc_estados_periodontales (
    historia_clinica_id  BIGINT   NOT NULL
        CONSTRAINT fk_hcep_historia REFERENCES historias_clinicas (id) ON DELETE CASCADE,
    estado_periodontal_id        SMALLINT NOT NULL
        CONSTRAINT fk_hcep_estado_periodontal REFERENCES estados_periodontales (id) ON DELETE RESTRICT,    
    CONSTRAINT pk_hc_estados_periodontales PRIMARY KEY (historia_clinica_id, estado_periodontal_id)
);


