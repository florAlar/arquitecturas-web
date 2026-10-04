package service;

import model.Estudiante;
import model.Genero;

import java.util.List;
import java.util.Optional;

public interface EstudianteService {
    /**
     * Servicio encargado de retornar un listado completo de estudiantes.
     *
     * @return Listado con estudiantes.
     * @throws Exception e
     */
    List<Estudiante> findAll()throws Exception;

    /**
     * Servicio encargado de retornar un listado completo de estudiantes ordenado.
     * por ejemplo:
     *1. Por defecto (Ordena por id de forma ascendente):
     *      GET http://localhost:8080/estudiantes
     * 2. Ordenar por edad de menor a mayor (ascendente):
     *      GET http://localhost:8080/estudiantes?sortBy=edad&direction=asc
     * 3. Ordenar por nombre de la Z a la A (descendente):
     *      GET http://localhost:8080/estudiantes?sortBy=apellido&direction=desc
     * @return Listado con estudiantes.
     * @throws Exception e
     */

    List<Estudiante> findAll(String sortBy, String direction);

    /**
     * Servicio encargado de buscar y retornar un estudiante coincidente con el id ingresado por parámetro.
     *
     * @param id Identificador únido del estudiante.
     * @return Estudiante coincidente con id.
     * @throws Exception e
     */
    Optional<Estudiante> findById(Long id)throws Exception;

    /**
     * Servicio encargado de retornar un listado de estudiantes de un género.
     *
     * @return Listado con estudiantes.
     * @throws Exception e
     */
    List<Estudiante> findByGenero(Genero genero)throws Exception;

    /**
     * Servicio encargado de persistir un estudiante ingresado por parámetro.
     * @param estudiante estudiante a persistir
     * @return Estudiante persistido con id asignado.
     * @throws Exception e
     */
    Estudiante save(Estudiante estudiante)throws  Exception;

    /**
     * Servicio encargado de actualizar un estudiante.
     * @param id Identificador único de la estudiante a actualizar.
     * @param estudiante Estudiante con los datos a actualizar.
     * @return Retorna al estudiante actualizado.
     * @throws Exception e
     */
    Estudiante update(Long id, Estudiante estudiante)throws Exception;

    /**
     * Servicio encargado de eliminar un estudiante correspondiente al id ingresado por parámetro.
     * @param id Identificador único del estudiante a eliminar.
     * @return True en caso de eliminación exitosa, caso contrario false.
     * @throws Exception e
     */
    boolean delete(Long id)throws Exception;
}
