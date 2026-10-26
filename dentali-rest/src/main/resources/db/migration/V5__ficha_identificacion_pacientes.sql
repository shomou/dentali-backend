--  ----------------------- Catálogo de estados civiles  --------------------
CREATE TABLE estados_civiles(
    id SMALLINT PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL UNIQUE
);

INSERT INTO estados_civiles (id, nombre) VALUES
    (1,'Soltero(a)'),
    (2,'Casado(a)'),
    (3,'Unión libre'),
    (4,'Separado(a)'),
    (5,'Divorciado(a)'),
    (6,'Viudo(a)');
    
-- ---------------------- Ficha de identificación completa ------------------
ALTER TABLE pacientes
    ADD COLUMN estado_civil_id SMALLINT
        CONSTRAINT fk_pacientes_estado_civil REFERENCES estados_civiles (id),
    ADD COLUMN ocupacion    VARCHAR(100),
    ADD COLUMN religion     VARCHAR(60),
    ADD COLUMN domicilio_calle              VARCHAR(150),
    ADD COLUMN domicilio_numero_exterior    VARCHAR(10),
    ADD COLUMN domicilio_numero_interior    VARCHAR(10),
    ADD COLUMN domicilio_colonia            VARCHAR(100),
    ADD COLUMN domicilio_codigo_postal      CHAR(5)
        CONSTRAINT chk_pacientes_codigo_postal CHECK (domicilio_codigo_postal ~ '^\d{5}$'),
    ADD COLUMN domicilio_municipio          VARCHAR(100),
    ADD COLUMN domicilio_entidad_id         SMALLINT
        CONSTRAINT fk_pacientes_domicilio_entidad REFERENCES entidades_federativas (id),

    ADD COLUMN contacto_emergencia_nombre   VARCHAR(150),
    ADD COLUMN contacto_emergencia_telefono VARCHAR(15);

COMMENT ON COLUMN pacientes.religion IS
    'DATO PERSONAL SENSIBLE (LFPDPPP). Requiere consentimiento expreso; acceso restringido.'; 
