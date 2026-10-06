package exception;

public class EstudianteGeneroException extends RuntimeException {
    public EstudianteGeneroException(String genero) {
        super("No se encuentra el genero: " + genero);
    }
}
