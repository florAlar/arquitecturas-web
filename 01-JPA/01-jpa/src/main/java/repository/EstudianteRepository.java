package repository;

import dto.EstudianteDTO;
import entity.Estudiante;
import entity.Genero;

import java.util.List;

public interface EstudianteRepository {

    void create(Estudiante estudiante);
    List<EstudianteDTO> getEstudiantesOrdered(String campo);
    List<EstudianteDTO> findAllByGenero(Genero genero);
    EstudianteDTO findByNroLibreta(Long nroLibreta);
    List<EstudianteDTO> getEstudiantesByCarreraAndCiudadResidencia(Long idCarrera, String ciudadResidencia);
}
