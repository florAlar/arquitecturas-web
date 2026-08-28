package app.Entidades;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class Producto {
    private int id;
    private String nombre;
    private Float precio;
    private int stock;
}
