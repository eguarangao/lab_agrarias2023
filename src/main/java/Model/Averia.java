package Model;

import lombok.Data;

import java.util.Date;

@Data
public class Averia {
    private int id_averia;
    private Date fecha_registro;
    private String descripcion;
    private Boolean enabled;
    private String prioridad;
    private String problema;

    //Relaciones entidades
    private Equipo equipo;
    private Aula aula;
}
