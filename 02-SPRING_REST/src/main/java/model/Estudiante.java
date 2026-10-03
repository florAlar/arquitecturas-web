package model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "estudiante")
@Getter
@Setter
@NoArgsConstructor
public class Estudiante {
    @Id
    @Setter(AccessLevel.NONE)
    private Long lu;

    @Column(nullable = false)
    private String nombres;

    @Column(nullable = false)
    private String apellido;

    private int edad;

    @Enumerated(EnumType.STRING)
    private Genero genero;

    @Column(unique = true, nullable = false)
    private Long dni;

    private String ciudad;

    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inscripcion> inscripciones = new ArrayList<>();

    public Estudiante(Long lu, String nombres, String apellido, int edad, Genero genero, Long dni, String ciudad) {
        this.lu = lu;
        this.nombres = nombres;
        this.apellido = apellido;
        this.edad = edad;
        this.genero = genero;
        this.dni = dni;
        this.ciudad = ciudad;
    }

    public void agregarInscripcion(Inscripcion inscripcion) {
        inscripciones.add(inscripcion);
        inscripcion.setEstudiante(this);
    }
}
