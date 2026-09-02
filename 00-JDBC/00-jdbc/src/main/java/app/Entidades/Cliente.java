package app.Entidades;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class Cliente {
    private Long idCliente;
    private String nombre;
    private String email;
}
