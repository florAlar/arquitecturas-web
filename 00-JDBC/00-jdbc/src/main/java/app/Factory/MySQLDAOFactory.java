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

public class MySQLDAOFactory extends DAOFactory implements ConnectionManager {

    private static Connection conn;

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/integrador1";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    protected MySQLDAOFactory(String pathFactura, String pathProducto, String pathCliente, String pathFacturaProducto) {

        conn = this.createConnection();

        try{

            DBInitializer.initialize(conn);
            DBInitializer.loadData(this.getClienteDAO(),this.getFacturaDAO(),this.getProductoDAO(),this.getFacturaProductoDAO(),
                    pathFactura,pathProducto,pathCliente,pathFacturaProducto);

        } catch (SQLException e) {
            throw new RuntimeException( "error iniciando base");
        } catch (IOException e) {
            throw new RuntimeException(e);
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
            if (conn != null && !conn.isClosed()) { return conn; }

        } catch (SQLException e) {
            throw new RuntimeException("Error verificando la conexión con MySQL",e);
        }

        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL,USER,PASSWORD);
            return conn;

        } catch (ClassNotFoundException e) {

            throw new RuntimeException("No se encontró el driver de MySQL: " + DRIVER, e);

        } catch (SQLException e) {

            throw new RuntimeException("Error conectando a MySQL: " + URL,e);
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
                throw new RuntimeException( "Error cerrando la conexión con MySQL",e);

            } finally {
                conn = null;
            }
        }
    }
}