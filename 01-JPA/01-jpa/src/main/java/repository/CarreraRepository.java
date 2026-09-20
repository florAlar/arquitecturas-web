package repository;

public interface CarreraRepository {

    List<CarreraDTOCant> getCarrerasConInscriptosOrdenadas();
    Carrera getCarreraByName(String name);
    List<ReporteCarreraDTO> generarReporteCarreras();
}
