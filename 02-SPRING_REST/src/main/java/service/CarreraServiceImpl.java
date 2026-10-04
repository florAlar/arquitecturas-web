package service;

import dto.CarreraDTO;
import dto.CarreraDTOCantidad;
import dto.ReporteCarreraDTO;
import exception.BadRequestException;
import exception.CarreraDuplicadaException;
import exception.CarreraNotFoundException;
import mapper.CarreraMapper;
import model.Carrera;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.CarreraRepository;

import java.util.List;

@Service
public class CarreraServiceImpl implements CarreraService {

    private final CarreraRepository carreraRepository;
    private final CarreraMapper carreraMapper;

    // Inyección de dependencias por constructor
    public CarreraServiceImpl(CarreraRepository carreraRepository, CarreraMapper carreraMapper) {
        this.carreraRepository = carreraRepository;
        this.carreraMapper = carreraMapper;
    }

    @Override
    @Transactional
    public CarreraDTO.Response altaCarrera(CarreraDTO.Create in) {
        if (in.nombre() == null || in.nombre().isBlank()) {
            throw new BadRequestException("El nombre de la carrera es obligatorio");
        }
        if (carreraRepository.existsByNombre(in.nombre())) {
            throw new CarreraDuplicadaException("Ya existe una carrera con el nombre: " + in.nombre());
        }

        Carrera entity = carreraMapper.toEntity(in);
        Carrera saved = carreraRepository.save(entity);
        return carreraMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CarreraDTO.Response actualizar(Long id, CarreraDTO.Create in) {
        Carrera existente = carreraRepository.findById(id)
                .orElseThrow(() -> new CarreraNotFoundException(id));

        if (in.nombre() == null || in.nombre().isBlank()) {
            throw new BadRequestException("El nombre de la carrera es obligatorio");
        }

        if (!in.nombre().equalsIgnoreCase(existente.getNombre())
                && carreraRepository.existsByNombre(in.nombre())) {
            throw new CarreraDuplicadaException("Ya existe una carrera con el nombre: " + in.nombre());
        }

        carreraMapper.applyUpdate(existente, in);
        Carrera saved = carreraRepository.save(existente);
        return carreraMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!carreraRepository.existsById(id)) {
            throw new CarreraNotFoundException(id);
        }
        carreraRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarreraDTOCantidad> getCarrerasConInscriptosOrdenadas() {
        return carreraRepository.getCarrerasConInscriptosOrdenadas();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteCarreraDTO> generarReporteCarreras() {
        return carreraRepository.generarReporteCarreras();
    }
}