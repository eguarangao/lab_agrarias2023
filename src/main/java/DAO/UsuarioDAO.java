package DAO;

import Model.Usuario;
import global.Conexion;
import jakarta.inject.Inject;
import lombok.Data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class UsuarioDAO extends Conexion {


    public Usuario getUsuario(String username) {
        Usuario usuario = null;
        String clave;
        try {
            this.conectar();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM laboratorio.usuario us WHERE us.nombre = ? AND us.enabled='true' ");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            System.out.println(username);
           System.out.println(rs.getString(1));
            if (rs.next()) {
                usuario = new Usuario(rs.getString("nombre"), rs.getString("clave") ,rs.getBoolean("enabled"));
                System.out.println(username);

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
