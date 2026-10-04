package mapper;

import dto.InscripcionDTO;
import model.Carrera;
import model.Estudiante;
import model.Inscripcion;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.Collection;
import java.util.List;

@Component
public class InscripcionMapper {

    // Alta de inscripción: año actual, sin graduación ni antigüedad
    // (igual que matricular en el TP2). Create solo aporta FKs (resueltas afuera).
    public Inscripcion toEntity(InscripcionDTO.Create ignored, Estudiante estudiante, Carrera carrera) {
        int anio = Year.now().getValue();
        return new Inscripcion(estudiante, carrera, anio, null, 0, false);
    }

    // Alta con año de inscripción explícito (por si el service lo define)
    public Inscripcion toEntity(
            Estudiante estudiante,
            Carrera carrera,
            int anioInscripcion
    ) {
        return new Inscripcion(estudiante, carrera, anioInscripcion, null, 0, false);
    }

    // Metodo helper para mapear de Entidad -> DTO de respuesta
    public InscripcionDTO.Response toResponse(Inscripcion inscripcion) {
        return new InscripcionDTO.Response(
                inscripcion.getId(),
                inscripcion.getEstudiante().getLu(),
                inscripcion.getCarrera().getId(),
                inscripcion.getAnioInscripcion(),
                inscripcion.getAnioGraduacion(),
                inscripcion.getAntiguedad(),
                inscripcion.isGraduado()
        );
    }

    // Metodo helper para mapear listas de Entidad -> DTO
    public List<InscripcionDTO.Response> toResponseList(Collection<Inscripcion> inscripciones) {
        if (inscripciones == null || inscripciones.isEmpty()) {
            return List.of();
        }
        return inscripciones.stream()
                .map(this::toResponse)
                .toList();
    }
}
