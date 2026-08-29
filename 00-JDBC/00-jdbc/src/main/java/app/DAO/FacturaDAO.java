package app.DAO;
import java.sql.Connection;
import app.Entidades.Factura;
import java.util.List;

public interface FacturaDAO {
    Factura getFacturaById(Long idFactura);
    Factura getFacturaByClienteId(Long idCliente);
    List<Factura> getAllFacturas();
    void insertFactura(Factura factura);
    void updateFactura(Factura factura);
    void deleteFactura(Long idFactura);



}

