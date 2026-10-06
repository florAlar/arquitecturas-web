package service;

import dto.InscripcionDTO;
import exception.CarreraNotFoundException;
import exception.EstudianteNotFoundException;
import exception.InscripcionDuplicadaException;
import exception.InscripcionNotFoundException;
import mapper.InscripcionMapper;
import model.Carrera;
import model.Estudiante;
import model.Inscripcion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.CarreraRepository;
import repository.EstudianteRepository;
import repository.InscripcionRepository;

@Service
public class InscripcionServiceImpl implements InscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final EstudianteRepository estudianteRepository;
    private final CarreraRepository carreraRepository;
    private final InscripcionMapper inscripcionMapper;

    public InscripcionServiceImpl(
            InscripcionRepository inscripcionRepository,
            EstudianteRepository estudianteRepository,
            CarreraRepository carreraRepository,
            InscripcionMapper inscripcionMapper
    ) {
        this.inscripcionRepository = inscripcionRepository;
        this.estudianteRepository = estudianteRepository;
        this.carreraRepository = carreraRepository;
        this.inscripcionMapper = inscripcionMapper;
    }

    @Override
    @Transactional
    public InscripcionDTO.Response getInscripcionByID(Long id){
        Inscripcion i = inscripcionRepository.findById(id).orElseThrow( () -> new InscripcionNotFoundException(id));
        return inscripcionMapper.toResponse(i);
    }

    @Override
    @Transactional
    public InscripcionDTO.Response inscribirEstudianteEnCarrera(InscripcionDTO.Create in) {
        Long lu = in.estudianteLu();
        Long carreraId = in.carreraId();

        Estudiante estudiante = estudianteRepository.findById(lu)
                .orElseThrow(() -> new EstudianteNotFoundException(lu));

        Carrera carrera = carreraRepository.findById(carreraId)
                .orElseThrow(() -> new CarreraNotFoundException(carreraId));

        if (inscripcionRepository.existsByEstudianteLuAndCarreraId(lu, carreraId)) {
            throw new InscripcionDuplicadaException(lu, carreraId);
        }

        Inscripcion entity = inscripcionMapper.toEntity(in, estudiante, carrera);
        Inscripcion saved = inscripcionRepository.save(entity);
        return inscripcionMapper.toResponse(saved);
    }
}
