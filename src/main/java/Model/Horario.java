package Model;

import lombok.Data;

import java.util.Calendar;
import java.util.Date;

@Data
public class Horario {
    private Long id;
    private Date fechaInicio;
    private Date fechaFin;
    private boolean enabled;
}
