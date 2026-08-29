package app.Factory.MYSQLEntityDAO;

import app.DAO.FacturaDAO;

import java.sql.Connection;

public class MYSQLFacturaDAO implements FacturaDAO {
    Connection connection;

    public MYSQLFacturaDAO(Connection conn) {
        this.connection = conn;
    }

  /*public void insert(Int IdFactura, Int idCliente) throws SQLException {
   String sql =
            "INSERT INTO Factura (idFactura, idCliente) VALUES (?, ?)";

    try (PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setInt(1, idFactura);
        statement.setInt(2,idCliente);

        statement.executeUpdate();
    } */

}
