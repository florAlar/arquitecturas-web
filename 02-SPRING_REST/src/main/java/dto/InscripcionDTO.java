package dto;

public record InscripcionDTO(
        Long id,
        Long carreraId,
        String carreraNombre,
        int anioInscripcion,
        Integer anioGraduacion,
        int antiguedad,
        boolean graduado
){}