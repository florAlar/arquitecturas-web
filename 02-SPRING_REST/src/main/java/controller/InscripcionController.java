package controller;

import dto.InscripcionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.InscripcionService;

@RestController
@RequestMapping("/inscripciones")
public class InscripcionController {

    private final InscripcionService inscripcionService;

    public InscripcionController(InscripcionService inscripcionService) {
        this.inscripcionService = inscripcionService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<InscripcionDTO.Response> getInscripcionByID(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(inscripcionService.getInscripcionByID(id));
    }

    @PostMapping("")
    public ResponseEntity<InscripcionDTO.Response> inscribir(@RequestBody InscripcionDTO.Create in) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inscripcionService.inscribirEstudianteEnCarrera(in));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> Delete(@PathVariable Long id){
        inscripcionService.Delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
