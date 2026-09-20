package repository;

public interface EstudianteRepository {

    List<EstudianteDTO> getEstudiantesOrderByApellido();
    List<EstudianteDTO> findAllByGenero(Genero genero);
    EstudianteDTO findByNroLibreta(Long nroLibreta);
    List<EstudianteDTO> getEstudiantesByCarreraAndCiudadResidencia(Long idCarrera, String ciudadResidencia);
}
