package Controller;

import Model.Persona;
import global.Conexion;
import jakarta.enterprise.context.SessionScoped;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import lombok.Data;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;


import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


@Data
@Named
@SessionScoped
public class ArchivoBean implements Serializable {
    private static final long serialVersionUID = 1L;
    //Credenciales para la conexion
    private String url = "jdbc:postgresql://suleiman.db.elephantsql.com:5432/oimnmvdt";
    private String usuario = "oimnmvdt";
    private String clave = "VaEhCszNDGavWsHsd1HvdJ6OM9I2KM9f";


    private UploadedFile file;

    String directorio = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "uploads/";

    //private String directorio = "/resources/pdf/";
    private String nombreArchivo;

    public void handleFileUpload(FileUploadEvent event) {
        try {
            UploadedFile archivo = event.getFile();
            nombreArchivo = archivo.getFileName();
            InputStream inputStream = archivo.getInputStream();
            OutputStream outputStream = new FileOutputStream(new File(directorio + nombreArchivo));
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            System.out.println("Ruta del archivo guardado: " + directorio);
            FacesMessage message = new FacesMessage("Exito", archivo.getFileName() + " se subió correctamente.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (IOException e) {
            e.printStackTrace();
            FacesMessage message = new FacesMessage("Error", event.getFile().getFileName() + " no se pudo subir.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void guardarArchivo() {
        FacesMessage message = new FacesMessage("Exito", nombreArchivo + " se guardó correctamente en " + directorio);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }



    public void save() throws SQLException {
        try {
            if(file !=null){
                Class.forName("org.postgresql.Driver");
                Connection cn =  DriverManager.getConnection(url, usuario, clave);
                PreparedStatement st = cn.prepareStatement("insert into laboratorio.archivos (datos) values (?);");
                st.setBinaryStream(1,file.getInputStream());
                st.executeUpdate();
                cn.close();
                FacesMessage message = new FacesMessage("Exito",file.getFileName()+ " fue subido.");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage("ERROR",file.getFileName()+ " no fue subido.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            }
    }






}