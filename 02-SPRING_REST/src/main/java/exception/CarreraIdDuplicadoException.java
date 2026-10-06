package exception;

public class CarreraIdDuplicadoException extends RuntimeException {
    public CarreraIdDuplicadoException(String id) {
        super("Ya existe una carrera con el id: " + id);
    }
}
