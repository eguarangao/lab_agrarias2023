package Model;

import lombok.Data;

import java.util.Date;
import java.util.List;

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
    private String comentario;
    private byte[] excelEstudiantes;
    private byte[] pdfResolucion;
    private byte[] pdfAprobacion;

    //Relaciones
    private Horario horario;
    private Laboratorio laboratorio;
    private Docente docente;
    private Periodo periodo;
    private List<Equipo> equipos;


}
