package DAO;

import Model.Docente;
import Model.Persona;
import global.Conexion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DocenteDAO extends Conexion {

    public List<Docente> findByUsuarioID(int idUsuario) throws SQLException {
        List<Docente> docentes = new ArrayList<>();
        ResultSet rs;
        try {
            this.conectar();
            String sql = "SELECT d.id AS id, CONCAT(p.nombre, ' ', p.apellido) AS nombre_completo\n" +
                    "FROM laboratorio.docente d\n" +
                    "INNER JOIN laboratorio.persona p ON p.id = d.id_persona\n" +
                    "INNER JOIN laboratorio.usuario u ON p.id = u.id_persona\n" +
                    "WHERE u.id = ?";
            PreparedStatement st = this.getConnection().prepareStatement(sql);
            st.setInt(1, idUsuario);
            rs = st.executeQuery();
            while (rs.next()) {
                Docente docente = new Docente();
                Persona persona = new Persona();

                docente.setId(rs.getInt("id"));
                persona.setApellido(rs.getString("nombre_completo"));
                persona.setNombre(rs.getString("nombre_completo"));

                docente.setPersona(persona);

                docentes.add(docente);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            this.desconectar();
        }
        System.out.println("...Docentes...");
        System.out.println(docentes);
        return docentes;
    }


}
