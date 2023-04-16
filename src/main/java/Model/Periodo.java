package Model;

import lombok.Data;

@Data
public class Periodo {
    private int id;
    private String fechaInicio;
    private String fechaFin;
    private boolean enabled;
}
