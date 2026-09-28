package loader;

import entity.Carrera;
import entity.Estudiante;
import entity.Inscripcion;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.Reader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Carga estudianteCarrera.csv → Entity Inscripcion.
 * Columnas: id, id_estudiante, id_carrera, inscripcion, graduacion, antiguedad
 *
 * id_estudiante del CSV = DNI (no LU).
 * graduacion = 0 → no egresó.
 * El id del CSV se ignora (PK IDENTITY en Inscripcion).
 */
public final class InscripcionCsvLoader {

    private static final String RESOURCE = "/CSV/estudianteCarrera.csv";

    public void load(EntityManager em) {
        Map<Long, Estudiante> porDni = indexEstudiantesPorDni(em);
        // CSV puede traer el mismo (estudiante, carrera) más de una vez
        Set<String> yaCargados = new HashSet<>();

        try (Reader reader = CsvClasspath.open(RESOURCE);
             CSVParser parser = CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .withIgnoreEmptyLines()
                     .withTrim()
                     .parse(reader)) {

            for (CSVRecord row : parser) {
                long dni = Long.parseLong(row.get("id_estudiante"));
                long idCarrera = Long.parseLong(row.get("id_carrera"));
                int anioInscripcion = Integer.parseInt(row.get("inscripcion"));
                int graduacionCsv = Integer.parseInt(row.get("graduacion"));
                int antiguedad = Integer.parseInt(row.get("antiguedad"));

                Estudiante estudiante = porDni.get(dni);
                if (estudiante == null) {
                    throw new IllegalStateException(
                            "Fila " + row.getRecordNumber()
                                    + ": no hay estudiante con DNI=" + dni
                    );
                }

                Carrera carrera = em.find(Carrera.class, idCarrera);
                if (carrera == null) {
                    throw new IllegalStateException(
                            "Fila " + row.getRecordNumber()
                                    + ": no hay carrera id=" + idCarrera
                    );
                }

                String clave = estudiante.getLu() + "-" + idCarrera;
                if (!yaCargados.add(clave)) {
                    System.out.println("Aviso: inscripción duplicada en CSV (se omite): LU="
                            + estudiante.getLu() + " carrera=" + idCarrera
                            + " fila=" + row.getRecordNumber());
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
                estudiante.agregarInscripcion(inscripcion);
                carrera.agregarInscripcion(inscripcion);
                em.persist(inscripcion);
            }
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("Error cargando " + RESOURCE, e);
        }
    }

    private static Map<Long, Estudiante> indexEstudiantesPorDni(EntityManager em) {
        TypedQuery<Estudiante> q = em.createQuery("SELECT e FROM Estudiante e", Estudiante.class);
        List<Estudiante> todos = q.getResultList();
        Map<Long, Estudiante> map = new HashMap<>(todos.size() * 2);
        for (Estudiante e : todos) {
            map.put(e.getDni(), e);
        }
        return map;
    }
}
