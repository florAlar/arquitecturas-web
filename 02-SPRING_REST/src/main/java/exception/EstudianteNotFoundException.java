package exception;

public class EstudianteNotFoundException extends RuntimeException {

    public EstudianteNotFoundException(Long lu) {
        super("Estudiante no encontrado con LU=" + lu);
    }
}
