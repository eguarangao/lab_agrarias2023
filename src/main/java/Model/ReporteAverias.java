package Model;

import lombok.Data;

import java.util.Date;

@Data
public class ReporteAverias {
    private int id_averia;
    private Date fecha_registro;
    private String descripcion;
    private String estado_averia;
    private int id_equipo;
    private String descrip_equipo;
    private int id_aula;
    private String aula;

}
