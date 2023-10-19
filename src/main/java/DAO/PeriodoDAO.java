package DAO;

import Model.Periodo;
import global.Conexion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PeriodoDAO extends Conexion {
    public void insertPeriodo(Periodo periodo) {
        this.conectar();
        String sql = "INSERT INTO laboratorio.periodo (name_periodo,enabled,fecha_creacion) VALUES (?,?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, periodo.getNamePeriodo());
            preparedStatement.setBoolean(2, periodo.isEnabled());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            this.desconectar();
        }
    }
    public void updatePeriodo(Periodo periodo) {
        try  {
            this.conectar();
            String sql = "UPDATE laboratorio.periodo SET name_periodo = ?, enabled = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, periodo.getNamePeriodo());
                preparedStatement.setBoolean(2, periodo.isEnabled());
                preparedStatement.setInt(3, periodo.getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            this.desconectar();
        }
    }
    public void deletePeriodo(int id) {
        try {
            this.conectar();
            String sql = "DELETE FROM laboratorio.periodo WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            this.desconectar();
        }
    }
    public List<Periodo> listarPeriodos() {
        List<Periodo> periodos = new ArrayList<>();

        try  {
            this.conectar();
            String sql = "SELECT * FROM laboratorio.periodo";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Periodo periodoObj = new Periodo();
                        periodoObj.setId(resultSet.getInt("id"));
                        periodoObj.setNamePeriodo(resultSet.getString("name_periodo"));
                        periodoObj.setEnabled(resultSet.getBoolean("enabled"));
                        periodoObj.setFechaCreacion(resultSet.getTimestamp("fecha_creacion"));
                        periodos.add(periodoObj);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            this.desconectar();
        }

        return periodos;
    }

    public List<Periodo> listarPeriodosHabilitados() {
        List<Periodo> periodos = new ArrayList<>();

        try  {
            this.conectar();
            String sql = "SELECT * FROM laboratorio.periodo pe where pe.enabled=true";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Periodo periodoObj = new Periodo();
                        periodoObj.setId(resultSet.getInt("id"));
                        periodoObj.setNamePeriodo(resultSet.getString("name_periodo"));
                        periodoObj.setFechaCreacion(resultSet.getTimestamp("fecha_creacion"));
                        periodoObj.setEnabled(resultSet.getBoolean("enabled"));
                        periodos.add(periodoObj);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            this.desconectar();
        }

        return periodos;
    }
}
