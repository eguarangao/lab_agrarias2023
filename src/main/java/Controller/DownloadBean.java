package Controller;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.*;

@Named
@SessionScoped
@Data
public class DownloadBean implements Serializable {
    //Credenciales para la conexion
    private String url = "jdbc:postgresql://suleiman.db.elephantsql.com:5432/oimnmvdt";
    private String usuario = "oimnmvdt";
    private String clave = "VaEhCszNDGavWsHsd1HvdJ6OM9I2KM9f";
    //
    private StreamedContent file;
    private int codigo;


//    public void download() throws ClassNotFoundException {
//        ResultSet rs;
//
//        try{
//            Class.forName("org.postgresql.Driver");
//            Connection cn =  DriverManager.getConnection(url, usuario, clave);
//            PreparedStatement st = cn.prepareStatement("select datos from laboratorio.archivos where id= (?);");
//            st.setInt(1,codigo);
//
//            rs = st.executeQuery();
//
//            while(rs.next()){
//                InputStream stream = rs.getBinaryStream("datos");
//                file = new DefaultStreamedContent(stream,"image/jpg","descarga.jpg");
//            }
//            cn.close();
//            FacesMessage message = new FacesMessage("Exito","fue descargado");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//        } catch (Exception e){
//            FacesMessage message = new FacesMessage("Error de conexión");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//        }
//    }


//    public void download() throws SQLException, IOException, ClassNotFoundException {
//        Class.forName("org.postgresql.Driver");
//        Connection cn = DriverManager.getConnection(url, usuario, clave);
//        PreparedStatement st = cn.prepareStatement("select datos from laboratorio.archivos where id = (?)");
//        st.setInt(1, codigo);
//        ResultSet rs = st.executeQuery();
//        if (rs.next()) {
//            InputStream inputStream = rs.getBinaryStream("datos");
//            // Obtener el nombre del archivo
//            String fileName = "archivo_" + codigo + ".pdf"; // O cualquier nombre que quieras darle
//            // Configurar la respuesta HTTP para descargar el archivo
//            FacesContext fc = FacesContext.getCurrentInstance();
//            ExternalContext ec = fc.getExternalContext();
//            ec.responseReset();
//            ec.setResponseContentType("application/pdf"); // Cambiar el tipo de contenido según el tipo de archivo
//            ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
//            // Escribir el archivo en la respuesta HTTP
//            OutputStream outputStream = ec.getResponseOutputStream();
//            byte[] buffer = new byte[4096];
//            int length;
//            while ((length = inputStream.read(buffer)) > 0) {
//                outputStream.write(buffer, 0, length);
//            }
//            // Cerrar los streams y la conexión
//            outputStream.flush();
//            outputStream.close();
//            inputStream.close();
//            cn.close();
//            // Terminar el ciclo de vida de JSF para evitar excepciones
//            fc.responseComplete();
//        } else {
//            cn.close();
//            FacesMessage message = new FacesMessage("ERROR", "El archivo no existe.");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//        }
//    }

    public void download() throws SQLException, IOException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        Connection cn = DriverManager.getConnection(url, usuario, clave);
        PreparedStatement st = cn.prepareStatement("select datos from laboratorio.archivos where id = (?)");
        st.setInt(1, codigo);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            InputStream inputStream = rs.getBinaryStream("datos");
            String fileName = "archivo_" + codigo + ".pdf"; // o cualquier nombre base de archivo que prefiera
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ec.responseReset();
            ec.setResponseContentType("application/octet-stream");
            ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            OutputStream outputStream = ec.getResponseOutputStream();
            IOUtils.copy(inputStream, outputStream);
            fc.responseComplete();
            cn.close();
        } else {
            cn.close();
            FacesMessage message = new FacesMessage("ERROR", "El archivo no existe.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }


//    public void download() throws SQLException, IOException, ClassNotFoundException {
//        Class.forName("org.postgresql.Driver");
//        Connection cn = DriverManager.getConnection(url, usuario, clave);
//        PreparedStatement st = cn.prepareStatement("select datos from laboratorio.archivos where id = (?)");
//        st.setInt(1, codigo);
//        ResultSet rs = st.executeQuery();
//        if (rs.next()) {
//            InputStream inputStream = rs.getBinaryStream("datos");
//            String fileName = "archivo_" + codigo + ".pdf"; // o cualquier nombre base de archivo que prefiera
//            FacesContext fc = FacesContext.getCurrentInstance();
//            ExternalContext ec = fc.getExternalContext();
//            ec.responseReset();
//            ec.setResponseContentType("application/octet-stream");
//            ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
//            OutputStream outputStream = ec.getResponseOutputStream();
//
//            // Copiar en bloques de 4KB
//            byte[] buffer = new byte[4096];
//            int bytesRead;
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, bytesRead);
//            }
//
//            outputStream.flush();
//            outputStream.close();
//            inputStream.close();
//            fc.responseComplete();
//            cn.close();
//        } else {
//            cn.close();
//            FacesMessage message = new FacesMessage("ERROR", "El archivo no existe.");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//        }
//    }


//    public void download() throws SQLException, IOException, ClassNotFoundException {
//        Class.forName("org.postgresql.Driver");
//        Connection cn = DriverManager.getConnection(url, usuario, clave);
//        PreparedStatement st = cn.prepareStatement("select datos from laboratorio.archivos where id = (?)");
//        st.setInt(1, codigo);
//        ResultSet rs = st.executeQuery();
//        if (rs.next()) {
//            InputStream inputStream = rs.getBinaryStream("datos");
//            String fileName = "archivo_" + codigo + ".jpg"; // o cualquier nombre base de archivo que prefiera
//            FacesContext fc = FacesContext.getCurrentInstance();
//            ExternalContext ec = fc.getExternalContext();
//            ec.responseReset();
//            ec.setResponseContentType("application/octet-stream");
//            ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
//            OutputStream outputStream = ec.getResponseOutputStream();
//
//            byte[] buffer = new byte[4096];
//            int bytesRead;
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, bytesRead);
//            }
//            outputStream.flush();
//            outputStream.close();
//            inputStream.close();
//
//            fc.responseComplete();
//            cn.close();
//        } else {
//            cn.close();
//            FacesMessage message = new FacesMessage("ERROR", "El archivo no existe.");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//        }
//    }




}
