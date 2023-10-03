package Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Periodo {
    private int id;
    private String namePeriodo;
    private int inicioPeriodo;
    private int finPeriodo;
    private boolean enabled;
    Timestamp fechaCreacion;
}
