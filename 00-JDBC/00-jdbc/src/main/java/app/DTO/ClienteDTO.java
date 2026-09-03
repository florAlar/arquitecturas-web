package app.DTO;

public class ClienteDTO {
    private String nombre;
    private String email;
    private float totalFacturado;

    public ClienteDTO() {
    }

    public ClienteDTO(String nombre, String email, float totalFacturado) {
        this.nombre = nombre;
        this.email = email;
        this.totalFacturado = totalFacturado;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public float getTotalFacturado() {
        return totalFacturado;
    }

    @Override
    public String toString() {
        return "ClienteDTO{" +
                "nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", totalFacturado=" + totalFacturado +
                '}';
    }
}
