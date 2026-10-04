package dto;

import model.Genero;

import java.util.List;

// este es el mismo EstudianteDTO, pero sin el List<InscripcionDTO>
// para evitar que si listás todos los estudiantes de la universidad (Punto c),
// meter una lista de inscripciones adentro de cada uno puede hacer que la respuesta
// sea gigantesca y lenta.
// Podés tener un EstudianteListaDTO (sin inscripciones) para los listados masivos,
// y usar tu EstudianteDTO actual (con inscripciones) únicamente para el findById
// (cuando buscás a uno solo).
public record EstudianteListadoDTO(
    Long lu,
    String nombres,
    String apellido,
    int edad,
    Genero genero,
    Long dni,
    String ciudad
){}

