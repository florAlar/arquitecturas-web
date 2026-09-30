import dto.EstudianteDTO;
import dto.ReporteCarreraDTO;
import entity.Estudiante;
import entity.Genero;
import factory.FactoryEntityManager;
import loader.DataLoader;
import repository.CarreraRepository;
import repository.EstudianteRepository;
import repository.InscripcionRepository;

import javax.persistence.EntityManager;


public class Main {

    public static void main(String[] args) {
        FactoryEntityManager factory = FactoryEntityManager.getDAOFactory(FactoryEntityManager.MYSQL);
        EntityManager em = factory.createEntityManager();

        try {
            System.out.println("=== Carga inicial desde CSV ===");
            new DataLoader().loadAll(em);
            System.out.println("Carga OK.");
            //consulto cuantos hay en total
            long carreras = em.createQuery("SELECT COUNT(c) FROM Carrera c", Long.class).getSingleResult();
            long estudiantes = em.createQuery("SELECT COUNT(e) FROM Estudiante e", Long.class).getSingleResult();
            long inscripciones = em.createQuery("SELECT COUNT(i) FROM Inscripcion i", Long.class).getSingleResult();
            System.out.println("Carreras: " + carreras + " (CSV: 15)");
            System.out.println("Estudiantes: " + estudiantes + " (CSV: 104)");
            System.out.println("Inscripciones: " + inscripciones + " (CSV: 109, 1 duplicado omitido)");

            EstudianteRepository estudianteRepo = factory.getEstudianteRepository(em);
            CarreraRepository carreraRepo = factory.getCarreraRepository(em);
            InscripcionRepository inscripcionRepo = factory.getInscripcionRepository(em);

            // 2.a) Alta de estudiante
            System.out.println("\n=== 2.a) Dar de alta un estudiante ===");
            Long luDemo = 99999L;
            Estudiante nuevo = new Estudiante(
                    luDemo, "Ana", "Prueba", 22, Genero.FEMENINO, 30000000L, "Tandil"
            );
            estudianteRepo.create(nuevo);
            System.out.println("Alta OK: " + estudianteRepo.findByNroLibreta(luDemo));

            // 2.b) Matricular en una carrera (TUDAI id=1)
            System.out.println("\n=== 2.b) Matricular un estudiante en una carrera ===");
            inscripcionRepo.matricular(luDemo, 1L, 2024);
            System.out.println("Matriculado LU " + luDemo + " en carrera id=1 (TUDAI), anio 2024.");

            // 2.c) Todos los estudiantes con ordenamiento
            System.out.println("\n=== 2.c) Estudiantes ordenados por apellido (primeros 5) ===");
            estudianteRepo.getEstudiantesOrdered("apellido").stream().limit(5).forEach(System.out::println);

            // 2.d) Por número de libreta
            System.out.println("\n=== 2.d) Estudiante por LU (34978 del CSV) ===");
            System.out.println(estudianteRepo.findByNroLibreta(34978L));

            // 2.e) Por género
            System.out.println("\n=== 2.e) Estudiantes por genero FEMENINO (primeros 5) ===");
            estudianteRepo.findAllByGenero(Genero.FEMENINO).stream().limit(5).forEach(System.out::println);

            // 2.f) Carreras con inscriptos ordenadas por cantidad
            System.out.println("\n=== 2.f) Carreras con inscriptos (orden por cantidad DESC) ===");
            carreraRepo.getCarrerasConInscriptosOrdenadas().forEach(System.out::println);

            // 2.g) Estudiantes de una carrera filtrados por ciudad
            System.out.println("\n=== 2.g) Estudiantes carrera id=1 (TUDAI) en ciudad Tandil ===");
            for (EstudianteDTO dto : estudianteRepo.getEstudiantesByCarreraAndCiudadResidencia(1L, "Tandil")) {
                System.out.println(dto);
            }

            // 3) Reporte de carreras: inscriptos y egresados por año
            System.out.println("\n=== 3) Reporte carreras (inscriptos/egresados por anio) ===");
            for (ReporteCarreraDTO fila : carreraRepo.generarReporteCarreras()) {
                System.out.println(fila);
            }

        } finally {
            if (em.isOpen()) {
                em.close();
            }
            factory.closeEntityManagerFactory();
        }
    }
}
