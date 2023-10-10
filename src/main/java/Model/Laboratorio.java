package Model;

import lombok.Data;

import java.util.List;

@Data
public class Laboratorio {
    private int id;
    private int idFacultad;
    private String nombre;
    private String descripcion;
    private String facultad;
    private List<Aula> aulas;
}
