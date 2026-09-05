package app.Factory.MYSQLEntityDAO;

import app.DAO.FacturaDAO;
import app.Entidades.Factura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MYSQLFacturaDAO implements FacturaDAO {
    private final Connection connection;

    public MYSQLFacturaDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Factura getFacturaById(Long idFactura) {
        String sql = "SELECT idFactura, idCliente FROM Factura WHERE idFactura = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, idFactura);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Factura(
                            rs.getLong("idFactura"),
                            rs.getLong("idCliente")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error consultando factura id=" + idFactura, e);
        }
        return null;
    }

    @Override
    public List<Factura> getFacturaByClienteId(Long idCliente) {
        String sql = "SELECT idFactura, idCliente FROM Factura WHERE idCliente = ?";
        List<Factura> res_facturas = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    res_facturas.add(new Factura(
                            rs.getLong("idFactura"),
                            rs.getLong("idCliente")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error consultando facturas del cliente id=" + idCliente, e);
        }
        return res_facturas;
    }

    @Override
    public List<Factura> getAllFacturas() {
        String sql = "SELECT idFactura, idCliente FROM Factura";
        List<Factura> res_facturas = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                res_facturas.add(new Factura(
                        rs.getLong("idFactura"),
                        rs.getLong("idCliente")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando facturas", e);
        }
        return res_facturas;
    }

    @Override
    public void insertFactura(Factura factura) {
        String sql = "INSERT INTO Factura (idFactura, idCliente) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, factura.getIdFactura());
            ps.setLong(2, factura.getIdCliente());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error insertando factura id=" + factura.getIdFactura(), e);
        }
    }

    @Override
    public void updateFactura(Factura factura) {
        String sql = "UPDATE Factura SET idCliente = ? WHERE idFactura = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, factura.getIdCliente());
            ps.setLong(2, factura.getIdFactura());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando factura id=" + factura.getIdFactura(), e);
        }
    }

    @Override
    public void deleteFactura(Long idFactura) {
        String sql = "DELETE FROM Factura WHERE idFactura = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, idFactura);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando factura id=" + idFactura, e);
        }
    }

    @Override
    public void createFactura(Long idFactura, Long idCliente) {
        String sql = "INSERT INTO Factura (idFactura, idCliente) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, idFactura);
            ps.setLong(2, idCliente);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error insertando factura id=" + idFactura + " cliente=" + idCliente, e);
        }
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM Factura";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando todas las facturas", e);
        }
    }
}
