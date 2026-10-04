package repository;

import dto.CarreraDTOCantidad;
import model.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarreraRepository extends JpaRepository<Carrera, Long > {

    boolean existsByNombre(String nombre);

    // f) carreras con estudiantes inscriptos ordenadas por cantidad
    @Query ("SELECT new dto.CarreraDTOCantidad(c.id, c.nombre, COUNT(i)) " +
            "FROM Carrera c JOIN c.inscripciones i " +
            "GROUP BY c.id, c.nombre " +
            "ORDER BY COUNT(i) DESC")
    List<CarreraDTOCantidad> getCarrerasConInscriptosOrdenadas();


    // h) reporte: por carrera y año calendario (inscripcion / graduacion), no antiguedad
    // native SQL (mismo criterio que 01-JPA); el service mapea Object[] -> ReporteCarreraDTO
    @Query(value =
            "SELECT c.nombre AS nombre_carrera, " +
            "       y.anio   AS anio, " +
            "       SUM(CASE WHEN y.tipo = 'I' THEN y.cnt ELSE 0 END) AS cant_inscriptos, " +
            "       SUM(CASE WHEN y.tipo = 'G' THEN y.cnt ELSE 0 END) AS cant_egresados " +
            "FROM carrera c " +
            "JOIN ( " +
            "    SELECT i.carrera_id AS carrera_id, i.anio_inscripcion AS anio, 'I' AS tipo, COUNT(*) AS cnt " +
            "    FROM inscripcion i " +
            "    GROUP BY i.carrera_id, i.anio_inscripcion " +
            "    UNION ALL " +
            "    SELECT i.carrera_id AS carrera_id, i.anio_graduacion AS anio, 'G' AS tipo, COUNT(*) AS cnt " +
            "    FROM inscripcion i " +
            "    WHERE i.anio_graduacion IS NOT NULL " +
            "    GROUP BY i.carrera_id, i.anio_graduacion " +
            ") y ON y.carrera_id = c.id " +
            "GROUP BY c.nombre, y.anio " +
            "ORDER BY c.nombre ASC, y.anio ASC",
            nativeQuery = true)
    List<Object[]> generarReporteCarrerasRaw();

}
