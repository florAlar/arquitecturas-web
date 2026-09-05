package app.Factory.MYSQLEntityDAO;

import app.DAO.ProductoDAO;
import app.DTO.ProductoDTO;
import app.Entidades.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MYSQLProductoDAO implements ProductoDAO {

    private final Connection conn;

    public MYSQLProductoDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insertProducto(Producto producto) {
        String sql = "INSERT INTO Producto (idProducto, nombre, valor) VALUES (?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, producto.getIdProducto());
            statement.setString(2, producto.getNombre());
            statement.setFloat(3, producto.getValor());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error insertando producto id=" + producto.getIdProducto(), e);
        }
    }

    @Override
    public ProductoDTO getProdMasRecaudado() {
        String sql =
                "SELECT p.idProducto, p.nombre, SUM(fp.cantidad * p.valor) AS recaudacion " +
                "FROM Factura_Producto fp " +
                "JOIN Producto p ON fp.idProducto = p.idProducto " +
                "GROUP BY p.idProducto, p.nombre " +
                "ORDER BY recaudacion DESC LIMIT 1";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return new ProductoDTO(
                        rs.getInt("idProducto"),
                        rs.getString("nombre"),
                        rs.getFloat("recaudacion")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error consultando producto mas recaudado", e);
        }
        return null;
    }

    @Override
    public ProductoDTO findById(int id) {
        String sql = "SELECT idProducto, nombre, valor FROM Producto WHERE idProducto = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Para findById usamos recaudacion=valor unitario solo como portador;
                    // el reporte de recaudacion real es getProdMasRecaudado.
                    return new ProductoDTO(
                            rs.getInt("idProducto"),
                            rs.getString("nombre"),
                            rs.getFloat("valor")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando producto id=" + id, e);
        }
        return null;
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM Producto";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando todos los productos", e);
        }
    }
}
