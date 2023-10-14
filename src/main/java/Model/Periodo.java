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
    Timestamp fechaCreacion;
    private boolean enabled;

}
