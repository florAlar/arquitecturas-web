package service;

import dto.EstudianteDTO;
import dto.EstudianteListadoDTO;
import dto.InscripcionDTO;
import jakarta.transaction.Transactional;
import model.Estudiante;
import model.Genero;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import repository.EstudianteRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EstudianteServiceImpl implements EstudianteService {
    private EstudianteRepository estudianteRepository;

    // Inyección de dependencias por constructor
    public EstudianteServiceImpl(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    @Override
    public List<Estudiante> findAll() throws Exception {
        try {
            return estudianteRepository.findAll();
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public List<Estudiante> findAll(String sortBy, String direction) throws  Exception {
        // Configuramos la dirección por defecto a ASC si no se envía de forma correcta
        Sort.Direction dir = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Creamos el objeto Sort usando el campo (sortBy) y la dirección (dir)
        Sort orden = Sort.by(dir, sortBy);

        try {
            // Buscamos las entidades de la base de datos
            return estudianteRepository.findAll(orden);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<EstudianteListadoDTO> findAllDTO() throws Exception {
        try {
            // 1. Buscamos las entidades de la base de datos
            List<Estudiante> estudiantes = findAll();

            // 2. Las convertimos a DTO usando Stream API de Java
            return estudiantes.stream()
                    .map(this::convertirAListadoDto)
                    .collect(Collectors.toList());
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<EstudianteListadoDTO> findAllDTO(String sortBy, String direction) throws  Exception {
        try {
            // 1. Buscamos las entidades de la base de datos
            List<Estudiante> estudiantes = findAll(sortBy, direction);

            // 2. Las convertimos a DTO usando Stream API de Java
            return estudiantes.stream()
                    .map(this::convertirAListadoDto)
                    .collect(Collectors.toList());
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    // Método helper para mapear de Entidad -> DTO
    private EstudianteListadoDTO convertirAListadoDto(Estudiante estudiante) {
        //armo el EstudianteListadoDTO sin las inscripciones
        return new EstudianteListadoDTO(
                estudiante.getLu(),
                estudiante.getNombres(),
                estudiante.getApellido(),
                estudiante.getEdad(),
                estudiante.getGenero(),
                estudiante.getDni(),
                estudiante.getCiudad()
        );
    }

    // Método helper para mapear de Entidad -> DTO
    private EstudianteDTO convertirADto(Estudiante estudiante) {
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
        return new EstudianteDTO(
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

    @Override
    public Optional<Estudiante> findById(Long id) throws Exception {
        try{
            Optional<Estudiante> estudianteBuscado = estudianteRepository.findById(id);
            return estudianteBuscado;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public Optional<EstudianteDTO> findByIdDTO(Long id) throws Exception {
        try{
            Optional<Estudiante> estudiante = estudianteRepository.findById(id);
            if (estudiante.isPresent())
                return Optional.of(convertirADto(estudiante.get()));
            else
                return Optional.empty();
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<Estudiante> findByGenero(Genero genero) throws Exception {
        try {
            return estudianteRepository.findByGenero(genero);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<EstudianteListadoDTO> findByGeneroDTO(Genero genero) throws Exception {
        try {
            // 1. Buscamos las entidades de la base de datos
            List<Estudiante> estudiantes = findByGenero(genero);

            // 2. Las convertimos a DTO usando Stream API de Java
            return estudiantes.stream()
                    .map(this::convertirAListadoDto)
                    .collect(Collectors.toList());
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }


    @Override
    public Estudiante save(Estudiante estudiante) throws Exception {
        try{
            return estudianteRepository.save(estudiante);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean delete(Long id) throws Exception {
        try{
            if(estudianteRepository.existsById(id)){
                estudianteRepository.deleteById(id);
                return true;
            }else{
                throw new Exception("No existe el estudiante que intenta eliminar");
            }
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }


    @Override
    @Transactional
    public Estudiante update(Long lu, Estudiante entity) throws Exception {
        try {
            Optional<Estudiante> entityOpcional = estudianteRepository.findById(lu);
            if (entityOpcional.isPresent()) {
                Estudiante estudianteExistente = entityOpcional.get();

                // Mapeás los campos que permitís modificar desde el objeto 'entity' entrante
                estudianteExistente.setNombres(entity.getNombres());
                estudianteExistente.setApellido(entity.getApellido());
                estudianteExistente.setEdad(entity.getEdad());
                estudianteExistente.setGenero(entity.getGenero());
                estudianteExistente.setDni(entity.getDni());
                estudianteExistente.setCiudad(entity.getCiudad());

                // Guardás la entidad existente ya actualizada
                return estudianteRepository.save(estudianteExistente);
            } else {
                throw new Exception("No existe el estudiante que intenta actualizar");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
