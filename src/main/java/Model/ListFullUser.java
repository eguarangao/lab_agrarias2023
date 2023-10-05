package Model;

import lombok.Data;

import java.sql.Timestamp;

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
    Timestamp fechaCreacion;
    //tabla rol_usuario
    int idRol;
    //tabla tecnico
    int idTecnico;
    //tabla docente
    int idDocente;

}
