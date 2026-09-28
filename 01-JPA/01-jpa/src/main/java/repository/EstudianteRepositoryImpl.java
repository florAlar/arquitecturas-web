package repository;

import dto.EstudianteDTO;
import entity.Estudiante;
import entity.Genero;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EstudianteRepositoryImpl implements EstudianteRepository {

    private final EntityManager em;

    private final ArrayList<String> campos = new ArrayList<>(
            Arrays.asList("nombre", "apellido", "dni", "edad", "genero", "ciudad"));


    public EstudianteRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void create(Estudiante estudiante) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(estudiante);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }

    }

    @Override
    public List<EstudianteDTO> getEstudiantesOrdered(String campo) {

        String campoLower = campo.toLowerCase();

        if (!this.campos.contains(campoLower)) {
            throw new IllegalArgumentException("No existe el campo solicitado!");
        }

        TypedQuery<EstudianteDTO> query = em.createQuery(
                "SELECT new dto.EstudianteDTO(e.lu, e.nombres, e.apellido, e.edad, e.genero, e.dni, e.ciudad) "
                        + "FROM Estudiante e ORDER BY LOWER(e." + campoLower + ")",
                EstudianteDTO.class
        );
        return query.getResultList();
    }

    @Override
    public List<EstudianteDTO> findAllByGenero(Genero genero) {


        TypedQuery<EstudianteDTO> query = em.createQuery(
                "SELECT new dto.EstudianteDTO(e.lu, e.nombres, e.apellido, e.edad, e.genero, e.dni, e.ciudad) "
                        + "FROM Estudiante e WHERE e.genero = :genero",
                EstudianteDTO.class
        );
        query.setParameter("genero", genero);
        return query.getResultList();

    }

    @Override
    public EstudianteDTO findByNroLibreta(Long nroLibreta) {
        TypedQuery<EstudianteDTO> query = em.createQuery(
                "SELECT new dto.EstudianteDTO(e.lu, e.nombres, e.apellido, e.edad, e.genero, e.dni, e.ciudad) "
                        + "FROM Estudiante e WHERE e.lu = :nroLibreta",
                EstudianteDTO.class
        );
        query.setParameter("nroLibreta", nroLibreta);
        return query.getResultStream().findFirst().orElse(null);
    }

    @Override
    public List<EstudianteDTO> getEstudiantesByCarreraAndCiudadResidencia(Long idCarrera, String ciudadResidencia) {
        String jpql = "SELECT new dto.EstudianteDTO(e.lu, e.nombres, e.apellido, e.edad, e.genero, e.dni, e.ciudad) " +
                "FROM Inscripcion i JOIN i.estudiante e " +
                "WHERE i.carrera.id = :idCarrera " +
                "  AND LOWER(e.ciudad) = LOWER(:ciudadResidencia)";
        TypedQuery<EstudianteDTO> query = em.createQuery(jpql,EstudianteDTO.class);
        query.setParameter("idCarrera", idCarrera);
        query.setParameter("ciudadResidencia", ciudadResidencia);
        return query.getResultList();
    }
}
