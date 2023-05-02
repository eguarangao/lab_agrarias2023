package Controller;

import DAO.LaboratorioDAO;
import DAO.RolDAO;
import Model.Laboratorio;
import Model.Rol;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Data;

import java.io.*;


import java.io.File;
import java.sql.SQLException;
import java.util.List;

@Data
@Named
@SessionScoped
public class SolicitudBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private String tipoSolicitud;
    private String directorio = "/resources/pdf/";
    private String nombreArchivo;
    private List<Laboratorio> listaLaboratorio;
    public void getTipoSolicitudes(int opcion) {
        if (opcion == 1) {
            tipoSolicitud = "SOLICITUD PRACTICA DE ESTUDIANTE";
        } else if (opcion == 2) {
            tipoSolicitud = "SOLICITUD PRACTICAS DE TESIS";
        } else {
            tipoSolicitud = "SOLICITUD DE INVESTIGACIÓN";
        }
    }


//    public void getTipoSolicitudes() {
//            tipoSolicitud = "SOLICITUD PRACTICA DE ESTUDIANTE";
//            System.out.println(tipoSolicitud);
//        }


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

//    public void redireccionar() throws IOException {
//        System.out.println("holaaaaaaaaaaaaa"+tipoSolicitud);
//        FacesContext context = FacesContext.getCurrentInstance();
//        ExternalContext externalContext = context.getExternalContext();
//        String contextPath = externalContext.getRequestContextPath();
//
//        if ("practicas".equals(tipoSolicitud)) {
//            externalContext.redirect(contextPath + "/views/solicitudes/newSolicitud.xhtml");
//            System.out.println("holaaaaaaaaaaaaa"+tipoSolicitud);
//        } else if ("investigacion".equals(tipoSolicitud)) {
//            externalContext.redirect(contextPath + "/views/solicitudes/newSolicitud.xhtml");
//            System.out.println("holaaaaaaaaaaaaa"+tipoSolicitud);
//        } else if ("tesis".equals(tipoSolicitud)) {
//            externalContext.redirect(contextPath + "/views/solicitudes/newSolicitud.xhtml");
//            System.out.println("holaaaaaaaaaaaaa"+tipoSolicitud);
//        }
//    }


    public void redireccionar() throws IOException {
        System.out.println("holaaaaaaaaaaaaa" + tipoSolicitud);
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        String contextPath = externalContext.getRequestContextPath();

        if ("SOLICITUD PRÁCTICAS ESTUDIANTES".equals(tipoSolicitud)) {
            externalContext.redirect(contextPath + "/views/solicitudes/newSolicitud.xhtml");
            System.out.println("holaaaaaaaaaaaaa" + tipoSolicitud);
        } else if ("SOLICITUD PRÁCTICAS DE PROYECTO DE INVESTIGACÓN".equals(tipoSolicitud)) {
            externalContext.redirect(contextPath + "/views/solicitudes/newSolicitud.xhtml");
            System.out.println("holaaaaaaaaaaaaa" + tipoSolicitud);
        } else if ("SOLICITUD PRÁCTICAS DE TESIS".equals(tipoSolicitud)) {
            externalContext.redirect(contextPath + "/views/solicitudes/newSolicitud.xhtml");
            System.out.println("holaaaaaaaaaaaaa" + tipoSolicitud);
        }

    }
//    public void listarLaboratorio() throws SQLException {
//        LaboratorioDAO laboratorioDAO;
//        try {
//            laboratorioDAO = new LaboratorioDAO();
//            listaLaboratorio = laboratorioDAO.findAllLaboratorio();
//        } catch (Exception e) {
//            throw e;
//        }
//    }

}
