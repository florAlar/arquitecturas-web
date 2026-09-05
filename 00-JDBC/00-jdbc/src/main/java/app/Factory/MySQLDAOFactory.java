package app.Factory;

import app.DAO.ClienteDAO;
import app.DAO.FacturaDAO;
import app.DAO.FacturaProductoDAO;
import app.DAO.ProductoDAO;
import app.Data.DBInitializer;
import app.Factory.MYSQLEntityDAO.MYSQLClienteDAO;
import app.Factory.MYSQLEntityDAO.MYSQLFacturaDAO;
import app.Factory.MYSQLEntityDAO.MYSQLFacturaProductoDAO;
import app.Factory.MYSQLEntityDAO.MYSQLProductoDAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLDAOFactory extends DAOFactory implements ConnectionManager {

    private static Connection conn;

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL_SERVER = "jdbc:mysql://localhost:3306/";
    private static final String URL = "jdbc:mysql://localhost:3306/integrador1";
    private static final String DB_NAME = "integrador1";
    private static final String USER = "root";
    // Password correcta del entorno local: no modificar.
    private static final String PASSWORD = "";

    protected MySQLDAOFactory(
            String pathFactura,
            String pathProducto,
            String pathCliente,
            String pathFacturaProducto) {

        conn = this.createConnection();

        try {
            // 1) Esquema limpio (DROP + CREATE). Anula schemas viejos de pruebas.
            DBInitializer.initialize(conn);

            ClienteDAO clienteDAO = this.getClienteDAO();
            FacturaDAO facturaDAO = this.getFacturaDAO();
            ProductoDAO productoDAO = this.getProductoDAO();
            FacturaProductoDAO facturaProductoDAO = this.getFacturaProductoDAO();

            // 2) Carga CSV (bootstrap de datos para los reportes 3 y 4).
            DBInitializer.loadData(
                    clienteDAO,
                    facturaDAO,
                    productoDAO,
                    facturaProductoDAO,
                    pathFactura,
                    pathProducto,
                    pathCliente,
                    pathFacturaProducto);
        } catch (SQLException e) {
            throw new RuntimeException("Error iniciando base de datos", e);
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo archivos CSV", e);
        }
    }

    @Override
    public ClienteDAO getClienteDAO() {
        return new MYSQLClienteDAO(createConnection());
    }

    @Override
    public FacturaDAO getFacturaDAO() {
        return new MYSQLFacturaDAO(createConnection());
    }

    @Override
    public FacturaProductoDAO getFacturaProductoDAO() {
        return new MYSQLFacturaProductoDAO(createConnection());
    }

    @Override
    public ProductoDAO getProductoDAO() {
        return new MYSQLProductoDAO(createConnection());
    }

    @Override
    public Connection createConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                return conn;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error verificando la conexión con MySQL", e);
        }

        try {
            Class.forName(DRIVER);
            ensureDatabaseExists();
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            return conn;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No se encontró el driver de MySQL: " + DRIVER, e);
        } catch (SQLException e) {
            throw new RuntimeException("Error conectando a MySQL: " + URL, e);
        }
    }

    private void ensureDatabaseExists() throws SQLException {
        try (Connection serverConn = DriverManager.getConnection(URL_SERVER, USER, PASSWORD);
             Statement st = serverConn.createStatement()) {
            st.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
        }
    }

    @Override
    public void closeConnection() {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error cerrando la conexión con MySQL", e);
            } finally {
                conn = null;
            }
        }
    }
}
