package Model;

import lombok.Data;

import java.util.Date;

@Data
public class TipoMantenimiento {
    private int id;
    private String tipo;
    private Date fechacreacion;
    private String descripcion;

}
