package DAO;

import Model.*;
import global.Conexion;
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

    HorarioDAO horarioDAO = new HorarioDAO();
    List<Item> itemsSeleccionados = new ArrayList<>();


    public void save(Solicitud solicitud, UploadedFile pdfResolucion, UploadedFile listaEstudiantes) throws SQLException {
        int idHorario = 0;
        int idSolicitudMax = 0;
        try {
            this.conectar();
            this.getConnection().setAutoCommit(false);

            LocalDateTime fechaActual = LocalDateTime.now();
            Date fechaActualConvert = Date.from(fechaActual.atZone(ZoneId.systemDefault()).toInstant());

            //Supongamos que horario.getFecha() devuelve un java.util.Date
            java.util.Date fechaUtil = solicitud.getHorario().getFecha();
            java.sql.Date fechaSql = new java.sql.Date(fechaUtil.getTime());

            System.out.println("esta es la fecha");

            System.out.println(fechaSql.toString());

            //EN CASO EXISTA EL HORARIO SE ACTUALIZA LAS JORNADAS

            if (horarioDAO.existeHorario(solicitud.getLaboratorio().getId(), fechaSql.toString())) {
                System.out.println("Se hace update");

                int idHor = horarioDAO.gerIdHorario(solicitud.getLaboratorio().getId(), fechaSql.toString());
                //   REGISTRA LA SOLICITUD
                String sqlRegistrarSolicitud = "insert into laboratorio.solicitud (codigo, \n" +
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
                PreparedStatement st0 = this.getConnection().prepareStatement(sqlRegistrarSolicitud);
                st0.setString(1, solicitud.getCodigo());  //Codigo
                st0.setString(2, solicitud.getTema());  //Tema
                st0.setString(3, solicitud.getObjetivo()); //Objetivo

                //Configuramos la fecha del sistema actual
                LocalDateTime fechaSistema = LocalDateTime.now();
                Date fechaRegistro = Date.from(fechaActual.atZone(ZoneId.systemDefault()).toInstant());

                java.util.Date fecha = fechaRegistro;
                java.sql.Date fechaActualSql = new java.sql.Date(fecha.getTime());

                st0.setDate(4, fechaActualSql); //Fecha Registro
                st0.setBoolean(5, !solicitud.isEnabled()); //Enabled
                st0.setString(6, solicitud.getTipo()); //Tipo de Solicitud
                st0.setString(7, solicitud.getAnalisis()); //Analisis
                st0.setInt(8, solicitud.getLaboratorio().getId()); //laboratorio
                st0.setInt(9, solicitud.getPeriodo().getId()); //periodo
                st0.setLong(10, idHor); //horario
                //st0.setInt(11, solicitud.getDocente().getId()); //docente
                st0.setInt(11, solicitud.getDocente().getId()); //docente
                st0.setBinaryStream(12, pdfResolucion.getInputStream());
                st0.setBinaryStream(13, listaEstudiantes.getInputStream());
                st0.setBinaryStream(14, pdfResolucion.getInputStream());
                st0.executeUpdate();
                st0.close();


                PreparedStatement st1 = this.getConnection().prepareStatement("update laboratorio.horario set jornada1=?, jornada2=?, jornada3=?, jornada4=?, jornada5=? , jornada6=? , jornada7=? ,  jornada8=? where fecha= '" + fechaSql + "'");
                st1.setBoolean(1, solicitud.getHorario().isJornada1()); //Jornada1
                st1.setBoolean(2, solicitud.getHorario().isJornada2()); //Jornada2
                st1.setBoolean(3, solicitud.getHorario().isJornada3()); //Jornada3
                st1.setBoolean(4, solicitud.getHorario().isJornada4()); //Jornada4
                st1.setBoolean(5, solicitud.getHorario().isJornada5()); //Jornada5
                st1.setBoolean(6, solicitud.getHorario().isJornada6()); //Jornada6
                st1.setBoolean(7, solicitud.getHorario().isJornada7()); //Jornada7
                st1.setBoolean(8, solicitud.getHorario().isJornada8()); //Jornada8
                st1.executeUpdate();
                st1.close();
                System.out.println("SE ACTUALIZÓ CORRECTAMENTE");
            } else {
                // REGISTRA HORARIO
                String sqlInsertSolicitud = "insert into laboratorio.horario (fecha, jornada1,  jornada2, jornada3, jornada4, jornada5,  jornada6, jornada7, jornada8, id_laboratorio) values (?,?,?,?,?,?,?,?,?,?);";
                PreparedStatement st2 = this.getConnection().prepareStatement(sqlInsertSolicitud);

                // Luego, asigna fechaSql a tu PreparedStatement
                st2.setDate(1, fechaSql);
                st2.setBoolean(2, solicitud.getHorario().isJornada1()); // Asigna Jornada #1
                st2.setBoolean(3, solicitud.getHorario().isJornada2()); // Asigna Jornada #2
                st2.setBoolean(4, solicitud.getHorario().isJornada3()); // Asigna Jornada #3
                st2.setBoolean(5, solicitud.getHorario().isJornada4()); // Asigna Jornada #4
                st2.setBoolean(6, solicitud.getHorario().isJornada5()); // Asigna Jornada #5
                st2.setBoolean(7, solicitud.getHorario().isJornada6()); // Asigna Jornada #6
                st2.setBoolean(8, solicitud.getHorario().isJornada7()); // Asigna Jornada #7
                st2.setBoolean(9, solicitud.getHorario().isJornada8()); // Asigna Jornada #8
                st2.setInt(10, solicitud.getLaboratorio().getId());     // Asigna ID de laboratorio

                st2.executeUpdate();

                st2.close();

                // OBTENER EL ÚLTIMO ID DE HORARIO
                PreparedStatement st3 = this.getConnection().prepareStatement("SELECT MAX(id) FROM laboratorio.horario;");
                ResultSet rsS = st3.executeQuery();
                if (rsS.next()) {
                    idHorario = rsS.getInt(1);
                    Horario horario = solicitud.getHorario();
                    horario.setId(idHorario);
                    solicitud.setHorario(horario);
                }
                st3.close();


                //   REGISTRA LA SOLICITUD
                String sqlRegistrarSolicitud = "insert into laboratorio.solicitud (codigo, \n" +
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

                st4.setDate(4, fechaActualSql); //Fecha Registro
                st4.setBoolean(5, !solicitud.isEnabled()); //Enabled
                st4.setString(6, solicitud.getTipo()); //Tipo de Solicitud
                st4.setString(7, solicitud.getAnalisis()); //Analisis
                st4.setInt(8, solicitud.getLaboratorio().getId()); //laboratorio
                st4.setInt(9, solicitud.getPeriodo().getId()); //periodo
                st4.setLong(10, solicitud.getHorario().getId()); //horario
                //st4.setInt(11, solicitud.getDocente().getId()); //docente
                st4.setInt(11, solicitud.getDocente().getId()); //docente
                st4.setBinaryStream(12, pdfResolucion.getInputStream());
                st4.setBinaryStream(13, listaEstudiantes.getInputStream());
                st4.setBinaryStream(14, pdfResolucion.getInputStream());
                st4.executeUpdate();
                st4.close();

                // OBTENER EL ÚLTIMO ID DE SOLICITUD
                PreparedStatement st5 = this.getConnection().prepareStatement("SELECT MAX(id) FROM laboratorio.solicitud;");
                ResultSet rs = st5.executeQuery();
                if (rs.next()) {
                    idSolicitudMax = rs.getInt(1);
                }
                rs.close();
                st5.close();

                //REGISTRAR LOS EQUIPOS DE LABORATORIO
                for (int i = 0; i < solicitud.getEquipos().size(); i++) {
                    System.out.println(idSolicitudMax);
                    int idEquipo = solicitud.getEquipos().get(i).getId();
                    PreparedStatement st6 = this.getConnection().prepareStatement("insert into laboratorio.equipo_solicitud  (id_equipo, id_solicitud) values (?,?);");
                    st6.setInt(1, idEquipo);  //idEquipo
                    st6.setInt(2, idSolicitudMax);  //idSolicitud
                    st6.executeUpdate();
                    st6.close();

                    // Hacer algo con el objeto "equipo" en cada iteración
                }
                System.out.println("SE GUARDO CORRECTAMENTE");
                this.getConnection().commit(); // Commit de la transacción si todo sale bien
            }
        } catch (Exception e) {
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
    }

//    public List<Solicitud> findAll(int idDocente) throws SQLException {
//
//        List<Solicitud> listaSolicitudes = new ArrayList<>();
//        List<Equipo> listaEquipos = new ArrayList<>();
//        ResultSet rs;
//        try {
//            this.conectar();
//            String sql = "SELECT solicitud.id               AS id,\n" +
//                    "       solicitud.tipo             AS tipo_solicitud,\n" +
//                    "       solicitud.analisis         AS analisis,\n" +
//                    "       solicitud.codigo           AS codigo,\n" +
//                    "       solicitud.excel_estudiante AS lista_estudiantes,\n" +
//                    "       solicitud.pdf_resolucion   AS pdf_resolucion,\n" +
//                    "       solicitud.fecha_registro   AS fecha_registro,\n" +
//                    "       solicitud.tema             AS tema,\n" +
//                    "       solicitud.enabled          AS enabled,\n" +
//                    "       e.codigo                   AS codigo_equipo,\n" +
//                    "       e.id                       AS id_equipo,\n" +
//                    "       e.descripcion              AS nombre_equipo,\n" +
//                    "       l.id                       AS id_laboratorio,\n" +
//                    "       l.nom_laboratorio          AS nombre_laboratorio\n" +
//                    "FROM laboratorio.solicitud\n" +
//                    "         INNER JOIN laboratorio.docente d ON d.id = solicitud.id_docente\n" +
//                    "         INNER JOIN laboratorio.horario h ON h.id = solicitud.id_horarios\n" +
//                    "         INNER JOIN laboratorio.equipo_solicitud es ON solicitud.id = es.id_solicitud\n" +
//                    "         INNER JOIN laboratorio.equipo e ON e.id = es.id_equipo\n" +
//                    "         INNER JOIN laboratorio.laboratorio l ON h.id_laboratorio = l.id\n" +
//                    "         INNER JOIN laboratorio.persona p ON d.id_persona = p.id\n" +
//                    "         INNER JOIN laboratorio.usuario u ON p.id = u.id_persona\n" +
//                    "WHERE u.id = ?\n" +
//                    "  and solicitud.enabled = true\n" +
//                    "ORDER BY solicitud.fecha_registro DESC;";
//            PreparedStatement st = this.getConnection().prepareStatement(sql);
//            st.setInt(1, idDocente);
//            rs = st.executeQuery();
//
//            List<Equipo> equipos = new ArrayList<>();
//
//            while (rs.next()) {
//
//                Solicitud solicitud = new Solicitud();
//                Equipo equipo= new Equipo();
//                Docente docente = new Docente();
//                Laboratorio laboratorio = new Laboratorio();
//
//                //Lleno solicitud
//                solicitud.setId(rs.getInt("id"));
//                solicitud.setTipo(rs.getString("tipo_solicitud"));
//                solicitud.setCodigo(rs.getString("codigo"));
//                solicitud.setAnalisis(rs.getString("analisis"));
//                solicitud.setTema(rs.getString("tema"));
//                solicitud.setFechaReserva(rs.getDate("fecha_registro"));
//
//                //Equipo
//                equipo.setId(rs.getInt("id_equipo"));
//                equipo.setDescripcion(rs.getString("nombre_equipo"));
//                equipo.setCodigo(rs.getString("codigo_equipo"));
//
//                listaEquipos.add(equipo);
//
//                solicitud.setEquipos(equipos);
//
//
//                //Lleno docente
//
//                //Lleno laboratorio
//                laboratorio.setId(rs.getInt("id_laboratorio"));
//                laboratorio.setNombre(rs.getString("nombre_laboratorio"));
//
//                solicitud.setLaboratorio(laboratorio);
//
//                //Asigna laboratorio
//                listaEquipos.add(equipo);
//                listaSolicitudes.add(solicitud);
//
//            }
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            this.desconectar();
//        }
//
//        return listaSolicitudes;
//    }


    public List<Solicitud> findAll(int idDocente) throws SQLException {
        List<Solicitud> listaSolicitudes = new ArrayList<>();
        ResultSet rs;

        try {
            this.conectar();
            String sql = "SELECT solicitud.id               AS id,\n" +
                    "       solicitud.tipo             AS tipo_solicitud,\n" +
                    "       solicitud.analisis         AS analisis,\n" +
                    "       solicitud.codigo           AS codigo,\n" +
                    "       solicitud.excel_estudiante AS lista_estudiantes,\n" +
                    "       solicitud.pdf_resolucion   AS pdf_resolucion,\n" +
                    "       solicitud.fecha_registro   AS fecha_registro,\n" +
                    "       solicitud.tema             AS tema,\n" +
                    "       solicitud.enabled          AS enabled,\n" +
                    "       e.codigo                   AS codigo_equipo,\n" +
                    "       e.id                       AS id_equipo,\n" +
                    "       e.descripcion              AS nombre_equipo,\n" +
                    "       l.id                       AS id_laboratorio,\n" +
                    "       l.nom_laboratorio          AS nombre_laboratorio,\n" +
                    "       ce.categoria               as categoria_equipo\n" +
                    "FROM laboratorio.solicitud\n" +
                    "         INNER JOIN laboratorio.docente d ON d.id = solicitud.id_docente\n" +
                    "         INNER JOIN laboratorio.horario h ON h.id = solicitud.id_horarios\n" +
                    "         INNER JOIN laboratorio.equipo_solicitud es ON solicitud.id = es.id_solicitud\n" +
                    "         INNER JOIN laboratorio.equipo e ON e.id = es.id_equipo\n" +
                    "         INNER JOIN laboratorio.laboratorio l ON h.id_laboratorio = l.id\n" +
                    "         INNER JOIN laboratorio.persona p ON d.id_persona = p.id\n" +
                    "         INNER JOIN laboratorio.usuario u ON p.id = u.id_persona\n" +
                    "         INNER JOIN laboratorio.categoria_equipo ce on ce.id_categoria = e.id_categoria_equipo\n" +
                    "WHERE u.id = ?\n" +
                    "  and solicitud.enabled = true\n" +
                    "ORDER BY solicitud.fecha_registro DESC;";
            PreparedStatement st = this.getConnection().prepareStatement(sql);
            st.setInt(1, idDocente);
            rs = st.executeQuery();

            while (rs.next()) {
                int solicitudId = rs.getInt("id");
                Solicitud solicitud = null;

                // Buscar si la solicitud ya existe en la lista de solicitudes
                for (Solicitud existingSolicitud : listaSolicitudes) {
                    if (existingSolicitud.getId() == solicitudId) {
                        solicitud = existingSolicitud;
                        break;
                    }
                }

                // Si no existe, crea una nueva solicitud
                if (solicitud == null) {
                    solicitud = new Solicitud();
                    // Llena los campos comunes de la solicitud
                    solicitud.setId(solicitudId);
                    solicitud.setTipo(rs.getString("tipo_solicitud"));
                    solicitud.setCodigo(rs.getString("codigo"));
                    solicitud.setAnalisis(rs.getString("analisis"));
                    solicitud.setTema(rs.getString("tema"));
                    solicitud.setFechaReserva(rs.getDate("fecha_registro"));

                    // Crea una nueva lista de equipos para esta solicitud
                    solicitud.setEquipos(new ArrayList<>());

                    // Crea un nuevo laboratorio y asigna los datos
                    Laboratorio laboratorio = new Laboratorio();
                    laboratorio.setId(rs.getInt("id_laboratorio"));
                    laboratorio.setNombre(rs.getString("nombre_laboratorio"));
                    solicitud.setLaboratorio(laboratorio);


                    listaSolicitudes.add(solicitud);
                }

                // Llena los campos del equipo
                Equipo equipo = new Equipo();
                equipo.setId(rs.getInt("id_equipo"));
                equipo.setDescripcion(rs.getString("nombre_equipo"));
                equipo.setCodigo(rs.getString("codigo_equipo"));

                //Creau una categoria Equipo y a asigna a EQUIPO
                CategoriaEquipo categoriaEquipo = new CategoriaEquipo();
                categoriaEquipo.setNombre(rs.getString("categoria_equipo"));
                equipo.setCategoriaEquipo(categoriaEquipo);


                // Agrega el equipo a la lista de equipos de la solicitud
                solicitud.getEquipos().add(equipo);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            this.desconectar();
        }
        System.out.println("LISTA$");
        System.out.println(listaSolicitudes);

        return listaSolicitudes;
    }

    public void delete(int idSolicitud) {
        try {
            this.conectar();
            this.getConnection().setAutoCommit(false);

            //ELIMINAR EQUIPO_SOLICITUD
            String sql = "DELETE FROM laboratorio.equipo_solicitud WHERE id_solicitud=?";
            PreparedStatement st = this.getConnection().prepareStatement(sql);
            st.setInt(1, idSolicitud);
            st.executeUpdate();
            st.close(); // Cierra la declaración


            //ELIMINAR SOLICITUD
            String sqlSolicitud = "DELETE FROM laboratorio.solicitud WHERE id=?";
            PreparedStatement st2 = this.getConnection().prepareStatement(sqlSolicitud);
            st2.setInt(1, idSolicitud);
            st2.executeUpdate();
            st2.close(); // Cierra la declaración

            this.getConnection().commit(); // Commit de la transacción si todo sale bien
        } catch (Exception e) {
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
    }


}
