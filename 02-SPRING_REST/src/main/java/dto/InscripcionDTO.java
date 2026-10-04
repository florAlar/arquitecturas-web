package dto;

public class InscripcionDTO {

    // Inscribir un estudiante (LU) en una carrera (id).
    public record Create(
            Long estudianteLu,
            Long carreraId
    ) {}

    public record Response(
            Long id,
            Long estudianteLu,
            Long carreraId,
            int anioInscripcion,
            Integer anioGraduacion,
            int antiguedad,
            boolean graduado
    ) {
    }
}
