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
            String sql = "select * from laboratorio.equipo eq inner join laboratorio.categoria_equipo ce on ce.id_categoria = eq.id_categoria_equipo where eq.id_laboratorio = ?";
            PreparedStatement st = this.getConnection().prepareStatement(sql);
            st.setInt(1, idLaboratorio);
            rs = st.executeQuery();
            while (rs.next()) {
                Equipo equipo = new Equipo();

                equipo.setId(rs.getInt("id"));
                equipo.setDescripcion(rs.getString("descripcion"));

                equipos.add(equipo);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            this.desconectar();
        }
        System.out.println("...Equipos...");
        System.out.println(equipos);
        return equipos;
    }



}
