package app.Factory.MYSQLEntityDAO;

import app.DAO.FacturaProductoDAO;

import java.sql.Connection;


public class MYSQLFacturaProductoDAO implements FacturaProductoDAO {

    private Connection connection;

    public MYSQLFacturaProductoDAO(Connection conn) {
        this.connection = conn;
    }


    /*
    public void insert(Int idFactura, Int IdProducto , Int cantidad) throws SQLException {String sql =
    "INSERT INTO Factura_Producto " +
    "(idFactura, idProducto, cantidad) VALUES (?, ?, ?)";

    try (PreparedStatement statement = connection.prepareStatement(sql)) {

    statement.setInt(1, idFactura);
    statement.setInt(2,idProducto);
    statement.setInt(3, cantidad);

    statement.executeUpdate();
    } */

}
