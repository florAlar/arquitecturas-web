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

    boolean existsByDni(Long dni);
}
