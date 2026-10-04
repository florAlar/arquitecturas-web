package service;

import dto.EstudianteDTO;
import model.Genero;

import java.util.List;

public interface EstudianteService {

    /**
     * Servicio encargado de persistir un estudiante ingresado por parámetro.
     * @param in DTO de alta del estudiante a persistir
     * @return Estudiante persistido (DTO de respuesta).
     */
    EstudianteDTO.Response altaEstudiante(EstudianteDTO.Create in);

    /**
     * Servicio encargado de retornar un listado completo de estudiantes ordenado.
     * por ejemplo:
     *1. Por defecto (Ordena por lu de forma ascendente):
     *      GET http://localhost:8080/estudiantes
     * 2. Ordenar por edad de menor a mayor (ascendente):
     *      GET http://localhost:8080/estudiantes?sortBy=edad&direction=asc
     * 3. Ordenar por nombre de la Z a la A (descendente):
     *      GET http://localhost:8080/estudiantes?sortBy=apellido&direction=desc
     * @return Listado con estudiantes (DTO sin inscripciones).
     */
    List<EstudianteDTO.Response> listarEstudiantes(String sortBy, String direction);

    /**
     * Servicio encargado de buscar y retornar un estudiante coincidente con la LU ingresada por parámetro.
     *
     * @param lu Identificador único del estudiante (libreta universitaria).
     * @return Estudiante coincidente con LU (DTO de detalle con inscripciones).
     */
    EstudianteDTO.Detail getByLu(Long lu);

    /**
     * Servicio encargado de retornar un listado de estudiantes de un género.
     *
     * @return Listado con estudiantes (DTO sin inscripciones).
     */
    List<EstudianteDTO.Response> listarPorGenero(Genero genero);

    /**
     * g) Estudiantes de una carrera filtrados por ciudad de residencia.
     * Ejemplo: GET http://localhost:8080/estudiantes/carrera/1?ciudad=Tandil
     *
     * @param idCarrera id de la carrera
     * @param ciudad ciudad de residencia (obligatoria)
     * @return Listado con estudiantes (DTO sin inscripciones).
     */
    List<EstudianteDTO.Response> listarPorCarreraYCiudad(Long idCarrera, String ciudad);

    /**
     * Servicio encargado de actualizar un estudiante.
     * @param lu Identificador único del estudiante a actualizar.
     * @param in DTO con los datos a actualizar.
     * @return Retorna al estudiante actualizado (DTO de respuesta).
     */
    EstudianteDTO.Response actualizar(Long lu, EstudianteDTO.Create in);

    /**
     * Servicio encargado de eliminar un estudiante correspondiente a la LU ingresada por parámetro.
     * @param lu Identificador único del estudiante a eliminar.
     */
    void eliminar(Long lu);
}
