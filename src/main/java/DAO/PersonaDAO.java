package DAO;

import Model.Persona;
import global.Conexion;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PersonaDAO extends Conexion {

    public void save(Persona persona) throws SQLException {
        try {
            this.conectar();
            PreparedStatement st = this.getConnection().prepareStatement("insert into laboratorio.persona ( nombre, apellido, telefono, email, genero, dni)\n" +
                    "values (?,?,?,?,?,?);");
            st.setString(1, persona.getNombre());
            st.setString(2, persona.getApellido());
            st.setString(3, persona.getTelefono());
            st.setString(4,persona.getEmail());
            st.setString(5,persona.getGenero());
            st.setString(6,persona.getDni());

        } catch (Exception e) {
            throw e;
        }finally {
            this.desconectar();
        }

    }
}
