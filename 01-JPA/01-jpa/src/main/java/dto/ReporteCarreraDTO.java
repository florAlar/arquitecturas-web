package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReporteCarreraDTO {
    private int anioEgreso;
    private String nombreCarrera;
    private long cantEgresados;
    private long inscriptos;

    @Override
    public String toString() {
        return "{" +

                "Carrera='" + nombreCarrera + '\'' +
                ", Año de egreso=" + anioEgreso +
                ", Cantidad de egresados=" + cantEgresados +
                ", Inscriptos=" + inscriptos +
                '}';
    }
}
