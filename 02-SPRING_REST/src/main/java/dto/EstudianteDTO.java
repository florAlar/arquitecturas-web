package dto;

import model.Genero;

import java.util.List;

// usamos records de Java anidados, ideales para DTOs porque son inmutables,
// limpios y generan automáticamente getters, equals, hashCode y toString.
// Este código crea registros que equivalen a una clase tradicional
// con muchos campos repetitivos. Java genera de manera automática:
//  • Un constructor con todos los parámetros (constructor canónico).
//  • Los métodos de acceso para leer cada atributo (con el mismo nombre del campo, sin el prefijo get).
//  • Los métodos toString(), equals() y hashCode().
//  • Campos privados y de tipo final (no se pueden modificar una vez creados)
//
// Response (sin inscripciones) para los listados masivos (Punto c / e):
// meter una lista de inscripciones adentro de cada uno puede hacer que la respuesta
// sea gigantesca y lenta.
// Detail (con inscripciones) únicamente para el findByLu
// (cuando buscás a uno solo, Punto d).
public class EstudianteDTO {

    // DTO para dar de alta / actualizar un estudiante (Punto a).
    // PK lu la define el cliente en el alta; en update viene por path.
    public record Create(
            Long lu,
            String nombres,
            String apellido,
            int edad,
            Genero genero,
            Long dni,
            String ciudad
    ) {
    }

    // DTO de listado: mismo estudiante, pero sin el List de inscripciones
    public record Response(
            Long lu,
            String nombres,
            String apellido,
            int edad,
            Genero genero,
            Long dni,
            String ciudad
    ) {
    }

    // DTO de detalle por LU, con inscripciones
    public record Detail(
            Long lu,
            String nombres,
            String apellido,
            int edad,
            Genero genero,
            Long dni,
            String ciudad,
            List<InscripcionDTO.Response> inscripciones
    ) {
    }
}
