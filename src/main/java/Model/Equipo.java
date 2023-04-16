package Model;

import lombok.Data;

@Data
public class Equipo {
    private int id;
    private String nombre;
    private Laboratorio laboratorio;
    private CategoriaEquipo categoriaEquipo;
}
