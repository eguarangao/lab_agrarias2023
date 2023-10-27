package Model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Mantenimiento {
    private int id;
    private Date fechaRegistro;
    private Boolean estado;
    private Date fechaRetorno;
    private String descripcion_mante;

    //Relaciones entidades
    private MantenimientoEquipo mantenimientoEquipo;
    private TipoMantenimiento tipoMantenimiento;
    private List<MantenimientoEquipo> LisMantenimientoEquipo;

}
