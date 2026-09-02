package app.Factory.MYSQLEntityDAO;

import app.DAO.FacturaProductoDAO;
import app.Entidades.FacturaProducto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MYSQLFacturaProductoDAO implements FacturaProductoDAO {

    private final Connection conexion;

    public MYSQLFacturaProductoDAO(Connection connection){
        this.conexion = connection;
    } //constructor MYSQLFacturaProductoDAO



    private FacturaProducto map(ResultSet resultado) throws SQLException { //del resultSet devuelve un FacturaProducto
        FacturaProducto  facturaProducto = new FacturaProducto(
                resultado.getInt("idFactura"),
                resultado.getInt("idProducto"),
                resultado.getInt("cantidad")
        );
        return facturaProducto;
    } // map



    @Override
    public FacturaProducto getById(int idFactura, int idProducto){
        final String sql = """
                SELECT idFactura, idProducto, cantidad
                    FROM FacturaProducto 
                    WHERE idFactura = ? 
                    AND idProducto = ?
                """;
        try (PreparedStatement sentencia = conexion.prepareStatement(sql);) {
            sentencia.setInt(1, idFactura);
            sentencia.setInt(2, idProducto);
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next())
                    return map(resultado);
                else
                    return null;
            } catch (RuntimeException e) {
                System.err.println("Error buscando FacturaProducto por idFactura " + idFactura +
                        " e idProducto " + idProducto + " al procesar ResultSet: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Error buscando FacturaProducto por idFactura " + idFactura +
                    " e idProducto " + idProducto + " : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    } //getById



    @Override
    public List<FacturaProducto> getByIdFactura(int idFactura){
        List<FacturaProducto> facturasProductos = new ArrayList<>();
        final String sql = """
                SELECT idFactura, idProducto, cantidad
                    FROM FacturaProducto 
                    WHERE idFactura = ? 
                """;
        try (PreparedStatement sentencia = conexion.prepareStatement(sql);) {
            sentencia.setInt(1, idFactura);
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    FacturaProducto  facturaProducto = map(resultado);
                    facturasProductos.add(facturaProducto);
                }
            } catch (RuntimeException e) {
                System.err.println("Error buscando FacturaProducto por idFactura " + idFactura +
                        " al procesar ResultSet: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Error buscando FacturaProducto por idFactura " + idFactura +
                    " : " + e.getMessage());
            e.printStackTrace();
        }
        return facturasProductos;
    } //getByIdFactura



    @Override
    public List<FacturaProducto> getByIdProducto(int idProducto){
        List<FacturaProducto> facturasProductos = new ArrayList<>();
        final String sql = """
                SELECT idFactura, idProducto, cantidad
                    FROM FacturaProducto 
                    WHERE idProducto = ? 
                    ORDER BY idFactura
                """;
        try (PreparedStatement sentencia = conexion.prepareStatement(sql);) {
            sentencia.setInt(1, idProducto);
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    FacturaProducto  facturaProducto = map(resultado);
                    facturasProductos.add(facturaProducto);
                }
            } catch (RuntimeException e) {
                System.err.println("Error buscando FacturaProducto por idProducto " + idProducto +
                        " al procesar ResultSet: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Error buscando FacturaProducto por idProducto " + idProducto +
                    " : " + e.getMessage());
            e.printStackTrace();
        }
        return facturasProductos;
    } //getByIdProducto



    @Override
    public List<FacturaProducto> getAll() {
        List<FacturaProducto> facturasProductos = new ArrayList<>();
        final String sql = """
                SELECT idFactura, idProducto, cantidad
                FROM FacturaProducto 
                ORDER BY idFactura, idProducto
                """;
        try (   PreparedStatement sentencia = conexion.prepareStatement(sql);
                ResultSet resultado = sentencia.executeQuery();
        ) {
            while (resultado.next()) {
                FacturaProducto  facturaProducto = map(resultado);
                facturasProductos.add(facturaProducto);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar facturas-productos: ");
            e.printStackTrace();
        }

        return facturasProductos;
    } //getAll



    private void mapInto(FacturaProducto facturaProducto, PreparedStatement sentencia) throws SQLException {
        sentencia.setInt(1, facturaProducto.getIdFactura());
        sentencia.setInt(2, facturaProducto.getIdProducto());
        sentencia.setInt(3, facturaProducto.getCantidad());
    } //mapInto



    @Override
    public boolean create(FacturaProducto facturaProducto) {
        final String sql = """
                INSERT INTO FacturaProducto(idFactura, idProducto, cantidad) 
                VALUES (?, ?, ?)
                """;
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            mapInto(facturaProducto, sentencia);
            int filas = sentencia.executeUpdate();
            return filas>0; //éxito si se modificó alguna fila.
        } catch (SQLException e) {
            System.err.println("Error al insertar FacturaProducto:");
            e.printStackTrace();
        }
        return false;
    } //create



    @Override
    public boolean update(FacturaProducto facturaProducto) {
        final String sql = """
                UPDATE FacturaProducto 
                SET cantidad = ? 
                WHERE idFactura = ? 
                AND idProducto = ?
                """;
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)){
            sentencia.setInt(1, facturaProducto.getCantidad());
            sentencia.setInt(2, facturaProducto.getIdFactura());
            sentencia.setInt(3, facturaProducto.getIdProducto());
            int filas = sentencia.executeUpdate();
            return filas > 0; //exitosos si modificó alguna fila
        } catch(SQLException e) {
            System.err.println("Error actualizando Factura-Producto: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    } //update



    @Override
    public boolean delete(int idFactura, int idProducto) {
        final String sql = "DELETE FROM FacturaProducto WHERE idFactura = ? AND idProducto = ?";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idFactura);
            sentencia.setInt(2, idProducto);
            int filas = sentencia.executeUpdate();
            return filas>0; //si hay filas modificadas el borrado fue exitoso
        } catch (SQLException e) {
            System.err.println("error eliminando factura-producto: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    } //delete



    @Override
    public boolean deleteAll() {
        final String sql = """
                DELETE FROM FacturaProducto
                """;
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)){
            int filas = sentencia.executeUpdate();
            return filas>0; //si hay filas modificadas el borrado fue exitoso
        } catch(SQLException e){
            System.err.println("Error eliminando Factura-Producto:" + e.getMessage());
            e.printStackTrace();
        }
        return false;
    } //deleteAll

} //class MYSQLFacturaProductoDAO

