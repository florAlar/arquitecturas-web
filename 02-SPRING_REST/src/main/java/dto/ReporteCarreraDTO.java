package dto;

public record ReporteCarreraDTO(
        String nombreCarrera,
        int anio,
        Long cantidadInscriptos,
        Long cantidadEgresados
) {
}
