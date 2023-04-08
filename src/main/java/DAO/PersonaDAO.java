package DAO;

import Model.Persona;
import global.Conexion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonaDAO extends Conexion {

    public void save(Persona persona) throws SQLException {
        System.out.println(persona + "DAO");
        try {
            this.conectar();
            PreparedStatement st = this.getConnection().prepareStatement("insert into laboratorio.persona ( nombre, apellido, telefono, email, genero, dni)\n" +
                    "values (?,?,?,?,?,?);");
            st.setString(1, persona.getNombre());
            st.setString(2, persona.getApellido());
            st.setString(3, persona.getTelefono());
            st.setString(4, persona.getEmail());
            st.setString(5, persona.getGenero());
            st.setString(6, persona.getDni());
            st.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error...Ebert");
            e.getMessage();
        } finally {
            this.desconectar();
        }
    }

    public void update(Persona persona) throws SQLException {

        try {
            this.conectar();
            PreparedStatement st = this.getConnection().prepareStatement("update laboratorio.persona\n" +
                    "set id       = ?,\n" +
                    "    nombre= ?,\n" +
                    "    apellido =?,\n" +
                    "    telefono = ?,\n" +
                    "    email    = ?,\n" +
                    "    dni      = ?,\n" +
                    "    genero   = ?\n" +
                    "where id=?;");

            st.setInt(1, persona.getId());
            st.setString(2, persona.getNombre());
            st.setString(3, persona.getApellido());
            st.setString(4, persona.getTelefono());
            st.setString(5, persona.getEmail());
            st.setString(6, persona.getDni());
            st.setString(7, persona.getGenero());
            st.setInt(8, persona.getId());


        } catch (Exception e) {
            System.out.println("Error...Ebert");
            e.getMessage();
        } finally {
            this.desconectar();
        }
    }

    public List<Persona> findAll() throws SQLException {
        List<Persona> listaPersonas = new ArrayList<>();
        ResultSet rs;
        try {
            this.conectar();
            PreparedStatement st = this.getConnection().prepareStatement("SELECT * FROM laboratorio.PERSONA");
            rs = st.executeQuery();
            while (rs.next()) {
                Persona persona = new Persona();
                persona.setId(rs.getInt("id"));
                persona.setNombre(rs.getString("nombre"));
                persona.setApellido(rs.getString("apellido"));
                persona.setGenero(rs.getString("genero"));
                persona.setDni(rs.getString("dni"));

                listaPersonas.add(persona);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            this.desconectar();
        }
        return listaPersonas;
    }


    public Persona findById(Persona personaOb) throws SQLException {
        Persona persona = new Persona();
        ResultSet rs;
        try {
            this.conectar();
            PreparedStatement st = this.getConnection().prepareStatement("SELECT pe.id       as id,\n" +
                    "       pe.nombre   as nombre,\n" +
                    "       pe.apellido as apellido,\n" +
                    "       pe.telefono as telefono,\n" +
                    "       pe.email    as email,\n" +
                    "       pe.dni      as dni,\n" +
                    "       pe.genero   as genero\n" +
                    "FROM laboratorio.PERSONA pe where pe.id= ?");
            st.setInt(1, personaOb.getId());
            rs = st.executeQuery();
            while (rs.next()) {
                persona = new Persona();

                persona.setId(rs.getInt("id"));
                persona.setNombre(rs.getString("nombre"));
                persona.setApellido(rs.getString("apellido"));
                persona.setTelefono(rs.getString("telefono"));
                persona.setEmail(rs.getString("email"));
                persona.setDni(rs.getString("dni"));
                persona.setGenero(rs.getString("genero"));
            }

        } catch (Exception e) {
            throw e;
        } finally {
            this.desconectar();
        }
        return persona;
    }
}
