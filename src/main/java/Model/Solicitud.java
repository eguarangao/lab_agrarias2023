package Model;

import lombok.Data;

import java.util.Date;

@Data
public class Solicitud {
    private int id;
    private String codigo;
    private String objetivo;
    private String tema;
    private boolean enabled;
    private Date fechaReserva;
    private Date fechaSolicita;
    private String tipo;
    private String analisis;
    private EXCEL excel;
    private PDF pdf;
    private Horario horario;
    private Laboratorio laboratorio;
    private Docente docente;


}
