package Model;

import lombok.Data;

import java.util.Date;

@Data
public class Mantenimiento {
    private int id;
    private Date fechaRegistro;
    private Boolean estado;
    private Date fechaRetorno;
}
