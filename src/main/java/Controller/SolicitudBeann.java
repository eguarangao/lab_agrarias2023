package Controller;

import DAO.HorarioDAO;
import DAO.LaboratorioDAO;
import DAO.SolicitudDAO;
import Model.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Data;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Named
@ViewScoped
public class SolicitudBeann implements Serializable {

    private static final long serialVersionUID = 1L;
    //DAO
    LaboratorioDAO laboratorioDAO;
    SolicitudDAO solicitudDAO;
    HorarioDAO horarioDAO;

    Horario horario = new Horario();
    Solicitud solicitud = new Solicitud();

    int idLaboratorio;
    int idHorario;
    Date fecha;
    String tipoSolicitud;
    Date fechaEspecifica;
    String fechaString = "2023-05-01";
    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");


    List<Laboratorio> laboratorios;
    List<Horario> horarios;
    List<Equipo> equipos;


    public void getTipoSolicitudes(int opcion) {

        if (opcion == 1) {
            tipoSolicitud = "SOLICITUD PRACTICA DE ESTUDIANTE";
            solicitud.setTipo(tipoSolicitud);
        } else if (opcion == 2) {
            tipoSolicitud = "SOLICITUD PRACTICAS DE TESIS";
            solicitud.setTipo(tipoSolicitud);
        } else {
            tipoSolicitud = "SOLICITUD DE INVESTIGACIÓN";
            solicitud.setTipo(tipoSolicitud);
        }
    }

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


    {
        try {
            fechaEspecifica = formato.parse(fechaString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    public void finAllLaboratorio() throws SQLException {
        laboratorios = new ArrayList<>();
        LaboratorioDAO laboratorioDAO = new LaboratorioDAO();
        laboratorios = laboratorioDAO.findAll();
    }


    @PostConstruct
    public void init() {
        try {
            // Llama al método para cargar los laboratorios al iniciar el bean.
            finAllLaboratorio();
//            solicitud = new Solicitud();
        } catch (SQLException e) {
            // Maneja cualquier excepción que pueda ocurrir durante la carga de los laboratorios.
            e.printStackTrace();
            // También puedes agregar un mensaje de error para mostrar en la vista.
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al cargar los laboratorios", null));
        }
    }


}
