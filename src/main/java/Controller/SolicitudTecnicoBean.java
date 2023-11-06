package Controller;

import DAO.LaboratorioDAO;
import DAO.SolicitudDAO;
import Model.Laboratorio;
import Model.Solicitud;
import Model.Usuario;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Named
@ViewScoped
public class SolicitudTecnicoBean implements Serializable {
    List<Laboratorio> laboratorios = new ArrayList<>();
    List<Solicitud> solicitudes = new ArrayList<>();

    //DAO
    LaboratorioDAO laboratorioDAO = new LaboratorioDAO();
    SolicitudDAO solicitudDAO = new SolicitudDAO();

    //Variables
    int idUsuarioSession =0;




    @PostConstruct
    public void init() {
        try {
            //Obtenemos el nombre del docente mediante el ID de usuario
            FacesContext facesContext = FacesContext.getCurrentInstance();
            Usuario usuarioLogueado = (Usuario) facesContext.getExternalContext().getSessionMap().get("usuario");
            idUsuarioSession = usuarioLogueado.getId();
            //Cargamos las solicitudes correspondientes a los tecncios con sus laboratorios

            solicitudes = solicitudDAO.findAllTecnico();
            laboratorios =  laboratorioDAO.findByIdUsuario(idUsuarioSession);




        } catch (SQLException e) {
            // Maneja cualquier excepción que pueda ocurrir durante la carga de los laboratorios.
            e.printStackTrace();
            // También puedes agregar un mensaje de error para mostrar en la vista.
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al cargar los datos", null));
        }
    }
}
