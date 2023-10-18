package DAO;

import Model.*;
import global.Conexion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public void agregarAveria(Averia averia){
        this.conectar();

    }

    public void editarAveria(Averia averia){
        this.conectar();
    }

}
