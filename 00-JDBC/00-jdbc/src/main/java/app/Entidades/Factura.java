package app.Entidades;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class Factura {
    private Long idFactura;
    private Long idCliente;
}
