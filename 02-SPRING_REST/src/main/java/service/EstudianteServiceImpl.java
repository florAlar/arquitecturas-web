package service;

import jakarta.transaction.Transactional;
import model.Estudiante;
import model.Genero;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import repository.EstudianteRepository;

import java.util.List;
import java.util.Optional;

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

    public List<Estudiante> findAll(String sortBy, String direction) {
        // Configuramos la dirección por defecto a ASC si no se envía de forma correcta
        Sort.Direction dir = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Creamos el objeto Sort usando el campo (sortBy) y la dirección (dir)
        Sort orden = Sort.by(dir, sortBy);

        return estudianteRepository.findAll(orden);
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

    @Override
    public List<Estudiante> findByGenero(Genero genero) throws Exception {
        try {
            return estudianteRepository.findByGenero(genero);
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
                throw new Exception();
            }
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Estudiante update(Long id, Estudiante entity) throws Exception {
        try{
            Optional<Estudiante> entityOpcional = estudianteRepository.findById(id);
            Estudiante estudiante = entityOpcional.get();
            estudiante = estudianteRepository.save(entity);
            return estudiante;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

}
