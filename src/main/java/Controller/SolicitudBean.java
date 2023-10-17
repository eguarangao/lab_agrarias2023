package Controller;

import DAO.*;
import Model.*;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.primefaces.PrimeFaces;
import org.primefaces.model.file.UploadedFile;

import java.io.Serial;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Named
@ViewScoped
public class SolicitudBean implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    //DAO
    LaboratorioDAO laboratorioDAO;
    SolicitudDAO solicitudDAO;
    HorarioDAO horarioDAO;
    EquipoDAO equipoDAO;
    DocenteDAO docenteDAO;
    Docente docente;
    PeriodoDAO periodoDAO;
    UsuarioDAO usuarioDAO;

    Periodo periodo;

    UsuarioBean usuarioBean;

    Horario horario = new Horario();

    Horario horarioBD = new Horario();


    Solicitud solicitud = new Solicitud();

    List<Periodo> periodos;
    private Date fechaMinima; // La fecha mínima permitida

    int idLaboratorio = 0;
    int idLaboratorio2;
    int idHorario;
    int idUsuarioSession = 0;
    int idPerido = 0;
    Date fecha;
    String tipoSolicitud;
    String fechaReserva2;
    Date fechaEspecifica;
    Date fechaReserva;
    String nombreDocente;
    String fechaString = "2023-05-01";
    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

    private UploadedFile fileResolucionPDF;
    private UploadedFile fileListaEstudiantes;


    boolean desactivarElementos;


    private List<Item> items;
    List<Laboratorio> laboratorios;
    List<Horario> horarios;
    List<Equipo> equipos;

    List<Solicitud> solicitudes;

    List<Equipo> equiposRequeridos;

    private List<Item> itemsSelecionados;
    private List<Item> elementosDeshabilitados = new ArrayList<>();
    String value;

    public void imprimir() {
        System.out.println("Items seleccionados");
        System.out.println(itemsSelecionados);
        System.out.println("Items seleccionados tamaño lista");
        System.out.println(itemsSelecionados.size());


        validarSeleccionHoras();


    }


    public void save() {
        try {
            //Asignación de Laboratorio
            Laboratorio laboratorio = new Laboratorio();
            laboratorio.setId(idLaboratorio);
            solicitud.setLaboratorio(laboratorio);

            //Asignación de Horario
            Horario horario1 = new Horario();
            horario1 = asignarHoras();
            solicitud.setHorario(horario1);

            //Asignamos tipo de Solicitud
            solicitud.setTipo(tipoSolicitud);

            //Asignar la fecha en la que se desea reservar
            solicitud.setFechaReserva(fechaReserva);

            //Asignar los equipos requeridos en la solicitud
            solicitud.setEquipos(equiposRequeridos);

            //Asignamos el Docente que realiza la solicitud
            solicitud.setDocente(findByDocenteID());

            //Asignamos Periodo
            periodo.setId(idPerido);
            solicitud.setPeriodo(periodo);

            System.out.println("Solicitud Empaquetada");
            System.out.println(solicitud);

            solicitudDAO.save(solicitud, fileResolucionPDF, fileListaEstudiantes);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Se registró correctamente"));
            // Llama a la función JavaScript para restablecer el formulario
//            PrimeFaces.current().executeScript("resetForm();");
            PrimeFaces.current().executeScript("refreshForm();");
            PrimeFaces.current().executeScript("resetForm()");
        } catch (SQLException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ocurrió un error en el registro, vuelva a intentarlo"));
            e.printStackTrace(); // Puedes imprimir el stack trace para depuración
        }
    }


    public void FindAllEquipos() throws SQLException {
        equipoDAO = new EquipoDAO();
        equipos = new ArrayList<>();
        System.out.println("id laboratorio");
        System.out.println(idLaboratorio);

        equipos = equipoDAO.findByLaboratorioID(idLaboratorio);
    }

    int seleccionCount = 0;
    int MAX_SELECCIONES = 3;


    public Horario asignarHoras() throws SQLException {
        // Crear un objeto de Horario
        Horario myHorario = new Horario();
        List<Item> itemsBdInvertidos = items;

        // Fusionar las listas
        for (Item selectedItem : itemsSelecionados) {
            int selectedItemId = selectedItem.getId();
            for (Item item : itemsBdInvertidos) {
                if (item.getId() == selectedItemId) {
                    item.setDato(false);
                    break;
                }
            }
        }

        for (Item selectedItem : itemsBdInvertidos) {
            boolean verifica = selectedItem.isDato();
            int selectedItemId = selectedItem.getId();

            // Usar un switch para asignar los valores a los atributos de Horario basados en 'verifica'
            switch (selectedItemId) {
                case 1:
                    myHorario.setJornada1(verifica);
                    break;
                case 2:
                    myHorario.setJornada2(verifica);
                    break;
                case 3:
                    myHorario.setJornada3(verifica);
                    break;
                case 4:
                    myHorario.setJornada4(verifica);
                    break;
                case 5:
                    myHorario.setJornada5(verifica);
                    break;
                case 6:
                    myHorario.setJornada6(verifica);
                    break;
                case 7:
                    myHorario.setJornada7(verifica);
                    break;
                case 8:
                    myHorario.setJornada8(verifica);
                    break;
                default:
                    // Manejar casos no válidos, si es necesario
                    break;
            }
        }
        myHorario.setFecha(fechaReserva);
        System.out.println("Horario generado:");
        System.out.println(myHorario);

        findByDocenteID();
        return myHorario;
    }


    public void validarSeleccionHoras() {
        String mensajeError = "";

        // Hacer una copia de la selección anterior
        List<Item> seleccionAnterior = new ArrayList<>(itemsSelecionados);

        if (itemsSelecionados != null && itemsSelecionados.size() > MAX_SELECCIONES) {
            mensajeError = "No se puede seleccionar más de " + MAX_SELECCIONES + " horas.";
            elementosDeshabilitados.addAll(itemsSelecionados.subList(MAX_SELECCIONES, itemsSelecionados.size()));
            // Eliminar los elementos seleccionados en exceso
            itemsSelecionados.subList(MAX_SELECCIONES, itemsSelecionados.size()).clear();
        } else if (itemsSelecionados != null && itemsSelecionados.size() > 1) {
            List<Integer> indicesSeleccionados = new ArrayList<>();
            for (Item item : itemsSelecionados) {
                indicesSeleccionados.add(item.getId());
            }

            Collections.sort(indicesSeleccionados);

            boolean sonConsecutivos = true;
            for (int i = 0; i < indicesSeleccionados.size() - 1; i++) {
                if (indicesSeleccionados.get(i + 1) != indicesSeleccionados.get(i) + 1) {
                    sonConsecutivos = false;
                    break;
                }
            }

            if (!sonConsecutivos) {
                mensajeError = "Solo se permite seleccionar horas consecutivas.";
                // Eliminar solo el último elemento no consecutivo
                itemsSelecionados.remove(seleccionAnterior.get(seleccionAnterior.size() - 1));
            }
        }

        if (!mensajeError.isEmpty()) {
            // Mostrar mensaje de error
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Aviso: ", mensajeError));
        }
    }




//    public void redireccionar() throws IOException {
//        FacesContext context = FacesContext.getCurrentInstance();
//        ExternalContext externalContext = context.getExternalContext();
//        String contextPath = externalContext.getRequestContextPath();
//
//        if ("SOLICITUD PRÁCTICAS ESTUDIANTES".equals(tipoSolicitud)) {
//            externalContext.redirect(contextPath + "/views/solicitudesDocente/registroSolicitud.xhtml");
//            System.out.println("holaaaaaaaaaaaaa" + tipoSolicitud);
//        } else if ("SOLICITUD PRÁCTICAS DE PROYECTO DE INVESTIGACÓN".equals(tipoSolicitud)) {
//            externalContext.redirect(contextPath + "/views/solicitudesDocente/registroSolicitud.xhtml");
//            System.out.println("holaaaaaaaaaaaaa" + tipoSolicitud);
//        } else if ("SOLICITUD PRÁCTICAS DE TESIS".equals(tipoSolicitud)) {
//            externalContext.redirect(contextPath + "/views/solicitudesDocente/registroSolicitud.xhtml");
//        }
//
//    }


    public void finAllLaboratorio() throws SQLException {
        laboratorios = new ArrayList<>();
        LaboratorioDAO laboratorioDAO = new LaboratorioDAO();
        laboratorios = laboratorioDAO.findAll();
    }

    public void listHoras() {
        horarios = new ArrayList<>();
        items = new ArrayList<>();
        horarioDAO = new HorarioDAO();
        // horarioListforLaboratorio = new ArrayList<>();
        horarios = horarioDAO.horarioForLaboratorio(idLaboratorio, formato.format(fechaReserva));

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

    }

    public Docente findByDocenteID() throws SQLException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Usuario usuarioLogueado = (Usuario) facesContext.getExternalContext().getSessionMap().get("usuario");
        idUsuarioSession = usuarioLogueado.getId();
        docente = docenteDAO.findByUsuarioID(idUsuarioSession).get(0);
        System.out.println("Este es eo docente logueado");
        System.out.println(docente);
        return docente;
    }

    public void findAllPeridosEnabled() {
        periodos = periodoDAO.listarPeriodosHabilitados();
    }

    public void findAllSolicitudes() throws SQLException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Usuario usuarioLogueado = (Usuario) facesContext.getExternalContext().getSessionMap().get("usuario");
        idUsuarioSession = usuarioLogueado.getId();
        solicitudes = solicitudDAO.findAll(idUsuarioSession);
        System.out.println("## SOLICITUD ");
        System.out.println(solicitudes);

    }

    @PostConstruct
    public void init() {
        try {

            fechaMinima = new Date(); // Configura la fecha mínima como la fecha actual al inicializar el bean

            // Llama al método para cargar los laboratorios al iniciar el bean.
            finAllLaboratorio();


            //Periodos
            periodo = new Periodo();
            periodos = new ArrayList<>();
            periodoDAO = new PeriodoDAO();
            findAllPeridosEnabled();

            //Docentes
            docente = new Docente();
            docenteDAO = new DocenteDAO();

            //Usaurios
            usuarioDAO = new UsuarioDAO();
            usuarioBean = new UsuarioBean();

            //Solicitud
            solicitudes = new ArrayList<>();
            //Equipos

            //Periodo
            periodo = new Periodo();

            solicitud = new Solicitud();
            itemsSelecionados = new ArrayList<>();


            solicitudDAO = new SolicitudDAO();

            desactivarElementos = false;

            FacesContext facesContext = FacesContext.getCurrentInstance();
            Usuario usuarioLogueado = (Usuario) facesContext.getExternalContext().getSessionMap().get("usuario");
            idUsuarioSession = usuarioLogueado.getId();
            nombreDocente = String.valueOf(docenteDAO.findByUsuarioID(getIdUsuarioSession()).get(0).getPersona().getApellido());


//            docenteDAO.findByUsuarioID(usuarioBean.getIdUsuarioSession());

            equiposRequeridos = new ArrayList<>();
            itemsSelecionados = new ArrayList<>();
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