package repository;

import dto.CarreraDTO;
import dto.CarreraDTOCantidad;
import dto.EstudianteDTO;
import dto.ReporteCarreraDTO;
import entity.Carrera;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class CarreraRepositoryImpl implements CarreraRepository {

    private final EntityManager em;

    private CarreraRepositoryImpl(EntityManager em){
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
                "SELECT new dto.CarreraDTO(c.id, c.nombres) "
                        + "FROM Carrera c WHERE c.nombres = :name",
                CarreraDTO.class
        );
        query.setParameter("name", name);
        return query.getResultStream().findFirst().orElse(null);
    }

    @Override
    public List<ReporteCarreraDTO> generarReporteCarreras() {
        TypedQuery<ReporteCarreraDTO> query = em.createQuery(
                "SELECT new dto.ReporteCarreraDTO(c.nombres, i.fechaIngreso, COUNT(i), "
                        + "SUM(CASE WHEN i.graduado = 1 THEN 1 ELSE 0 END)) "
                        + "FROM Inscripcion i JOIN i.carrera c "
                        + "GROUP BY c.nombres, i.fechaIngreso "
                        + "ORDER BY c.nombres, i.fechaIngreso",
                ReporteCarreraDTO.class
        );
        return query.getResultList();
    }


}
