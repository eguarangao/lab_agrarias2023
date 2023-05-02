package DAO;

import Model.AjustePerfil;
import Model.Horario;
import Model.Solicitud;
import global.Conexion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HorarioDAO extends Conexion {

//    public void save(Horario horario) throws SQLException {
//        System.out.println(horario + "DAO");
//        try {
//            this.conectar();
//            PreparedStatement st = this.getConnection().prepareStatement("insert into laboratorio.horario ( nombre, apellido, telefono, email, genero, dni)\n" +
//                    "values (?,?,?,?,?,?);");
//            st.setString(1, horario.getNombre());
//            st.setString(2, horario.getApellido());
//            st.setString(3, horario.getTelefono());
//            st.setString(4, horario.getEmail());
//            st.setString(5, horario.getGenero());
//            st.setString(6, horario.getDni());
//            st.executeUpdate();
//        } catch (Exception e) {
//            System.out.println("Error...Ebert");
//            e.getMessage();
//        } finally {
//            this.desconectar();
//        }
//    }

//    public void update(Horario horario) throws SQLException {
//
//        try {
//            this.conectar();
//            PreparedStatement st = this.getConnection().prepareStatement("update laboratorio.horario\n" +
//                    "set id       = ?,\n" +
//                    "    nombre= ?,\n" +
//                    "    apellido =?,\n" +
//                    "    telefono = ?,\n" +
//                    "    email    = ?,\n" +
//                    "    dni      = ?,\n" +
//                    "    genero   = ?\n" +
//                    "where id=?;");
//
//            st.setInt(1, horario.getId());
//            st.setString(2, horario.getNombre());
//            st.setString(3, horario.getApellido());
//            st.setString(4, horario.getTelefono());
//            st.setString(5, horario.getEmail());
//            st.setString(6, horario.getDni());
//            st.setString(7, horario.getGenero());
//            st.setInt(8, horario.getId());
//
//
//        } catch (Exception e) {
//            System.out.println("Error...Ebert");
//            e.getMessage();
//        } finally {
//            this.desconectar();
//        }
//    }
//
//
//
//
//    public Horario findById(Horario horarioOb) throws SQLException {
//        Horario horario = new Horario();
//        ResultSet rs;
//        try {
//            this.conectar();
//            PreparedStatement st = this.getConnection().prepareStatement("SELECT pe.id       as id,\n" +
//                    "       pe.nombre   as nombre,\n" +
//                    "       pe.apellido as apellido,\n" +
//                    "       pe.telefono as telefono,\n" +
//                    "       pe.email    as email,\n" +
//                    "       pe.dni      as dni,\n" +
//                    "       pe.genero   as genero\n" +
//                    "FROM laboratorio.PERSONA pe where pe.id= ?");
//            st.setInt(1, horarioOb.getId());
//            rs = st.executeQuery();
//            while (rs.next()) {
//                horario = new Horario();
//
//                horario.setId(rs.getInt("id"));
//                horario.setNombre(rs.getString("nombre"));
//                horario.setApellido(rs.getString("apellido"));
//                horario.setTelefono(rs.getString("telefono"));
//                horario.setEmail(rs.getString("email"));
//                horario.setDni(rs.getString("dni"));
//                horario.setGenero(rs.getString("genero"));
//            }
//
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            this.desconectar();
//        }
//        return horario;
//    }

//    public List<Horario> findAll() throws SQLException {
//        List<Horario> listaHorarios = new ArrayList<>();
//        ResultSet rs;
//        try {
//            this.conectar();
//            PreparedStatement st = this.getConnection().prepareStatement("SELECT hor.id, hor.fecha_inicio, hor.fecha_fin, hor.esatdo FROM laboratorio.horario hor where hor.");
//            st.setInt(1, personaOb.getId());
//            rs = st.executeQuery();
//            while (rs.next()) {
//                Horario horario = new Horario();
//                horario.setId(rs.getInt("id"));
//                horario.setFechaInicio(rs.getDate("fecha_inicio"));
//                horario.setFechaFin(rs.getDate("fecha_fin"));
//                horario.setEstado(rs.getInt("estado"));
//                listaHorarios.add(horario);
//            }
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            this.desconectar();
//        }
//        return listaHorarios;
//    }

    //    public List<Horario> findAllByDate(Date fecha, Solicitud solicitud) throws SQLException {
//        List<Horario> listaHorarios = new ArrayList<>();
//        ResultSet rs;
//        try {
//            this.conectar();
//            PreparedStatement st = this.getConnection().prepareStatement("SELECT * FROM laboratorio.horario hor");
//            st.setInt(1,);
//            rs = st.executeQuery();
//            while (rs.next()) {
//                Horario horario = new Horario();
//                horario.setId(rs.getInt("id"));
//                horario.setFechaInicio(rs.getDate("fecha_inicio"));
//                horario.setFechaFin(rs.getDate("fecha_fin"));
//                horario.setEstado(rs.getInt("estado"));
//                listaHorarios.add(horario);
//            }
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            this.desconectar();
//        }
//        return listaHorarios;
//    }
    Horario horario;
    public void horarioForLaboratorio(int id) {

        try {
            this.conectar();
            PreparedStatement ps = connection.prepareStatement("");

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                horario = new Horario();
                horario.setId(rs.getLong("id"));
                horario.setFecha(rs.getDate("fecha"));
                horario.setJornada1(rs.getBoolean("jornada1"));
                horario.setJornada2(rs.getBoolean("jornada2"));
                horario.setJornada3(rs.getBoolean("jornada3"));
                horario.setJornada4(rs.getBoolean("jornada4"));
                horario.setJornada5(rs.getBoolean("jornada5"));

                System.out.println(horario);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
