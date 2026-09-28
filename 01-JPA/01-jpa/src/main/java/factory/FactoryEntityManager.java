package factory;

import repository.CarreraRepository;
import repository.CarreraRepositoryImpl;
import repository.EstudianteRepository;
import repository.EstudianteRepositoryImpl;
import repository.InscripcionRepository;
import repository.InscripcionRepositoryImpl;

import javax.persistence.EntityManager;

/**
 * Abstract Factory de EntityManager + repositorios del TP.
 */
public abstract class FactoryEntityManager {

    public static final int MYSQL = 1;

    public abstract EntityManager createEntityManager();

    public abstract void closeEntityManagerFactory();

    public abstract CarreraRepository getCarreraRepository(EntityManager em);

    public abstract EstudianteRepository getEstudianteRepository(EntityManager em);

    public abstract InscripcionRepository getInscripcionRepository(EntityManager em);

    public static FactoryEntityManager getDAOFactory(int persistence) {
        switch (persistence) {
            case MYSQL:
                return MySQLFactory.getInstance();
            default:
                throw new IllegalArgumentException("Persistencia no soportada: " + persistence);
        }
    }
}
