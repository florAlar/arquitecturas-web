package loader;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

// coordina la carga inicial CSV → Entities → persist (una TX).
// orden de FKs :  Carrera → Estudiante → Inscripcion.
// Los repositorios de consulta no usan esta clase. Solo se usa para la carga de datos inicial de la base.


public final class DataLoader {

    private final CarreraCsvLoader carreraLoader = new CarreraCsvLoader();
    private final EstudianteCsvLoader estudianteLoader = new EstudianteCsvLoader();
    private final InscripcionCsvLoader inscripcionLoader = new InscripcionCsvLoader();

    // Carga los tres CSV en una sola transacción sobre el EntityManager recibido.
    // No se cierra EntityManager lo administra Main/factory.


    public void loadAll(EntityManager em) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager requerido");
        }

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            carreraLoader.load(em);
            estudianteLoader.load(em);
            inscripcionLoader.load(em);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }
}
