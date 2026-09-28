package loader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

// Abre CSV del path (src/main/resources).

public final class CsvClasspath {

    public static final Charset CSV_CHARSET = Charset.forName("ISO-8859-1");

    private CsvClasspath() { }

    public static Reader open(String classpathLocation) {
        String path = classpathLocation.startsWith("/")
                ? classpathLocation
                : "/" + classpathLocation;
        InputStream in = CsvClasspath.class.getResourceAsStream(path);
        if (in == null) {
            throw new IllegalStateException("No se encontró el recurso classpath: " + path);
        }
        return new BufferedReader(new InputStreamReader(in, CSV_CHARSET));
    }
}
