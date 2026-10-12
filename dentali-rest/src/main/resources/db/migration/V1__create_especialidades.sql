CREATE TABLE especialidades(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

INSERT INTO especialidades(nombre) VALUES
('Odontología General'),
('Odontopediatría'),
('Ortodoncia'),
('Endodoncia'),
('Periodoncia'),
('Cirugía Oral y Maxilofacial'),
('Prostodoncia'),
('Implantología'),
('Odontología Estética'),
('Odontología Preventiva');