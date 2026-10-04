package loader;

import model.Carrera;
import model.Estudiante;
import model.Genero;
import model.Inscripcion;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import repository.CarreraRepository;
import repository.EstudianteRepository;
import repository.InscripcionRepository;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


// Bootstrap CSV → DB (una unica TX). Orden FK: Carrera → Estudiante → Inscripcion.
// Solo para arranque; no lo usan controllers/services de negocio.

@Component
public class DataLoader implements ApplicationRunner {

    private static final String CARRERAS = "/CSV/carreras.csv";
    private static final String ESTUDIANTES = "/CSV/estudiantes.csv";
    private static final String INSCRIPCIONES = "/CSV/estudianteCarrera.csv";

    private final CarreraRepository carreraRepository;
    private final EstudianteRepository estudianteRepository;
    private final InscripcionRepository inscripcionRepository;

    public DataLoader(
            CarreraRepository carreraRepository,
            EstudianteRepository estudianteRepository,
            InscripcionRepository inscripcionRepository
    ) {
        this.carreraRepository = carreraRepository;
        this.estudianteRepository = estudianteRepository;
        this.inscripcionRepository = inscripcionRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (carreraRepository.count() > 0) {
            return;
        }
        Map<Long, Carrera> carrerasPorId = loadCarreras();
        Map<Long, Estudiante> estudiantesPorDni = loadEstudiantes();
        loadInscripciones(carrerasPorId, estudiantesPorDni);
    }

    private Map<Long, Carrera> loadCarreras() {
        List<Carrera> lista = new ArrayList<>();
        try (Reader reader = open(CARRERAS);
             CSVParser parser = parser(reader)) {
            for (CSVRecord row : parser) {
                Long id = Long.valueOf(row.get("id_carrera"));
                String nombre = row.get("carrera");
                int duracion = Integer.parseInt(row.get("duracion"));
                lista.add(new Carrera(id, nombre, duracion));
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error cargando " + CARRERAS, e);
        }

        Map<Long, Carrera> porId = new HashMap<>();
        for (Carrera c : carreraRepository.saveAll(lista)) {
            porId.put(c.getId(), c);
        }
        return porId;
    }

    private Map<Long, Estudiante> loadEstudiantes() {
        List<Estudiante> lista = new ArrayList<>();
        try (Reader reader = open(ESTUDIANTES);
             CSVParser parser = parser(reader)) {
            for (CSVRecord row : parser) {
                Long dni = Long.valueOf(row.get("DNI"));
                String nombres = row.get("nombre");
                String apellido = row.get("apellido");
                int edad = Integer.parseInt(row.get("edad"));
                Genero genero = GeneroMapper.fromCsv(row.get("genero"));
                String ciudad = row.get("ciudad");
                Long lu = Long.valueOf(row.get("LU"));
                lista.add(new Estudiante(lu, nombres, apellido, edad, genero, dni, ciudad));
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error cargando " + ESTUDIANTES, e);
        }

        Map<Long, Estudiante> porDni = new HashMap<>();
        for (Estudiante e : estudianteRepository.saveAll(lista)) {
            porDni.put(e.getDni(), e);
        }
        return porDni;
    }

    private void loadInscripciones(
            Map<Long, Carrera> carrerasPorId,
            Map<Long, Estudiante> estudiantesPorDni
    ) {
        List<Inscripcion> lista = new ArrayList<>();
        Set<String> yaCargados = new HashSet<>();

        try (Reader reader = open(INSCRIPCIONES);
             CSVParser parser = parser(reader)) {
            for (CSVRecord row : parser) {
                long dni = Long.parseLong(row.get("id_estudiante"));
                long idCarrera = Long.parseLong(row.get("id_carrera"));
                int anioInscripcion = Integer.parseInt(row.get("inscripcion"));
                int graduacionCsv = Integer.parseInt(row.get("graduacion"));
                int antiguedad = Integer.parseInt(row.get("antiguedad"));
                // columna id del CSV se ignora (PK IDENTITY)

                Estudiante estudiante = estudiantesPorDni.get(dni);
                if (estudiante == null) {
                    throw new IllegalStateException(
                            "Fila " + row.getRecordNumber()
                                    + ": no hay estudiante con DNI=" + dni
                    );
                }

                Carrera carrera = carrerasPorId.get(idCarrera);
                if (carrera == null) {
                    throw new IllegalStateException(
                            "Fila " + row.getRecordNumber()
                                    + ": no hay carrera id=" + idCarrera
                    );
                }

                String clave = estudiante.getLu() + "-" + idCarrera;
                if (!yaCargados.add(clave)) {
                    System.out.println(
                            "Aviso: inscripción duplicada en CSV (se omite): LU="
                                    + estudiante.getLu()
                                    + " carrera=" + idCarrera
                                    + " fila=" + row.getRecordNumber()
                    );
                    continue;
                }

                Integer anioGraduacion = graduacionCsv == 0 ? null : graduacionCsv;
                boolean graduado = graduacionCsv != 0;

                Inscripcion inscripcion = new Inscripcion(
                        estudiante,
                        carrera,
                        anioInscripcion,
                        anioGraduacion,
                        antiguedad,
                        graduado
                );
                lista.add(inscripcion);
            }
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("Error cargando " + INSCRIPCIONES, e);
        }

        inscripcionRepository.saveAll(lista);
    }

    private static CSVParser parser(Reader reader) throws Exception {
        return CSVFormat.DEFAULT
                .builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setIgnoreEmptyLines(true)
                .setTrim(true)
                .build()
                .parse(reader);
    }

    private static Reader open(String resource) {
        InputStream in = DataLoader.class.getResourceAsStream(resource);
        if (in == null) {
            throw new IllegalStateException("No se encontró recurso classpath " + resource);
        }
        return new InputStreamReader(in, StandardCharsets.ISO_8859_1);
    }
}
