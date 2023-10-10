package DAO;

import Model.Aula;
import Model.Laboratorio;
import global.Conexion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TecnicoLaboratorioDAO extends Conexion {
    public List<Laboratorio> listarPorFacultad(int idFacultad) {
        List<Laboratorio> laboratorios = new ArrayList<>();
        try {
            this.conectar();
            String sql = "SELECT * FROM laboratorio.laboratorio WHERE id_facultad = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idFacultad);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Laboratorio laboratorio = new Laboratorio();
                laboratorio.setId(resultSet.getInt("id"));
                laboratorio.setIdFacultad(resultSet.getInt("id_facultad"));
                laboratorio.setNombre(resultSet.getString("nom_laboratorio"));
                laboratorios.add(laboratorio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return laboratorios;
    }

    public List<Aula> listarAulaPorLaboratorio(int idLaboratorio) {
        List<Aula> aulas = new ArrayList<>();
        try {
            this.conectar();
            String sql = "SELECT *\n" +
                    "\tFROM laboratorio.laboratorio_aula la\n" +
                    "\tinner join laboratorio.aula ON aula.id_aula = la.id_aula\n" +
                    "\twhere la.id_laboratorio = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idLaboratorio);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Aula aula = new Aula();

                aula.setId(resultSet.getInt("id"));
                aula.setNombre(resultSet.getString("aula"));
                aula.setCapacidad(resultSet.getInt("capacidad"));

                aulas.add(aula);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aulas;
    }
}
