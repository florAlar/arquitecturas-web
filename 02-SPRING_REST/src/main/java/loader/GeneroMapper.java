package loader;

import model.Genero;

// Mapea los strings distintos de género del CSV al enum Genero.
public final class GeneroMapper {

    private GeneroMapper() {
    }

    public static Genero fromCsv(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("genero vacío en CSV");
        }
        String v = raw.trim();
        switch (v.toLowerCase()) {
            case "male":
            case "masculino":
                return Genero.MASCULINO;
            case "female":
            case "femenino":
                return Genero.FEMENINO;
            case "non-binary":
            case "nonbinary":
            case "no_binario":
            case "nobinario":
                return Genero.NO_BINARIO;
            case "polygender":
            case "poligenérico":
            case "poligenerico":
                return Genero.POLIGENERICO;
            case "agender":
            case "agénero":
            case "agenero":
                return Genero.AGENERO;
            case "genderfluid":
            case "gender fluid":
                return Genero.GENERO_FLUIDO;
            case "bigender":
                return Genero.BIGENERO;
            default:
                throw new IllegalArgumentException("genero CSV no mapeado: '" + raw + "'");
        }
    }
}
