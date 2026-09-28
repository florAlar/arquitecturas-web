package dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class CarreraDTO {
    private int id;
    private String nombreCarrera;

    @Override
    public String toString() {
        return "CarreraDTO{" +
                "id=" + id +
                ", nombreCarrera='" + nombreCarrera + '\'' +
                '}';
    }
}
