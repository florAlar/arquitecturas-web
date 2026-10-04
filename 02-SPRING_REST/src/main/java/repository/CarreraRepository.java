package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import model.Carrera;

import java.util.List;

@Repository
public interface CarreraRepository extends JpaRepository<Carrera, Long > {

    //carreras con estudiantes inscriptos ordenadas por cantidad
    @Query ("SELECT new dto.CarreraDTOCantidad(c.id, c.nombre, COUNT(i)) " +
            "FROM Carrera c JOIN c.inscripciones i " +
            "GROUP BY c.id, c.nombre " +
            "ORDER BY COUNT(i) DESC")
    List<CarreraDTOCantidad> getCarrerasConInscriptosOrdenadas();


    //reporte de carreras(ordenadas alfabeticamente), por año, inscriptos y egresados
    @Query("SELECT new dto.ReporteCarreraDTO(" +
            "c.nombre, " +
            "i.antiguedad, " +
            "COUNT(i), " +
            "(SELECT COUNT(i2) " +
            " FROM Inscripcion i2 " +
            " WHERE i2.carrera = c " +
            "   AND i2.antiguedad = i.antiguedad " +
            "   AND i2.graduado = 1) " +
            ") " +
            "FROM Inscripcion i JOIN i.carrera c " +
            "GROUP BY c.id, c.nombre, i.antiguedad " +
            "ORDER BY c.nombre ASC, i.antiguedad ASC")
    List<ReporteCarreraDTO> generarReporteCarreras()


}
