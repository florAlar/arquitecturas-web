package repository;

import dto.CarreraDTO;
import dto.CarreraDTOCantidad;
import dto.ReporteCarreraDTO;
import entity.Carrera;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class CarreraRepositoryImpl implements CarreraRepository {

    private final EntityManager em;

    public CarreraRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void create(Carrera carrera) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(carrera);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public List<CarreraDTOCantidad> getCarrerasConInscriptosOrdenadas() {
        String jpql = "SELECT new dto.CarreraDTOCantidad(c.id, c.nombre, COUNT(i)) " +
                "FROM Carrera c JOIN c.inscripciones i " +
                "GROUP BY c.id, c.nombre " +
                "ORDER BY COUNT(i) DESC";

        TypedQuery<CarreraDTOCantidad> query = em.createQuery(jpql, CarreraDTOCantidad.class);
        return query.getResultList();
    }

    @Override
    public CarreraDTO getCarreraByName(String name) {
        TypedQuery<CarreraDTO> query = em.createQuery(
                "SELECT new dto.CarreraDTO(c.id, c.nombre) "
                        + "FROM Carrera c WHERE LOWER(c.nombre) = LOWER(:name)",
                CarreraDTO.class
        );
        query.setParameter("name", name);
        return query.getResultStream().findFirst().orElse(null);
    }

    @Override
    public List<ReporteCarreraDTO> generarReporteCarreras() {
        // Punto 3: native SQL — por carrera y año, inscriptos (anio_inscripcion)
        // y egresados (anio_graduacion). UNION ALL une ambos ejes de años.
        String sql =
                "SELECT c.nombre AS nombre_carrera, "
                        + "       y.anio   AS anio, "
                        + "       SUM(CASE WHEN y.tipo = 'I' THEN y.cnt ELSE 0 END) AS cant_inscriptos, "
                        + "       SUM(CASE WHEN y.tipo = 'G' THEN y.cnt ELSE 0 END) AS cant_egresados "
                        + "FROM Carrera c "
                        + "JOIN ( "
                        + "    SELECT i.carrera_id AS carrera_id, i.anio_inscripcion AS anio, 'I' AS tipo, COUNT(*) AS cnt "
                        + "    FROM Inscripcion i "
                        + "    GROUP BY i.carrera_id, i.anio_inscripcion "
                        + "    UNION ALL "
                        + "    SELECT i.carrera_id AS carrera_id, i.anio_graduacion AS anio, 'G' AS tipo, COUNT(*) AS cnt "
                        + "    FROM Inscripcion i "
                        + "    WHERE i.anio_graduacion IS NOT NULL "
                        + "    GROUP BY i.carrera_id, i.anio_graduacion "
                        + ") y ON y.carrera_id = c.id "
                        + "GROUP BY c.nombre, y.anio "
                        + "ORDER BY c.nombre ASC, y.anio ASC";

        @SuppressWarnings("unchecked")
        List<Object[]> rows = em.createNativeQuery(sql).getResultList();

        List<ReporteCarreraDTO> reporte = new ArrayList<>();
        for (Object[] r : rows) {
            String nombreCarrera = (String) r[0];
            int anio = ((Number) r[1]).intValue();
            long cantInscriptos = ((Number) r[2]).longValue();
            long cantEgresados = ((Number) r[3]).longValue();
            reporte.add(new ReporteCarreraDTO(nombreCarrera, anio, cantInscriptos, cantEgresados));
        }
        return reporte;
    }
}
