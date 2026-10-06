package service;

import dto.CarreraDTO;
import dto.CarreraDTOCantidad;
import dto.ReporteCarreraDTO;

import java.util.List;

public interface CarreraService {

    CarreraDTO.Response altaCarrera(CarreraDTO.Create in);
    CarreraDTO.Response getCarreraByID(Long id);
    CarreraDTO.Response actualizar(Long id, CarreraDTO.Create in);

    void eliminar(Long id);

    List<CarreraDTOCantidad> getCarrerasConInscriptosOrdenadas();

    List<ReporteCarreraDTO> generarReporteCarreras();

}
