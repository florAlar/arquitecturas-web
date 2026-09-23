package repository;

import dto.CarreraDTOCantidad;
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






}
