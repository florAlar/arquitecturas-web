package app.Entidades;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class Producto {
    private int idProducto;
    private String Nombre;
    private Float Precio;
}
