package dto;

import java.util.List;

// usamos records de Java anidados, ideales para DTOs porque son inmutables,
// limpios y generan automáticamente getters, equals, hashCode y toString.
// Response (sin inscripciones) para listados.
// Detail (con inscripciones) cuando se consulta una carrera sola.
public class CarreraDTO {

    // DTO para dar de alta / actualizar una carrera.
    // PK id la define el cliente en el alta (como en el CSV); en update viene por path.
    public record Create(
            Long id,
            String nombre,
            int duracion
    ) {
    }

    // DTO de listado: carrera sin el List de inscripciones
    public record Response(
            Long id,
            String nombre,
            int duracion
    ) {
    }

    // DTO de detalle, con inscripciones
    public record Detail(
            Long id,
            String nombre,
            int duracion,
            List<InscripcionDTO.Response> inscripciones
    ) {
    }
}
