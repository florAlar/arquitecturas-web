package loader;

import entity.Carrera;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javax.persistence.EntityManager;
import java.io.Reader;

/**
 * Carga carreras.csv → Entity Carrera.
 * Columnas: id_carrera, carrera, duracion
 */
public final class CarreraCsvLoader {

    private static final String RESOURCE = "/CSV/carreras.csv";

    public void load(EntityManager em) {
        try (Reader reader = CsvClasspath.open(RESOURCE);
             CSVParser parser = CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .withIgnoreEmptyLines()
                     .withTrim()
                     .parse(reader)) {

            for (CSVRecord row : parser) {
                Long id = Long.valueOf(row.get("id_carrera"));
                String nombre = row.get("carrera");
                int duracion = Integer.parseInt(row.get("duracion"));

                Carrera carrera = new Carrera(id, nombre, duracion);
                em.persist(carrera);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error cargando " + RESOURCE, e);
        }
    }
}
