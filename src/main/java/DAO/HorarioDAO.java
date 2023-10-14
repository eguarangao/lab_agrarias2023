package DAO;

import Model.Horario;
import global.Conexion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HorarioDAO extends Conexion {

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
            boolean registrosEncontrados = false;
        //    ps.setString(2, fecha);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                registrosEncontrados = true;
                Horario horas = new Horario();
                horas.setId(rs.getInt("id"));
                horas.setFecha(rs.getDate("fecha"));
                horas.setJornada1(rs.getBoolean("jornada1"));
                horas.setJornada2(rs.getBoolean("jornada2"));
                horas.setJornada3(rs.getBoolean("jornada3"));
                horas.setJornada4(rs.getBoolean("jornada4"));
                horas.setJornada5(rs.getBoolean("jornada5"));
                horas.setJornada6(rs.getBoolean("jornada6"));
                horas.setJornada7(rs.getBoolean("jornada7"));
                horas.setJornada8(rs.getBoolean("jornada8"));
                listaHorario.add(horas);
            }
            if (!registrosEncontrados) {
                Horario horas = new Horario();
                horas.setId(1); // Puedes establecer un valor específico para el ID o dejarlo en 0
                horas.setFecha(java.sql.Date.valueOf(fecha));
                horas.setJornada1(true);
                horas.setJornada2(true);
                horas.setJornada3(true);
                horas.setJornada4(true);
                horas.setJornada5(true);
                horas.setJornada6(true);
                horas.setJornada7(true);
                horas.setJornada8(true);
                listaHorario.add(horas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.desconectar();
        }
        return listaHorario;
    }


    public boolean existeHorario(int idLaboratorio, String fecha) {
        boolean registrosEncontrados = false;
        try {
            this.conectar();
            PreparedStatement ps = connection.prepareStatement("select * from laboratorio.horario where id_laboratorio = ? and fecha =   '" + fecha + "'");
            ps.setInt(1, idLaboratorio);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                registrosEncontrados = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.desconectar();
        }
        return registrosEncontrados;
    }




    public List<Horario> findByLaboratorioIdAndFecha(int id, String fecha) {
        List<Horario> listaHorario = new ArrayList<>();
        try {
            this.conectar();

            // Consulta SQL parametrizada para buscar horarios por ID de laboratorio y fecha
            String sql = "SELECT * FROM laboratorio.horario WHERE id_laboratorio = ? AND fecha = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, fecha);

            ResultSet rs = ps.executeQuery();

            boolean registrosEncontrados = false;

            while (rs.next()) {
                registrosEncontrados = true;
                Horario horas = new Horario();
                horas.setId(rs.getInt("id"));
                horas.setFecha(rs.getDate("fecha"));
                horas.setJornada1(rs.getBoolean("jornada1"));
                horas.setJornada2(rs.getBoolean("jornada2"));
                horas.setJornada3(rs.getBoolean("jornada3"));
                horas.setJornada4(rs.getBoolean("jornada4"));
                horas.setJornada5(rs.getBoolean("jornada5"));
                horas.setJornada6(rs.getBoolean("jornada6"));
                horas.setJornada7(rs.getBoolean("jornada7"));
                horas.setJornada8(rs.getBoolean("jornada8"));
                listaHorario.add(horas);
            }

            // Si no se encontraron registros, crear un objeto Horario con todas las jornadas en false
            if (!registrosEncontrados) {
                Horario horas = new Horario();
                horas.setId(1); // Puedes establecer un valor específico para el ID o dejarlo en 0
                horas.setFecha(java.sql.Date.valueOf(fecha));
                horas.setJornada1(false);
                horas.setJornada2(false);
                horas.setJornada3(false);
                horas.setJornada4(false);
                horas.setJornada5(false);
                horas.setJornada6(false);
                horas.setJornada7(false);
                horas.setJornada8(false);
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
