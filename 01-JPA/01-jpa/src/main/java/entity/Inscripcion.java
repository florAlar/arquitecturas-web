package entity;

import javax.persistence.*;

@Table( // un alumno no se matricula dos veces en la misma carrera
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"estudiante_lu", "carrera_id"}
        )
)
@Entity
public class Inscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_lu", nullable = false)
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrera_id", nullable = false)
    private Carrera carrera;

    /** Año de ingreso/inscripción (CSV: columna inscripcion). */
    @Column(name = "anio_inscripcion", nullable = false)
    private int anioInscripcion;

    /** null si no egresó (CSV graduacion = 0) */
    @Column(name = "anio_graduacion")
    private Integer anioGraduacion;

    private int antiguedad;

    private boolean graduado;

    public Inscripcion() { }

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

    public Long getId() {
        return id;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    public int getAnioInscripcion() {
        return anioInscripcion;
    }

    public void setAnioInscripcion(int anioInscripcion) {
        this.anioInscripcion = anioInscripcion;
    }

    public Integer getAnioGraduacion() {
        return anioGraduacion;
    }

    public void setAnioGraduacion(Integer anioGraduacion) {
        this.anioGraduacion = anioGraduacion;
    }

    public int getAntiguedad() {
        return antiguedad;
    }

    public void setAntiguedad(int antiguedad) {
        this.antiguedad = antiguedad;
    }

    public boolean isGraduado() {
        return graduado;
    }

    public void setGraduado(boolean graduado) {
        this.graduado = graduado;
    }

    @Override
    public String toString() {
        return "Inscripcion{" +
                "id=" + id +
                ", anioInscripcion=" + anioInscripcion +
                ", anioGraduacion=" + anioGraduacion +
                ", antiguedad=" + antiguedad +
                ", graduado=" + graduado +
                '}';
    }
}
