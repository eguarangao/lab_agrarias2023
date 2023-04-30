package Controller;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Data;

import java.io.*;




import java.io.File;

@Data
@Named
@ViewScoped
public class SolicitudBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String directorio = "/resources/pdf/";
    private String nombreArchivo;

//    public void handleFileUpload(FileUploadEvent event) {
//        try {
//            nombreArchivo = event.getFile().getFileName();
//            InputStream inputStream = event.getFile().getInputstream();
//            OutputStream outputStream = new FileOutputStream(new File(directorio + nombreArchivo));
//            int read = 0;
//            byte[] bytes = new byte[1024];
//            while ((read = inputStream.read(bytes)) != -1) {
//                outputStream.write(bytes, 0, read);
//            }
//            outputStream.flush();
//            outputStream.close();
//            inputStream.close();
//            FacesMessage message = new FacesMessage("Exito", event.getFile().getFileName() + " se subió correctamente.");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//        } catch (IOException e) {
//            e.printStackTrace();
//            FacesMessage message = new FacesMessage("Error", event.getFile().getFileName() + " no se pudo subir.");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//        }
//    }
//
//    public void guardarArchivo() {
//        FacesMessage message = new FacesMessage("Exito", nombreArchivo + " se guardó correctamente en " + directorio);
//        FacesContext.getCurrentInstance().addMessage(null, message);
//    }
}
