package Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Facultad {
    int idFacd;
    String nombreFacd;
    String correoFacd;
    String descripcionFacd;
    Timestamp fechaCreacion;
}
