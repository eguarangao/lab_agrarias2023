package Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    private int id;
    private String nombre;
    private String clave;
    private Persona persona;

    private List<Rol> roles;



    public Usuario(String nombre, String clave, boolean enabled) {
        this.nombre = nombre;
        this.clave = clave;
        this.enabled = enabled;
    }

    private boolean  enabled;


}

