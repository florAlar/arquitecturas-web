package controller;

import dto.InscripcionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.InscripcionService;

@RestController
@RequestMapping("/inscripciones")
public class InscripcionController {

    private final InscripcionService inscripcionService;

    public InscripcionController(InscripcionService inscripcionService) {
        this.inscripcionService = inscripcionService;
    }

    @PostMapping("/inscribir")
    public ResponseEntity<InscripcionDTO.Response> inscribir(@RequestBody InscripcionDTO.Create in) {
        InscripcionDTO.Response body = inscripcionService.inscribirEstudianteEnCarrera(in);
        return ResponseEntity.ok(body);
    }
}
