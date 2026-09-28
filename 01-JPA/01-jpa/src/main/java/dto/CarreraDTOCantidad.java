package dto;

public class CarreraDTOCantidad {
    private final Long id;
    private final String nombre;
    private final Long cantidad;

    public CarreraDTOCantidad(Long id, String nombre, Long cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Long getCantidad() {
        return cantidad;
    }

    @Override
    public String toString() {
        return "CarreraDTOCantidad{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", cantidad Inscriptos=" + cantidad +
                '}';
    }
}
