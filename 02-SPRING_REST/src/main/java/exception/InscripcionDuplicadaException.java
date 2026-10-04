package exception;

public class InscripcionDuplicadaException extends RuntimeException {

    public InscripcionDuplicadaException(Long estudianteLu, Long carreraId) {
        super("El estudiante LU=" + estudianteLu
                + " ya está inscripto en la carrera id=" + carreraId);
    }
}
