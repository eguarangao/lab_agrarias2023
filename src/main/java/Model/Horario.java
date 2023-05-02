package Model;

import lombok.Data;

import java.util.Calendar;
import java.util.Date;

@Data
public class Horario {
    private Long id;
    private Date fecha;
    private boolean jornada1;
    private boolean jornada2;
    private boolean jornada3;
    private boolean jornada4;
    private boolean jornada5;
    private Laboratorio idLaboratorio;

}
