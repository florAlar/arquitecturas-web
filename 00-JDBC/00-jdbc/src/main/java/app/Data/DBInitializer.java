package app.Data;

import app.DAO.ClienteDAO;
import app.DAO.FacturaDAO;
import app.DAO.FacturaProductoDAO;
import app.DAO.ProductoDAO;
import app.Factory.DAOFactory;
import app.Entidades.Cliente;
import app.Entidades.FacturaProducto;
import app.Entidades.Producto;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBInitializer {

    public static void initialize(Connection connection)
            throws SQLException {

        createClienteTable(connection);
        createFacturaTable(connection);
        createProductoTable(connection);
        createFacturaProductoTable(connection);
    }

    private static void createClienteTable(Connection connection)
            throws SQLException {

        String sql =
                "CREATE TABLE IF NOT EXISTS Cliente (" +
                        "idCliente INT NOT NULL, " +
                        "Nombre VARCHAR(500), " +
                        "Email VARCHAR(150), " +
                        "PRIMARY KEY (idCliente)" +
                        ")";

        executeCreateTable(connection, sql);
    }

    private static void createFacturaTable(Connection connection)
            throws SQLException {

        String sql =
                "CREATE TABLE IF NOT EXISTS Factura (" +
                        "idFactura INT NOT NULL, " +
                        "idCliente INT NOT NULL, " +
                        "PRIMARY KEY (idFactura), " +
                        "FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente)" +
                        ")";

        executeCreateTable(connection, sql);
    }

    private static void createProductoTable(Connection connection)
            throws SQLException {

        String sql =
                "CREATE TABLE IF NOT EXISTS Producto (" +
                        "idProducto INT NOT NULL, " +
                        "nombre VARCHAR(45), " +
                        "valor FLOAT NOT NULL, " +
                        "PRIMARY KEY (idProducto)" +
                        ")";

        executeCreateTable(connection, sql);
    }

    private static void createFacturaProductoTable(Connection connection)
            throws SQLException {

        String sql =
                "CREATE TABLE IF NOT EXISTS Factura_Producto (" +
                        "idFactura INT NOT NULL, " +
                        "idProducto INT NOT NULL, " +
                        "cantidad INT, " +
                        "PRIMARY KEY (idFactura, idProducto), " +
                        "FOREIGN KEY (idFactura) REFERENCES Factura(idFactura), " +
                        "FOREIGN KEY (idProducto) REFERENCES Producto(idProducto)" +
                        ")";

        executeCreateTable(connection, sql);
    }

    private static void executeCreateTable(
            Connection connection,
            String sql) throws SQLException {

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    public static void loadData(
            ClienteDAO clienteDAO,
            FacturaDAO facturaDAO,
            ProductoDAO productoDAO,
            FacturaProductoDAO facturaProductoDAO,
            String pathFactura,
            String pathProducto,
            String pathCliente,
            String pathFacturaProducto)
            throws SQLException, IOException {

        // El orden es importante por las claves foráneas.
        loadClientes(clienteDAO, pathCliente);
        loadProductos(productoDAO, pathProducto);
        loadFacturas(facturaDAO, pathFactura);
        loadFacturaProductos(
                facturaProductoDAO,
                pathFacturaProducto
        );
    }

    private static void loadClientes(
            ClienteDAO dao,
            String path)
            throws IOException, SQLException {

        try (Reader reader = new FileReader(path)) {

            Iterable<CSVRecord> records =
                    CSVFormat.DEFAULT
                            .builder()
                            .setHeader()
                            .setSkipHeaderRecord(true)
                            .build()
                            .parse(reader);

            for (CSVRecord record : records) {

                int idCliente =
                        Integer.parseInt(record.get("idCliente"));

                String nombre =
                        record.get("Nombre");

                String email =
                        record.get("Email");

                dao.create(
                        new Cliente(
                                (long) idCliente,
                                nombre,
                                email
                        )
                );
            }
        }
    }

    private static void loadProductos(
            ProductoDAO dao,
            String path)
            throws IOException, SQLException {

        try (Reader reader = new FileReader(path)) {

            Iterable<CSVRecord> records =
                    CSVFormat.DEFAULT
                            .builder()
                            .setHeader()
                            .setSkipHeaderRecord(true)
                            .build()
                            .parse(reader);

            for (CSVRecord record : records) {

                int idProducto =
                        Integer.parseInt(record.get("idProducto"));

                String nombre =
                        record.get("nombre");

                float valor =
                        Float.parseFloat(record.get("valor"));

                dao.insertProducto(
                        new Producto(
                                idProducto,
                                nombre,
                                valor,
                                0
                        )
                );
            }
        }
    }

    private static void loadFacturas(
            FacturaDAO dao,
            String path)
            throws IOException, SQLException {

        try (Reader reader = new FileReader(path)) {

            Iterable<CSVRecord> records =
                    CSVFormat.DEFAULT
                            .builder()
                            .setHeader()
                            .setSkipHeaderRecord(true)
                            .build()
                            .parse(reader);

            for (CSVRecord record : records) {

                int idFactura =
                        Integer.parseInt(record.get("idFactura"));

                int idCliente =
                        Integer.parseInt(record.get("idCliente"));

                dao.createFactura(
                        (long) idFactura,
                        (long) idCliente
                );
            }
        }
    }

    private static void loadFacturaProductos(
            FacturaProductoDAO dao,
            String path)
            throws IOException, SQLException {

        try (Reader reader = new FileReader(path)) {

            Iterable<CSVRecord> records =
                    CSVFormat.DEFAULT
                            .builder()
                            .setHeader()
                            .setSkipHeaderRecord(true)
                            .build()
                            .parse(reader);

            for (CSVRecord record : records) {

                int idFactura =
                        Integer.parseInt(record.get("idFactura"));

                int idProducto =
                        Integer.parseInt(record.get("idProducto"));

                int cantidad =
                        Integer.parseInt(record.get("cantidad"));

                boolean created = dao.create(new FacturaProducto(idFactura, idProducto, cantidad));
                if (!created) {
                    throw new SQLException("No se pudo insertar FacturaProducto idFactura=" + idFactura + ", idProducto=" + idProducto);
                }
            }
        }
    }
}

