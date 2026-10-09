package controller;

import dto.CarreraDTO;
import dto.CarreraDTOCantidad;
import dto.ReporteCarreraDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.CarreraService;

import java.util.List;

@RestController
@RequestMapping("/carreras")
public class CarreraController {

    private final CarreraService carreraService;

    public CarreraController(CarreraService carreraService){
        this.carreraService = carreraService;
    }


    //buscar carrera
    @GetMapping("/{id}")
    public ResponseEntity<CarreraDTO.Response> getCarreraByID(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(carreraService.getCarreraByID(id));
    }

    //Alta carrera
    @PostMapping("")
    public ResponseEntity<CarreraDTO.Response> save(@RequestBody CarreraDTO.Create in) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carreraService.altaCarrera(in));
    }

    //Eliminar una carrera
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        carreraService.eliminar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //Actualizar una carrera
    @PutMapping("/{id}")
    public ResponseEntity<CarreraDTO.Response> update(@PathVariable Long id, @RequestBody CarreraDTO.Create in) {
        return ResponseEntity.status(HttpStatus.OK).body(carreraService.actualizar(id, in));
    }


    // f) Obtener las carreras con estudiantes inscriptos y ordenadas por cantidad de inscriptos
    // GET: http://localhost:8080/carreras/inscriptos
    @GetMapping("/inscriptos")
    public ResponseEntity<List<CarreraDTOCantidad>> getCarrerasConInscriptosOrdenadas() {
        return ResponseEntity.status(HttpStatus.OK).body(carreraService.getCarrerasConInscriptosOrdenadas());
    }

    // h) Generar un reporte de las carreras por orden alfabético con inscriptos y egresados por año
    // GET: http://localhost:8080/carreras/reporte
    @GetMapping("/reporte")
    public ResponseEntity<List<ReporteCarreraDTO>> getReporteCarreras() {
        return ResponseEntity.status(HttpStatus.OK).body(carreraService.generarReporteCarreras());
    }

}
