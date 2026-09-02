package app.DAO;

import java.sql.Connection;

public interface FacturaProductoDAO {
    FacturaProducto getById(int idFactura, int idProducto);
    List<FacturaProducto> getByIdFactura(int idFactura);
    List<FacturaProducto> getByIdProducto(int idProducto);
    List<FacturaProducto> getAll();
    void insert(FacturaProducto facturaProducto);
    void update(FacturaProducto facturaProducto);
    void delete(int idFactura, int idProducto);
}
