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

    public void agregarAveria(Averia averia, List<Equipo> equiposIDS){
        this.conectar();
        String query = "select * from laboratorio.agregaraveriaequipo(?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setBoolean(2, averia.getEnabled());
            preparedStatement.setString(3, averia.getDescripcion());

            preparedStatement.executeQuery();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            this.desconectar();
        }
    }

    public void editarAveria(Averia averia){
        this.conectar();
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
                    reporteAverias.setFecha_registro(resultSet.getDate("fecha_registro"));
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



}
