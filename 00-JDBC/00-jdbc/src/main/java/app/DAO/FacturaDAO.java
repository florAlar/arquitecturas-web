package app.DAO;

import app.Entidades.Factura;

import java.util.List;

public interface FacturaDAO {
    Factura getFacturaById(Long idFactura);
    List<Factura> getFacturaByClienteId(Long idCliente);
    List<Factura> getAllFacturas();
    void insertFactura(Factura factura);
    void updateFactura(Factura factura);
    void deleteFactura(Long idFactura);
    void createFactura(Long idFactura, Long idCliente);
    void deleteAll();
}
