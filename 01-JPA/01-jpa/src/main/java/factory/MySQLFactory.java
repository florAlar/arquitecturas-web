package factory;

import repository.CarreraRepository;
import repository.CarreraRepositoryImpl;
import repository.EstudianteRepository;
import repository.EstudianteRepositoryImpl;
import repository.InscripcionRepository;
import repository.InscripcionRepositoryImpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class MySQLFactory extends FactoryEntityManager {

    private static MySQLFactory instance;
    private final EntityManagerFactory emf;

    private MySQLFactory() {
        this.emf = Persistence.createEntityManagerFactory("MySqlPersistenceUnit");
    }

    public static synchronized MySQLFactory getInstance() {
        if (instance == null) {
            instance = new MySQLFactory();
        }
        return instance;
    }

    @Override
    public EntityManager createEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public void closeEntityManagerFactory() {
        if (emf.isOpen()) {
            emf.close();
        }
    }

    @Override
    public CarreraRepository getCarreraRepository(EntityManager em) {
        return new CarreraRepositoryImpl(em);
    }

    @Override
    public EstudianteRepository getEstudianteRepository(EntityManager em) {
        return new EstudianteRepositoryImpl(em);
    }

    @Override
    public InscripcionRepository getInscripcionRepository(EntityManager em) {
        return new InscripcionRepositoryImpl(em);
    }
}
