package app.Factory.MYSQLEntityDAO;

import app.DAO.ProductoDAO;
import app.DTO.ProductoDTO;
import app.Entidades.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MYSQLProductoDAO implements ProductoDAO {

    Connection conn;

    public MYSQLProductoDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insertProducto(Producto producto) {
        String sql = "INSERT INTO Producto (idProducto, nombre, valor) VALUES (?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, producto.getIdProducto());
            statement.setString(2, producto.getNombre());
            statement.setFloat(3, producto.getPrecio());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al realizar la operacion!");
            e.printStackTrace();
        }
    }

    @Override
    public ProductoDTO getProdMasRecaudado() {
        String sql = "SELECT p.idProducto, p.nombre, SUM(fp.cantidad * p.valor) AS recaudacion " +
                "FROM Factura_Producto fp " +
                "JOIN Producto p ON fp.idProducto = p.idProducto " +
                "GROUP BY p.idProducto, p.nombre " +
                "ORDER BY recaudacion DESC LIMIT 1";
        ProductoDTO p = null;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = new ProductoDTO(
                            rs.getInt("idProducto"),
                            rs.getString("nombre"),
                            rs.getFloat("recaudacion")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar!");
            e.printStackTrace();
        }

        return p;
    }

    @Override
    public ProductoDTO findById(int id) {
        String sql = "SELECT idProducto, nombre, valor FROM Producto WHERE idProducto = ?";
        ProductoDTO p = null;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = new ProductoDTO(
                            rs.getInt("idProducto"),
                            rs.getString("nombre"),
                            rs.getFloat("valor")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar!");
            e.printStackTrace();
        }

        return p;
    }
}