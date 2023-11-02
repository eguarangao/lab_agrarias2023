package DAO;

import Model.*;
import global.Conexion;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AveriaDAO extends Conexion {
    public List<Averia> listarAveriasPorLaboratorio(int LaboID) throws SQLException {
        List<Averia> ListAveria = new ArrayList<>();
        this.conectar();
        String query = "select * from laboratorio.listaraveriasquiposporlaboratorio(?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, LaboID);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    Averia averia = new Averia();
                    Equipo equipo = new Equipo();
                    Aula aula = new Aula();

                    averia.setId_averia(resultSet.getInt("id_averia"));
                    averia.setFecha_registro(resultSet.getDate("fecha_registro"));
                    averia.setDescripcion(resultSet.getString("descripcion"));
                    averia.setEnabled(resultSet.getBoolean("estado_averia"));
                    averia.setPrioridad(resultSet.getString("prioridad"));
                    averia.setProblema(resultSet.getString("problema"));

                    equipo.setId(resultSet.getInt("id_equipo"));
                    equipo.setDescripcion(resultSet.getString("descrip_equipo"));

                    aula.setId(resultSet.getInt("id_aula"));
                    aula.setNombre(resultSet.getString("aula"));

                    averia.setEquipo(equipo);
                    averia.setAula(aula);

                    ListAveria.add(averia);

                }
                System.out.println("Ya liste Averias");
            }
        } finally {
            this.desconectar();
        }
        return ListAveria;
    }

    public List<Equipo> listarEquiposPorLaboratorioAverias(int LaboID) throws SQLException {
        List<Equipo> Listequipos = new ArrayList<>();
        this.conectar();
        String query = "select * from laboratorio.listarequiposporlaboratorioquenoexistenenaveria(?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, LaboID);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    Equipo equipo = new Equipo();

                    equipo.setId(resultSet.getInt("id"));
                    equipo.setDescripcion(resultSet.getString("descripcion"));

                    Listequipos.add(equipo);
                }
                System.out.println("Ya liste equipos");
            }
        } finally {
            this.desconectar();
        }
        return Listequipos;
    }

    public void agregarAveria(Averia averia){
        this.conectar();
        String query = "select * from laboratorio.agregaraveria(?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, averia.getEquipo().getId());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setString(3, averia.getDescripcion());
            preparedStatement.setString(4,averia.getPrioridad());

            preparedStatement.executeQuery();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            this.desconectar();
        }
    }

    public void editarAveria(Averia averia){
        try  {
            this.conectar();
            String query = "SELECT laboratorio.editaraveria(?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, averia.getId_averia());
                preparedStatement.setString(2, averia.getDescripcion());
                preparedStatement.setString(3, averia.getPrioridad());

                preparedStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            this.desconectar();
        }
    }


    public List<ReporteAverias> listarAveriasPorLaboratorioString(int LaboID) throws SQLException {
        List<ReporteAverias> ListAveria = new ArrayList<>();
        this.conectar();
        String query = "select * from laboratorio.listaraveriasquiposporlaboratorio(?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, LaboID);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {

                    ReporteAverias reporteAverias = new ReporteAverias();

                    reporteAverias.setId_averia(resultSet.getInt("id_averia"));
                    reporteAverias.setFecha_registro(resultSet.getString("fecha_registro"));
                    reporteAverias.setDescripcion(resultSet.getString("descripcion"));
                    reporteAverias.setEstado_averia(resultSet.getString("estado_averia"));
                    reporteAverias.setId_equipo(resultSet.getInt("id_equipo"));
                    reporteAverias.setDescrip_equipo(resultSet.getString("descrip_equipo"));
                    reporteAverias.setId_aula(resultSet.getInt("id_aula"));
                    reporteAverias.setAula(resultSet.getString("aula"));

                    ListAveria.add(reporteAverias);

                }
                System.out.println("Ya liste Averias STRING");
            }
        } finally {
            this.desconectar();
        }
        return ListAveria;
    }

    public List<ReporteAverias> listarAveriaparareporte(int aveID, int UsuID) throws SQLException {
        List<ReporteAverias> ListAveria = new ArrayList<>();
        this.conectar();
        String query = "select * from laboratorio.listaraveriaparareporte(?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, aveID);
            preparedStatement.setInt(2, UsuID);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {

                    ReporteAverias reporteAverias = new ReporteAverias();

                    reporteAverias.setId_averia(resultSet.getInt("id_averia"));
                    reporteAverias.setFecha_registro(resultSet.getString("fecha_registro"));
                    reporteAverias.setDescripcion(resultSet.getString("descripcion"));
                    reporteAverias.setEstado_averia(resultSet.getString("estado_averia"));
                    reporteAverias.setPrioridad(resultSet.getString("prioridad"));
                    reporteAverias.setProblema(resultSet.getString("problema"));

                    reporteAverias.setId_equipo(resultSet.getInt("id_equipo"));
                    reporteAverias.setCodigo(resultSet.getString("codigo"));
                    reporteAverias.setDescrip_equipo(resultSet.getString("descrip_equipo"));
                    reporteAverias.setMarca(resultSet.getString("marca"));
                    reporteAverias.setModelo(resultSet.getString("modelo"));
                    reporteAverias.setNum_serie(resultSet.getString("num_serie"));
                    reporteAverias.setFecha_adquisicion(resultSet.getString("fecha_adquisicion"));
                    reporteAverias.setEstado(resultSet.getString("estado"));
                    reporteAverias.setAveria(resultSet.getString("averia"));

                    reporteAverias.setId_categoria_equipo(resultSet.getInt("id_categoria_equipo"));
                    reporteAverias.setCategoria(resultSet.getString("categoria"));

                    reporteAverias.setId_aula(resultSet.getInt("id_aula"));
                    reporteAverias.setAula(resultSet.getString("aula"));

                    reporteAverias.setId_laborat(resultSet.getInt("id_laborat"));
                    reporteAverias.setNom_laboratorio(resultSet.getString("nom_laboratorio"));

                    reporteAverias.setId_person(resultSet.getInt("id_person"));
                    reporteAverias.setNombre(resultSet.getString("nombre"));
                    reporteAverias.setApellido(resultSet.getString("apellido"));

                    reporteAverias.setId_facultad(resultSet.getInt("id_facultad"));
                    reporteAverias.setFacultad(resultSet.getString("facultad"));

                    ListAveria.add(reporteAverias);

                }
                System.out.println("Ya liste la Averia del TECNICO");
                System.out.println(ListAveria);
            }
        } finally {
            this.desconectar();
        }
        return ListAveria;
    }

    public void ConfirmarAveriaRealizada(Averia averia) {
        try  {
            this.conectar();
            String query = "SELECT laboratorio.averiarealizadaaequipo(?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, averia.getId_averia());
                preparedStatement.setInt(2, averia.getEquipo().getId());
                preparedStatement.setBoolean(3, averia.getEquipo().getEstadoAveriaEquipo());
                preparedStatement.setString(4, averia.getProblema());

                preparedStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            this.desconectar();
        }
    }

    public boolean verificarEstadoAveriaEquipo(Averia averia) {
        boolean resultado = false;
        try {
            this.conectar();
            String query = "SELECT laboratorio.verificaraveriaequipo(?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, averia.getEquipo().getId());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        resultado = resultSet.getBoolean(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.desconectar();
        }

        return resultado;
    }



}
