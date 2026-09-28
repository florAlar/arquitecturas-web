package loader;

import entity.Estudiante;
import entity.Genero;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javax.persistence.EntityManager;
import java.io.Reader;

 // Carga estudiantes.csv → Entity Estudiante.
 // Columnas: DNI, nombre, apellido, edad, genero, ciudad, LU
 // PK de la entity = LU (no autogenerada).


public final class EstudianteCsvLoader {

    private static final String RESOURCE = "/CSV/estudiantes.csv";

    public void load(EntityManager em) {
        try (Reader reader = CsvClasspath.open(RESOURCE);
             CSVParser parser = CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .withIgnoreEmptyLines()
                     .withTrim()
                     .parse(reader)) {

            for (CSVRecord row : parser) {
                Long dni = Long.valueOf(row.get("DNI"));
                String nombres = row.get("nombre");
                String apellido = row.get("apellido");
                int edad = Integer.parseInt(row.get("edad"));
                Genero genero = GeneroMapper.fromCsv(row.get("genero"));
                String ciudad = row.get("ciudad");
                Long lu = Long.valueOf(row.get("LU"));

                Estudiante estudiante = new Estudiante(lu, nombres, apellido, edad, genero, dni, ciudad);
                em.persist(estudiante);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error cargando " + RESOURCE, e);
        }
    }
}
