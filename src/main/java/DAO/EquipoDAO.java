package DAO;

import Model.*;
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
            String sql = "select *\n" +
                    "from laboratorio.equipo eq\n" +
                    "         inner join laboratorio.aula au on eq.id_aula_equipo = au.id_aula\n" +
                    "         inner join laboratorio.laboratorio_aula la on au.id_aula = la.id_aula\n" +
                    "where la.id = ?;\n";
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

                    laboratorio.setId(resultSet.getInt("id_laboratorio"));
                    laboratorio.setNombre(resultSet.getString("nom_laboratorio"));

                    aula.setId(resultSet.getInt("id_aula"));
                    aula.setNombre(resultSet.getString("aula"));

                    categoriaEquipo.setId(resultSet.getInt("id_categoria_equipo"));
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

    public List<Equipo> listarEquiposPorLaboratorio(int LaboID) throws SQLException {
        List<Equipo> Listequipos = new ArrayList<>();
        this.conectar();
        String query = "select * from laboratorio.listarequiposporlaboratorio(?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, LaboID);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    Equipo equipo = new Equipo();
                    Aula aula = new Aula();
                    CategoriaEquipo categoriaEquipo = new CategoriaEquipo();
                    Laboratorio laboratorio = new Laboratorio();

                    equipo.setId(resultSet.getInt("id"));

                    laboratorio.setId(resultSet.getInt("id"));
                    laboratorio.setNombre(resultSet.getString("nom_laboratorio"));

                    aula.setId(resultSet.getInt("id_aula_equipo"));
                    aula.setNombre(resultSet.getString("aula"));

                    categoriaEquipo.setId(resultSet.getInt("id_categoria_equipo"));
                    categoriaEquipo.setNombre(resultSet.getString("categoria"));

                    equipo.setCodigo(resultSet.getString("codigo"));
                    equipo.setDescripcion(resultSet.getString("descripcion"));
                    equipo.setMarca(resultSet.getString("marca"));
                    equipo.setModelo(resultSet.getString("modelo"));
                    equipo.setNumeroSerie(resultSet.getString("num_serie"));
                    equipo.setFechaAdquisicion(resultSet.getDate("fecha_adquisicion"));
                    equipo.setEstado(resultSet.getBoolean("estado"));
                    equipo.setEstadoAveriaEquipo(resultSet.getBoolean("averia"));

                    equipo.setAula(aula);
                    equipo.setCategoriaEquipo(categoriaEquipo);
                    equipo.setLaboratorio(laboratorio);

                    Listequipos.add(equipo);
                }
                System.out.println("Ya liste equipos");
            }
        } finally {
            this.desconectar();
        }
        return Listequipos;
    }

    public void agregarEquipo(Equipo equipo) {

        this.conectar();
        String query = "select * from laboratorio.agregarnuevoequipo(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, equipo.getAula().getId());
            preparedStatement.setInt(2, equipo.getCategoriaEquipo().getId());
            preparedStatement.setString(3, equipo.getCodigo());
            preparedStatement.setString(4, equipo.getDescripcion());
            preparedStatement.setString(5, equipo.getMarca());
            preparedStatement.setString(6, equipo.getModelo());
            preparedStatement.setString(7, equipo.getNumeroSerie());
            preparedStatement.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setBoolean(9, true);

            preparedStatement.executeQuery();

        } catch (SQLException e) {
            throw new RuntimeException(e);
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

    public List<Laboratorio> listarlaboratoriosPorTecnico(int tecnicoID) throws SQLException {
        List<Laboratorio> ListLaboratorios = new ArrayList<>();
        this.conectar();
        String query = "select * from laboratorio.listarlaboratoriosPorTecnico(?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, tecnicoID);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {

                    Laboratorio laboratorio = new Laboratorio();

                    laboratorio.setId(resultSet.getInt("id_laboratorio"));
                    laboratorio.setNombre(resultSet.getString("nom_laboratorio"));

                    ListLaboratorios.add(laboratorio);
                }
            }
        } finally {
            this.desconectar();
        }
        return ListLaboratorios;
    }

    public List<Laboratorio> ListarLosLaboratorios() throws SQLException {
        List<Laboratorio> ListLaboratorios = new ArrayList<>();
        this.conectar();
        String query = "select * from laboratorio.listartodosloslaboratorios()";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            try(ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {

                    Laboratorio laboratorio = new Laboratorio();

                    laboratorio.setId(resultSet.getInt("id"));
                    laboratorio.setNombre(resultSet.getString("nom_laboratorio"));

                    ListLaboratorios.add(laboratorio);
                }
            }
        } finally {
            this.desconectar();
        }
        return ListLaboratorios;
    }

    public List<CategoriaEquipo> listarCategorias() throws SQLException {
        List<CategoriaEquipo> ListCategoriaEquipos = new ArrayList<>();
        this.conectar();
        String query = "select * from laboratorio.listarcategoriasequipos()";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            try(ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {

                    CategoriaEquipo categoriaEquipo = new CategoriaEquipo();

                    categoriaEquipo.setId(resultSet.getInt("id_categoria"));
                    categoriaEquipo.setNombre(resultSet.getString("categoria"));

                    ListCategoriaEquipos.add(categoriaEquipo);
                }
            }
        } finally {
            this.desconectar();
        }
        return ListCategoriaEquipos;
    }

    public List<Aula> ListarAulaPorLaboratorio(int laboratorioID) throws SQLException {
        List<Aula> ListAulas = new ArrayList<>();
        this.conectar();
        String query = "select * from laboratorio.listarareasporlaboratorio(?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, laboratorioID);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {

                    Aula aula = new Aula();

                    aula.setId(resultSet.getInt("id_aula"));
                    aula.setNombre(resultSet.getString("aula"));

                    ListAulas.add(aula);
                }
                System.out.println("Ya liste aulas");
            }
        } finally {
            this.desconectar();
        }
        return ListAulas;
    }

    public List<Aula> ListaAulas() throws SQLException {
        List<Aula> ListAulas = new ArrayList<>();
        this.conectar();
        String query = "select * from laboratorio.listaraulasequipos()";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            try(ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {

                    Aula aula = new Aula();

                    aula.setId(resultSet.getInt("id_aula"));
                    aula.setNombre(resultSet.getString("aula"));

                    ListAulas.add(aula);
                }
                System.out.println("Ya liste aulas al inicio");
            }
        } finally {
            this.desconectar();
        }
        return ListAulas;
    }

    public void editarEquipo(Equipo equipo) {
        try  {
            this.conectar();
            String query = "SELECT laboratorio.editarequipo(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, equipo.getId());
                preparedStatement.setInt(2, equipo.getAula().getId());
                preparedStatement.setInt(3, equipo.getCategoriaEquipo().getId());
                preparedStatement.setString(4, equipo.getCodigo());
                preparedStatement.setString(5, equipo.getDescripcion());
                preparedStatement.setString(6, equipo.getMarca());
                preparedStatement.setString(7, equipo.getModelo());
                preparedStatement.setString(8, equipo.getNumeroSerie());
                preparedStatement.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
                preparedStatement.setBoolean(10, equipo.getEstado());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            this.desconectar();
        }
    }
    public boolean VerificadorAdmin(int ID) throws SQLException {
        boolean verificador = false;
        try {
            this.conectar();
            String query = "select * from laboratorio.verificaradministrador(?)";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, ID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                verificador = resultSet.getBoolean(1);
            }

            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.desconectar();
        }

        return verificador;
    }

    public boolean VerificadorTecnic(int ID) throws SQLException {
        boolean verificador = false;

        try {
            this.conectar();
            String query = "select * from laboratorio.verificartecnico(?)";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, ID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                verificador = resultSet.getBoolean(1);
            }

            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.desconectar();
        }

        return verificador;
    }

}
