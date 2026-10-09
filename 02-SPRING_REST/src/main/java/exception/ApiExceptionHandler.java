package exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler({
            EstudianteNotFoundException.class,
            CarreraNotFoundException.class,
            InscripcionNotFoundException.class
    })
    public ResponseEntity<ApiError> handleNotFound(RuntimeException ex) {
        return build(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler({
            EstudianteDuplicadoException.class,
            CarreraNombreDuplicadoException.class,
            CarreraIdDuplicadoException.class,
            InscripcionDuplicadaException.class
    })
    public ResponseEntity<ApiError> handleConflict(RuntimeException ex) {
        return build(HttpStatus.CONFLICT, ex);
    }

    @ExceptionHandler({
            BadRequestException.class,
            EstudianteGeneroException.class
    })
    public ResponseEntity<ApiError> handleBadRequest(RuntimeException ex) {
        return build(HttpStatus.BAD_REQUEST, ex);
    }

    private ResponseEntity<ApiError> build(HttpStatus status, RuntimeException ex) {
        return ResponseEntity.status(status).body(new ApiError(status, ex.getMessage()));
    }
}