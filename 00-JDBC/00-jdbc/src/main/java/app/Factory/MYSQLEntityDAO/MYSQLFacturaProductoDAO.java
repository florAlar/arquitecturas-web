package app.Factory.MYSQLEntityDAO;

import app.DAO.FacturaProductoDAO;
import app.Entidades.FacturaProducto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MYSQLFacturaProductoDAO implements FacturaProductoDAO {

    private final Connection conexion;

    public MYSQLFacturaProductoDAO(Connection connection) {
        this.conexion = connection;
    }

    private FacturaProducto map(ResultSet resultado) throws SQLException {
        return new FacturaProducto(
                resultado.getInt("idFactura"),
                resultado.getInt("idProducto"),
                resultado.getInt("cantidad")
        );
    }

    @Override
    public FacturaProducto getById(int idFactura, int idProducto) {
        final String sql =
                "SELECT idFactura, idProducto, cantidad " +
                "FROM Factura_Producto " +
                "WHERE idFactura = ? AND idProducto = ?";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idFactura);
            sentencia.setInt(2, idProducto);
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return map(resultado);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error buscando Factura_Producto idFactura=" + idFactura +
                            " idProducto=" + idProducto, e);
        }
    }

    @Override
    public List<FacturaProducto> getByIdFactura(int idFactura) {
        List<FacturaProducto> facturasProductos = new ArrayList<>();
        final String sql =
                "SELECT idFactura, idProducto, cantidad " +
                "FROM Factura_Producto WHERE idFactura = ?";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idFactura);
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    facturasProductos.add(map(resultado));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error buscando Factura_Producto por idFactura=" + idFactura, e);
        }
        return facturasProductos;
    }

    @Override
    public List<FacturaProducto> getByIdProducto(int idProducto) {
        List<FacturaProducto> facturasProductos = new ArrayList<>();
        final String sql =
                "SELECT idFactura, idProducto, cantidad " +
                "FROM Factura_Producto WHERE idProducto = ? ORDER BY idFactura";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idProducto);
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    facturasProductos.add(map(resultado));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error buscando Factura_Producto por idProducto=" + idProducto, e);
        }
        return facturasProductos;
    }

    @Override
    public List<FacturaProducto> getAll() {
        List<FacturaProducto> facturasProductos = new ArrayList<>();
        final String sql =
                "SELECT idFactura, idProducto, cantidad " +
                "FROM Factura_Producto ORDER BY idFactura, idProducto";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                facturasProductos.add(map(resultado));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando Factura_Producto", e);
        }
        return facturasProductos;
    }

    private void mapInto(FacturaProducto facturaProducto, PreparedStatement sentencia)
            throws SQLException {
        sentencia.setInt(1, facturaProducto.getIdFactura());
        sentencia.setInt(2, facturaProducto.getIdProducto());
        sentencia.setInt(3, facturaProducto.getCantidad());
    }

    @Override
    public boolean create(FacturaProducto facturaProducto) {
        final String sql =
                "INSERT INTO Factura_Producto (idFactura, idProducto, cantidad) VALUES (?, ?, ?)";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            mapInto(facturaProducto, sentencia);
            return sentencia.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error insertando Factura_Producto idFactura=" +
                            facturaProducto.getIdFactura() +
                            " idProducto=" + facturaProducto.getIdProducto(), e);
        }
    }

    @Override
    public boolean update(FacturaProducto facturaProducto) {
        final String sql =
                "UPDATE Factura_Producto SET cantidad = ? " +
                "WHERE idFactura = ? AND idProducto = ?";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, facturaProducto.getCantidad());
            sentencia.setInt(2, facturaProducto.getIdFactura());
            sentencia.setInt(3, facturaProducto.getIdProducto());
            return sentencia.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando Factura_Producto", e);
        }
    }

    @Override
    public boolean delete(int idFactura, int idProducto) {
        final String sql =
                "DELETE FROM Factura_Producto WHERE idFactura = ? AND idProducto = ?";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idFactura);
            sentencia.setInt(2, idProducto);
            return sentencia.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando Factura_Producto", e);
        }
    }

    @Override
    public boolean deleteAll() {
        final String sql = "DELETE FROM Factura_Producto";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando todos los Factura_Producto", e);
        }
    }
}
