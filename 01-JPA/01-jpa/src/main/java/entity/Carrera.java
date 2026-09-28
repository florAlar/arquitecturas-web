package entity;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Carrera {
    @Id
    // ID viene de carreras.csv (id_carrera); no autogenerar
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private int duracion;

    @OneToMany(mappedBy = "carrera")
    private List<Inscripcion> inscripciones = new ArrayList<>();

    public Carrera() { }

    public Carrera(Long id, String nombre, int duracion) {
        this.id = id;
        this.nombre = nombre;
        this.duracion = duracion;
    }

    public void agregarInscripcion(Inscripcion inscripcion) {
        inscripciones.add(inscripcion);
        inscripcion.setCarrera(this);
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public List<Inscripcion> getInscripciones() { return inscripciones; }

    public void setInscripciones(List<Inscripcion> inscripciones) { this.inscripciones = inscripciones; }

    @Override
    public String toString() {
        return "Carrera{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", duracion=" + duracion +
                '}';
    }
}
