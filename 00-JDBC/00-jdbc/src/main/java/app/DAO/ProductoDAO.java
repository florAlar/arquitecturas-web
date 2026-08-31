package app.DAO;

import java.sql.Connection;
import app.entidades.Producto;


public interface ProductoDAO {

    void insertProducto(Producto producto);
    ProductoDTO getProdMasRecaudado();
    ProductoDTO findById(int id);

}
