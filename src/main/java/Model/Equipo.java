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
    private String estado;
    private String imagen;
    private Date fechaAdquisicion;
    private Date FechaRegistro;
    //Relaciones entidades
    private Aula aula;
    private CategoriaEquipo categoriaEquipo;
}
