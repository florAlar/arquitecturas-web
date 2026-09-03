package app.DAO;

import app.DTO.ProductoDTO;
import app.Entidades.Producto;


public interface ProductoDAO {

    void insertProducto(Producto producto);
    ProductoDTO getProdMasRecaudado();
    ProductoDTO findById(int id);

}
