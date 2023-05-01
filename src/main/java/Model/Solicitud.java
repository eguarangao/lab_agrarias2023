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
    private Date fechaRegistro;
    private String tipo;
    private String analisis;
    private String excelEstudiantes;
    private String pdfResolucion;
    private String pdfAprobacion;
    private Horario horario;
    private Laboratorio laboratorio;
    private Docente docente;


}
