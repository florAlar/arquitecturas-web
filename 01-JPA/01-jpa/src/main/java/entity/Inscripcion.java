package entity;

import javax.persistence.*;

@Table( // para impedir que un alumno se matricule en la misma carrera más de una vez
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



    private int fechaIngreso;     // se debe ingresar el AÑO en el que el estudiante ingresó/ingresa
    private int fechaEgreso;      // por defecto es 0
    private int antiguedad;       // Antigüedad en la carrera
    private int graduado;         // Indica si se graduó o no 0 = False, 1 = True



    public Inscripcion() { }

    //el constructor no incluye id, porque es generado automáticamente
    public Inscripcion(Estudiante estudiante, Carrera carrera, int antiguedad, int graduado,int fechaIngreso, int fechaEgreso) {
        this.estudiante     = estudiante;
        this.carrera        = carrera;
        this.antiguedad     = antiguedad;
        this.graduado       = graduado;
        this.fechaIngreso   = fechaIngreso;
        this.fechaEgreso    = fechaEgreso;
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

    public int getAntiguedad() {
        return antiguedad;
    }

    public void setAntiguedad(int antiguedad) {
        this.antiguedad = antiguedad;
    }

    public int isGraduado() {
        return graduado;
    }

    public void setGraduado(int graduado) {
        this.graduado = graduado;
    }

    public void SetFechaIngreso(int fechaIngreso) { this.fechaIngreso = fechaIngreso;}

    public int getFechaIngreso() {
        return fechaIngreso;
    }
    public int getFechaEgreso() {
        return fechaEgreso;
    }

    public void setFechaEgreso(int fechaEgreso) {
        this.fechaEgreso = fechaEgreso;
    }


    @Override
    public String toString() {
        return "Inscripcion{" +
                "id=" + id +
                ", estudiante=" + estudiante +
                ", carrera=" + carrera +
                ", antiguedad=" + antiguedad +
                ", graduado=" + graduado +
                '}';
    }
}