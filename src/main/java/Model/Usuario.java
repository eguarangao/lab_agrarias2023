package Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    private int id;
    private String nombre;
    private String clave;
    private Persona persona;

    public Usuario(String nombre, String clave, boolean enabled) {
        this.nombre = nombre;
        this.clave = clave;
        this.enabled = enabled;
    }

    private boolean  enabled;


}

