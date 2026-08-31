package app.Factory.MYSQLEntityDAO;

import app.DAO.FacturaDAO;
import app.Entidades.Factura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class MYSQLFacturaDAO implements FacturaDAO {
    private final Connection connection;

    public MYSQLFacturaDAO(Connection connection){
        this.connection = connection;
    }

    @Override
    public Factura getFacturaById(Long idFactura){
        String sql = "SELECT idFactura, idCliente FROM Factura WHERE idFactura = ?";
        Factura res_factura= null;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, idFactura);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    res_factura = new Factura(
                            rs.getLong("idFactura"),
                            rs.getLong("idCliente")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("error consultando la factura con id "+idFactura+": "+e.getMessage());
        }
        return res_factura;
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
            System.err.println("error consultando las facturas con cliente "+idCliente+": "+e.getMessage());

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
            System.err.println("error al consultar las facturas: "+e.getMessage());

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
            System.err.println("error al insertar la factura: "+e.getMessage());

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
            System.err.println("error al actualizar la factura: "+e.getMessage());

        }
    }

    @Override
    public void deleteFactura(Long idFactura) {
        String sql = "DELETE FROM Factura WHERE idFactura = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, idFactura);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("error al borrar la factura: "+e.getMessage());

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
            System.err.println("error al insertar la factura: "+e.getMessage());
        }
    }
}


