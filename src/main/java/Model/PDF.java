package Model;

import lombok.Data;

@Data
public class PDF {
    private int id;
    private String titulo;
    private byte[] contenido;
    private String tipo;
}
