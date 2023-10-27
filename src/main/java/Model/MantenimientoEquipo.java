package Model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class MantenimientoEquipo {
    private int id_mant_equipo;
    private Boolean estado;
    private Date fecha_retorno_equipo;

    //Relaciones entidades
    private Mantenimiento mantenimiento;
    private Equipo equipo;
    private List<Equipo> equiposRequeridosMantenimiento;
    private TipoMantenimiento tipoMantenimiento;
    private Aula aula;
}
