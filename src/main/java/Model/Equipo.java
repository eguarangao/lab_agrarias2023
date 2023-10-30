package Model;

import lombok.Data;

import java.util.Date;

@Data
public class Equipo {
    private int id;
    private String codigo;
    private String descripcion;
    private String marca;
    private String modelo;
    private String numeroSerie;
    private Date fechaAdquisicion;
    private Boolean estado;
    private Boolean estadoAveriaEquipo;
    //Relaciones entidades
    private Aula aula;
    private CategoriaEquipo categoriaEquipo;
    private Laboratorio laboratorio;
}
