package entity;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Carrera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "carrera")
    private List<Matricula> matriculas = new ArrayList<>();

    public Carrera() {  }

    //no incluye el id, porque se autogenera
    public Carrera(String nombre) {
        this.nombre = nombre;
    }

    public void agregarMatricula(Matricula matricula) {
        matriculas.add(matricula);
        matricula.setCarrera(this);
    }

    public Long getId() { return id; }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Matricula> getMatriculas() { return matriculas;  }

    public void setMatriculas(List<Matricula> matriculas) { this.matriculas = matriculas; }

    @Override
    public String toString() {
        return "Carrera{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                // ", matriculas=" + matriculas +
                '}';
    }
}
