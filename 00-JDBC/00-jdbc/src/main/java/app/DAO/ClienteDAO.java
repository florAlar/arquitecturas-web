package app.DAO;

import app.DTO.ClienteDTO;
import app.Entidades.Cliente;

import java.util.List;

public interface ClienteDAO {
    Cliente findByIdCliente(Long idCliente);
    List<Cliente> findAllClientes();
    void create(Cliente c);
    void update(Cliente c);
    void delete(Long idCliente);
    void deleteAll();
    List<ClienteDTO> getClientesPorFacturacion();
}
