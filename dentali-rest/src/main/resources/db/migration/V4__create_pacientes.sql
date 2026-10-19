CREATE TABLE pacientes (
    id                   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre               VARCHAR(100) NOT NULL,
    primer_apellido      VARCHAR(100) NOT NULL,
    segundo_apellido     VARCHAR(100),
    curp                 CHAR(18) UNIQUE,
    fecha_nacimiento     DATE NOT NULL CONSTRAINT chk_fecha_nacimiento CHECK (fecha_nacimiento <= CURRENT_DATE),
    sexo CHAR(1) NOT NULL CONSTRAINT chk_sexo CHECK (sexo IN ('H', 'M', 'X')),  
    entidad_nacimiento SMALLINT NOT NULL
    CONSTRAINT fk_pacientes_entidad_nacimiento
        REFERENCES entidades_federativas (id) ON DELETE RESTRICT,
    email                VARCHAR(150),
    telefono             VARCHAR(15),
    foto_ruta            VARCHAR(255),
    estado               VARCHAR(20)  NOT NULL DEFAULT 'ACTIVO'
                         CHECK (estado IN ('ACTIVO', 'DESACTIVADO')),
    fecha_creacion       TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    fecha_actualizacion  TIMESTAMPTZ                     
    
);

CREATE INDEX busqueda_apellido ON pacientes (primer_apellido, segundo_apellido);