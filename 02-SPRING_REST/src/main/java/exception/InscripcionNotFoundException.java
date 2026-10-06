package exception;

public class InscripcionNotFoundException extends RuntimeException {
    public InscripcionNotFoundException(Long id) {super("Inscripcion no encontrada con id=" + id);
    }
}
