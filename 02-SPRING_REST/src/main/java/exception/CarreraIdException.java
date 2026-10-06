package exception;

public class CarreraIdException extends RuntimeException {
    public CarreraIdException() {
        super("no se puede modificar el id de una instancia!");
    }
}
