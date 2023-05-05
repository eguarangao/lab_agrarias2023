package DAO;

import Model.Equipo;
import Model.Horario;
import Model.Persona;
import Model.Solicitud;
import global.Conexion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class SolicitudDAO extends Conexion {
    public void save(Solicitud solicitud, Horario horario, List<Equipo> equipos) throws SQLException {
        System.out.println(solicitud + "DAO");
        try {
            this.conectar();
            this.getConnection().setAutoCommit(false);

            int idExcel;
            int idPDF;
            int idHorario= 0;
            int idSolicitud= 0;
            boolean existeHorario;

            LocalDateTime fechaActual = LocalDateTime.now();
            Date fechaActualConvert = Date.from(fechaActual.atZone(ZoneId.systemDefault()).toInstant());

            String sql = "select * from laboratorio.horario where fecha=?";


            //VERIFICA SI EL HORARIO EXISTE
            PreparedStatement st = this.getConnection().prepareStatement("select * from laboratorio.horario where fecha=?");
            st.setDate(1, (java.sql.Date) horario.getFecha()); //Fecha
            ResultSet rs;
            rs = st.executeQuery();

            if (rs != null) {
                existeHorario = true;
            } else {
                existeHorario = false;
            }

            if (existeHorario) { //EN CASO EXISTA LO QUE HACE ES ACTUALIZAR LAS JORDANAS



                PreparedStatement st1 = this.getConnection().prepareStatement("update laboratorio.horario set jornada1=?, jornada2=?, jornada3=?, jornada4=?, jornada5=? where fecha=?;");
                st1.setBoolean(1, horario.isJornada1()); //Jornada1
                st1.setBoolean(2, horario.isJornada2()); //Jornada2
                st1.setBoolean(3, horario.isJornada3()); //Jornada3
                st1.setBoolean(4, horario.isJornada4()); //Jornada4
                st1.setBoolean(5, horario.isJornada5()); //Jornada5
                st1.setDate(6, (java.sql.Date) horario.getFecha()); //Fecha
                st1.executeUpdate();
                st1.close();

            }
            else {  //EN CASO NO EXISTA CREA EL HORARIO



                PreparedStatement st3 = this.getConnection().prepareStatement("insert into laboratorio.horario (fecha, jornada1,  jornada2, jornada3, jornada4, jornada5)\n" +
                        "values (?,?,?,?,?,?);");
                st3.setDate(1, (java.sql.Date) horario.getFecha()); //Fecha
                st3.setBoolean(2, horario.isJornada1()); //Jornada1
                st3.setBoolean(3, horario.isJornada2()); //Jornada2
                st3.setBoolean(2, horario.isJornada3()); //Jornada3
                st3.setBoolean(3, horario.isJornada4()); //Jornada4
                st3.setBoolean(3, horario.isJornada5()); //Jornada4
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

            PreparedStatement st4 = this.getConnection().prepareStatement("insert into laboratorio.solicitud ( codigo, tema, objetivo, fecha_registro, enabled, tipo, analisis, id_laboratorio, id_periodo, id_horarios, id_docente, pdf_resolucion, excel_estudiante,)  values (?,?,?,?,?,?,?,?,?,?,?,?,?);");
            st4.setString(1, solicitud.getCodigo());  //Codigo
            st4.setString(2, solicitud.getTema());  //Tema
            st4.setString(3, solicitud.getObjetivo()); //Objetivo
            st4.setDate(4, (java.sql.Date) fechaActualConvert); //Fecha Registro
            st4.setBoolean(5, !solicitud.isEnabled()); //Enabled
            st4.setString(6, solicitud.getTipo()); //Tipo
            st4.setString(7, solicitud.getAnalisis()); //Analisis
            st4.setInt(8, solicitud.getLaboratorio().getId()); //laboratorio
            st4.setString(9, solicitud.getPeriodo().getPeriodo()); //periodo
            st4.setLong(10, solicitud.getHorario().getId()); //horario
            st4.setInt(11, solicitud.getDocente().getId()); //docente
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

}
