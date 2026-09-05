package app.Data;

import app.DAO.ClienteDAO;
import app.DAO.FacturaDAO;
import app.DAO.FacturaProductoDAO;
import app.DAO.ProductoDAO;
import app.Entidades.Cliente;
import app.Entidades.FacturaProducto;
import app.Entidades.Producto;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DBInitializer {

    public static void initialize(Connection connection) throws SQLException {
        dropTables(connection);
        createClienteTable(connection);
        createProductoTable(connection);
        createFacturaTable(connection);
        createFacturaProductoTable(connection);
    }

    private static void dropTables(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("SET FOREIGN_KEY_CHECKS = 0");
            statement.executeUpdate("DROP TABLE IF EXISTS Factura_Producto");
            statement.executeUpdate("DROP TABLE IF EXISTS FacturaProducto"); // legacy
            statement.executeUpdate("DROP TABLE IF EXISTS Factura");
            statement.executeUpdate("DROP TABLE IF EXISTS Producto");
            statement.executeUpdate("DROP TABLE IF EXISTS Cliente");
            statement.execute("SET FOREIGN_KEY_CHECKS = 1");
        }
    }

    private static void createClienteTable(Connection connection) throws SQLException {
        String sql =
                "CREATE TABLE Cliente (" +
                        "idCliente INT NOT NULL, " +
                        "Nombre VARCHAR(500), " +
                        "Email VARCHAR(150), " +
                        "PRIMARY KEY (idCliente)" +
                        ")";
        executeUpdate(connection, sql);
    }

    private static void createProductoTable(Connection connection) throws SQLException {
        String sql =
                "CREATE TABLE Producto (" +
                        "idProducto INT NOT NULL, " +
                        "nombre VARCHAR(45), " +
                        "valor FLOAT NOT NULL, " +
                        "PRIMARY KEY (idProducto)" +
                        ")";
        executeUpdate(connection, sql);
    }

    private static void createFacturaTable(Connection connection) throws SQLException {
        String sql =
                "CREATE TABLE Factura (" +
                        "idFactura INT NOT NULL, " +
                        "idCliente INT NOT NULL, " +
                        "PRIMARY KEY (idFactura), " +
                        "FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente)" +
                        ")";
        executeUpdate(connection, sql);
    }

    private static void createFacturaProductoTable(Connection connection) throws SQLException {
        String sql =
                "CREATE TABLE Factura_Producto (" +
                        "idFactura INT NOT NULL, " +
                        "idProducto INT NOT NULL, " +
                        "cantidad INT, " +
                        "PRIMARY KEY (idFactura, idProducto), " +
                        "FOREIGN KEY (idFactura) REFERENCES Factura(idFactura), " +
                        "FOREIGN KEY (idProducto) REFERENCES Producto(idProducto)" +
                        ")";
        executeUpdate(connection, sql);
    }

    private static void executeUpdate(Connection connection, String sql) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }


    public static void clearData( ClienteDAO clienteDAO,FacturaDAO facturaDAO, ProductoDAO productoDAO, FacturaProductoDAO facturaProductoDAO) {

        facturaProductoDAO.deleteAll();
        facturaDAO.deleteAll();
        productoDAO.deleteAll();
        clienteDAO.deleteAll();
    }


     //Carga CSV a la base (punto 2).

    public static void loadData( ClienteDAO clienteDAO, FacturaDAO facturaDAO, ProductoDAO productoDAO, FacturaProductoDAO facturaProductoDAO,
            String pathFactura, String pathProducto, String pathCliente, String pathFacturaProducto) throws SQLException, IOException {
        clearData(clienteDAO, facturaDAO, productoDAO, facturaProductoDAO);
        loadClientes(clienteDAO, pathCliente);
        loadProductos(productoDAO, pathProducto);
        loadFacturas(facturaDAO, pathFactura);
        loadFacturaProductos(facturaProductoDAO, pathFacturaProducto);
    }


     //Acepta formatos: - "CSV/cliente.csv"
    //                  - ruta de archivo: "src/main/resources/CSV/cliente.csv"

    private static Reader openCsv(String location) throws IOException {
        String normalized = location.replace('\\', '/');

        InputStream in = openClasspath(normalized);

        if (in != null) {
            return new InputStreamReader(in, StandardCharsets.UTF_8);
        }

        String fileName = Paths.get(normalized).getFileName().toString();
        in = openClasspath("CSV/" + fileName);

        if (in != null) {
            return new InputStreamReader(in, StandardCharsets.UTF_8);
        }

        Path path = Paths.get(location);

        if (!Files.exists(path)) {
            Path alternativo = Paths.get(System.getProperty("user.dir"), location);
            if (Files.exists(alternativo)) {
                path = alternativo;
            }
        }
        if (!Files.exists(path)) {
            throw new IOException(
                    "No se encontró el CSV '" + location + "'. Probé (CSV/" + fileName +
                            ") y filesystem. user.dir=" + System.getProperty("user.dir"));
        }
        return new InputStreamReader(new FileInputStream(path.toFile()), StandardCharsets.UTF_8);
    }

    private static InputStream openClasspath(String resource) {
        String name = resource.startsWith("/") ? resource.substring(1) : resource;
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream in = cl != null ? cl.getResourceAsStream(name) : null;
        if (in == null) {
            in = DBInitializer.class.getClassLoader().getResourceAsStream(name);
        }
        if (in == null) {
            in = DBInitializer.class.getResourceAsStream("/" + name);
        }
        return in;
    }

    private static List<CSVRecord> parseCsv(Reader reader) throws IOException {
        try (CSVParser parser = CSVFormat.DEFAULT
                .builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setIgnoreEmptyLines(true)
                .setTrim(true)
                .build()
                .parse(reader)) {
            return parser.getRecords();
        }
    }

    private static void loadClientes(ClienteDAO dao, String path) throws IOException {
        try (Reader reader = openCsv(path)) {
            for (CSVRecord record : parseCsv(reader)) {
                int idCliente = Integer.parseInt(record.get("idCliente"));
                String nombre = record.get("nombre");
                String email = record.get("email");
                dao.create(new Cliente((long) idCliente, nombre, email));
            }
        }
    }

    private static void loadProductos(ProductoDAO dao, String path) throws IOException {
        try (Reader reader = openCsv(path)) {
            for (CSVRecord record : parseCsv(reader)) {
                int idProducto = Integer.parseInt(record.get("idProducto"));
                String nombre = record.get("nombre");
                float valor = Float.parseFloat(record.get("valor"));
                dao.insertProducto(new Producto(idProducto, nombre, valor));
            }
        }
    }

    private static void loadFacturas(FacturaDAO dao, String path) throws IOException {
        try (Reader reader = openCsv(path)) {
            for (CSVRecord record : parseCsv(reader)) {
                int idFactura = Integer.parseInt(record.get("idFactura"));
                int idCliente = Integer.parseInt(record.get("idCliente"));
                dao.createFactura((long) idFactura, (long) idCliente);
            }
        }
    }

    private static void loadFacturaProductos(FacturaProductoDAO dao, String path)
            throws IOException, SQLException {
        try (Reader reader = openCsv(path)) {
            for (CSVRecord record : parseCsv(reader)) {
                int idFactura = Integer.parseInt(record.get("idFactura"));
                int idProducto = Integer.parseInt(record.get("idProducto"));
                int cantidad = Integer.parseInt(record.get("cantidad"));

                boolean created = dao.create(new FacturaProducto(idFactura, idProducto, cantidad));
                if (!created) {
                    throw new SQLException(
                            "No se pudo insertar Factura_Producto idFactura=" +
                                    idFactura + ", idProducto=" + idProducto);
                }
            }
        }
    }
}
