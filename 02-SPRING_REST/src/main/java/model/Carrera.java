package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carrera")
@Getter
@Setter
@NoArgsConstructor
public class Carrera {
    @Id
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private int duracion;

    @OneToMany(mappedBy = "carrera")
    private List<Inscripcion> inscripciones = new ArrayList<>();

    public Carrera(Long id, String nombre, int duracion) {
        this.id = id;
        this.nombre = nombre;
        this.duracion = duracion;
    }

    public void agregarInscripcion(Inscripcion inscripcion) {
        inscripciones.add(inscripcion);
        inscripcion.setCarrera(this);
    }

}
