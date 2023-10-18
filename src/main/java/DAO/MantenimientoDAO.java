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

    public void agregarMantenimiento(MantenimientoEquipo mantenimientoEquipo){
        this.conectar();

    }

    public void editarMantenimiento(MantenimientoEquipo mantenimientoEquipo){
        this.conectar();
    }


}
