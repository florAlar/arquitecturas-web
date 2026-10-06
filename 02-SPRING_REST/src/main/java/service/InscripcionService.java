package service;

import dto.InscripcionDTO;

public interface InscripcionService {

    InscripcionDTO.Response inscribirEstudianteEnCarrera(InscripcionDTO.Create in);
    InscripcionDTO.Response getInscripcionByID(Long id);
}
