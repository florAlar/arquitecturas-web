package controller;


import model.Estudiante;
import model.Genero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.EstudianteService;

@RestController
@RequestMapping("/estudiantes")
public class EstudianteController  {
    @Autowired
    private EstudianteService estudianteService;

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
    public ResponseEntity<?> getAll(
        @RequestParam(defaultValue = "lu") String sortBy,
        @RequestParam(defaultValue = "asc") String direction) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(estudianteService.findAll(sortBy, direction));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }

    // d) recuperar un estudiante, en base a su número de libreta universitaria.
    // GET: http://localhost:8080/estudiantes/1
    @GetMapping("/{id}")
    public ResponseEntity<?>getOne(@PathVariable Long id){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(estudianteService.findById(id));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. No se encuentra el objeto buscado" +
                    ".\"}");
        }
    }

    // e) recuperar todos los estudiantes, en base a su género.
    // GET: http://localhost:8080/estudiantesgenero/MASCULINO
    @GetMapping("genero/{genero}")
    public ResponseEntity<?> getByGenero(@PathVariable String genero){
        try{
            Genero queGenero = Genero.valueOf(genero);
            return ResponseEntity.status(HttpStatus.OK).body(estudianteService.findByGenero(queGenero));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }

    // a) dar de alta un estudiante
    // POST: http://localhost:8080/estudiantes
    @PostMapping("")
    public ResponseEntity<?> save(@RequestBody Estudiante entity){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(estudianteService.save(entity));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. No se pudo ingresar, revise los campos e intente nuevamente.\"}");
        }
    }

    // actualizar un estudiante
    // PUT: http://localhost:8080/estudiantes/1
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,@RequestBody Estudiante entity){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(estudianteService.update(id,entity));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. No se pudo editar, revise los campos e intente nuevamente.\"}");
        }
    }

    // eliminar un estudiante
    // DELETE: http://localhost:8080/estudiantes/1
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        try{
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(estudianteService.delete(id));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. no se pudo eliminar intente nuevamente.\"}");
        }
    }
}
