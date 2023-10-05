package DAO;

import Model.Facultad;
import global.Conexion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//se realizan un CRUD en donde se involucran las tablas Facultad y Periodo
//dichas tablas no estan entrelazadas
public class FacultadDAO extends Conexion {

    public void insert(Facultad facultad) throws SQLException {
        try {
            this.conectar();
            String sql = "INSERT INTO laboratorio.facultad ( facultad, correo, descripcion,fecha_creacion) VALUES ( ?, ?, ?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, facultad.getNombreFacd());
            statement.setString(2, facultad.getCorreoFacd());
            statement.setString(3, facultad.getDescripcionFacd());
            statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));

            statement.executeUpdate();
            statement.close();
        } finally {
            this.desconectar();

        }


    }
    public void update(Facultad facultad) throws SQLException {
        try {
            this.conectar();
            String sql = "UPDATE laboratorio.facultad SET facultad = ?, correo = ?, descripcion = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, facultad.getNombreFacd());
            statement.setString(2, facultad.getCorreoFacd());
            statement.setString(3, facultad.getDescripcionFacd());
            statement.setInt(4, facultad.getIdFacd()); // Suponiendo que hay un método getIdFacd() en la clase Facultad.

            statement.executeUpdate();
            statement.close();
        } finally {
            this.desconectar();
        }
    }

    public void delete(int id) throws SQLException {
        try {
            this.conectar();
            String sql = "DELETE FROM laboratorio.facultad WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
            statement.close();
        } finally {
            this.desconectar();
        }
    }


    public List<Facultad> listarFacultades() throws SQLException {
        List<Facultad> facultades = new ArrayList<>();
        PreparedStatement statement ;
        ResultSet resultSet ;
        try {
            this.conectar();
            String sql = "SELECT * FROM laboratorio.facultad";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Facultad facultad = new Facultad();
                facultad.setIdFacd(resultSet.getInt("id"));
                facultad.setNombreFacd(resultSet.getString("facultad"));
                facultad.setCorreoFacd(resultSet.getString("correo"));
                facultad.setDescripcionFacd(resultSet.getString("descripcion"));
                facultad.setFechaCreacion(resultSet.getTimestamp("fecha_creacion"));

                facultades.add(facultad);
            }

        } finally {
           if (isEstado()){
               this.desconectar();
           }

        }
        return facultades;
    }
}
