package entity;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;

public class Estudiante {

    @Enumerated(EnumType.STRING)
    private Genero genero;
}
