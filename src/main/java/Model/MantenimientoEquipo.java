package Model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MantenimientoEquipo {
    private int id_mant_equipo;
    private Boolean estado;

    //Relaciones entidades
    private Mantenimiento mantenimiento;
    private Equipo equipo;
    private TipoMantenimiento tipoMantenimiento;
    private Aula aula;
}
