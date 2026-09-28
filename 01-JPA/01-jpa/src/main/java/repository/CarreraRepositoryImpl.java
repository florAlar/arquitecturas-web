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
                "SELECT new dto.ReporteCarreraDTO(i.fechaEgreso, c.nombre, COUNT(i), "
                        + "(SELECT COUNT(i2) FROM Inscripcion i2 WHERE i2.carrera = c AND i2.fechaIngreso = i.fechaEgreso)) "
                        + "FROM Inscripcion i "
                        + "JOIN i.carrera c "
                        + "WHERE i.fechaEgreso != 0 "
                        + "GROUP BY c.nombre, i.fechaEgreso "
                        + "ORDER BY c.nombre, i.fechaEgreso",
                ReporteCarreraDTO.class
        );
        return query.getResultList();
    }

}
