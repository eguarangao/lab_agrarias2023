package Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class Usuario {
    private int id;
    private String nombre;
    private String clave;
    private Persona persona;
    private boolean  enabled;


}

