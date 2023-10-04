package DAO;

import Model.AjustePerfil;
import Model.ListFullUser;
import Model.Persona;
import Model.Usuario;
import global.Conexion;
import jakarta.inject.Inject;
import lombok.Data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO extends Conexion {


    public Usuario getUsuario(String username, String clave) {
        Usuario usuario = null;
        try {
            this.conectar();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM laboratorio.usuario us WHERE nombre = ? AND clave=? AND enabled=true");
            ps.setString(1, username);
            ps.setString(2, clave);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                usuario = new Usuario();
                usuario.setNombre(rs.getString("nombre"));
                usuario.setClave(rs.getString("clave"));
                usuario.setEnabled(rs.getBoolean("enabled"));
                System.out.println(usuario);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuario;
    }

    //    public String getUsuario(String username) {
//        Usuario usuario = null;
//        String clave = null;
//        try {
//            PreparedStatement ps = connection.prepareStatement("SELECT * FROM laboratorio.usuario us WHERE us.nombre = ? + ");
//            ps.setString(1, username);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                clave =  rs.getString("clave");
//
//
//            }
//            rs.close();
//            ps.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return clave;
//    }
    public AjustePerfil SelectAjustePerfil(String usuario, String nameRol) {
        AjustePerfil ajustePerfil = null;
        try {
            this.conectar();
            PreparedStatement ps = connection.prepareStatement("select u.nombre as usuario, u.clave as clave, p.nombre as nombre,\n" +
                    "       p.apellido, p.telefono, p.email,\n" +
                    "       p.dni, p.genero, r.nombre as nombre_rol,r.descripcion\n" +
                    "from laboratorio.usuario u\n" +
                    "         inner join laboratorio.persona p\n" +
                    "                    on p.id = u.id_persona\n" +
                    "         inner join laboratorio.rol_usuario ru\n" +
                    "                    on u.id = ru.id_usuario\n" +
                    "         inner join laboratorio.rol r\n" +
                    "                    on r.id = ru.id_rol\n" +
                    "where u.nombre = ? and r.nombre = ?;");
            ps.setString(1, usuario);
            ps.setString(2, nameRol);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ajustePerfil = new AjustePerfil();
                ajustePerfil.setUsuario(rs.getString("usuario"));
                ajustePerfil.setClave(rs.getString("clave"));
                ajustePerfil.setNombre(rs.getString("nombre"));
                ajustePerfil.setApellido(rs.getString("apellido"));
                ajustePerfil.setTelefono(rs.getString("telefono"));
                ajustePerfil.setEmail(rs.getString("email"));
                ajustePerfil.setDni(rs.getString("dni"));
                ajustePerfil.setGenero(rs.getString("genero"));
                ajustePerfil.setRolNombre(rs.getString("nombre_rol"));
                ajustePerfil.setRolDescripcion(rs.getString("descripcion"));
                System.out.println(ajustePerfil);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ajustePerfil;
    }

    public AjustePerfil UpdateAjustePerfil(AjustePerfil ajustePerfil) {

        try {
            this.conectar();
            PreparedStatement ps = connection.prepareStatement("");

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ajustePerfil = new AjustePerfil();
                ajustePerfil.setUsuario(rs.getString("usuario"));
                ajustePerfil.setClave(rs.getString("clave"));
                ajustePerfil.setNombre(rs.getString("nombre"));
                ajustePerfil.setApellido(rs.getString("apellido"));
                ajustePerfil.setTelefono(rs.getString("telefono"));
                ajustePerfil.setEmail(rs.getString("email"));
                ajustePerfil.setDni(rs.getString("dni"));
                ajustePerfil.setGenero(rs.getString("genero"));
                System.out.println(ajustePerfil);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ajustePerfil;
    }

    public List<ListFullUser> listarUsuariosPersonas() throws SQLException{
        List<ListFullUser> usuariosPersonas = new ArrayList<>();
   this.conectar();
        String query = "select * from laboratorio.persona p inner join laboratorio.usuario ON usuario.id_persona = p.id ";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                ListFullUser usuarioPersona = new ListFullUser();
                usuarioPersona.setUsuarioId(resultSet.getInt("id"));
                usuarioPersona.setNombreUsuario(resultSet.getString("nombre"));
                usuarioPersona.setClave(resultSet.getString("clave"));
                usuarioPersona.setEnabled(resultSet.getBoolean("enabled"));

                usuarioPersona.setPersonaId(resultSet.getInt("id"));
                usuarioPersona.setNombrePersona(resultSet.getString("nombre"));
                usuarioPersona.setApellido(resultSet.getString("apellido"));
                usuarioPersona.setTelefono(resultSet.getString("telefono"));
                usuarioPersona.setEmail(resultSet.getString("email"));
                usuarioPersona.setDni(resultSet.getString("dni"));
                usuarioPersona.setGenero(resultSet.getString("genero"));


                usuariosPersonas.add(usuarioPersona);
            }
        } finally {
            this.desconectar();
        }

        return usuariosPersonas;
    }
}
