package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "inscripcion",
        uniqueConstraints = @UniqueConstraint(columnNames = {"estudiante_lu", "carrera_id"})
)
@Getter
@Setter
@NoArgsConstructor
public class Inscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_lu", nullable = false)
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrera_id", nullable = false)
    private Carrera carrera;

    @Column(name = "anio_inscripcion", nullable = false)
    private int anioInscripcion;

    @Column(name = "anio_graduacion")
    private Integer anioGraduacion;

    private int antiguedad;

    private boolean graduado;

    public Inscripcion(
            Estudiante estudiante,
            Carrera carrera,
            int anioInscripcion,
            Integer anioGraduacion,
            int antiguedad,
            boolean graduado
    ) {
        this.estudiante = estudiante;
        this.carrera = carrera;
        this.anioInscripcion = anioInscripcion;
        this.anioGraduacion = anioGraduacion;
        this.antiguedad = antiguedad;
        this.graduado = graduado;
    }
}
