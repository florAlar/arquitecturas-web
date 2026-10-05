package dto;

import model.Genero;

import java.util.List;

public class EstudianteDTO {

    // usamos un record de Java, que es ideal para DTOs porque es inmutable,
    // limpio y genera automáticamente getters, equals, hashCode y toString.
        public record Detalle(
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
    // Este código de una sola línea crea un registro que equivale a una clase tradicional
    // con muchos campos repetitivos. Java genera de manera automática:
    //  • Un constructor con todos los parámetros (constructor canónico).
    //  • Los métodos de acceso para leer cada atributo (con el mismo nombre del campo, sin el prefijo get).
    //  • Los métodos toString(), equals() y hashCode().
    //  • Campos privados y de tipo final (no se pueden modificar una vez creados)

    // este es el mismo Detalle, pero sin el List<InscripcionDTO.Response>
    // para evitar que si listás todos los estudiantes de la universidad (Punto c),
    // meter una lista de inscripciones adentro de cada uno puede hacer que la respuesta
    // sea gigantesca y lenta.
    // Podés tener un Listado (sin inscripciones) para los listados masivos,
    // y usar tu Detalle actual (con inscripciones) únicamente para el findById
    // (cuando buscás a uno solo).
    public record Listado(
            Long lu,
            String nombres,
            String apellido,
            int edad,
            Genero genero,
            Long dni,
            String ciudad
    ){}

}
