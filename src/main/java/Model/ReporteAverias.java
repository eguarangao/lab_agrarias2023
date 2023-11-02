package Model;

import lombok.Data;

import java.util.Date;

@Data
public class ReporteAverias {
    private int id_averia;
    private String fecha_registro;
    private String descripcion;
    private String estado_averia;
    private String prioridad;
    private String problema;

    private int id_equipo;
    private String codigo;
    private String descrip_equipo;
    private String marca;
    private String modelo;
    private String num_serie;
    private String fecha_adquisicion;
    private String estado;
    private String averia;

    private int id_categoria_equipo;
    private String categoria;

    private int id_aula;
    private String aula;

    private int id_laborat;
    private String nom_laboratorio;

    private int id_person;
    private String nombre;
    private String apellido;

    private int id_facultad;
    private String facultad;

}
