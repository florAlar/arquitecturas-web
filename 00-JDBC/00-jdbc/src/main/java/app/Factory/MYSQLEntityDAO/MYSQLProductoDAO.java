package app.Factory.MYSQLEntityDAO;

import app.DAO.ProductoDAO;
import app.DTO.ProductoDTO;
import app.Entidades.Producto;

import java.sql.Connection;

public class MYSQLProductoDAO implements ProductoDAO {

    Connection conn;

    public MYSQLProductoDAO(Connection conn) {
        this.conn = conn;

    }

    /*
    public void insert(Int idProducto, String nombre, Float valor) throws SQLException {
    String sql =
    "INSERT INTO Producto (idProducto, nombre, valor) VALUES (?, ?, ?)";

    try (PreparedStatement statement = connection.prepareStatement(sql)) {

    statement.setInt(1, idProducto);
    statement.setString(2, nombre);
    statement.setFloat(3, valor);

    statement.executeUpdate();
    }
    */

    @Override
    public void insertProducto(Producto producto) {
        throw new UnsupportedOperationException("insertProducto no implementado");
    }

    @Override
    public ProductoDTO getProdMasRecaudado() {
        return null;
    }

    @Override
    public ProductoDTO findById(int id) {
        return null;
    }
}
