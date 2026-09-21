package dto;

import entity.Genero;

public class EstudianteDTO {
    private final Long lu;
    private final String nombre;
    private final String apellido;
    private final int edad;
    private final Genero genero;
    private final Long dni;
    private final String ciudad;

    public EstudianteDTO(
            Long lu,
            String nombre,
            String apellido,
            int edad,
            Genero genero,
            Long dni,
            String ciudad
    ){
        this.lu = lu;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.genero = genero;
        this.dni = dni;
        this.ciudad = ciudad;
    }

    public Long getLu() {
        return lu;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public int getEdad() {
        return edad;
    }

    public Genero getGenero() {
        return genero;
    }

    public Long getDni() {
        return dni;
    }

    public String getCiudad() {
        return ciudad;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "EstudianteDTO{" +
                "lu=" + lu +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", edad=" + edad +
                ", genero=" + genero +
                ", dni=" + dni +
                ", ciudad='" + ciudad + '\'' +
                '}';
    }
}
