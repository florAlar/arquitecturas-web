package dto;

import model.Genero;

import java.util.List;

// usamos un record de Java, que es ideal para DTOs porque es inmutable,
// limpio y genera automáticamente getters, equals, hashCode y toString.
public record EstudianteDTO(
    Long lu,
    String nombres,
    String apellido,
    int edad,
    Genero genero,
    Long dni,
    String ciudad,
    List<InscripcionDTO> inscripciones
){}
// Este código de una sola línea crea un registro que equivale a una clase tradicional
// con muchos campos repetitivos. Java genera de manera automática:
//  • Un constructor con todos los parámetros (constructor canónico).
//  • Los métodos de acceso para leer cada atributo (con el mismo nombre del campo, sin el prefijo get).
//  • Los métodos toString(), equals() y hashCode().
//  • Campos privados y de tipo final (no se pueden modificar una vez creados)

