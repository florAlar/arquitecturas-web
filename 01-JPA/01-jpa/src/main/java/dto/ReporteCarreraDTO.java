package dto;

/**
 * Fila del reporte de carreras (punto 3): por carrera y año, inscriptos y egresados.
 * Se completa cuando se implemente generarReporteCarreras en el repository.
 */
public class ReporteCarreraDTO {
    private final String nombreCarrera;
    private final int anio;
    private final long inscriptos;
    private final long egresados;

    public ReporteCarreraDTO(String nombreCarrera, int anio, long inscriptos, long egresados) {
        this.nombreCarrera = nombreCarrera;
        this.anio = anio;
        this.inscriptos = inscriptos;
        this.egresados = egresados;
    }

    public String getNombreCarrera() {
        return nombreCarrera;
    }

    public int getAnio() {
        return anio;
    }

    public long getInscriptos() {
        return inscriptos;
    }

    public long getEgresados() {
        return egresados;
    }

    @Override
    public String toString() {
        return "ReporteCarreraDTO{" +
                "carrera='" + nombreCarrera + '\'' +
                ", anio=" + anio +
                ", inscriptos=" + inscriptos +
                ", egresados=" + egresados +
                '}';
    }
}
