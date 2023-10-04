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
import org.primefaces.event.SelectEvent;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    int idLaboratorio=0;
    int idLaboratorio2;
    int idHorario;
    Date fecha;
    String tipoSolicitud;
    String fechaReserva2;
    Date fechaEspecifica;
    Date fechaReserva;
    String fechaString = "2023-05-01";
    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");


    List<Item> items;
    List<Laboratorio> laboratorios;
    List<Horario> horarios;
    List<Equipo> equipos;

    List<Item> itemsSelecionados;


    //select


    private List<Boolean> selectedItems;

    // Inicializar la lista de elementos y la lista de selecciones


    public void cargarHorarios() {
        if ( idLaboratorio > 0) {  // Asegúrate de tener una fecha y un laboratorio seleccionados
            // Convertir Date a String con el formato deseado
            String fechaFormateada = formatoFecha.format(fechaReserva.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
            horarios = horarioDAO.findByLaboratorioIdAndFecha(idLaboratorio, fechaFormateada);
        } else {
            horarios = new ArrayList<>();  // Si no hay fecha o laboratorio seleccionados, limpia la lista
        }
    }
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

    public void validarSeleccion() {
        int seleccionados = 0;
        for (Boolean item : selectedItems) {
            if (item != null && item) {
                seleccionados++;
            }
        }

        if (seleccionados > 3) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Solo se permiten un máximo de 3 selecciones.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            // Puedes reiniciar la selección aquí o tomar alguna otra acción
        }
    }


    // Crear un objeto DateTimeFormatter con el formato deseado
    DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public void redireccionar() throws IOException {
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





    String itemSeleccionado;
    public void listHoras() {
        horarios = new ArrayList<>();
        items = new ArrayList<>();
        System.out.println("#########################");
        System.out.println(idLaboratorio);

//        System.out.println(listaPrueba);
//        System.out.println(fechaEspecifica);

        horarioDAO = new HorarioDAO();
        // horarioListforLaboratorio = new ArrayList<>();
        horarios = horarioDAO.horarioForLaboratorio(idLaboratorio, formato.format(fechaReserva));
        System.out.println("#########################TAMAÑO LISTA");
        System.out.println(horarios.size());
        System.out.println(horarios);
        System.out.println("#########################ID LABORATORIO");
        System.out.println(idLaboratorio);

        System.out.println("#########################FECHA RESERVA DATE");
        System.out.println(fechaReserva);

        System.out.println("#########################FECHA RESERVA STRING");
        System.out.println(formato.format(fechaReserva));
        // Boolean iten = String.valueOf(horarioListforLaboratorio.get(i).isJornada1() + " " + horarioListforLaboratorio.get(i).is);

        Item item1 = new Item();
        item1.setId(1);
        item1.setDato(horarios.get(0).isJornada1());
        item1.setFecha("07:30 am / 08:29 am");

        Item item2 = new Item();
        item2.setId(2);
        item2.setDato(horarios.get(0).isJornada2());
        item2.setFecha("08:30 am / 09:29 am");

        Item item3 = new Item();
        item3.setId(3);
        item3.setDato(horarios.get(0).isJornada3());
        item3.setFecha("09:30 pm / 10:29 am ");

        Item item4 = new Item();
        item4.setId(4);
        item4.setDato(horarios.get(0).isJornada4());
        item4.setFecha("14:00 pm / 16:00 pm");

        Item item5 = new Item();
        item5.setId(5);
        item5.setDato(horarios.get(0).isJornada5());
        item5.setFecha("10:30 am / 11:29 pm");


        Item item6 = new Item();
        item6.setId(6);
        item6.setDato(horarios.get(0).isJornada6());
        item6.setFecha("15:00 pm / 15:59 pm");

        Item item7 = new Item();
        item7.setId(7);
        item7.setDato(horarios.get(0).isJornada7());
        item7.setFecha("14:00 pm / 16:59 pm");

        Item item8 = new Item();
        item8.setId(8);
        item8.setDato(horarios.get(0).isJornada8());
        item8.setFecha("17:00 pm / 18:00 pm");

        items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(item4);
        items.add(item5);
        items.add(item6);
        items.add(item7);
        items.add(item8);

        // Hacer algo con el objeto "equipo" en cada iteración


        System.out.println("ITEMS LISTAAAAAAAAAAAAAAAAAAAAAAAA");
        System.out.println(items.size());
        System.out.println(items);




        // Llama al método validarSeleccion para realizar la validación inicial si es necesario

    }


    @PostConstruct
    public void init() {
        try {
            // Llama al método para cargar los laboratorios al iniciar el bean.
            finAllLaboratorio();

            //horarios = horarioDAO.findByLaboratorioIdAndFecha(idLaboratorio, fechaReserva.toString());
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