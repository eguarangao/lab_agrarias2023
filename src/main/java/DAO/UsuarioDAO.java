package DAO;

import Model.*;
import global.Conexion;
import org.mindrot.jbcrypt.BCrypt;
import utils.PasswordHashing;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO extends Conexion {

    PasswordHashing passwordHashing = new PasswordHashing(); // Aquí estás creando una instancia de PasswordHashing


    public Usuario getUsuario(String username, String clave) {
        Usuario usuario = null;
        try {
            this.conectar();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM laboratorio.usuario us WHERE nombre_usuario = ? AND clave=? AND enabled=true");
            ps.setString(1, username);
            ps.setString(2, clave);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                usuario = new Usuario();
                usuario.setNombre(rs.getString("nombre_usuario"));
                usuario.setClave(rs.getString("clave"));
                usuario.setId(rs.getInt("id"));
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
            PreparedStatement ps = connection.prepareStatement("select p.id, u.nombre_usuario as usuario, u.clave as clave, p.nombre as nombre,\n" +
                    "       p.apellido, p.telefono, p.email,\n" +
                    "       p.dni, p.genero, r.nombre as nombre_rol,r.descripcion\n" +
                    "from laboratorio.usuario u\n" +
                    "         inner join laboratorio.persona p\n" +
                    "                    on p.id = u.id_persona\n" +
                    "         inner join laboratorio.rol_usuario ru\n" +
                    "                    on u.id = ru.id_usuario\n" +
                    "         inner join laboratorio.rol r\n" +
                    "                    on r.id = ru.id_rol\n" +
                    "where u.nombre_usuario = ? and r.nombre = ?;");
            ps.setString(1, usuario);
            ps.setString(2, nameRol);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ajustePerfil = new AjustePerfil();
                ajustePerfil.setIdPersona(rs.getInt("id"));
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
    public void updateAjustePerfil(AjustePerfil ajustePerfil) {
        try {
            this.conectar();
            PreparedStatement ps = connection.prepareStatement("UPDATE laboratorio.persona\n" +
                    "\tSET nombre=?, apellido=?, telefono=?, email=?, dni=?, genero=?\n" +
                    "\tWHERE id = ?;");
            PreparedStatement ps2 = connection.prepareStatement("UPDATE laboratorio.usuario SET nombre_usuario=?, clave=? WHERE id_persona = ?;");

          //
            ps.setString(1, ajustePerfil.getNombre());
            ps.setString(2, ajustePerfil.getApellido());
            ps.setString(3, ajustePerfil.getTelefono());
            ps.setString(4, ajustePerfil.getEmail());
            ps.setString(5, ajustePerfil.getDni());
            ps.setString(6, ajustePerfil.getGenero());
            ps.setInt(7, ajustePerfil.getIdPersona());
         //   ps.setString(8, ajustePerfil.getRolNombre());
          //  ps.setString(9, ajustePerfil.getRolDescripcion());
          //  ps.setString(10, ajustePerfil.getUsuario());
         //   ps.setString(11, ajustePerfil.getRolNombre());
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("La actualización fue exitosa.");
            } else {
                System.out.println("No se realizó ninguna actualización.");
            }
            ps2.setString(2, ajustePerfil.getClave());
            ps2.setString(1, ajustePerfil.getUsuario());
            ps2.setInt(3, ajustePerfil.getIdPersona());
            int rowsUpdated2 = ps2.executeUpdate();

            if (rowsUpdated2 > 0) {
                System.out.println("La actualización fue exitosa.");
            } else {
                System.out.println("No se realizó ninguna actualización.");
            }

            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            desconectar();
        }
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
        String query = "SELECT * FROM laboratorio.persona p\n" +
                "INNER JOIN laboratorio.usuario u ON p.id = u.id_persona\n" +
                "INNER JOIN laboratorio.rol_usuario ru ON ru.id_usuario = u.id\n" +
                "INNER join laboratorio.rol ON rol.id = ru.id_rol";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                ListFullUser usuarioPersona = new ListFullUser();
                usuarioPersona.setUsuarioId(resultSet.getInt("id"));
                usuarioPersona.setNombrePersona(resultSet.getString("nombre"));
                usuarioPersona.setApellido(resultSet.getString("apellido"));
                usuarioPersona.setTelefono(resultSet.getString("telefono"));
                usuarioPersona.setEmail(resultSet.getString("email"));
                usuarioPersona.setDni(resultSet.getString("dni"));
                usuarioPersona.setGenero(resultSet.getString("genero"));
                usuarioPersona.setFechaCreacion(resultSet.getTimestamp("fecha_creacion"));
                usuarioPersona.setPersonaId(resultSet.getInt("id"));

                usuarioPersona.setNombreUsuario(resultSet.getString("nombre_usuario"));
                usuarioPersona.setClave(resultSet.getString("clave"));
                usuarioPersona.setEnabled(resultSet.getBoolean("enabled"));
                usuarioPersona.setFechaCreacion(resultSet.getTimestamp("fecha_creacion"));







                usuarioPersona.setGenero(resultSet.getString("genero"));
                //tabla rol_usuario
                usuarioPersona.setIdRol(resultSet.getInt("id_rol"));

                usuariosPersonas.add(usuarioPersona);
            }
        } finally {
            this.desconectar();
        }

        return usuariosPersonas;
    }
    public List<ListFullUser> listarUsuariosDocentes() throws SQLException {
        List<ListFullUser> usuariosPersonas = new ArrayList<>();
        this.conectar();
        String query = "SELECT * FROM laboratorio.persona p \n" +
                "                INNER JOIN laboratorio.usuario u ON p.id = u.id_persona\n" +
                "                INNER JOIN laboratorio.rol_usuario ru ON ru.id_usuario = u.id \n" +
                "\t\tINNER join laboratorio.rol ON rol.id = ru.id_rol where rol.id=3;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                ListFullUser usuarioPersona = new ListFullUser();
                usuarioPersona.setUsuarioId(resultSet.getInt("id"));
                usuarioPersona.setNombreUsuario(resultSet.getString("nombre_usuario"));
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
                //tabla rol_usuario
                usuarioPersona.setIdRol(resultSet.getInt("id_rol"));

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
        String updateUsuarioQuery = "UPDATE laboratorio.usuario SET nombre_usuario = ?, clave = ?, enabled = ? WHERE id_persona = ?";

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

    public void update2(ListFullUser usuarioPersona) throws SQLException {
        this.conectar();

        String updatePersonaQuery = "UPDATE laboratorio.persona SET nombre=?, apellido=?, telefono=?, email=?, dni=?, genero=? WHERE id=?";
        String updateUsuarioQuery = "UPDATE laboratorio.usuario SET id_persona=?, nombre=?, clave=?, enabled=? WHERE id=?";
        String updateRolUsuarioQuery = "UPDATE laboratorio.rol_usuario SET id_rol=? WHERE id_usuario=?";
        String updateTecnicoQuery = "UPDATE laboratorio.tecnico SET id_usuario=? WHERE id=?";
        String updateDocenteQuery = "UPDATE laboratorio.docente SET id_persona=? WHERE id=?";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatementPersona = connection.prepareStatement(updatePersonaQuery)) {
                preparedStatementPersona.setString(1, usuarioPersona.getNombrePersona());
                preparedStatementPersona.setString(2, usuarioPersona.getApellido());
                preparedStatementPersona.setString(3, usuarioPersona.getTelefono());
                preparedStatementPersona.setString(4, usuarioPersona.getEmail());
                preparedStatementPersona.setString(5, usuarioPersona.getDni());
                preparedStatementPersona.setString(6, usuarioPersona.getGenero());
                preparedStatementPersona.setInt(7, usuarioPersona.getPersonaId());

                int affectedRowsPersona = preparedStatementPersona.executeUpdate();
                if (affectedRowsPersona == 0) {
                    throw new SQLException("La actualización en la tabla persona falló, no se modificó ninguna fila.");
                }
            }

            try (PreparedStatement preparedStatementUsuario = connection.prepareStatement(updateUsuarioQuery)) {
                preparedStatementUsuario.setInt(1, usuarioPersona.getPersonaId());
                preparedStatementUsuario.setString(2, usuarioPersona.getNombreUsuario());
                preparedStatementUsuario.setString(3, usuarioPersona.getClave());
                preparedStatementUsuario.setBoolean(4, usuarioPersona.isEnabled());
                preparedStatementUsuario.setInt(5, usuarioPersona.getUsuarioId());

                int affectedRowsUsuario = preparedStatementUsuario.executeUpdate();
                if (affectedRowsUsuario == 0) {
                    throw new SQLException("La actualización en la tabla usuario falló, no se modificó ninguna fila.");
                }
            }

            try (PreparedStatement preparedStatementRolUsuario = connection.prepareStatement(updateRolUsuarioQuery)) {
                preparedStatementRolUsuario.setInt(1, usuarioPersona.getIdRol());
                preparedStatementRolUsuario.setInt(2, usuarioPersona.getUsuarioId());

                int affectedRowsRolUsuario = preparedStatementRolUsuario.executeUpdate();
                if (affectedRowsRolUsuario == 0) {
                    throw new SQLException("La actualización en la tabla rol_usuario falló, no se modificó ninguna fila.");
                }
            }

            if (usuarioPersona.getIdRol() == 2) {
                try (PreparedStatement preparedStatementTecnico = connection.prepareStatement(updateTecnicoQuery)) {
                    preparedStatementTecnico.setInt(1, usuarioPersona.getUsuarioId());
                    preparedStatementTecnico.setInt(2, usuarioPersona.getIdTecnico());

                    int affectedRowsTecnico = preparedStatementTecnico.executeUpdate();
                    if (affectedRowsTecnico == 0) {
                        throw new SQLException("La actualización en la tabla tecnico falló, no se modificó ninguna fila.");
                    }
                }
            }

            if (usuarioPersona.getIdRol() == 3) {
                try (PreparedStatement preparedStatementDocente = connection.prepareStatement(updateDocenteQuery)) {
                    preparedStatementDocente.setInt(1, usuarioPersona.getPersonaId());
                    preparedStatementDocente.setInt(2, usuarioPersona.getIdDocente());

                    int affectedRowsDocente = preparedStatementDocente.executeUpdate();
                    if (affectedRowsDocente == 0) {
                        throw new SQLException("La actualización en la tabla docente falló, no se modificó ninguna fila.");
                    }
                }
            }

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            this.desconectar();
        }
    }
    public void delete(int personaId) throws SQLException {
        this.conectar();

        String deletePersonaQuery = "DELETE FROM laboratorio.persona WHERE id=?";
        String deleteUsuarioQuery = "DELETE FROM laboratorio.usuario WHERE id_persona=?";
        String deleteRolUsuarioQuery = "DELETE FROM laboratorio.rol_usuario WHERE id_usuario=?";
        String deleteTecnicoQuery = "DELETE FROM laboratorio.tecnico WHERE id_usuario=?";
        String deleteDocenteQuery = "DELETE FROM laboratorio.docente WHERE id_persona=?";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatementTecnico = connection.prepareStatement(deleteTecnicoQuery)) {
                preparedStatementTecnico.setInt(1, personaId);
                int affectedRowsTecnico = preparedStatementTecnico.executeUpdate();
                // No es necesario verificar si no se eliminó ninguna fila, ya que puede no haber un registro en la tabla técnico para todas las personas.
            }

            try (PreparedStatement preparedStatementDocente = connection.prepareStatement(deleteDocenteQuery)) {
                preparedStatementDocente.setInt(1, personaId);
                int affectedRowsDocente = preparedStatementDocente.executeUpdate();
                // No es necesario verificar si no se eliminó ninguna fila, ya que puede no haber un registro en la tabla docente para todas las personas.
            }

            try (PreparedStatement preparedStatementRolUsuario = connection.prepareStatement(deleteRolUsuarioQuery)) {
                preparedStatementRolUsuario.setInt(1, personaId);
                int affectedRowsRolUsuario = preparedStatementRolUsuario.executeUpdate();
                // No es necesario verificar si no se eliminó ninguna fila, ya que puede no haber un registro en la tabla rol_usuario para todas las personas.
            }

            try (PreparedStatement preparedStatementUsuario = connection.prepareStatement(deleteUsuarioQuery)) {
                preparedStatementUsuario.setInt(1, personaId);
                int affectedRowsUsuario = preparedStatementUsuario.executeUpdate();
                if (affectedRowsUsuario == 0) {
                    throw new SQLException("La eliminación en la tabla usuario falló, no se eliminó ninguna fila.");
                }
            }

            try (PreparedStatement preparedStatementPersona = connection.prepareStatement(deletePersonaQuery)) {
                preparedStatementPersona.setInt(1, personaId);
                int affectedRowsPersona = preparedStatementPersona.executeUpdate();
                if (affectedRowsPersona == 0) {
                    throw new SQLException("La eliminación en la tabla persona falló, no se eliminó ninguna fila.");
                }
            }

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            this.desconectar();
        }
    }

    public void insert(ListFullUser usuarioPersona) throws SQLException {
        String hashedPassword = BCrypt.hashpw(usuarioPersona.getClave().toString().trim(), BCrypt.gensalt());
        this.conectar();
        //insert tabla persona
        String insertPersonaQuery = "INSERT INTO laboratorio.persona (nombre, apellido, telefono, email, dni, genero,fecha_creacion) VALUES (?, ?, ?, ?, ?, ?,?)";
        //insert tabla usuario
        String insertUsuarioQuery = "INSERT INTO laboratorio.usuario (id_persona, nombre_usuario, clave, enabled) VALUES (?, ?, ?, ?)";
        //insert tabla rol_usuario
        String insertRolUsuarioQuery = "INSERT INTO laboratorio.rol_usuario (id_rol, id_usuario) VALUES (?, ?)";
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
                usuarioPersona.setNombreUsuario(usuarioPersona.getDni());
                usuarioPersona.setClave(usuarioPersona.getDni());
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
            try (PreparedStatement preparedStatementUsuario = connection.prepareStatement(insertUsuarioQuery, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatementUsuario.setInt(1, usuarioPersona.getPersonaId());
                preparedStatementUsuario.setString(2, usuarioPersona.getNombreUsuario());
                preparedStatementUsuario.setString(3, passwordHashing.hashPassword(usuarioPersona.getDni()) );
                preparedStatementUsuario.setBoolean(4, true);

                int affectedRowsUsuario = preparedStatementUsuario.executeUpdate();

                if (affectedRowsUsuario == 0) {
                    throw new SQLException("La inserción en la tabla usuario falló, no se agregó ninguna fila.");
                }
                // Obtener el ID generado para el usuario
                try (ResultSet generatedKeys = preparedStatementUsuario.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        usuarioPersona.setUsuarioId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("No se pudo obtener el ID del usuario.");
                    }
                }
            }
            try (PreparedStatement preparedStatementRolUsuario = connection.prepareStatement(insertRolUsuarioQuery)) {
                preparedStatementRolUsuario.setInt(1, usuarioPersona.getIdRol());
                preparedStatementRolUsuario.setInt(2, usuarioPersona.getUsuarioId());
                int affectedRowsRolUsuario = preparedStatementRolUsuario.executeUpdate();
                if (affectedRowsRolUsuario == 0) {
                    throw new SQLException("La inserción en la tabla rol_usuario falló, no se agregó ninguna fila.");
                }
            }
            if (usuarioPersona.getIdRol() == 2) {
                String insertTecnico = "INSERT INTO laboratorio.tecnico (id_usuario) VALUES (?)";
                try (PreparedStatement preparedStatementTecnico = connection.prepareStatement(insertTecnico, Statement.RETURN_GENERATED_KEYS)) {
                    preparedStatementTecnico.setInt(1, usuarioPersona.getUsuarioId());
                    int affectedRowsTecnico = preparedStatementTecnico.executeUpdate();
                    if (affectedRowsTecnico == 0) {
                        throw new SQLException("La inserción en la tabla tecnico falló, no se agregó ninguna fila.");
                    }
                }
            }
            if (usuarioPersona.getIdRol() == 3) {
                String insertDocente = "INSERT INTO laboratorio.docente (id_persona) VALUES (?)";
                try (PreparedStatement preparedStatementDocente = connection.prepareStatement(insertDocente, Statement.RETURN_GENERATED_KEYS)) {
                    preparedStatementDocente.setInt(1, usuarioPersona.getPersonaId());
                    int affectedRowsDocente = preparedStatementDocente.executeUpdate();
                    if (affectedRowsDocente == 0) {
                        throw new SQLException("La inserción en la tabla docente falló, no se agregó ninguna fila.");
                    }
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

    public void delete2(int personaId) throws SQLException {
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

    public boolean verificarCredenciales(String username, String password) throws SQLException{
        // Recupera la contraseña hash almacenada en la base de datos para el usuario 'username'
        String hashedPasswordFromDB = getHashedPasswordFromDatabase(username);

        // Compara la contraseña ingresada con la contraseña almacenada
        return BCrypt.checkpw(password, hashedPasswordFromDB);
    }
    public String getHashedPasswordFromDatabase(String username)throws SQLException{
       ResultSet rs;
        String sql = "SELECT  us.clave as clave FROM laboratorio.usuario us where us.nombre_usuario = ?";
        String passwordBD="";
        try {
            this.conectar();
            PreparedStatement st = this.getConnection().prepareStatement(sql);
            st.setString(1, username);
            rs = st.executeQuery();
            while (rs.next()) {
                passwordBD = rs.getString("clave");
            }
            return passwordBD;
        } catch (SQLException e) {
            throw e;
        } finally {
            this.desconectar();
        }

    }
    public Usuario getUsuario2(String username, String password) {
        Usuario usuario = null;
        try {
            this.conectar();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM laboratorio.usuario us WHERE nombre_usuario = ? AND enabled=true");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Obtén la contraseña encriptada almacenada en la base de datos
                String hashedPasswordFromDB = rs.getString("clave");

                // Verifica si la contraseña ingresada coincide con la almacenada en la base de datos
//                if (BCrypt.checkpw(password, hashedPasswordFromDB)) {
                System.out.println("VALOR DE VALIDACION");
                System.out.println(passwordHashing.verifyPassword(password,hashedPasswordFromDB));
                if (passwordHashing.verifyPassword(password,hashedPasswordFromDB)) {
                    usuario = new Usuario();
                    usuario.setNombre(rs.getString("nombre_usuario"));
                    usuario.setClave(rs.getString("clave"));
                    usuario.setId(rs.getInt("id"));
                    usuario.setEnabled(rs.getBoolean("enabled"));
                    System.out.println(usuario);
                }
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return usuario;
    }

    public void actualizarUsuario(String nuevaclave, int idUsuario){
        try  {
            this.conectar();
            String query = "SELECT laboratorio.cambiarclaveusuario(?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, idUsuario);
                preparedStatement.setString(2, nuevaclave);

                preparedStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            this.desconectar();
        }
    }

}
