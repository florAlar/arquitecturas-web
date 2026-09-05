package app.Factory.MYSQLEntityDAO;

import app.DAO.ClienteDAO;
import app.DTO.ClienteDTO;
import app.Entidades.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MYSQLClienteDAO implements ClienteDAO {

    private final Connection connection;

    public MYSQLClienteDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Cliente findByIdCliente(Long idCliente) {
        String sql = "SELECT idCliente, Nombre, Email FROM Cliente WHERE idCliente = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, idCliente);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                            rs.getLong("idCliente"),
                            rs.getString("Nombre"),
                            rs.getString("Email")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando cliente id=" + idCliente, e);
        }
        return null;
    }

    @Override
    public List<Cliente> findAllClientes() {
        String sql = "SELECT idCliente, Nombre, Email FROM Cliente ORDER BY idCliente";
        List<Cliente> clientes = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                clientes.add(new Cliente(
                        rs.getLong("idCliente"),
                        rs.getString("Nombre"),
                        rs.getString("Email")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando clientes", e);
        }
        return clientes;
    }

    @Override
    public void create(Cliente c) {
        String sql = "INSERT INTO Cliente (idCliente, Nombre, Email) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, c.getIdCliente());
            statement.setString(2, c.getNombre());
            statement.setString(3, c.getEmail());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error insertando cliente id=" + c.getIdCliente(), e);
        }
    }

    @Override
    public void update(Cliente c) {
        String sql = "UPDATE Cliente SET Nombre = ?, Email = ? WHERE idCliente = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, c.getNombre());
            statement.setString(2, c.getEmail());
            statement.setLong(3, c.getIdCliente());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando cliente id=" + c.getIdCliente(), e);
        }
    }

    @Override
    public void delete(Long idCliente) {
        String sql = "DELETE FROM Cliente WHERE idCliente = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, idCliente);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando cliente id=" + idCliente, e);
        }
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM Cliente";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando todos los clientes", e);
        }
    }

    @Override
    public List<ClienteDTO> getClientesPorFacturacion() {
        String sql =
                "SELECT c.Nombre, c.Email, " +
                "COALESCE(SUM(fp.cantidad * p.valor), 0) AS totalFacturado " +
                "FROM Cliente c " +
                "LEFT JOIN Factura f ON c.idCliente = f.idCliente " +
                "LEFT JOIN Factura_Producto fp ON f.idFactura = fp.idFactura " +
                "LEFT JOIN Producto p ON fp.idProducto = p.idProducto " +
                "GROUP BY c.idCliente, c.Nombre, c.Email " +
                "ORDER BY totalFacturado DESC";

        List<ClienteDTO> resultado = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                resultado.add(new ClienteDTO(
                        rs.getString("Nombre"),
                        rs.getString("Email"),
                        rs.getFloat("totalFacturado")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error consultando clientes por facturacion", e);
        }
        return resultado;
    }
}
