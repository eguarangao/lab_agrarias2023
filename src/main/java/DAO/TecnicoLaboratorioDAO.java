package DAO;

import Model.Aula;
import Model.Laboratorio;
import Model.Persona;
import Model.Tecnico;
import global.Conexion;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

import java.sql.*;
import java.time.LocalDateTime;
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
                aula.setFechaCreacion(resultSet.getTimestamp("fecha_creacion"));
                aula.setId_aula(resultSet.getInt("id_aula"));
                aulas.add(aula);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aulas;
    }

    public void insert(int idLaboratorio, Aula aula) throws SQLException {

        this.conectar();
        try {

            connection.setAutoCommit(false);
            String insertAulaQuery = "INSERT INTO laboratorio.aula (aula, capacidad,fecha_creacion) VALUES (?, ?,?)";
            String insertLaboratorioAulaQuery = "INSERT INTO laboratorio.laboratorio_aula (id_laboratorio, id_aula) VALUES (?, ?)";

            try (PreparedStatement statementAula = connection.prepareStatement(insertAulaQuery, Statement.RETURN_GENERATED_KEYS)) {

                statementAula.setString(1, aula.getNombre());
                statementAula.setInt(2, aula.getCapacidad());
                statementAula.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                int affectedRowsAula = statementAula.executeUpdate();

                if (affectedRowsAula <= 0) {
                    throw new SQLException("La actualización en la tabla aula falló, no se modificó ninguna fila.");
                }
                try (ResultSet generateKeys = statementAula.getGeneratedKeys()) {
                    if (generateKeys.next()) {
                        aula.setId(generateKeys.getInt(1));
                    } else {
                        throw new SQLException("No se pudo obtener el ID del aula.");
                    }
                }
            }

            try (PreparedStatement statementLaboratorioAula = connection.prepareStatement(insertLaboratorioAulaQuery)) {

                statementLaboratorioAula.setInt(1, idLaboratorio);
                statementLaboratorioAula.setInt(2, aula.getId());

                int affectedRowsLaboratorioAula = statementLaboratorioAula.executeUpdate();

                if (affectedRowsLaboratorioAula <= 0) {
                    throw new SQLException("La actualización en la tabla aula_laboratorio falló, no se modificó ninguna fila.");
                } else {
                    connection.commit();
                }

            }
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            this.desconectar();
        }


    }

    public void insert(int idLaboratorio, int idTecnico, int idPeriodo) throws SQLException {

        this.conectar();
        try {

            connection.setAutoCommit(false);
            String insertAulaQuery = "insert into laboratorio.tecnico_laboratorio (id_laboratorio, id_tecnico, id_periodo, enable)\n" +
                    "values (?,?,?,true);";

            try (PreparedStatement statementAula = connection.prepareStatement(insertAulaQuery, Statement.RETURN_GENERATED_KEYS)) {

                statementAula.setInt(1, idLaboratorio);
                statementAula.setInt(2, idTecnico);
                statementAula.setInt(3, idPeriodo);
                int affectedRowsAula = statementAula.executeUpdate();

                if (affectedRowsAula <= 0) {
                    throw new SQLException("La actualización en la tabla aula falló, no se modificó ninguna fila.");
                }
                try (ResultSet generateKeys = statementAula.getGeneratedKeys()) {
                    if (generateKeys.next()) {
                        connection.commit();
                    } else {
                        throw new SQLException("No se pudo obtener el ID del aula.");

                    }
                }
            }

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            this.desconectar();
        }


    }

    public void update(Aula aula) throws SQLException {
        try {
            // Establece la conexión a la base de datos (reemplaza con tu lógica).
            this.conectar();
            connection.setAutoCommit(false);
            String sql = "UPDATE laboratorio.aula SET aula = ?, capacidad = ? WHERE id_aula = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, aula.getNombre());
            preparedStatement.setInt(2, aula.getCapacidad());
            preparedStatement.setInt(3, aula.getId_aula());

            int filasActualizadas = preparedStatement.executeUpdate();

            if (filasActualizadas == 0) {
                throw new SQLException("No se encontraron registros para actualizar.");
            }

            // Confirmar la transacción.
            connection.commit();
        } catch (SQLException e) {
            // Si ocurre un error, se debe realizar un rollback.
            connection.rollback();
            throw e; // Puedes manejar la excepción de otra manera si es necesario.
        } finally {
            // Cerrar la conexión (reemplaza con tu lógica).
            if (connection != null) {
                connection.close();
            }
            desconectar();
        }
    }

    public void deleteLaboratorioAulaAndAula(Aula aula) throws SQLException {
        try {
            // Establece la conexión a la base de datos (reemplaza con tu lógica).
            this.conectar();
            connection.setAutoCommit(false);

            // Eliminar el registro de laboratorio_aula
            String sqlLaboratorioAula = "DELETE FROM laboratorio.laboratorio_aula WHERE id = ?";
            PreparedStatement preparedStatementLaboratorioAula = connection.prepareStatement(sqlLaboratorioAula);
            preparedStatementLaboratorioAula.setInt(1, aula.getId());
            int filasEliminadasLaboratorioAula = preparedStatementLaboratorioAula.executeUpdate();

            // Eliminar el registro de la tabla aula
            String sqlAula = "DELETE FROM laboratorio.aula WHERE id_aula = ?";
            PreparedStatement preparedStatementAula = connection.prepareStatement(sqlAula);
            preparedStatementAula.setInt(1, aula.getId_aula());
            int filasEliminadasAula = preparedStatementAula.executeUpdate();

            // Verificar si se eliminaron filas en ambas tablas
            if (filasEliminadasLaboratorioAula == 0 || filasEliminadasAula == 0) {
                throw new SQLException("No se encontraron registros para eliminar con los IDs especificados.");
            }

            // Confirmar la transacción.
            connection.commit();
        } catch (SQLException e) {
            // Si ocurre un error, se debe realizar un rollback.
            connection.rollback();
            throw e; // Puedes manejar la excepción de otra manera si es necesario.
        } finally {
            // Cerrar la conexión (reemplaza con tu lógica).
            if (connection != null) {
                connection.close();
            }
            desconectar();
        }
    }

    public List<Persona> listarTecnicobyLab(int idLaboratorio, int idPeriodo) throws SQLException {
        List<Persona> resultados = new ArrayList<>();


        String sql = "SELECT tl.id, per.nombre, per.apellido, per.dni, tl.enable\n" +
                "\tFROM laboratorio.tecnico_laboratorio tl \n" +
                "\tinner join laboratorio.laboratorio l on tl.id_laboratorio = l.id\n" +
                "\tinner join laboratorio.tecnico t ON t.id = tl.id_tecnico\n" +
                "\tinner join laboratorio.periodo p on p.id=tl.id_periodo\n" +
                "\tinner join laboratorio.usuario u on u.id = t.id_usuario\n" +
                "\tinner join laboratorio.persona per on per.id = u.id_persona\n" +
                "\twhere tl.id_laboratorio=? and tl.id_periodo=?;";
        this.conectar();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, idLaboratorio);
            preparedStatement.setInt(2, idPeriodo);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                // Mapea los resultados a objetos de tu clase personalizada
                Persona objeto = new Persona();
                // Llena el objeto con los valores del ResultSet
                objeto.setId(resultSet.getInt("id"));
                objeto.setNombre(resultSet.getString("nombre"));
                objeto.setApellido(resultSet.getString("apellido"));
                objeto.setDni(resultSet.getString("dni"));
                objeto.setEnable(resultSet.getBoolean("enable"));
                // Agrega el objeto a la lista de resultados
                resultados.add(objeto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            desconectar();
        }

        return resultados;
    }

    public List<Persona> listTecnico() {
        List<Persona> personas = new ArrayList<>();
        ResultSet rs;
        String sql = "select t.id, p.nombre, p.apellido, p.dni from laboratorio.tecnico t\n" +
                "    inner join laboratorio.usuario u\n" +
                "        on u.id = t.id_usuario\n" +
                "inner join laboratorio.persona p on u.id_persona = p.id\n" +
                "inner join laboratorio.rol_usuario ru on u.id = ru.id_usuario\n" +
                "where ru.id_rol = 2;";
        conectar();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Persona tecnico = new Persona();
                tecnico.setId(rs.getInt("id"));
                tecnico.setDni(rs.getString("dni"));
                tecnico.setNombre(rs.getString("nombre"));
                tecnico.setApellido(rs.getString("apellido"));
                personas.add(tecnico);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            desconectar();
        }
        return personas;
    }
}
