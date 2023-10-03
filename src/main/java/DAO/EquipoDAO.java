package DAO;

import Model.Equipo;
import Model.Equipo;
import global.Conexion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EquipoDAO extends Conexion {

    public List<Equipo> findByLaboratorioID(int idLaboratorio) throws SQLException {
        List<Equipo> equipos = new ArrayList<>();
        ResultSet rs;
        try {
            this.conectar();
            PreparedStatement st = this.getConnection().prepareStatement("select *\n" +
                    "from laboratorio.equipo e\n" +
                    "         inner join laboratorio.categoria_equipo ce on ce.id = e.id_categoria_equipo\n" +
                    "         inner join laboratorio.laboratorio lb on e.id_laboratorio = lb.id\n" +
                    "where lb.id = ?;");
            st.setInt(1, idLaboratorio);
            rs = st.executeQuery();
            while (rs.next()) {
                Equipo equipo = new Equipo();

                equipo.setId(rs.getInt("id"));
                equipo.setDescripcion(rs.getString("nombre"));

                equipos.add(equipo);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            this.desconectar();
        }
        return equipos;
    }
}
