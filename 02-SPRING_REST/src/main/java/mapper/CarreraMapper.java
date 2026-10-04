package mapper;

import dto.CarreraDTO;
import dto.InscripcionDTO;
import model.Carrera;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CarreraMapper {

    private final InscripcionMapper inscripcionMapper;

    // Inyección de dependencias por constructor
    public CarreraMapper(InscripcionMapper inscripcionMapper) {
        this.inscripcionMapper = inscripcionMapper;
    }

    // Metodo helper para mapear de DTO Create -> Entidad
    public Carrera toEntity(CarreraDTO.Create in) {
        return new Carrera(
                in.id(),
                in.nombre(),
                in.duracion()
        );
    }

    // Metodo helper para mapear de Entidad -> DTO de listado (sin inscripciones)
    public CarreraDTO.Response toResponse(Carrera c) {
        return new CarreraDTO.Response(
                c.getId(),
                c.getNombre(),
                c.getDuracion()
        );
    }

    // Metodo helper para mapear de Entidad -> DTO de detalle (con inscripciones)
    public CarreraDTO.Detail toDetail(Carrera c) {
        // armo la lista de InscripcionDTO.Response de la carrera
        List<InscripcionDTO.Response> insc = c.getInscripciones().stream()
                .map(inscripcionMapper::toResponse)
                .toList();
        // armo el CarreraDTO.Detail
        return new CarreraDTO.Detail(
                c.getId(),
                c.getNombre(),
                c.getDuracion(),
                insc
        );
    }

    // Mapeo los campos que permitís modificar desde el DTO entrante (no cambia la PK id)
    public void applyUpdate(Carrera target, CarreraDTO.Create in) {
        target.setNombre(in.nombre());
        target.setDuracion(in.duracion());
    }
}
