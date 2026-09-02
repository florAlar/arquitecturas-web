package app.DAO;

import app.Entidades.FacturaProducto;

import java.sql.Connection;
import java.util.List;

public interface FacturaProductoDAO {
    FacturaProducto getById(int idFactura, int idProducto);
    List<FacturaProducto> getByIdFactura(int idFactura);
    List<FacturaProducto> getByIdProducto(int idProducto);
    List<FacturaProducto> getAll();
    boolean create(FacturaProducto facturaProducto); //true si pudo crearlo, false si no pudo
    boolean update(FacturaProducto facturaProducto); //true si pudo actualizarlo, false si no
    boolean delete(int idFactura, int idProducto);   //true si pudo borrarlo, false si no
    boolean deleteAll();                             //true si pudo borrarlo, false si no
} //FacturaProductoDAO

