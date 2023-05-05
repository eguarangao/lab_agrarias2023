package DAO;

import Model.Horario;
import global.Conexion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public List<Horario> horarioForLaboratorio(int id, String fecha) {
        List<Horario> listaHorario = new ArrayList<>();
        try {
            this.conectar();
            PreparedStatement ps = connection.prepareStatement("select * from laboratorio.horario where id_laboratorio = ? and fecha =   '"+fecha+"'");
            System.out.println("******************************SQL***********************************");
            System.out.println(id + "-----------"+ fecha);
            System.out.println(ps);
            ps.setInt(1, id);
        //    ps.setString(2, fecha);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Horario horas = new Horario();
                horas.setId(rs.getInt("id"));
                horas.setFecha(rs.getDate("fecha"));
                horas.setJornada1(rs.getBoolean("jornada1"));
                horas.setJornada2(rs.getBoolean("jornada2"));
                horas.setJornada3(rs.getBoolean("jornada3"));
                horas.setJornada4(rs.getBoolean("jornada4"));
                horas.setJornada5(rs.getBoolean("jornada5"));
                listaHorario.add(horas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.desconectar();
        }
        return listaHorario;
    }
}
