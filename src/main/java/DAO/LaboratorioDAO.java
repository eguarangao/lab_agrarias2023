package DAO;

import Model.Laboratorio;
import Model.Rol;
import global.Conexion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LaboratorioDAO extends Conexion {
    public List<Rol> findAllFacultadesAndLaboratorios(String username) throws SQLException {
        List<Rol> listaRoles = new ArrayList<>();
        ResultSet rs;
        try {
            this.conectar();
            PreparedStatement st = this.getConnection().prepareStatement("SELECT ro.id as id, ro.nombre as nombre\n" +
                    "FROM laboratorio.usuario us\n" +
                    "         INNER JOIN laboratorio.rol_usuario ru ON us.id = ru.id_usuario\n" +
                    "         INNER JOIN laboratorio.rol ro ON ru.id_rol = ro.id WHERE us.nombre =?;");
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
    public List<Laboratorio> findAllLaboratorio() throws SQLException {
        List<Laboratorio> listaLaboratorio = new ArrayList<>();
        ResultSet rs;
        try{
            this.conectar();
            PreparedStatement st = this.getConnection().prepareStatement("select id, nombre from laboratorio.laboratorio");
        rs = st.executeQuery();
            while (rs.next()) {
                Laboratorio laboratorio = new Laboratorio();
                laboratorio.setNombre(rs.getString("nombre"));
                laboratorio.setId(rs.getInt("id"));

                listaLaboratorio.add(laboratorio);
            }
            rs.close();
        }catch (Exception e){
            throw e;
        }finally {
            this.desconectar();
        }
        return listaLaboratorio;
    }
}
