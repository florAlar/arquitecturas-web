package factory;

import repository.CarreraRepositoryImpl;
import repository.EstudianteRepositoryImpl;
import repository.InscripcionRepositoryImpl;

import javax.persistence.EntityManager;
import java.sql.SQLException;

public abstract class FactoryEntityManager {

    public static final int MYSQL = 1;

    public abstract CarreraRepositoryImpl getCarreraRepository(EntityManager em);
    public abstract EstudianteRepositoryImpl getEstudianteRepository(EntityManager em);
    public abstract InscripcionRepositoryImpl getInscripcionRepository(EntityManager em);

    public abstract void closeEntityManagerFactory();
    public abstract EntityManager createEntityManager();

    public static FactoryEntityManager getDAOFactory(int persistence) throws SQLException {
        switch (persistence) {
            case MYSQL:
                return MySQLFactory.getInstance();
            default:
                return null;
        }
    }

}
