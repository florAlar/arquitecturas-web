package service;

import dto.EstudianteDTO;
import exception.BadRequestException;
import exception.CarreraNotFoundException;
import exception.EstudianteDuplicadoException;
import exception.EstudianteNotFoundException;
import mapper.EstudianteMapper;
import model.Estudiante;
import model.Genero;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.CarreraRepository;
import repository.EstudianteRepository;

import java.util.List;
import java.util.Set;

@Service
public class EstudianteServiceImpl implements EstudianteService {

    // whitelist de campos permitidos en order-by dinámico
    private static final Set<String> SORT_WHITELIST = Set.of(
            "lu", "nombres", "apellido", "edad", "genero", "dni", "ciudad"
    );

    private final EstudianteRepository estudianteRepository;
    private final CarreraRepository carreraRepository;
    private final EstudianteMapper estudianteMapper;

    // Inyección de dependencias por constructor
    public EstudianteServiceImpl(
            EstudianteRepository estudianteRepository,
            CarreraRepository carreraRepository,
            EstudianteMapper estudianteMapper
    ) {
        this.estudianteRepository = estudianteRepository;
        this.carreraRepository = carreraRepository;
        this.estudianteMapper = estudianteMapper;
    }

    @Override
    @Transactional
    public EstudianteDTO.Response altaEstudiante(EstudianteDTO.Create in) {
        if (in.lu() == null) {
            throw new BadRequestException("lu es obligatorio");
        }
        if (in.dni() == null) {
            throw new BadRequestException("dni es obligatorio");
        }
        if (estudianteRepository.existsById(in.lu())) {
            throw new EstudianteDuplicadoException("Ya existe un estudiante con LU=" + in.lu());
        }
        if (estudianteRepository.existsByDni(in.dni())) {
            throw new EstudianteDuplicadoException("Ya existe un estudiante con DNI=" + in.dni());
        }

        Estudiante entity = estudianteMapper.toEntity(in);
        // Guardás la entidad
        Estudiante saved = estudianteRepository.save(entity);
        return estudianteMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstudianteDTO.Response> listarEstudiantes(String sortBy, String direction) {
        String campo = (sortBy == null || sortBy.isBlank()) ? "lu" : sortBy.trim();
        if (!SORT_WHITELIST.contains(campo)) {
            throw new BadRequestException(
                    "sortBy inválido: '" + campo + "'. Permitidos: " + SORT_WHITELIST
            );
        }


        // Configuramos la dirección por defecto a ASC si no se envía de forma correcta
        Sort.Direction dir = "desc".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        // Creamos el objeto Sort usando el campo (sortBy) y la dirección (dir)
        // 1. Buscamos las entidades de la base de datos
        // 2. Las convertimos a DTO usando Stream API de Java
        return estudianteRepository.findAll(Sort.by(dir, campo)).stream()
                .map(estudianteMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EstudianteDTO.Detail getByLu(Long lu) {
        // Buscamos la entidad; si no está, 404 vía exception
        Estudiante e = estudianteRepository.findById(lu)
                .orElseThrow(() -> new EstudianteNotFoundException(lu));
        return estudianteMapper.toDetail(e);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstudianteDTO.Response> listarPorGenero(Genero genero) {
        // 1. Buscamos las entidades de la base de datos
        // 2. Las convertimos a DTO usando Stream API de Java
        return estudianteRepository.findByGenero(genero).stream()
                .map(estudianteMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstudianteDTO.Response> listarPorCarreraYCiudad(Long idCarrera, String ciudad) {
        if (ciudad == null || ciudad.isBlank()) {
            throw new BadRequestException("ciudad es obligatoria");
        }
        if (!carreraRepository.existsById(idCarrera)) {
            throw new CarreraNotFoundException(idCarrera);
        }
        return estudianteRepository.findByCarreraIdAndCiudad(idCarrera, ciudad.trim()).stream()
                .map(estudianteMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public EstudianteDTO.Response actualizar(Long lu, EstudianteDTO.Create in) {
        Estudiante existente = estudianteRepository.findById(lu)
                .orElseThrow(() -> new EstudianteNotFoundException(lu));


        if (in.dni() != null
                && !in.dni().equals(existente.getDni())
                && estudianteRepository.existsByDni(in.dni())) {
            throw new EstudianteDuplicadoException("Ya existe un estudiante con DNI=" + in.dni());
        }

        // Mapeás los campos que permitís modificar desde el DTO entrante
        estudianteMapper.applyUpdate(existente, in);
        // Guardás la entidad existente ya actualizada
        Estudiante saved = estudianteRepository.save(existente);
        return estudianteMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void eliminar(Long lu) {
        if (!estudianteRepository.existsById(lu)) {
            throw new EstudianteNotFoundException(lu);
        }
        estudianteRepository.deleteById(lu);
    }
}
