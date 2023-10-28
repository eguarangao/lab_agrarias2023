package DAO;

import Model.*;
import global.Conexion;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MantenimientoDAO extends Conexion {

    public List<MantenimientoEquipo> listarMantenimientoEquipoPorLaboratorio(int LaboID) throws SQLException {
        List<MantenimientoEquipo> ListMantenimientos = new ArrayList<>();
        this.conectar();
        String query = "select * from laboratorio.listarequiposmantenimientoequipoid(?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, LaboID);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    MantenimientoEquipo mantenimientoEquipo = new MantenimientoEquipo();

                    Equipo equipo = new Equipo();
                    Aula aula = new Aula();

                    mantenimientoEquipo.setId_mant_equipo(resultSet.getInt("id_mantenimientoequipo"));
                    mantenimientoEquipo.setFecha_retorno_equipo(resultSet.getDate("fecha_retorno_equipo"));

                    equipo.setId(resultSet.getInt("equipo_id"));
                    equipo.setDescripcion(resultSet.getString("descripcion"));
                    mantenimientoEquipo.setEquiposRequeridosMantenimiento(new ArrayList<>());

                    mantenimientoEquipo.setEstado(resultSet.getBoolean("estado_me"));

                    aula.setId(resultSet.getInt("id_aula"));
                    aula.setNombre(resultSet.getString("aula"));

                    mantenimientoEquipo.setEquipo(equipo);
                    mantenimientoEquipo.setAula(aula);


                    ListMantenimientos.add(mantenimientoEquipo);

                }
                System.out.println("Ya liste Mantenimiento equipos");
            }
        } finally {
            this.desconectar();
        }
        return ListMantenimientos;
    }

    public List<Mantenimiento> listarMantenimientoPorLaboratorio(int LaboID) throws SQLException {
        List<Mantenimiento> ListMantenimientos = new ArrayList<>();

        ResultSet rs = null;
        PreparedStatement st = null;

        try{
        this.conectar();
        String query = "select * from laboratorio.listarmantenimientosporlaboratorioperfect(?)";


            st = this.getConnection().prepareStatement(query);
            st.setInt(1, LaboID);
            rs = st.executeQuery();

            Map<Integer, Mantenimiento> mantenimientoMap = new HashMap<>();

            while (rs.next()) {
                int mantenimientoID = rs.getInt("id_mantenimiento");

                if (!mantenimientoMap.containsKey(mantenimientoID)) {
                    Mantenimiento mantenimiento = new Mantenimiento();
                    TipoMantenimiento tipoMantenimiento = new TipoMantenimiento();

                    mantenimiento.setId(mantenimientoID);

                    tipoMantenimiento.setId(rs.getInt("tipo_mantenimiento_id"));
                    tipoMantenimiento.setTipo(rs.getString("tipo"));
                    mantenimiento.setTipoMantenimiento(tipoMantenimiento);

                    mantenimiento.setDescripcion_mante(rs.getString("descripcion_mante"));
                    mantenimiento.setEstado(rs.getBoolean("estado_mantenimiento"));
                    mantenimiento.setFechaRegistro(rs.getDate("fecha_registro"));
                    mantenimiento.setFechaRetorno(rs.getDate("fecha_retorno"));
                    mantenimiento.setLisMantenimientoEquipo(new ArrayList<>());

                    mantenimientoMap.put(mantenimientoID,mantenimiento);
                    ListMantenimientos.add(mantenimiento);

                   }

                    MantenimientoEquipo mantenimientoEquipo = new MantenimientoEquipo();
                    Equipo equipo = new Equipo();
                    Aula aula = new Aula();

                    mantenimientoEquipo.setId_mant_equipo(rs.getInt("id_mantenimientoequipo"));
                    mantenimientoEquipo.setFecha_retorno_equipo(rs.getDate("fecha_retorno_equipo"));

                    equipo.setId(rs.getInt("equipo_id"));
                    equipo.setDescripcion(rs.getString("descripcion"));
                    mantenimientoEquipo.setEquipo(equipo);

                    mantenimientoEquipo.setEstado(rs.getBoolean("estado_me"));

                    aula.setId(rs.getInt("id_aula"));
                    aula.setNombre(rs.getString("aula"));
                    mantenimientoEquipo.setAula(aula);

                    mantenimientoMap.get(mantenimientoID).getLisMantenimientoEquipo().add(mantenimientoEquipo);

                }
                System.out.println("Ya liste Mantenimiento equipos por laboratorio");
        } catch (Exception e) {
            System.out.println("Error:");
            e.printStackTrace();
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            this.desconectar();
        }

        System.out.println("LISTA DE MANTENIMIENTOS");
        System.out.println(ListMantenimientos);

        return ListMantenimientos;
    }

    public List<Equipo> listarEquiposPorLaboratorioActivos(int LaboID) throws SQLException {
        List<Equipo> Listequipos = new ArrayList<>();
        this.conectar();
        String query = "select * from laboratorio.listarequiposporlaboratorioactivos(?)";

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

    public List<TipoMantenimiento> listartipomantenimientos() throws SQLException {
        List<TipoMantenimiento> ListTiposMantenimientos = new ArrayList<>();
        this.conectar();
        String query = "select * from laboratorio.listatipomantenimientos()";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            try(ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    TipoMantenimiento tipoMantenimiento = new TipoMantenimiento();

                    tipoMantenimiento.setId(resultSet.getInt("id"));
                    tipoMantenimiento.setTipo(resultSet.getString("tipo"));

                    ListTiposMantenimientos.add(tipoMantenimiento);
                }
            }
        } finally {
            this.desconectar();
        }
        return ListTiposMantenimientos;
    }

    public void agregarMantenimiento(Mantenimiento mantenimiento, List<Equipo> equipoIDS){

        this.conectar();

        String query = "select * from laboratorio.agregarmantenimientoequipo(?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setString(2,mantenimiento.getDescripcion_mante());

            Object[] equipoIdsArray = equipoIDS.toArray();

            Array array = connection.createArrayOf("integer", equipoIdsArray);
                preparedStatement.setArray(3,array);
                preparedStatement.setInt(4, mantenimiento.getTipoMantenimiento().getId());
                preparedStatement.executeQuery();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            this.desconectar();
        }

    }

    public void editarMantenimiento(Mantenimiento mantenimiento){
        try  {
            System.out.println("Estoy editando MANTENIMIENTO:" + mantenimiento.getId());
            this.conectar();
            String query = "SELECT laboratorio.editarmantenimiento(?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, mantenimiento.getId());
                preparedStatement.setInt(2, mantenimiento.getTipoMantenimiento().getId());
                preparedStatement.setString(3, mantenimiento.getDescripcion_mante());

                preparedStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            this.desconectar();
        }
    }


    public void ConfirmarMantenimientoRealizado(Mantenimiento mantenimiento) {
        try  {
            System.out.println("Este es el ID MANTENIMIENTO:" + mantenimiento.getId());
            this.conectar();
            String query = "SELECT laboratorio.mantenimientorealizado(?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, mantenimiento.getId());
                preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            this.desconectar();
        }
    }

    public void ConfirmarMantenimientoRealizadoEquipo(MantenimientoEquipo mantenimientoEquipo, Mantenimiento mantenimiento) {
        try  {
            System.out.println("Este es el ID del equipo del mantenimiento realizado:" + mantenimiento.getId());
            this.conectar();
            String query = "SELECT laboratorio.mantenimientorealizadoaequipoperfect(?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, mantenimiento.getId());
                preparedStatement.setInt(2, mantenimientoEquipo.getEquipo().getId());
                preparedStatement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));

                preparedStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            this.desconectar();
        }
    }

    public void eliminarMantenimiento(int manteId) throws SQLException {
        this.conectar();
        String query = "SELECT laboratorio.eliminarmantenimiento(?)";

        try {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, manteId);
                preparedStatement.execute();

                connection.commit(); }
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            this.desconectar();
        }
    }

    public void eliminarelEquipodeMantenimiento(int manteId, int equipID) throws SQLException {
        this.conectar();
        String query = "SELECT laboratorio.eliminarequipodelmantenimiento(?, ?)";

        try {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, manteId);
                preparedStatement.setInt(2,equipID);
                preparedStatement.execute();

                connection.commit(); }
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            this.desconectar();
        }
    }

    public boolean verificarexisteidmante(int manteId) {
        try {
            this.conectar();
            String query = "SELECT laboratorio.verificarexisteidmantenimiento(?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, manteId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getBoolean(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.desconectar();
        }

        return false;
    }

}
