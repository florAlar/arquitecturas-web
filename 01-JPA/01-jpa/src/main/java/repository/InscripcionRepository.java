package repository;

public interface InscripcionRepository {

    void matricular(Long nroLibreta, Long idCarrera, int fechaIngreso);
    void graduar(Long nroLibreta, Long idCarrera,int fechaEgreso);
}
