package Model;

import lombok.Data;

@Data
public class Persona {
    private int id;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private String genero;
    private String dni;
    boolean enable;
}
