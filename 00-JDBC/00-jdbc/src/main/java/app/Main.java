package app;

import app.DAO.ClienteDAO;
import app.DAO.ProductoDAO;
import app.DTO.ClienteDTO;
import app.DTO.ProductoDTO;
import app.Factory.DAOFactory;

public class Main {

    public static void main(String[] args) {
        // Recursos del classpath (src/main/resources/CSV → target/classes/CSV).
        // No dependen del working directory.
        String pathCliente = "CSV/cliente.csv";
        String pathProducto = "CSV/producto.csv";
        String pathFactura = "CSV/factura.csv";
        String pathFacturaProducto = "CSV/factura-producto.csv";

        DAOFactory factory = DAOFactory.getDAOFactory(
                DAOFactory.DBType.MYSQL,
                pathFactura,
                pathProducto,
                pathCliente,
                pathFacturaProducto
        );

        try {
            ProductoDAO productoDAO = factory.getProductoDAO();
            ClienteDAO clienteDAO = factory.getClienteDAO();

            // 3) Producto que más recaudó (cantidad * valor)
            ProductoDTO topProducto = productoDAO.getProdMasRecaudado();
            System.out.println("Producto que mas recaudo:");
            System.out.println(topProducto);

            System.out.println("****************************************");

            // 4) Clientes ordenados por total facturado (desc)
            System.out.println("Clientes ordenados por facturacion:");
            for (ClienteDTO cliente : clienteDAO.getClientesPorFacturacion()) {
                System.out.println(cliente);
            }
        } finally {
            factory.closeConnection();
        }
    }
}
