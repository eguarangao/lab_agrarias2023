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
    public List<Laboratorio> findAll() throws SQLException {
        List<Laboratorio> listaLaboratorio = new ArrayList<>();
        ResultSet rs;
        try{
            this.conectar();
            PreparedStatement st = this.getConnection().prepareStatement("select id, nom_laboratorio from laboratorio.laboratorio");
        rs = st.executeQuery();
            while (rs.next()) {
                Laboratorio laboratorio = new Laboratorio();
                laboratorio.setNombre(rs.getString("nom_laboratorio"));
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

    public void saveLaboratoriobyFacultad(int idFacultad, String nomLaboratorio) throws SQLException {
        String sql = "INSERT INTO laboratorio.laboratorio (id_facultad, nom_laboratorio) VALUES (?, ?)";
             this.conectar();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idFacultad);
            preparedStatement.setString(2, nomLaboratorio);
            preparedStatement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            desconectar();
        }
    }


    public List<Laboratorio> findByIdUsuario(int idUsuario) throws SQLException {
        List<Laboratorio> listaLaboratorio = new ArrayList<>();
        ResultSet rs;
        try{
            this.conectar();
            PreparedStatement st = this.getConnection().prepareStatement("SELECT l.id as id, l.nom_laboratorio as nombre\n" +
                    "FROM laboratorio.laboratorio l\n" +
                    "         inner join laboratorio.tecnico_laboratorio tl on l.id = tl.id_laboratorio\n" +
                    "         inner join laboratorio.tecnico t on l.id = t.id_laboratorio\n" +
                    "         inner join laboratorio.usuario u on u.id = t.id_usuario\n" +
                    "where u.id =?;");
            st.setInt(1,idUsuario);
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
