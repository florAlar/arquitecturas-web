package app.Factory;


import app.DAO.ClienteDAO;
import app.DAO.FacturaDAO;
import app.DAO.FacturaProductoDAO;
import app.DAO.ProductoDAO;
import app.Factory.MYSQLEntityDAO.MYSQLFacturaDAO;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class DAOFactory {

    public enum DBType {
        MYSQL, POSTGRES, DERBY
    }

    ArrayList paths = new ArrayList();

    private static final HashMap<DBType, DAOFactory> instances = new HashMap<>();

    public static DAOFactory getDAOFactory(DBType typeDB, String pathFactura, String pathProducto,
                                           String pathCliente, String pathFacturaProducto) {

        // El singleton esta acá,
        // se soportan todas las fabricas a la vez pero solo se puede generar una unica instancia por cada fabrica especifica.
        //si la instancia de la base especifica no existe se crea, de lo contrario se devuelve la que existe.

        if (!instances.containsKey(typeDB)) {

            switch (typeDB) {

                case MYSQL:
                    instances.put(typeDB, new MySQLDAOFactory( pathFactura, pathProducto, pathCliente,
                                                                pathFacturaProducto));
                    break;

                case POSTGRES:
                    //instances.put(typeDB, new PostgresDAOFactory());
                    break;

                case DERBY:
                    //instances.put(typeDB, new DerbyDAOFactory());
                    break;

                default:
                    throw new IllegalArgumentException(
                            "Base de datos no soportada"
                    );
            }
        }

        return instances.get(typeDB);
    }
    public abstract ClienteDAO getClienteDAO();

    public abstract FacturaDAO getFacturaDAO();

    public abstract FacturaProductoDAO getFacturaProductoDAO();

    public abstract ProductoDAO getProductoDAO();
}