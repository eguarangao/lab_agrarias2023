package Model;

import lombok.Data;

@Data
public class Usuario {
    private int id;
    private String nombre;
    private String clave;
    private Persona persona;
}
