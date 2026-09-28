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
    public void matricular(Long nroLibreta, Long idCarrera, int fechaIngreso) {
        if (nroLibreta == null || idCarrera == null) {
            throw new IllegalArgumentException("La libreta y la carrera son obligatorias");
        }

        if (fechaIngreso < 1900) {
            throw new IllegalArgumentException("Ingrese una fecha reciente!");
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

            Carrera carrera = em.find(Carrera.class, idCarrera);
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

            // Alta nueva: antiguedad 0, sin graduación
            Inscripcion inscripcion = new Inscripcion(estudiante, carrera, fechaIngreso, null, 0, false);
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

    @Override
    public void graduar(Long nroLibreta, Long idCarrera, int fechaEgreso) {
        if (nroLibreta == null || idCarrera == null) {
            throw new IllegalArgumentException("La libreta y la carrera son obligatorias");
        }

        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

            Inscripcion inscripcion = em.createQuery(
                            "SELECT i FROM Inscripcion i "
                                    + "WHERE i.estudiante.lu = :nroLibreta AND i.carrera.id = :idCarrera",
                            Inscripcion.class
                    )
                    .setParameter("nroLibreta", nroLibreta)
                    .setParameter("idCarrera", idCarrera)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (inscripcion == null) {
                throw new IllegalArgumentException(
                        "No existe una inscripción para el estudiante " + nroLibreta + " en la carrera " + idCarrera);
            }

            if (fechaEgreso < inscripcion.getAnioInscripcion()) {
                throw new IllegalArgumentException("Fecha de Egreso Invalida!");
            }

            if (inscripcion.isGraduado()) {
                throw new IllegalArgumentException("El estudiante ya se encuentra graduado");
            }

            inscripcion.setGraduado(true);
            inscripcion.setAnioGraduacion(fechaEgreso);

            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
}
