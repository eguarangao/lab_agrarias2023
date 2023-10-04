package Model;

import lombok.Data;

import java.util.Date;

@Data
public class Horario {
    private int id;
    private Date fecha;
    private boolean jornada1;
    private boolean jornada2;
    private boolean jornada3;
    private boolean jornada4;
    private boolean jornada5;
    private boolean jornada6;
    private boolean jornada7;
    private boolean jornada8;
    private Laboratorio idLaboratorio;

}
