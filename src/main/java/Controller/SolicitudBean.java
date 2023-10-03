package Controller;

import DAO.*;
import Model.*;
import jakarta.annotation.PostConstruct;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

     Solicitud solicitud =  new Solicitud();

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


//    Solicitud solicitud;
    SolicitudDAO solicitudDAO;
    Horario horario;
    List<Equipo> equipos;
    int idLaboratorio;
    List<Laboratorio> laboratorioList;
    Date fecha;
    List<Horario> horarioListforLaboratorio;
    LaboratorioDAO laboratorioDAO;
    HorarioDAO horarioDAO;
    List<Item> listaPrueba;
    boolean seleccionadoLaboratorio = false;
    int idHorario;
    int idItems;
    Date fechaDate = new Date();
    String fechaString = "2023-05-01";
    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
    Date fechaEspecifica;

    {
        try {
            fechaEspecifica = formato.parse(fechaString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isLaboratorio() {
        return seleccionadoLaboratorio = true;
    }

    public void finAllLaboratorio() throws SQLException {
        laboratorioList = new ArrayList<>();
        LaboratorioDAO laboratorioDAO = new LaboratorioDAO();
        laboratorioList = laboratorioDAO.findAllLaboratorio();
    }


    public void listHoras() {
        horarioListforLaboratorio = new ArrayList<>();
        listaPrueba = new ArrayList<>();
        System.out.println("#########################");
        System.out.println(idLaboratorio);

//        System.out.println(listaPrueba);
//        System.out.println(fechaEspecifica);

        horarioDAO = new HorarioDAO();
        // horarioListforLaboratorio = new ArrayList<>();
        horarioListforLaboratorio = horarioDAO.horarioForLaboratorio(idLaboratorio, formato.format(fechaDate));
        System.out.println("#########################TAMAÑO LISTA");
        System.out.println(horarioListforLaboratorio.size());
        System.out.println(horarioListforLaboratorio);
        System.out.println("#########################ID LA ORATORIO");
        System.out.println(idLaboratorio);
        // Boolean iten = String.valueOf(horarioListforLaboratorio.get(i).isJornada1() + " " + horarioListforLaboratorio.get(i).is);

        Item item1 = new Item();
        item1.setId(1);
        item1.setDato(horarioListforLaboratorio.get(0).isJornada1());
        item1.setFecha("08:00 am / 10:00 am");

        Item item2 = new Item();
        item2.setId(2);
        item2.setDato(horarioListforLaboratorio.get(0).isJornada2());
        item2.setFecha("10:00 am / 12:00 pm");

        Item item3 = new Item();
        item3.setId(3);
        item3.setDato(horarioListforLaboratorio.get(0).isJornada3());
        item3.setFecha("12:00 pm / 14:00 pm");

        Item item4 = new Item();
        item4.setId(4);
        item4.setDato(horarioListforLaboratorio.get(0).isJornada4());
        item4.setFecha("14:00 pm / 16:00 pm");

        Item item5 = new Item();
        item5.setId(5);
        item5.setDato(horarioListforLaboratorio.get(0).isJornada5());
        item5.setFecha("16:00 pm / 18:00 pm");

        listaPrueba.add(item1);
        listaPrueba.add(item2);
        listaPrueba.add(item3);
        listaPrueba.add(item4);
        listaPrueba.add(item5);

        // Hacer algo con el objeto "equipo" en cada iteración


        System.out.println("LISTAAAAAAAAAAAAAAAAAAAAAAAA");
        System.out.println(listaPrueba.size());
        System.out.println(listaPrueba);


    }

    String itemSeleccionado;

    public void saveSolicitud() throws SQLException {

        solicitud = new Solicitud();
        solicitudDAO = new SolicitudDAO();
        equipos = new ArrayList<>();
        try {
            Laboratorio laboratorio = new Laboratorio();
            laboratorio.setId(idLaboratorio);
            solicitud.setLaboratorio(laboratorio);
            Docente docente = new Docente();
            docente.setId(1);
            solicitud.setDocente(docente);

            solicitudDAO.save(solicitud, horario, equipos, idItems, formato.format(fechaDate));
        } catch (Exception e) {
            System.out.println(e.toString() + "EBERT ERROR");
        }
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
