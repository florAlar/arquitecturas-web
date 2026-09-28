package repository;

import dto.CarreraDTOCantidad;
import dto.ReporteCarreraDTO;
import entity.Carrera;

import java.util.List;

public interface CarreraRepository {

    void create(Carrera carrera);

    List<CarreraDTOCantidad> getCarrerasConInscriptosOrdenadas();

    Carrera getCarreraByName(String name);

    List<ReporteCarreraDTO> generarReporteCarreras();
}
