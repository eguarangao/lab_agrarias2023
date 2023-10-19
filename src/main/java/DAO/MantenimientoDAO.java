package DAO;

import Model.*;
import global.Conexion;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class MantenimientoDAO extends Conexion {

    public List<MantenimientoEquipo> listarMantenimientoPorLaboratorio(int LaboID) throws SQLException {
        List<MantenimientoEquipo> ListMantenimientos = new ArrayList<>();
        this.conectar();
        String query = "select * from laboratorio.listarmanteequipoporlaboratorio(?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, LaboID);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    MantenimientoEquipo mantenimientoEquipo = new MantenimientoEquipo();
                    Mantenimiento mantenimiento = new Mantenimiento();
                    TipoMantenimiento tipoMantenimiento =new TipoMantenimiento();
                    
                    Equipo equipo = new Equipo();
                    Aula aula = new Aula();

                    mantenimientoEquipo.setId_mant_equipo(resultSet.getInt("id_mantenimientoequipo"));

                    mantenimiento.setId(resultSet.getInt("mantenimiento_id"));
                    mantenimiento.setFechaRegistro(resultSet.getDate("fecha_registro"));
                    mantenimiento.setEstado(resultSet.getBoolean("estado_mantenimiento"));
                    mantenimiento.setFechaRetorno(resultSet.getDate("fecha_retorno"));

                    equipo.setId(resultSet.getInt("equipo_id"));
                    equipo.setDescripcion(resultSet.getString("descripcion"));

                    mantenimientoEquipo.setEstado(resultSet.getBoolean("estado_me"));

                    tipoMantenimiento.setId(resultSet.getInt("tipo_mantenimiento_id"));
                    tipoMantenimiento.setTipo(resultSet.getString("tipo"));

                    aula.setId(resultSet.getInt("id_aula"));
                    aula.setNombre(resultSet.getString("aula"));

                    mantenimientoEquipo.setMantenimiento(mantenimiento);
                    mantenimientoEquipo.setEquipo(equipo);
                    mantenimientoEquipo.setTipoMantenimiento(tipoMantenimiento);
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

    public void agregarMantenimiento(MantenimientoEquipo mantenimientoEquipo, List<Equipo> equipoIDS){

        this.conectar();

        String query = "select * from laboratorio.agregarmantenimientoequipo(?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));

            Object[] equipoIdsArray = equipoIDS.toArray();

            Array array = connection.createArrayOf("integer", equipoIdsArray);
                preparedStatement.setArray(2,array);
                preparedStatement.setInt(3, mantenimientoEquipo.getTipoMantenimiento().getId());
                preparedStatement.executeQuery();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            this.desconectar();
        }

    }

    public void editarMantenimiento(MantenimientoEquipo mantenimientoEquipo){
        this.conectar();
    }


}
