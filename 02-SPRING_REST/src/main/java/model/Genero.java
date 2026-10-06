package model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Genero {
    MASCULINO,
    FEMENINO,
    NO_BINARIO,
    POLIGENERICO,
    AGENERO,
    GENERO_FLUIDO,
    BIGENERO,
    TRANSGENERO;

    @JsonCreator
    public static Genero fromString(String value) {
        return Genero.valueOf(value.trim().toUpperCase());
    }
}
