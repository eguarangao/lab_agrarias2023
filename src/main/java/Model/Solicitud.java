package Model;

import lombok.Data;

@Data
public class Solicitud {
    private int id;
    private String codigo;
    private String objetivo;
    private String tema;
    private boolean enabled;
    private String tipo;
    private String analisis;
    private EXCEL excel;
    private PDF pdf;
    private Horario horario;
    private Laboratorio laboratorio;
    private Docente docente;


}
