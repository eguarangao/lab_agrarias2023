package DAO;

import Model.*;
import global.Conexion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class SolicitudDAO extends Conexion {
    Horario tempHorario = new Horario();
    public void save(Solicitud solicitud, Horario horario, List<Equipo> equipos, int idItems, String fecha) throws SQLException {
        System.out.println(solicitud + "DAO");
        try {
            this.conectar();
            this.getConnection().setAutoCommit(false);

            int idHorario = 0;
            int idSolicitud = 0;
            boolean existeHorario;

            LocalDateTime fechaActual = LocalDateTime.now();
            Date fechaActualConvert = Date.from(fechaActual.atZone(ZoneId.systemDefault()).toInstant());

            String sql = "select * from laboratorio.horario where fecha=?";


            //VERIFICA SI EL HORARIO EXISTE
            PreparedStatement st =
                    this.getConnection().
                            prepareStatement
                                    ("select * from laboratorio.horario " +
                                            "where id_laboratorio=? and fecha= '" + fecha + "'");
            st.setInt(1, solicitud.getLaboratorio().getId()); //Fecha
            ResultSet rs;
            rs = st.executeQuery();

            if (rs != null) {
                existeHorario = true;

                while (rs.next()) {
                    tempHorario.setId(rs.getInt("id"));
                    tempHorario.setFecha(rs.getDate("fecha"));
                    tempHorario.setJornada1(rs.getBoolean("jornada1"));
                    tempHorario.setJornada2(rs.getBoolean("jornada2"));
                    tempHorario.setJornada3(rs.getBoolean("jornada3"));
                    tempHorario.setJornada4(rs.getBoolean("jornada4"));
                    tempHorario.setJornada5(rs.getBoolean("jornada5"));
                }
            } else {
                existeHorario = false;
            }

            if (existeHorario) { //EN CASO EXISTA LO QUE HACE ES ACTUALIZAR LAS JORDANAS
                PreparedStatement st1 = this.getConnection().prepareStatement("update laboratorio.horario set jornada1=?, jornada2=?, jornada3=?, jornada4=?, jornada5=? where fecha= '"+fecha+"'");
                asiganarUpdateItems(idItems);
                Horario horarioJK = tempHorario;


                st1.setBoolean(1, horarioJK.isJornada1()); //Jornada1
                st1.setBoolean(2, horarioJK.isJornada2()); //Jornada2
                st1.setBoolean(3, horarioJK.isJornada3()); //Jornada3
                st1.setBoolean(4, horarioJK.isJornada4()); //Jornada4
                st1.setBoolean(5, horarioJK.isJornada5()); //Jornada5
                st1.setDate(6, (java.sql.Date) horarioJK.getFecha()); //Fecha
                st1.executeUpdate();
                st1.close();

            } else {  //EN CASO NO EXISTA CREA EL HORARIO

                Horario tempHorario2 = new Horario();
                tempHorario2 = asiganarNewItems(idItems);
                PreparedStatement st3 = this.getConnection().prepareStatement("insert into laboratorio.horario (fecha, jornada1,  jornada2, jornada3, jornada4, jornada5, id_laboratorio)\n" +
                        "values (?,?,?,?,?,?,?);");
                st3.setDate(1, (java.sql.Date) horario.getFecha()); //Fecha
                st3.setBoolean(2, tempHorario2.isJornada1()); //Jornada1
                st3.setBoolean(3, tempHorario2.isJornada2()); //Jornada2
                st3.setBoolean(4, tempHorario2.isJornada3()); //Jornada3
                st3.setBoolean(5, tempHorario2.isJornada4()); //Jornada4
                st3.setBoolean(6, tempHorario2.isJornada5()); //Jornada5
                st3.setInt(7, horario.getIdLaboratorio().getId()); //id_laboratorio
                st3.executeUpdate();
                st3.close();

                //OPTENER EL ÚLTIMO ID DE horario
                PreparedStatement stMAX = this.getConnection().prepareStatement("SELECT MAX(id) FROM laboratorio.horario;");
                ResultSet rsM = stMAX.executeQuery();
                if (rsM.next()) {
                    idHorario = rs.getInt(1);
                    horario.setId(idHorario);
                    // hacer algo con el valor de idHorario
                }
            }

            ///


            //REGISTRA LA SOLICITUD
            PreparedStatement st4 = this.getConnection().prepareStatement(
                    "insert into laboratorio.solicitud (" +
                            " codigo, " +
                            "tema," +
                            " objetivo, " +
                            "fecha_registro, " +
                            "enabled, tipo, " +
                            "analisis, id_laboratorio, " +
                            "id_periodo, id_horarios, " +
                            "id_docente, pdf_resolucion, excel_estudiante,)  values (?,?,?,?,?,?,?,?,?,?,?,?,?);");
            st4.setString(1, solicitud.getCodigo());  //Codigo
            st4.setString(2, solicitud.getTema());  //Tema
            st4.setString(3, solicitud.getObjetivo()); //Objetivo
            st4.setDate(4, (java.sql.Date) fechaActualConvert); //Fecha Registro
            st4.setBoolean(5, !solicitud.isEnabled()); //Enabled
            st4.setString(6, solicitud.getTipo()); //Tipo
            st4.setString(7, solicitud.getAnalisis()); //Analisis
            st4.setInt(8, solicitud.getLaboratorio().getId()); //laboratorio
            st4.setString(9, solicitud.getPeriodo().getNamePeriodo()); //periodo
            st4.setLong(10, solicitud.getHorario().getId()); //horario
//            st4.setInt(11, solicitud.getDocente().getId()); //docente
            st4.setInt(11, 1); //docente
            st4.setBytes(12, solicitud.getPdfResolucion().getBytes());
            st4.setBytes(13, solicitud.getExcelEstudiantes().getBytes());
            st4.executeUpdate();

            st4.close();


            //OPTENER EL ÚLTIMO ID DE LA SOLICITUD
            PreparedStatement stMAX = this.getConnection().prepareStatement("SELECT MAX(id) FROM laboratorio.solicitud;");
            ResultSet rsS = stMAX.executeQuery();
            if (rsS.next()) {
                idSolicitud = rsS.getInt(1);
                // hacer algo con el valor de idHorario
            }


            //REGISTRAR LOS EQUIPOS DE LABORATORIO

            for (int i = 0; i < equipos.size(); i++) {
                Equipo equipo = equipos.get(i);
                PreparedStatement stO = this.getConnection().prepareStatement("insert into laboratorio.equipo_solicitud (id_equipo, id_solicitud)\n" +
                        "values (?,?);");

                st4.setInt(1, equipos.get(i).getId());  //idEquipo

                st4.setInt(2, idSolicitud);  //idSolicitud
                st4.close();

                // Hacer algo con el objeto "equipo" en cada iteración
            }
            this.getConnection().commit();


        } catch (Exception e) {
            System.out.println("Error...Ebert");
            e.getMessage();
            this.getConnection().rollback();
        } finally {
            this.desconectar();
        }
    }
/*
    public Horario asiganarNewItems(int idItems) {
        Horario myHorario = new Horario();
        switch (idItems) {
            case 1:
                myHorario.setJornada1(true);
                myHorario.setJornada2(false);
                myHorario.setJornada3(false);
                myHorario.setJornada4(false);
                myHorario.setJornada5(false);
            case 2:
                myHorario.setJornada1(false);
                myHorario.setJornada2(true);
                myHorario.setJornada3(false);
                myHorario.setJornada4(false);
                myHorario.setJornada5(false);
            case 3:
                myHorario.setJornada1(false);
                myHorario.setJornada2(false);
                myHorario.setJornada3(true);
                myHorario.setJornada4(false);
                myHorario.setJornada5(false);
            case 4:
                myHorario.setJornada1(false);
                myHorario.setJornada2(false);
                myHorario.setJornada3(false);
                myHorario.setJornada4(true);
                myHorario.setJornada5(false);
            case 5:
                myHorario.setJornada1(false);
                myHorario.setJornada2(false);
                myHorario.setJornada3(false);
                myHorario.setJornada4(false);
                myHorario.setJornada5(true);
        }
        return myHorario;
    }*/
public Horario asiganarNewItems(int idItems) {
    Horario myHorario = new Horario();
    switch (idItems) {
        case 1:
            myHorario.setJornada1(true);
            myHorario.setJornada2(false);
            myHorario.setJornada3(false);
            myHorario.setJornada4(false);
            myHorario.setJornada5(false);
        case 2:
            myHorario.setJornada1(false);
            myHorario.setJornada2(true);
            myHorario.setJornada3(false);
            myHorario.setJornada4(false);
            myHorario.setJornada5(false);
        case 3:
            myHorario.setJornada1(false);
            myHorario.setJornada2(false);
            myHorario.setJornada3(true);
            myHorario.setJornada4(false);
            myHorario.setJornada5(false);
        case 4:
            myHorario.setJornada1(false);
            myHorario.setJornada2(false);
            myHorario.setJornada3(false);
            myHorario.setJornada4(true);
            myHorario.setJornada5(false);
        case 5:
            myHorario.setJornada1(false);
            myHorario.setJornada2(false);
            myHorario.setJornada3(false);
            myHorario.setJornada4(false);
            myHorario.setJornada5(true);
    }
    return myHorario;
}
    public void asiganarUpdateItems(int idItems) {

        switch (idItems) {
            case 1:
                tempHorario.setJornada1(true);
            case 2:
                tempHorario.setJornada2(true);
            case 3:
                tempHorario.setJornada3(true);
            case 4:
                tempHorario.setJornada4(true);
            case 5:
                tempHorario.setJornada5(true);
        }

    }


}
