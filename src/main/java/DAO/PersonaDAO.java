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

        } catch (Exception e) {
            throw e;
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
}
