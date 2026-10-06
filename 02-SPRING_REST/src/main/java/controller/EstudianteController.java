package controller;

import dto.EstudianteDTO;
import exception.EstudianteGeneroException;
import model.Genero;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.EstudianteService;

import java.util.List;

@RestController
@RequestMapping("/estudiantes")
public class EstudianteController {
    // no necesita @Autowired porque en Spring moderno, si usás el constructor, no hace falta el @Autowired.
    private final EstudianteService estudianteService;

    // Inyección de dependencias por constructor
    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }


    // c) obtener todos los estudiantes ordenados por algún criterio
    // Por defecto (Ordena por lu de forma ascendente):
    //      GET http://localhost:8080/estudiantes
    // Ordenar por edad de menor a mayor (ascendente):
    //      GET http://localhost:8080/estudiantes?sortBy=edad&direction=asc
    // Ordenar por apellido de la Z a la A (descendente):
    //      GET http://localhost:8080/estudiantes?sortBy=apellido&direction=desc
    @GetMapping("")
    public ResponseEntity<List<EstudianteDTO.Response>> getAll(
            @RequestParam(defaultValue = "lu") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        return ResponseEntity.status(HttpStatus.OK).body(estudianteService.listarEstudiantes(sortBy, direction));
    }

    // e) recuperar todos los estudiantes, en base a su género.
    // GET: http://localhost:8080/estudiantes/genero/MASCULINO
    // (literal /genero antes de /{lu} para no capturar "genero" como LU)
    @GetMapping("/genero/{genero}")
    public ResponseEntity<List<EstudianteDTO.Response>> getByGenero(@PathVariable String genero) {
        Genero g;
        try {
            g = Genero.valueOf(genero.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new EstudianteGeneroException(genero);
        }
        return ResponseEntity.status(HttpStatus.OK).body(estudianteService.listarPorGenero(g));
    }
    // g) recuperar los estudiantes de una determinada carrera, filtrado por ciudad de residencia.
    // GET: http://localhost:8080/estudiantes/carrera/1?ciudad=Tandil
    // (literal /carrera antes de /{lu})
    @GetMapping("/carrera/{idCarrera}")
    public ResponseEntity<List<EstudianteDTO.Response>> getByCarreraAndCiudad(
            @PathVariable Long idCarrera,
            @RequestParam String ciudad) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(estudianteService.listarPorCarreraYCiudad(idCarrera, ciudad));
    }

    // d) recuperar un estudiante, en base a su número de libreta universitaria.
    // GET: http://localhost:8080/estudiantes/1
    @GetMapping("/{lu}")
    public ResponseEntity<EstudianteDTO.Detail> getOne(@PathVariable Long lu) {
        return ResponseEntity.status(HttpStatus.OK).body(estudianteService.getByLu(lu));
    }

    // a) dar de alta un estudiante
    // POST: http://localhost:8080/estudiantes
    @PostMapping("")
    public ResponseEntity<EstudianteDTO.Response> save(@RequestBody EstudianteDTO.Create in) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estudianteService.altaEstudiante(in));
    }

    // actualizar un estudiante
    // PUT: http://localhost:8080/estudiantes/1
    @PutMapping("/{lu}")
    public ResponseEntity<EstudianteDTO.Response> update(@PathVariable Long lu, @RequestBody EstudianteDTO.Create in) {
        return ResponseEntity.status(HttpStatus.OK).body(estudianteService.actualizar(lu, in));
    }

    // eliminar un estudiante
    // DELETE: http://localhost:8080/estudiantes/1
    @DeleteMapping("/{lu}")
    public ResponseEntity<Void> delete(@PathVariable Long lu) {
        estudianteService.eliminar(lu);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
