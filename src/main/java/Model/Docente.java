package Model;

import lombok.Data;

@Data
public class Docente {
    private int id;
    private boolean enabled;
    private Persona persona;
}
