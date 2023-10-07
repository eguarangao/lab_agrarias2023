package DAO;

import Model.Equipo;
import Model.Aula;
import Model.CategoriaEquipo;
import Model.Laboratorio;
import global.Conexion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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




    public List<Equipo> listarEquiposPorTecnico(int tecnicoID) throws SQLException {
        List<Equipo> Listequipos = new ArrayList<>();
        this.conectar();
        String query = "select * from laboratorio.ListarEquiposPorTecnico(?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, tecnicoID);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {


                while (resultSet.next()) {
                    Equipo equipo = new Equipo();
                    Aula aula = new Aula();
                    CategoriaEquipo categoriaEquipo = new CategoriaEquipo();
                    Laboratorio laboratorio = new Laboratorio();

                    equipo.setId(resultSet.getInt("id"));

                    equipo.setId(resultSet.getInt("id_laboratorio"));
                    laboratorio.setNombre(resultSet.getString("nom_laboratorio"));

                    aula.setId(resultSet.getInt("id_aula"));
                    aula.setNombre(resultSet.getString("aula"));

                    equipo.setId(resultSet.getInt("id_categoria_equipo"));
                    categoriaEquipo.setNombre(resultSet.getString("categoria"));

                    equipo.setCodigo(resultSet.getString("codigo"));
                    equipo.setDescripcion(resultSet.getString("descripcion"));
                    equipo.setMarca(resultSet.getString("marca"));
                    equipo.setModelo(resultSet.getString("modelo"));
                    equipo.setNumeroSerie(resultSet.getString("num_serie"));
                    equipo.setFechaAdquisicion(resultSet.getDate("fecha_adquisicion"));
                    equipo.setEstado(resultSet.getBoolean("estado"));

                    equipo.setAula(aula);
                    equipo.setCategoriaEquipo(categoriaEquipo);
                    equipo.setLaboratorio(laboratorio);

                    Listequipos.add(equipo);

                }
            }
        } finally {
            this.desconectar();
        }

        return Listequipos;
    }

    public void agregarEquipo(Equipo equipo) throws SQLException {
        this.conectar();
        String query = "select * from laboratorio.AgregarEquipo(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, equipo.getLaboratorio().getId());
            preparedStatement.setInt(2, equipo.getCategoriaEquipo().getId());
            preparedStatement.setString(3, equipo.getCodigo());
            preparedStatement.setString(4, equipo.getDescripcion());
            preparedStatement.setString(5, equipo.getMarca());
            preparedStatement.setString(6, equipo.getModelo());
            preparedStatement.setString(7, equipo.getNumeroSerie());
            preparedStatement.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setBoolean(9, equipo.getEstado());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw e;
        } finally {
            this.desconectar();
        }
    }

    public void eliminarEquipo(int equipoId) throws SQLException {
        this.conectar();
        String query = "DELETE FROM laboratorio.equipo WHERE id = ?";

        try {
            connection.setAutoCommit(false);

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, equipoId);
            preparedStatement.executeUpdate();

            connection.commit(); }
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            this.desconectar();
        }
    }




}
