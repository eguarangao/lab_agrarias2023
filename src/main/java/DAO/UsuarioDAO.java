package DAO;

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






}
