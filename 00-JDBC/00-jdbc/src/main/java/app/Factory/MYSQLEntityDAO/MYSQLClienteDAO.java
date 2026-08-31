package app.Factory.MYSQLEntityDAO;

import app.DAO.ClienteDAO;
import app.Entidades.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MYSQLClienteDAO implements ClienteDAO {

    private final Connection connection;

    public MYSQLClienteDAO(Connection connection) {
        this.connection = connection;
    }

/*
    public void insert(Int idCliente,String nombre, String email) throws SQLException {

        String sql = "INSERT INTO Cliente (idCliente, Nombre, Email) " +
                "VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cliente.getIdCliente());
            statement.setString(2, cliente.getNombre());
            statement.setString(3, cliente.getEmail());

            statement.executeUpdate();
        }
    }

    //agregar metodos faltantes para conseguir los datos */
}
