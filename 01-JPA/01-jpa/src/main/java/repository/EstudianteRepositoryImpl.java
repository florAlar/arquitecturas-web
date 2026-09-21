package repository;

import dto.EstudianteDTO;
import entity.Estudiante;
import entity.Genero;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class EstudianteRepositoryImpl implements EstudianteRepository {

    private final EntityManager em;

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
    public List<EstudianteDTO> getEstudiantesOrderByApellido() {
        return List.of();
    }

    @Override
    public List<EstudianteDTO> findAllByGenero(Genero genero) {
        return List.of();
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
        return List.of();
    }
}
