package mapper;

import dto.EstudianteDTO;
import dto.InscripcionDTO;
import model.Estudiante;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EstudianteMapper {
    // Método helper para mapear de Estudiante -> EstudianteDTO.Listado
    public EstudianteDTO.Listado toListado(Estudiante estudiante) {
        //armo el EstudianteDTO.Listado sin las inscripciones
        return new EstudianteDTO.Listado(
                estudiante.getLu(),
                estudiante.getNombres(),
                estudiante.getApellido(),
                estudiante.getEdad(),
                estudiante.getGenero(),
                estudiante.getDni(),
                estudiante.getCiudad()
        );
    }

    // Método helper para mapear de Estudiante -> EstudianteDTO.Detalle
    public EstudianteDTO.Detalle toDetalle(Estudiante estudiante) {
        //armo la lista de InscripcionesDTO del estudiante
        List<InscripcionDTO.Response> inscripcionesDTO = estudiante.getInscripciones().stream()
                .map(inscripcion -> new InscripcionDTO.Response(
                        inscripcion.getId(),
                        inscripcion.getEstudiante().getLu(),
                        inscripcion.getCarrera().getId(),
                        inscripcion.getAnioInscripcion(),
                        inscripcion.getAnioGraduacion(),
                        inscripcion.getAntiguedad(),
                        inscripcion.isGraduado()
                ))
                .collect(Collectors.toList());

        //armo el EstudianteDTO
        return new EstudianteDTO.Detalle(
                estudiante.getLu(),
                estudiante.getNombres(),
                estudiante.getApellido(),
                estudiante.getEdad(),
                estudiante.getGenero(),
                estudiante.getDni(),
                estudiante.getCiudad(),
                inscripcionesDTO
        );
    }

}
