package repository;

import entity.Carrera;
import entity.Estudiante;
import entity.Inscripcion;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class InscripcionRepositoryImpl implements InscripcionRepository {

    private final EntityManager em;

    public InscripcionRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void matricular(Long nroLibreta, Long idCarrera) {
        if (nroLibreta == null || idCarrera == null) {
            throw new IllegalArgumentException("La libreta y la carrera son obligatorias");
        }

        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

            Estudiante estudiante = em.createQuery(
                            "SELECT e FROM Estudiante e WHERE e.lu = :nroLibreta",
                            Estudiante.class
                    )
                    .setParameter("nroLibreta", nroLibreta)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if (estudiante == null) {
                throw new IllegalArgumentException("No existe un estudiante con libreta " + nroLibreta);
            }

            Carrera carrera = em.createQuery(
                            "SELECT c FROM Carrera c WHERE c.id = :idCarrera",
                            Carrera.class
                    )
                    .setParameter("idCarrera", idCarrera)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if (carrera == null) {
                throw new IllegalArgumentException("No existe una carrera con id " + idCarrera);
            }

            Long cantidadInscripciones = em.createQuery(
                            "SELECT COUNT(i) FROM Inscripcion i "
                                    + "WHERE i.estudiante.lu = :nroLibreta AND i.carrera.id = :idCarrera",
                            Long.class
                    )
                    .setParameter("nroLibreta", nroLibreta)
                    .setParameter("idCarrera", idCarrera)
                    .getSingleResult();
            if (cantidadInscripciones > 0) {
                throw new IllegalStateException("El estudiante ya esta matriculado en la carrera");
            }

            Inscripcion inscripcion = new Inscripcion(estudiante, carrera, 0, false);
            estudiante.agregarInscripcion(inscripcion);
            carrera.agregarInscripcion(inscripcion);
            em.persist(inscripcion);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
}
