package app.Entidades;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class FacturaProducto {
    private int idFactura;
    private int idProducto;
    private int cantidad;
}
