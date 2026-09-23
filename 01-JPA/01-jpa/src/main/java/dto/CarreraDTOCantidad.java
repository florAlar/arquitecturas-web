package dto;

public class CarreraDTOCantidad {
    private Long id;
    private String nombre;
    private Long cantidad;

    public Long getId(){
        return id;
    }

    public String getNombre(){
        return nombre;
    }

    public Long getCantidad(){
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
