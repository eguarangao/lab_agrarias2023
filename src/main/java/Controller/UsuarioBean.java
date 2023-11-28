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
import jakarta.mail.Message;
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
    private boolean noExiste;
    String header = "";


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
            noExiste = true;

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

        header = "Crear";
        PrimeFaces.current().ajax().update(":dialogs");
        this.nuevoUsuario = new ListFullUser();

    }
    public void nameHeader(){

         header = "Editar";
         noExiste = false;
        PrimeFaces.current().ajax().update(":dialogs");
    }

    public void addUsuario() {
        try {
            if (this.nuevoUsuario.getFechaCreacion() == null) {

                    DAO.insert(nuevoUsuario);
                    listFullUsers = DAO.listarUsuariosPersonas();
                    listFullDocente = DAO.listarUsuariosDocentes();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuario agregado"));
                    PrimeFaces.current().executeScript("PF('manageUserDialog').hide()");

            } else if (DAO.existeRolPorDNI(nuevoUsuario)&& header.equals("Crear")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Rol existe", "El rol del usuario ya esta registrado, cambiar rol"));
            } else if (!DAO.existeRolPorDNI(nuevoUsuario)&&header.equals("Crear")) {
                DAO.insert(nuevoUsuario);
                listFullUsers = DAO.listarUsuariosPersonas();
                listFullDocente = DAO.listarUsuariosDocentes();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuario agregado"));
                PrimeFaces.current().executeScript("PF('manageUserDialog').hide()");
            }else if (header.equals("Editar")){
                DAO.update(nuevoUsuario);
                listFullUsers = DAO.listarUsuariosPersonas();
                listFullDocente = DAO.listarUsuariosDocentes();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuario actualizado"));
                PrimeFaces.current().executeScript("PF('manageUserDialog').hide()");
            }


            PrimeFaces.current().ajax().update("form:messages");
            PrimeFaces.current().ajax().update("formUser:messages");

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: ", "Error al agregar el usuario"));

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
    public void searchForDni(){
        DAO = new UsuarioDAO();
        boolean existe = DAO.existePersonaPorDNI(nuevoUsuario.getDni());
        System.out.println(existe);
        if(existe){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuario existente", "El usuario se encuentra registrado"));

            nuevoUsuario = DAO.listarUsuarioExistente(nuevoUsuario);
            noExiste = true;
            System.out.println(nuevoUsuario);
        }else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario no existente", "Rellene los campos para crear su usuario"));

            noExiste = false;
        }
        PrimeFaces.current().ajax().update("form:messages");
        PrimeFaces.current().ajax().update("form:manage-user-content");


    }


}
