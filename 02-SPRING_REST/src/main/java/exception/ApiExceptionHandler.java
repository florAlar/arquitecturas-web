package exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(EstudianteNotFoundException.class)
    public ResponseEntity<ApiError> handleEstudianteNotFound(EstudianteNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(ex.getMessage()));
    }

    @ExceptionHandler(CarreraNotFoundException.class)
    public ResponseEntity<ApiError> handleCarreraNotFound(CarreraNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(ex.getMessage()));
    }

    @ExceptionHandler(CarreraNombreDuplicadoException.class)
    public ResponseEntity<ApiError> handleCarreraDuplicada(CarreraNombreDuplicadoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError(ex.getMessage()));
    }

    @ExceptionHandler(InscripcionDuplicadaException.class)
    public ResponseEntity<ApiError> handleInscripcionDuplicada(InscripcionDuplicadaException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError(ex.getMessage()));
    }

    @ExceptionHandler(EstudianteDuplicadoException.class)
    public ResponseEntity<ApiError> handleEstudianteDuplicado(EstudianteDuplicadoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError(ex.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(ex.getMessage()));
    }

    @ExceptionHandler(InscripcionNotFoundException.class)
    public ResponseEntity<ApiError> handleInscripcionNotFound(InscripcionNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(ex.getMessage()));
    }

    @ExceptionHandler(EstudianteGeneroException.class)
    public ResponseEntity<ApiError> handleInscripcionNotFound(EstudianteGeneroException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(ex.getMessage()));
    }

    @ExceptionHandler(CarreraIdDuplicadoException.class)
    public ResponseEntity<ApiError> handleInscripcionNotFound(CarreraIdDuplicadoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError(ex.getMessage()));
    }

    @ExceptionHandler(CarreraIdException.class)
    public ResponseEntity<ApiError> handleInscripcionNotFound(CarreraIdException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError(ex.getMessage()));
    }
}
