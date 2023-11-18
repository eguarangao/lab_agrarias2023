package Controller;

import DAO.PersonaDAO;
import DAO.RolDAO;
import DAO.UsuarioDAO;
import Model.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.primefaces.PrimeFaces;

import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import utils.PasswordHashing;

@Data
@NoArgsConstructor
@Named
@ViewScoped
public class UsuarioBean implements Serializable {


    private boolean isAdministrador = false;
    private boolean isTecnico = false;
    private boolean isADocente = false;
    private List<ListFullUser> listFullUsers;
    private List<ListFullUser> listFullDocente;
    private ListFullUser nuevoUsuario;
    private UsuarioDAO DAO = new UsuarioDAO();
    private boolean isCreateUser;
    private boolean deshabilitado;


    @PostConstruct
    public void main() {
        try {
            PrimeFaces.current().ajax().update("form:dt-user");
            this.listFullUsers = new ArrayList<>();
            listFullUsers = DAO.listarUsuariosPersonas();
            PrimeFaces.current().ajax().update("formUser:dt-user");
            this.listFullDocente = new ArrayList<>();
            listFullDocente = DAO.listarUsuariosDocentes();
            isCreateUser = false;
            deshabilitado = true;


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean isCreateUser() {
        return isCreateUser;
    }

    public void EditCreateUser(boolean createUser) {
        this.isCreateUser = createUser;
        deshabilitado = !createUser;
    }

    //    public String login() {
//        UsuarioDAO usuarioDAO=new UsuarioDAO();
//        Usuario usuario = usuarioDAO.getUsuario(username);
//        if (usuario != null && usuario.getClave().equals(password) ) {
//            // Las credenciales son válidas, redirigir al usuario a la página principal
//            return "newPersona.xhtml?faces-redirect=true";
//        } else {
//            // Las credenciales son inválidas, mostrar un mensaje de error
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Credenciales inválidas", null));
//            return null;
//        }
//    }


    //    public boolean existeUsuario(boolean ob){
//        while (ob=true)
//
//    }
    public void openNew() {
        this.nuevoUsuario = new ListFullUser();

    }

    public void addUsuario() {
        try {
            if (this.nuevoUsuario.getFechaCreacion() == null) {
                DAO.insert(nuevoUsuario);
                listFullUsers = DAO.listarUsuariosPersonas();
                listFullDocente = DAO.listarUsuariosDocentes();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuario agregado"));
            } else {
                DAO.update(nuevoUsuario);
                listFullUsers = DAO.listarUsuariosPersonas();
                listFullDocente = DAO.listarUsuariosDocentes();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuario actualizado"));

            }
            PrimeFaces.current().executeScript("PF('manageUserDialog').hide()");
            PrimeFaces.current().ajax().update("form:messages");
            PrimeFaces.current().ajax().update("formUser:messages");

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error al agregar la facultad"));
            e.printStackTrace();
        }
    }

    public void delete() {
        try {
            int deletedId = nuevoUsuario.getPersonaId();
            DAO.delete(deletedId);
            listFullUsers = DAO.listarUsuariosPersonas();
            listFullDocente = DAO.listarUsuariosDocentes();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuario Eliminado"));
            PrimeFaces.current().ajax().update("form:messages", "form:dt-user");
            PrimeFaces.current().ajax().update("formUser:messages", "formUser:dt-user");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
