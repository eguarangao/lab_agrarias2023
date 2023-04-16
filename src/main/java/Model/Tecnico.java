package Model;

import lombok.Data;

@Data
public class Tecnico {
    private int id;
    private boolean enabled;
    private Persona persona;
}
