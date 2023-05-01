package DAO;

import global.Conexion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SolicitudDAO extends Conexion {
    public void create(String aux){
        try {
            this.conectar();
            PreparedStatement st = this.getConnection().prepareStatement("insert into laboratorio.excel_estudiante(file_name) values (?)");
            st.setString(1,aux);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                EXCEL excel= new EXCEL();
                excel.setTitulo(rs.getString("file_name"));
                System.out.println(excel);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("Error...Ebert");
            e.getMessage();
        } finally {
            this.desconectar();
        }
    }
}
