package mapper;

import dto.InscripcionDTO;
import model.Carrera;
import model.Estudiante;
import model.Inscripcion;
import org.springframework.stereotype.Component;

import java.time.Year;

@Component
public class InscripcionMapper {

    // Alta de inscripcion : año actual, sin graduacion ni antiguedad.

    public Inscripcion toEntity(InscripcionDTO.Create ignored, Estudiante estudiante, Carrera carrera) {
        int anio = Year.now().getValue();
        return new Inscripcion(estudiante, carrera, anio, null, 0, false);
    }

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
}
