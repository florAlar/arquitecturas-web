package mapper;

import dto.EstudianteDTO;
import dto.InscripcionDTO;
import model.Estudiante;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EstudianteMapper {

    private final InscripcionMapper inscripcionMapper;

    // Inyección de dependencias por constructor
    public EstudianteMapper(InscripcionMapper inscripcionMapper) {
        this.inscripcionMapper = inscripcionMapper;
    }

    // Metodo helper para mapear de DTO Create -> Entidad
    public Estudiante toEntity(EstudianteDTO.Create in) {
        return new Estudiante(
                in.lu(),
                in.nombres(),
                in.apellido(),
                in.edad(),
                in.genero(),
                in.dni(),
                in.ciudad()
        );
    }

    // Metodo helper para mapear de Entidad -> DTO de listado (sin inscripciones)
    public EstudianteDTO.Response toResponse(Estudiante e) {
        return new EstudianteDTO.Response(
                e.getLu(),
                e.getNombres(),
                e.getApellido(),
                e.getEdad(),
                e.getGenero(),
                e.getDni(),
                e.getCiudad()
        );
    }

    // Metodo helper para mapear de Entidad -> DTO de detalle (con inscripciones)
    public EstudianteDTO.Detail toDetail(Estudiante e) {
        // armo la lista de InscripcionDTO.Response del estudiante
        List<InscripcionDTO.Response> insc = e.getInscripciones().stream()
                .map(inscripcionMapper::toResponse)
                .toList();
        // armo el EstudianteDTO.Detail
        return new EstudianteDTO.Detail(
                e.getLu(),
                e.getNombres(),
                e.getApellido(),
                e.getEdad(),
                e.getGenero(),
                e.getDni(),
                e.getCiudad(),
                insc
        );
    }

    // Mapeo los campos que permitís modificar desde el DTO entrante (no cambia la PK lu)
    public void applyUpdate(Estudiante target, EstudianteDTO.Create in) {
        target.setNombres(in.nombres());
        target.setApellido(in.apellido());
        target.setEdad(in.edad());
        target.setGenero(in.genero());
        target.setDni(in.dni());
        target.setCiudad(in.ciudad());
    }
}
