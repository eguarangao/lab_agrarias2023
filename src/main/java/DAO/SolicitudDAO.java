package DAO;

import Model.*;
import global.Conexion;
import org.primefaces.component.fileupload.FileUpload;
import org.primefaces.model.file.UploadedFile;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SolicitudDAO extends Conexion {
    Horario tempHorario = new Horario();
    List<Item> itemsSeleccionados = new ArrayList<>();

    public void save(Solicitud solicitud, Horario horario, List<Equipo> equipos, String fecha, List<Item> items) throws SQLException {
        itemsSeleccionados = items;
        System.out.println(solicitud + "DAO");
        try {
            this.conectar();
            this.getConnection().setAutoCommit(false);

            int idHorario = 0;
            int idSolicitud = 0;
            int idPeriodo = 0;
            int idDocente = 0;

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
                PreparedStatement st1 = this.getConnection().prepareStatement("update laboratorio.horario set jornada1=?, jornada2=?, jornada3=?, jornada4=?, jornada5=? where fecha= '" + fecha + "'");
//                asiganarUpdateItems(idItems);
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


//                tempHorario2 = asiganarNewItems(idItems);
                PreparedStatement st3 = this.getConnection().prepareStatement("insert into laboratorio.horario (fecha, jornada1,  jornada2, jornada3, jornada4, jornada5,jornada6,jornada7,jornada8, id_laboratorio)\n" +
                        "values (?,?,?,?,?,?,?,?,?);");
                st3.setDate(1, (java.sql.Date) horario.getFecha()); //Fecha
                st3.setBoolean(2, horario.isJornada1()); //Jornada1
                st3.setBoolean(3, horario.isJornada1()); //Jornada2
                st3.setBoolean(4, horario.isJornada1()); //Jornada3
                st3.setBoolean(5, horario.isJornada1()); //Jornada4
                st3.setBoolean(6, horario.isJornada1()); //Jornada5
                st3.setBoolean(7, horario.isJornada1()); //Jornada6
                st3.setBoolean(8, horario.isJornada1()); //Jornada7
                st3.setBoolean(9, horario.isJornada1()); //Jornada8
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
            st4.setInt(9, 1); //periodo
            st4.setLong(10, solicitud.getHorario().getId()); //horario
//            st4.setInt(11, solicitud.getDocente().getId()); //docente
            st4.setInt(11, 1); //docente
//            st4.setBinaryStream(12, solicitud.getPdfResolucion().getBytes());
//            st4.setBinaryStream(13, solicitud.getExcelEstudiantes().getBytes());
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

//            for (int i = 0; i < equipos.size(); i++) {
//                Equipo equipo = equipos.get(i);
//                PreparedStatement stO = this.getConnection().prepareStatement("insert into laboratorio.equipo_solicitud (id_equipo, id_solicitud)\n" +
//                        "values (?,?);");
//
//                st4.setInt(1, equipos.get(i).getId());  //idEquipo
//
//                st4.setInt(2, idSolicitud);  //idSolicitud
//                st4.close();
//
//                // Hacer algo con el objeto "equipo" en cada iteración
//            }
//            this.getConnection().commit();


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
//public Horario asiganarNewItems(int idItems) {
//    Horario myHorario = new Horario();
//    switch (idItems) {
//        case 1:
//            myHorario.setJornada1(true);
//            myHorario.setJornada2(false);
//            myHorario.setJornada3(false);
//            myHorario.setJornada4(false);
//            myHorario.setJornada5(false);
//        case 2:
//            myHorario.setJornada1(false);
//            myHorario.setJornada2(true);
//            myHorario.setJornada3(false);
//            myHorario.setJornada4(false);
//            myHorario.setJornada5(false);
//        case 3:
//            myHorario.setJornada1(false);
//            myHorario.setJornada2(false);
//            myHorario.setJornada3(true);
//            myHorario.setJornada4(false);
//            myHorario.setJornada5(false);
//        case 4:
//            myHorario.setJornada1(false);
//            myHorario.setJornada2(false);
//            myHorario.setJornada3(false);
//            myHorario.setJornada4(true);
//            myHorario.setJornada5(false);
//        case 5:
//            myHorario.setJornada1(false);
//            myHorario.setJornada2(false);
//            myHorario.setJornada3(false);
//            myHorario.setJornada4(false);
//            myHorario.setJornada5(true);
//    }
//    return myHorario;
//}

//    public Horario asiganarNewItems(int idItems) {
//        Horario myHorario = new Horario();
//
//
//        myHorario.setJornada1(itemsSeleccionados.get(0).isDato());
//        myHorario.setJornada2(itemsSeleccionados.get(1).isDato());
//        myHorario.setJornada3(itemsSeleccionados.get(2).isDato());
//        myHorario.setJornada4(itemsSeleccionados.get(3).isDato());
//        myHorario.setJornada5(itemsSeleccionados.get(4).isDato());
//        myHorario.setJornada6(itemsSeleccionados.get(5).isDato());
//        myHorario.setJornada7(itemsSeleccionados.get(6).isDato());
//        myHorario.setJornada8(itemsSeleccionados.get(7).isDato());
//        myHorario.setFecha();
//
//
//
// }


//    public void save2(Solicitud solicitud, Horario horario,  int idLaboratorio,String tipoSolicitud) throws SQLException {
//        System.out.println(solicitud + "DAO");
//        try {
//            this.conectar();
//            this.getConnection().setAutoCommit(false);
//
//            int idHorario = 0;
//            int idSolicitud = 0;
//            int idPeriodo=0;
//            int idDocente=0;
//
//            boolean existeHorario;
//
//            LocalDateTime fechaActual = LocalDateTime.now();
//            Date fechaActualConvert = Date.from(fechaActual.atZone(ZoneId.systemDefault()).toInstant());
//
//            String sql = "select * from laboratorio.horario where fecha=?";
//
//
//
//
//            //REGISTRA HORARIO
//            String sqlInsertSolicitud ="insert into laboratorio.horario ( fecha, jornada1,  jornada2, jornada3, jornada4, jornada5,  jornada6, jornada7, jornada8, id_laboratorio)\n" +
//                    "values (?,?,?,?,?,?,?,?,?,?);";
//
//            PreparedStatement stRS = this.getConnection().prepareStatement(sqlInsertSolicitud);
//            stRS.setDate(1, (java.sql.Date) horario.getFecha()); //Asigna fecha de regsitro
//            stRS.setBoolean(2,horario.isJornada1()); //Asigna Jornada #1
//            stRS.setBoolean(3,horario.isJornada2()); //Asigna Jornada #2
//            stRS.setBoolean(4,horario.isJornada3()); //Asigna Jornada #3
//            stRS.setBoolean(5,horario.isJornada4()); //Asigna Jornada #4
//            stRS.setBoolean(6,horario.isJornada5()); //Asigna Jornada #5
//            stRS.setBoolean(7,horario.isJornada6()); //Asigna Jornada #6
//            stRS.setBoolean(8,horario.isJornada7()); //Asigna Jornada #7
//            stRS.setBoolean(9,horario.isJornada8()); //Asigna Jornada #8
//            stRS.setInt(10, idLaboratorio); //Asigna ID de laboratorio
//            stRS.executeUpdate();
//s
//            stRS.close();
//
//            //OBTENER EL ÚLTIMO ID DE HORARIO
//            PreparedStatement stMAX = this.getConnection().prepareStatement("SELECT MAX(id) FROM laboratorio.horario;");
//            ResultSet rsS = stMAX.executeQuery();
//            if (rsS.next()) {
//                idHorario = rsS.getInt(1);
//                // hacer algo con el valor de idHorario
//            }
//
//
//
//            //REGISTRA LA SOLICITUD
//           String sqlRegistrarSolicitud="insert into laboratorio.solicitud (codigo, \n" +
//                   "                                   tema, \n" +
//                   "                                   objetivo, \n" +
//                   "                                   fecha_registro, \n" +
//                   "                                   enabled, \n" +
//                   "                                   tipo, \n" +
//                   "                                   analisis, \n" +
//                   "                                   id_laboratorio,\n" +
//                   "                                   id_periodo,\n" +
//                   "                                   id_horarios, \n" +
//                   "                                   id_docente, \n" +
//                   "                                   pdf_resolucion, \n" +
//                   "                                   excel_estudiante, \n" +
//                   "                                   pdf_aprobacion)\n" +
//                   "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
//            PreparedStatement st4 = this.getConnection().prepareStatement(sqlRegistrarSolicitud);
//            st4.setString(1, solicitud.getCodigo());  //Codigo
//            st4.setString(2, solicitud.getTema());  //Tema
//            st4.setString(3, solicitud.getObjetivo()); //Objetivo
//            st4.setDate(4, (java.sql.Date) fechaActualConvert); //Fecha Registro
//            st4.setBoolean(5, !solicitud.isEnabled()); //Enabled
//            st4.setString(6,tipoSolicitud); //Tipo
//            st4.setString(7, solicitud.getAnalisis()); //Analisis
//            st4.setInt(8, idLaboratorio); //laboratorio
//            st4.setInt(9, 1); //periodo
//            st4.setLong(10, idHorario); //horario
////            st4.setInt(11, solicitud.getDocente().getId()); //docente
//            st4.setInt(11, 1); //docente
//            st4.setBytes(12, solicitud.getPdfResolucion().getBytes());
//            st4.setBytes(13, solicitud.getExcelEstudiantes().getBytes());
//            st4.executeUpdate();
//
//            st4.close();
//
//
//        } catch (Exception e) {
//            System.out.println("Error...Ebert");
//            e.getMessage();
//            this.getConnection().rollback();
//        } finally {
//            this.desconectar();
//        }
//    }
//


    public void save2(Solicitud solicitud, Horario horario, int idLaboratorio, String tipoSolicitud,  UploadedFile pdfResolucion, UploadedFile listaEstudiantes) throws SQLException {
        System.out.println(horario + "DAO");
        int idHorario = 0;
        try {
            this.conectar();
            this.getConnection().setAutoCommit(false);

            LocalDateTime fechaActual = LocalDateTime.now();
            Date fechaActualConvert = Date.from(fechaActual.atZone(ZoneId.systemDefault()).toInstant());

            // REGISTRA HORARIO
            String sqlInsertSolicitud = "insert into laboratorio.horario (fecha, jornada1,  jornada2, jornada3, jornada4, jornada5,  jornada6, jornada7, jornada8, id_laboratorio) values (?,?,?,?,?,?,?,?,?,?);";

            PreparedStatement stRS = this.getConnection().prepareStatement(sqlInsertSolicitud);

            // Supongamos que horario.getFecha() devuelve un java.util.Date
            java.util.Date fechaUtil = horario.getFecha();
            java.sql.Date fechaSql = new java.sql.Date(fechaUtil.getTime());

            // Luego, asigna fechaSql a tu PreparedStatement
            stRS.setDate(1, fechaSql);
            System.out.println("fecha: " + fechaSql);

            stRS.setBoolean(2, horario.isJornada1()); // Asigna Jornada #1
            stRS.setBoolean(3, horario.isJornada2()); // Asigna Jornada #2
            stRS.setBoolean(4, horario.isJornada3()); // Asigna Jornada #3
            stRS.setBoolean(5, horario.isJornada4()); // Asigna Jornada #4
            stRS.setBoolean(6, horario.isJornada5()); // Asigna Jornada #5
            stRS.setBoolean(7, horario.isJornada6()); // Asigna Jornada #6
            stRS.setBoolean(8, horario.isJornada7()); // Asigna Jornada #7
            stRS.setBoolean(9, horario.isJornada8()); // Asigna Jornada #8
            stRS.setInt(10, idLaboratorio); // Asigna ID de laboratorio
            stRS.executeUpdate();

            stRS.close();

            // OBTENER EL ÚLTIMO ID DE HORARIO
            PreparedStatement stMAX = this.getConnection().prepareStatement("SELECT MAX(id) FROM laboratorio.horario;");
            ResultSet rsS = stMAX.executeQuery();
            if (rsS.next()) {
                idHorario = rsS.getInt(1);
                horario.setId(idHorario);
                // hacer algo con el valor de idHorario
            }
            stMAX.close();


            //   REGISTRA LA SOLICITUD
           String sqlRegistrarSolicitud="insert into laboratorio.solicitud (codigo, \n" +
                   "                                   tema, \n" +
                   "                                   objetivo, \n" +
                   "                                   fecha_registro, \n" +
                   "                                   enabled, \n" +
                   "                                   tipo, \n" +
                   "                                   analisis, \n" +
                   "                                   id_laboratorio,\n" +
                   "                                   id_periodo,\n" +
                   "                                   id_horarios, \n" +
                   "                                   id_docente, \n" +
                   "                                   pdf_resolucion, \n" +
                   "                                   excel_estudiante, \n" +
                   "                                   pdf_aprobacion)\n" +
                   "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
            PreparedStatement st4 = this.getConnection().prepareStatement(sqlRegistrarSolicitud);
            st4.setString(1, solicitud.getCodigo());  //Codigo
            st4.setString(2, solicitud.getTema());  //Tema
            st4.setString(3, solicitud.getObjetivo()); //Objetivo
            //Configuramos la fecha del sistema actual
            LocalDateTime fechaSistema = LocalDateTime.now();
            Date fechaRegistro = Date.from(fechaActual.atZone(ZoneId.systemDefault()).toInstant());

            java.util.Date fecha = fechaRegistro;
            java.sql.Date fechaActualSql = new java.sql.Date(fecha.getTime());

            st4.setDate(4,fechaActualSql); //Fecha Registro
            st4.setBoolean(5, !solicitud.isEnabled()); //Enabled
            st4.setString(6,tipoSolicitud); //Tipo
            st4.setString(7, solicitud.getAnalisis()); //Analisis
            st4.setInt(8, idLaboratorio); //laboratorio
            st4.setInt(9, 1); //periodo
            st4.setLong(10, horario.getId()); //horario
//            st4.setInt(11, solicitud.getDocente().getId()); //docente
            st4.setInt(11, 1); //docente
            st4.setBinaryStream(12, pdfResolucion.getInputStream());
            st4.setBinaryStream(13, listaEstudiantes.getInputStream());
            st4.setBinaryStream(14, pdfResolucion.getInputStream());
            System.out.println("este es el excell");
            System.out.println(solicitud.getExcelEstudiantes().getBytes());
            st4.executeUpdate();
            st4.close();


            this.getConnection().commit(); // Commit de la transacción si todo sale bien

        } catch (Exception e) {
            System.out.println("Error...Ebert");
            System.out.println("ERROR:" + e.getMessage());
            e.printStackTrace(); // Imprime la traza de la excepción para depurar
            try {
                this.getConnection().rollback(); // Rollback de la transacción si ocurre una excepción
            } catch (SQLException rollbackEx) {
                System.err.println("Rollback fallido: " + rollbackEx.getMessage());
            }
        } finally {
            try {
                this.getConnection().setAutoCommit(true); // Restablece el auto-commit a true
                this.desconectar();
            } catch (SQLException closeEx) {
                System.err.println("Error al cerrar la conexión: " + closeEx.getMessage());
            }
        }
        System.out.println("id horario nuevo: " + idHorario);
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
