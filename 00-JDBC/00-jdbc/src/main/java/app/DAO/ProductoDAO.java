package app.DAO;

import java.sql.Connection;
import app.entidades.Producto;


public interface ProductoDAO {

    void inserProducto(Producto producto);
    ProductoDTO getProdMasRecaudado();
    Producto findById(int id);

}
