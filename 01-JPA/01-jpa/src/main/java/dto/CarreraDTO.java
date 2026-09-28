package dto;

/**
 * DTO de carrera para consultas (p. ej. getCarreraByName).
 */
public class CarreraDTO {
    private final Long id;
    private final String nombreCarrera;

    public CarreraDTO(Long id, String nombreCarrera) {
        this.id = id;
        this.nombreCarrera = nombreCarrera;
    }

    public Long getId() {
        return id;
    }

    public String getNombreCarrera() {
        return nombreCarrera;
    }

    @Override
    public String toString() {
        return "CarreraDTO{" +
                "id=" + id +
                ", nombreCarrera='" + nombreCarrera + '\'' +
                '}';
    }
}
