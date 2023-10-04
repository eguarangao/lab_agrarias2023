package DAO;

import Model.AjustePerfil;
import Model.ListFullUser;
import Model.Persona;
import Model.Usuario;
import global.Conexion;
import jakarta.inject.Inject;
import lombok.Data;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO extends Conexion {


    public Usuario getUsuario(String username, String clave) {
        Usuario usuario = null;
        try {
            this.conectar();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM laboratorio.usuario us WHERE nombre = ? AND clave=? AND enabled=true");
            ps.setString(1, username);
            ps.setString(2, clave);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                usuario = new Usuario();
                usuario.setNombre(rs.getString("nombre"));
                usuario.setClave(rs.getString("clave"));
                usuario.setEnabled(rs.getBoolean("enabled"));
                System.out.println(usuario);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuario;
    }

    //    public String getUsuario(String username) {
//        Usuario usuario = null;
//        String clave = null;
//        try {
//            PreparedStatement ps = connection.prepareStatement("SELECT * FROM laboratorio.usuario us WHERE us.nombre = ? + ");
//            ps.setString(1, username);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                clave =  rs.getString("clave");
//
//
//            }
//            rs.close();
//            ps.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return clave;
//    }
    public AjustePerfil SelectAjustePerfil(String usuario, String nameRol) {
        AjustePerfil ajustePerfil = null;
        try {
            this.conectar();
            PreparedStatement ps = connection.prepareStatement("select u.nombre as usuario, u.clave as clave, p.nombre as nombre,\n" +
                    "       p.apellido, p.telefono, p.email,\n" +
                    "       p.dni, p.genero, r.nombre as nombre_rol,r.descripcion\n" +
                    "from laboratorio.usuario u\n" +
                    "         inner join laboratorio.persona p\n" +
                    "                    on p.id = u.id_persona\n" +
                    "         inner join laboratorio.rol_usuario ru\n" +
                    "                    on u.id = ru.id_usuario\n" +
                    "         inner join laboratorio.rol r\n" +
                    "                    on r.id = ru.id_rol\n" +
                    "where u.nombre = ? and r.nombre = ?;");
            ps.setString(1, usuario);
            ps.setString(2, nameRol);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ajustePerfil = new AjustePerfil();
                ajustePerfil.setUsuario(rs.getString("usuario"));
                ajustePerfil.setClave(rs.getString("clave"));
                ajustePerfil.setNombre(rs.getString("nombre"));
                ajustePerfil.setApellido(rs.getString("apellido"));
                ajustePerfil.setTelefono(rs.getString("telefono"));
                ajustePerfil.setEmail(rs.getString("email"));
                ajustePerfil.setDni(rs.getString("dni"));
                ajustePerfil.setGenero(rs.getString("genero"));
                ajustePerfil.setRolNombre(rs.getString("nombre_rol"));
                ajustePerfil.setRolDescripcion(rs.getString("descripcion"));
                System.out.println(ajustePerfil);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ajustePerfil;
    }

    public AjustePerfil UpdateAjustePerfil(AjustePerfil ajustePerfil) {

        try {
            this.conectar();
            PreparedStatement ps = connection.prepareStatement("");

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ajustePerfil = new AjustePerfil();
                ajustePerfil.setUsuario(rs.getString("usuario"));
                ajustePerfil.setClave(rs.getString("clave"));
                ajustePerfil.setNombre(rs.getString("nombre"));
                ajustePerfil.setApellido(rs.getString("apellido"));
                ajustePerfil.setTelefono(rs.getString("telefono"));
                ajustePerfil.setEmail(rs.getString("email"));
                ajustePerfil.setDni(rs.getString("dni"));
                ajustePerfil.setGenero(rs.getString("genero"));
                System.out.println(ajustePerfil);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ajustePerfil;
    }

    public List<ListFullUser> listarUsuariosPersonas() throws SQLException {
        List<ListFullUser> usuariosPersonas = new ArrayList<>();
        this.conectar();
        String query = "select * from laboratorio.persona p inner join laboratorio.usuario ON usuario.id_persona = p.id ";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                ListFullUser usuarioPersona = new ListFullUser();
                usuarioPersona.setUsuarioId(resultSet.getInt("id"));
                usuarioPersona.setNombreUsuario(resultSet.getString("nombre"));
                usuarioPersona.setClave(resultSet.getString("clave"));
                usuarioPersona.setEnabled(resultSet.getBoolean("enabled"));
                usuarioPersona.setFechaCreacion(resultSet.getTimestamp("fecha_creacion"));

                usuarioPersona.setPersonaId(resultSet.getInt("id"));
                usuarioPersona.setNombrePersona(resultSet.getString("nombre"));
                usuarioPersona.setApellido(resultSet.getString("apellido"));
                usuarioPersona.setTelefono(resultSet.getString("telefono"));
                usuarioPersona.setEmail(resultSet.getString("email"));
                usuarioPersona.setDni(resultSet.getString("dni"));
                usuarioPersona.setGenero(resultSet.getString("genero"));


                usuariosPersonas.add(usuarioPersona);
            }
        } finally {
            this.desconectar();
        }

        return usuariosPersonas;
    }

    public void update(ListFullUser usuarioPersona) throws SQLException {
        this.conectar();
        String updatePersonaQuery = "UPDATE laboratorio.persona SET nombre = ?, apellido = ?, telefono = ?, email = ?, dni = ?, genero = ? WHERE id = ?";
        String updateUsuarioQuery = "UPDATE laboratorio.usuario SET nombre = ?, clave = ?, enabled = ? WHERE id_persona = ?";

        try {
            connection.setAutoCommit(false);  // Inicia una transacción

            // Actualizar en la tabla persona
            try (PreparedStatement preparedStatementPersona = connection.prepareStatement(updatePersonaQuery)) {
                preparedStatementPersona.setInt(7, usuarioPersona.getPersonaId());
                preparedStatementPersona.setString(1, usuarioPersona.getNombrePersona());
                preparedStatementPersona.setString(2, usuarioPersona.getApellido());
                preparedStatementPersona.setString(3, usuarioPersona.getTelefono());
                preparedStatementPersona.setString(4, usuarioPersona.getEmail());
                preparedStatementPersona.setString(5, usuarioPersona.getDni());
                preparedStatementPersona.setString(6, usuarioPersona.getGenero());


                int affectedRowsPersona = preparedStatementPersona.executeUpdate();

                if (affectedRowsPersona == 0) {
                    throw new SQLException("La actualización en la tabla persona falló, no se modificó ninguna fila.");
                }
            }

            // Actualizar en la tabla usuario
            try (PreparedStatement preparedStatementUsuario = connection.prepareStatement(updateUsuarioQuery)) {
                preparedStatementUsuario.setInt(4, usuarioPersona.getPersonaId());
                preparedStatementUsuario.setString(1, usuarioPersona.getNombreUsuario());
                preparedStatementUsuario.setString(2, usuarioPersona.getClave());
                preparedStatementUsuario.setBoolean(3, usuarioPersona.isEnabled());


                int affectedRowsUsuario = preparedStatementUsuario.executeUpdate();

                if (affectedRowsUsuario == 0) {
                    throw new SQLException("La actualización en la tabla usuario falló, no se modificó ninguna fila.");
                }
            }

            connection.commit();  // Confirma la transacción
        } catch (SQLException e) {
            connection.rollback();  // Si ocurre un error, deshace la transacción
            throw e;
        } finally {
            this.desconectar();
        }
    }


    public void insert(ListFullUser usuarioPersona) throws SQLException {
        this.conectar();
        String insertPersonaQuery = "INSERT INTO laboratorio.persona (nombre, apellido, telefono, email, dni, genero,fecha_creacion) VALUES (?, ?, ?, ?, ?, ?,?)";
        String insertUsuarioQuery = "INSERT INTO laboratorio.usuario (id_persona, nombre, clave, enabled) VALUES (?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);  // Inicia una transacción

            // Insertar en la tabla persona
            try (PreparedStatement preparedStatementPersona = connection.prepareStatement(insertPersonaQuery, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatementPersona.setString(1, usuarioPersona.getNombrePersona());
                preparedStatementPersona.setString(2, usuarioPersona.getApellido());
                preparedStatementPersona.setString(3, usuarioPersona.getTelefono());
                preparedStatementPersona.setString(4, usuarioPersona.getEmail());
                preparedStatementPersona.setString(5, usuarioPersona.getDni());
                preparedStatementPersona.setString(6, usuarioPersona.getGenero());
                preparedStatementPersona.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
                int affectedRowsPersona = preparedStatementPersona.executeUpdate();

                if (affectedRowsPersona == 0) {
                    throw new SQLException("La inserción en la tabla persona falló, no se agregó ninguna fila.");
                }

                // Obtener el ID generado para la persona
                try (ResultSet generatedKeys = preparedStatementPersona.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        usuarioPersona.setPersonaId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("No se pudo obtener el ID de la persona.");
                    }
                }
            }

            // Insertar en la tabla usuario
            try (PreparedStatement preparedStatementUsuario = connection.prepareStatement(insertUsuarioQuery)) {
                preparedStatementUsuario.setInt(1, usuarioPersona.getPersonaId());
                preparedStatementUsuario.setString(2, usuarioPersona.getNombreUsuario());
                preparedStatementUsuario.setString(3, usuarioPersona.getClave());
                preparedStatementUsuario.setBoolean(4, usuarioPersona.isEnabled());

                int affectedRowsUsuario = preparedStatementUsuario.executeUpdate();

                if (affectedRowsUsuario == 0) {
                    throw new SQLException("La inserción en la tabla usuario falló, no se agregó ninguna fila.");
                }
            }

            connection.commit();  // Confirma la transacción
        } catch (SQLException e) {
            connection.rollback();  // Si ocurre un error, deshace la transacción
            throw e;
        } finally {
            this.desconectar();
        }
    }
    public void delete(int personaId) throws SQLException {
        this.conectar();
        String deleteUsuarioQuery = "DELETE FROM laboratorio.usuario WHERE id_persona = ?";
        String deletePersonaQuery = "DELETE FROM laboratorio.persona WHERE id = ?";

        try {
            connection.setAutoCommit(false);  // Inicia una transacción

            // Eliminar en la tabla usuario
            try (PreparedStatement preparedStatementUsuario = connection.prepareStatement(deleteUsuarioQuery)) {
                preparedStatementUsuario.setInt(1, personaId);

                int affectedRowsUsuario = preparedStatementUsuario.executeUpdate();

                if (affectedRowsUsuario == 0) {
                    throw new SQLException("La eliminación en la tabla usuario falló, no se eliminó ninguna fila.");
                }
            }

            // Eliminar en la tabla persona
            try (PreparedStatement preparedStatementPersona = connection.prepareStatement(deletePersonaQuery)) {
                preparedStatementPersona.setInt(1, personaId);

                int affectedRowsPersona = preparedStatementPersona.executeUpdate();

                if (affectedRowsPersona == 0) {
                    throw new SQLException("La eliminación en la tabla persona falló, no se eliminó ninguna fila.");
                }
            }

            connection.commit();  // Confirma la transacción
        } catch (SQLException e) {
            connection.rollback();  // Si ocurre un error, deshace la transacción
            throw e;
        } finally {
            this.desconectar();
        }
    }


}
