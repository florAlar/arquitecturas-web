package factory;
import java.sql.SQLException;
import javax.persistence.EntityManager;

import repository.CarreraRepositoryImpl;
import repository.InscripcionRepositoryImpl;
import repository.EstudianteRepositoryImpl;

public class MySQLFactory {

    private static MySQLFactory instance;
    private EntityManagerFactory emf;

    private MySQLFactory() {
        this.emf = Persistence.createEntityManagerFactory("MySqlPersistenceUnit");
    }

    public static MySQLFactory getInstance() {
        if(instance == null){
            instance = new MySQLFactory();
        }
        return instance;
    }

    @Override
    public EntityManager createEntityManager() {
        return emf.createEntityManager();
    }

    public void closeEntityManagerFactory() {
        emf.close();
    }

    @Override
    public CarreraRepositoryImpl getCarreraRepository(EntityManager em) {
        return CarreraRepositoryImpl.getInstance(em);
    }
    @Override
    public EstudianteRepositoryImpl getEstudianteRepository(EntityManager em) {
        return EstudianteRepositoryImpl.getInstance(em);
    }
    @Override
    public InscripcionRepositoryImpl getInscripcionRepository(EntityManager em) {
        return InscripcionRepositoryImpl.getInstance(em);
    }
}
