import factory.FactoryEntityManager;
import loader.DataLoader;
import repository.CarreraRepository;
import repository.EstudianteRepository;

import javax.persistence.EntityManager;

/**
 * Demo del TP: EMF → carga CSV (una vez) → consultas vía Repository (sin leer CSV).
 */
public class Main {

    public static void main(String[] args) {
        FactoryEntityManager factory = FactoryEntityManager.getDAOFactory(FactoryEntityManager.MYSQL);
        EntityManager em = factory.createEntityManager();

        try {
            System.out.println("=== Carga inicial desde CSV ===");
            new DataLoader().loadAll(em);
            System.out.println("Carga OK.");
            //
            long carreras = em.createQuery("SELECT COUNT(c) FROM Carrera c", Long.class).getSingleResult();
            long estudiantes = em.createQuery("SELECT COUNT(e) FROM Estudiante e", Long.class).getSingleResult();
            long inscripciones = em.createQuery("SELECT COUNT(i) FROM Inscripcion i", Long.class).getSingleResult();
            System.out.println("Carreras: " + carreras + " (CSV tiene  15 carreras)");
            System.out.println("Estudiantes: " + estudiantes + " (CSV tiene 104)");
            System.out.println("Inscripciones: " + inscripciones + " (CSV tiene 109)");

            EstudianteRepository estudianteRepo = factory.getEstudianteRepository(em);
            CarreraRepository carreraRepo = factory.getCarreraRepository(em);

            System.out.println("\n=== 2d) Estudiante por LU (ejemplo primer LU del CSV: 34978) ===");
            System.out.println(estudianteRepo.findByNroLibreta(34978L));

            System.out.println("\n=== 2f) Carreras con inscriptos (top) ===");
            carreraRepo.getCarrerasConInscriptosOrdenadas().stream().limit(5).forEach(System.out::println);

        } finally {
            if (em.isOpen()) {
                em.close();
            }
            factory.closeEntityManagerFactory();
        }
    }
}
