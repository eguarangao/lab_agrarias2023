package Model;

import lombok.Data;

@Data
public class ListFullUser {
    private int usuarioId;
    private String nombreUsuario;
    private String clave;
    private int personaId;
    private String nombrePersona;
    private String apellido;
    private String telefono;
    private String email;
    private String dni;
    private String genero;
    private boolean enabled;
}
