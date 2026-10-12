CREATE TABLE doctores(
    id                   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre               VARCHAR(100) NOT NULL,
    primer_apellido      VARCHAR(100) NOT NULL,
    segundo_apellido     VARCHAR(100),
    curp                 CHAR(18)     NOT NULL UNIQUE,
    cedula_profesional   VARCHAR(20)  NOT NULL UNIQUE,
    email                VARCHAR(150) NOT NULL UNIQUE,
    telefono             VARCHAR(15),
    foto_ruta            VARCHAR(255),
    estado               VARCHAR(20)  NOT NULL DEFAULT 'ACTIVO'
                         CHECK (estado IN ('ACTIVO', 'DESACTIVADO')),
    fecha_creacion       TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    fecha_actualizacion  TIMESTAMPTZ
);

CREATE TABLE doctor_especialidades(
    doctor_id             BIGINT      NOT NULL REFERENCES doctores (id),
    especialidad_id       BIGINT      NOT NULL REFERENCES especialidades (id),
    cedula_especialidad   VARCHAR(20) NOT NULL UNIQUE,
    PRIMARY KEY (doctor_id, especialidad_id)
);