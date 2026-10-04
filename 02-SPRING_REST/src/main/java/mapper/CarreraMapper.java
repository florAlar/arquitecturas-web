package mapper;

import dto.CarreraDTO;
import dto.InscripcionDTO;
import model.Carrera;
import org.springframework.stereotype.Component;

import java.util.Collection;
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

    // Metodo helper para mapear listas de Entidad -> DTO de listado
    public List<CarreraDTO.Response> toResponseList(Collection<Carrera> carreras) {
        if (carreras == null || carreras.isEmpty()) {
            return List.of();
        }
        return carreras.stream()
                .map(this::toResponse)
                .toList();
    }

    // Metodo helper para mapear de Entidad -> DTO de detalle (con inscripciones)
    public CarreraDTO.Detail toDetail(Carrera c) {
        // armo la lista de InscripcionDTO.Response de la carrera
        List<InscripcionDTO.Response> insc = inscripcionMapper.toResponseList(c.getInscripciones());
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
