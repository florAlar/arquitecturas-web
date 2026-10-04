package repository;

import model.Estudiante;
import model.Genero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    @Query("SELECT e FROM Estudiante e WHERE e.genero = :queGenero")
    List<Estudiante> findByGenero(Genero queGenero);

    // g) estudiantes de una carrera filtrados por ciudad de residencia
    @Query("SELECT e FROM Inscripcion i JOIN i.estudiante e " +
            "WHERE i.carrera.id = :idCarrera " +
            "  AND LOWER(e.ciudad) = LOWER(:ciudadResidencia)")
    List<Estudiante> findByCarreraIdAndCiudad(Long idCarrera, String ciudadResidencia);

    boolean existsByDni(Long dni);
}
