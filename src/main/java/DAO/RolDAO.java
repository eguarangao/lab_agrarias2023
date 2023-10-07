package DAO;

import Model.Persona;
import Model.Rol;
import global.Conexion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RolDAO extends Conexion {
    public List<Rol> findAllRolesUsuarioByUsername(String username) throws SQLException {
        List<Rol> listaRoles = new ArrayList<>();
        ResultSet rs;
        try {
            this.conectar();
            PreparedStatement st = this.getConnection().prepareStatement("SELECT ro.id as id, ro.nombre as nombre\n" +
                    "FROM laboratorio.usuario us\n" +
                    "         INNER JOIN laboratorio.rol_usuario ru ON us.id = ru.id_usuario\n" +
                    "         INNER JOIN laboratorio.rol ro ON ru.id_rol = ro.id WHERE us.nombre_usuario =?;");
            st.setString(1, username);
            rs = st.executeQuery();
            while (rs.next()) {
                Rol rol = new Rol();
                rol.setNombre(rs.getString("nombre"));
                rol.setId(rs.getInt("id"));

                listaRoles.add(rol);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            this.desconectar();
        }
        return listaRoles;
    }
}
