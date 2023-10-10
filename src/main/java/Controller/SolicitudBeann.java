package Controller;

import DAO.*;
import Model.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.primefaces.PrimeFaces;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Named
@SessionScoped
public class SolicitudBeann implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    //DAO
    LaboratorioDAO laboratorioDAO;
    SolicitudDAO solicitudDAO;
    HorarioDAO horarioDAO;
    EquipoDAO equipoDAO;
    DocenteDAO docenteDAO;

    UsuarioDAO usuarioDAO;

    UsuarioBean usuarioBean;

    Horario horario = new Horario();

    Horario horarioBD = new Horario();


    Solicitud solicitud = new Solicitud();

    int idLaboratorio = 0;
    int idLaboratorio2;
    int idHorario;
    int idUsuarioSession = 0;
    Date fecha;
    String tipoSolicitud;
    String fechaReserva2;
    Date fechaEspecifica;
    Date fechaReserva;
    String docente;
    String fechaString = "2023-05-01";
    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

    boolean desactivarElementos;


    private List<Item> items;
    List<Laboratorio> laboratorios;
    List<Horario> horarios;
    List<Equipo> equipos;
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

    public void save() throws SQLException {
        horario = asignarHoras(itemsSelecionados);
        horario.setFecha(fechaReserva);
        solicitudDAO.save2(solicitud, horario, idLaboratorio, tipoSolicitud);
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
//    public void validarSeleccionHoras() {
//
//         String mensajeError = "";
////        // Aquí puedes procesar los elementos seleccionados y guardarlos en tu lista
////        for (Item item : itemsSelecionados) {
////            // Realiza las acciones necesarias para guardar el elemento           // Puedes agregarlo a otra lista, guardar en una base de datos, etc.
////        }
//
////        if (itemsSelecionados != null && itemsSelecionados.size() > MAX_SELECCIONES) {
//        if (itemsSelecionados != null && itemsSelecionados.size() > MAX_SELECCIONES) {
//            mensajeError = "No se pueden seleccionar más de " + MAX_SELECCIONES + " horas.";
//            System.out.println(mensajeError);
//            // Deselecciona la última casilla marcada si se excede el límite
//            itemsSelecionados.remove(itemsSelecionados.size() - 1);
//
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Máximo de horas", mensajeError);
//            PrimeFaces.current().dialog().showMessageDynamic(message);
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(mensajeError));
//        } else {
//            mensajeError = "";
//        }
//    }

//    public void validarSeleccionHoras() {
//        String mensajeError = "";
//
//        if (itemsSelecionados != null && itemsSelecionados.size() > MAX_SELECCIONES) {
//            mensajeError = "No se pueden seleccionar más de " + MAX_SELECCIONES + " horas.";
//        } else if (itemsSelecionados != null && itemsSelecionados.size() > 1) {
//            List<Integer> indicesSeleccionados = new ArrayList<>();
//            for (Item item : itemsSelecionados) {
//                indicesSeleccionados.add(item.getId()); // Agregamos el índice a la lista
//            }
//
//            Collections.sort(indicesSeleccionados); // Ordena los índices seleccionados
//
//            boolean sonConsecutivos = true;
//            for (int i = 0; i < indicesSeleccionados.size() - 1; i++) {
//                if (indicesSeleccionados.get(i + 1) != indicesSeleccionados.get(i) + 1) {
//                    sonConsecutivos = false;
//                    break;
//                }
//            }
//
//            if (!sonConsecutivos) {
//                mensajeError = "Solo puedes seleccionar horas consecutivas.";
//            }
//        }
//
//        if (!mensajeError.isEmpty()) {
//            System.out.println(mensajeError);
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Validación de horas", mensajeError);
//            PrimeFaces.current().dialog().showMessageDynamic(message);
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(mensajeError));
//        }
//    }
//


//    public void validarSeleccionHoras() {
//        String mensajeError = "";
//
//        if (itemsSelecionados != null && itemsSelecionados.size() > MAX_SELECCIONES) {
//            mensajeError = "No se pueden seleccionar más de " + MAX_SELECCIONES + " horas.";
//            desactivarElementos = true; // Desactiva todos los elementos
//        } else if (itemsSelecionados != null && itemsSelecionados.size() > 1) {
//            List<Integer> indicesSeleccionados = new ArrayList<>();
//            for (Item item : itemsSelecionados) {
//                indicesSeleccionados.add(item.getId()); // Agregamos el índice a la lista
//            }
//
//            Collections.sort(indicesSeleccionados); // Ordena los índices seleccionados
//
//            boolean sonConsecutivos = true;
//            for (int i = 0; i < indicesSeleccionados.size() - 1; i++) {
//                if (indicesSeleccionados.get(i + 1) != indicesSeleccionados.get(i) + 1) {
//                    sonConsecutivos = false;
//                    break;
//                }
//            }
//
//            if (!sonConsecutivos) {
//                mensajeError = "Solo puedes seleccionar horas consecutivas.";
//                desactivarElementos = true; // Desactiva todos los elementos
//            }
//        } else {
//            desactivarElementos = false; // Si no se cumple ninguna restricción, no desactiva ningún elemento
//        }
//
//        if (!mensajeError.isEmpty()) {
//            System.out.println(mensajeError);
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Validación de horas", mensajeError);
//            PrimeFaces.current().dialog().showMessageDynamic(message);
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(mensajeError));
//        }
//    }


    public Horario asignarHoras(List<Item> idItems) {
        Horario myHorario = new Horario();
        myHorario.setFecha(fechaReserva); // Asegúrate de definir fechaReserva antes de usarla

        // Asumiendo que idItems tiene al menos 8 elementos
        for (int i = 0; i < itemsSelecionados.size(); i++) {
            switch (i) {
                case 0:
                    myHorario.setJornada1(idItems.get(i).isDato());
                    break;
                case 1:
                    myHorario.setJornada2(idItems.get(i).isDato());
                    break;
                case 2:
                    myHorario.setJornada3(idItems.get(i).isDato());
                    break;
                case 3:
                    myHorario.setJornada4(idItems.get(i).isDato());
                    break;
                case 4:
                    myHorario.setJornada5(idItems.get(i).isDato());
                    break;
                case 5:
                    myHorario.setJornada6(idItems.get(i).isDato());
                    break;
                case 6:
                    myHorario.setJornada7(idItems.get(i).isDato());
                    break;
                case 7:
                    myHorario.setJornada8(idItems.get(i).isDato());
                    break;
            }
        }

        System.out.println("prueba retorno Horario");
        System.out.println(myHorario);
        return myHorario;
    }


    public void validarSeleccionHoras() {
        String mensajeError = "";

        if (itemsSelecionados != null && itemsSelecionados.size() > MAX_SELECCIONES) {
            mensajeError = "No se pueden seleccionar más de " + MAX_SELECCIONES + " horas.";
            elementosDeshabilitados.addAll(itemsSelecionados.subList(MAX_SELECCIONES, itemsSelecionados.size()));
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
                mensajeError = "Solo puedes seleccionar horas consecutivas.";
            }
        }

        if (!mensajeError.isEmpty()) {
            System.out.println(mensajeError);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Validación de horas", mensajeError);
            PrimeFaces.current().dialog().showMessageDynamic(message);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(mensajeError));
        }
    }


    //select


    private List<Boolean> selectedItems;

    // Inicializar la lista de elementos y la lista de selecciones


    public void cargarHorarios() {
        if (idLaboratorio > 0) {  // Asegúrate de tener una fecha y un laboratorio seleccionados
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
            externalContext.redirect(contextPath + "/views/solicitudes/registroSolicitud.xhtml");
            System.out.println("holaaaaaaaaaaaaa" + tipoSolicitud);
        } else if ("SOLICITUD PRÁCTICAS DE PROYECTO DE INVESTIGACÓN".equals(tipoSolicitud)) {
            externalContext.redirect(contextPath + "/views/solicitudes/registroSolicitud.xhtml");
            System.out.println("holaaaaaaaaaaaaa" + tipoSolicitud);
        } else if ("SOLICITUD PRÁCTICAS DE TESIS".equals(tipoSolicitud)) {
            externalContext.redirect(contextPath + "/views/solicitudes/registroSolicitud.xhtml");
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

    public void guardarSeleccionHoras() {
        horario = new Horario();
        // Crear un nuevo objeto Horario
        horario.setFecha(fechaReserva);

        // Obtener los valores de dato de los elementos en itemsSeleccionados
        List<Boolean> valoresDato = itemsSelecionados.stream()
                .map(Item::isDato)
                .collect(Collectors.toList());

        // Asignar los valores de dato a las propiedades de jornada de horario1
        horario.setJornada1(valoresDato.get(0));
        horario.setJornada2(valoresDato.get(1));
        horario.setJornada3(valoresDato.get(2));
        horario.setJornada4(valoresDato.get(3));
        horario.setJornada5(valoresDato.get(4));
        horario.setJornada6(valoresDato.get(5));
        horario.setJornada7(valoresDato.get(6));
        horario.setJornada8(valoresDato.get(7));

        // Agregar horario a una lista o realizar cualquier otra acción necesaria

        System.out.println("objeto horario sacado de las selecciones");
        System.out.println(horario);
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

    public void asignarHoras() {
//        horarioBD.setFecha(fechaReserva); // Asignar la fecha de reserva
//
//        // Recorrer la lista de itemsSelecionados y asignar los valores de 'dato' a las propiedades de jornada
//        for (int i = 0; i < 8; i++) {
//            boolean dato = itemsSelecionados.get(i).isDato();
//            switch (i) {
//                case 0:
//                    horarioBD.setJornada1(dato);
//                    break;
//                case 1:
//                    horarioBD.setJornada2(dato);
//                    break;
//                case 2:
//                    horarioBD.setJornada3(dato);
//                    break;
//                case 3:
//                    horarioBD.setJornada4(dato);
//                    break;
//                case 4:
//                    horarioBD.setJornada5(dato);
//                    break;
//                case 5:
//                    horarioBD.setJornada6(dato);
//                    break;
//                case 6:
//                    horarioBD.setJornada7(dato);
//                    break;
//                case 7:
//                    horarioBD.setJornada8(dato);
//                    break;
//            }
//        }
        // Ahora 'horarioBD' contiene la información actualizada de las jornadas y la fecha.
        // Puedes usar 'horarioBD' según tus necesidades.
        System.out.println(horarioBD);
        System.out.println("items Seleccionados");
        System.out.println(itemsSelecionados);
        System.out.println(itemsSelecionados.size());
    }

//    public int getIdUsurioSession() {
//        Usuario usuarioLogueado = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
//        return usuarioLogueado.getId();
//    }


    @PostConstruct
    public void init() {
        try {
            // Llama al método para cargar los laboratorios al iniciar el bean.
            finAllLaboratorio();
            docenteDAO = new DocenteDAO();
            usuarioDAO = new UsuarioDAO();
            usuarioBean = new UsuarioBean();
            solicitud = new Solicitud();
            itemsSelecionados = new ArrayList<>();

            solicitudDAO = new SolicitudDAO();
            desactivarElementos = false;

            FacesContext facesContext = FacesContext.getCurrentInstance();
            Usuario usuarioLogueado = (Usuario) facesContext.getExternalContext().getSessionMap().get("usuario");
            idUsuarioSession = usuarioLogueado.getId();
            docente = String.valueOf(docenteDAO.findByUsuarioID(getIdUsuarioSession()).get(0).getPersona().getApellido());


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

    public void validateSelection(FacesContext context, UIComponent component, Object value) {
        List<Item> selectedItems = (List<Item>) value;

        if (selectedItems != null && selectedItems.size() > 3) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Solo se permiten un máximo de 3 selecciones.");
            context.addMessage(null, message);
            // Clear the selection or take any other necessary action to handle the error.
        }
    }


}