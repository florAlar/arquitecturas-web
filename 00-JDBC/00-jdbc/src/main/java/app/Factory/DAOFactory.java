package app.Factory;

import app.DAO.ClienteDAO;
import app.DAO.FacturaDAO;
import app.DAO.FacturaProductoDAO;
import app.DAO.ProductoDAO;

import java.util.HashMap;

public abstract class DAOFactory {

    public enum DBType {
        MYSQL, POSTGRES, DERBY
    }

    private static final HashMap<DBType, DAOFactory> instances = new HashMap<>();

    public static DAOFactory getDAOFactory(
            DBType typeDB,
            String pathFactura,
            String pathProducto,
            String pathCliente,
            String pathFacturaProducto) {

        // Singleton por tipo de base: una sola instancia de cada fábrica concreta.
        if (!instances.containsKey(typeDB)) {
            switch (typeDB) {
                case MYSQL:
                    instances.put(
                            typeDB,
                            new MySQLDAOFactory(
                                    pathFactura,
                                    pathProducto,
                                    pathCliente,
                                    pathFacturaProducto));
                    break;
                case POSTGRES:
                    // instances.put(typeDB, new PostgresDAOFactory());
                    break;
                case DERBY:
                    // instances.put(typeDB, new DerbyDAOFactory());
                    break;
                default:
                    throw new IllegalArgumentException("Base de datos no soportada");
            }
        }

        return instances.get(typeDB);
    }

    public abstract ClienteDAO getClienteDAO();

    public abstract FacturaDAO getFacturaDAO();

    public abstract FacturaProductoDAO getFacturaProductoDAO();

    public abstract ProductoDAO getProductoDAO();

    public abstract void closeConnection();
}
