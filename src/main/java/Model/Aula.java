package Model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Aula {
    private int id;
    private String nombre;
    private String descripcion;
    private int capacidad;
    Timestamp fechaCreacion;
    private int id_aula;//variable de prueba
}
