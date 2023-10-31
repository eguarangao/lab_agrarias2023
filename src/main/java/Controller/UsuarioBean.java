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
@Named
@SessionScoped
public class UsuarioBean implements Serializable {
    private List<Rol> listaRoles = new ArrayList<>();
    private String rolSesion;

    private Boolean btnLogin = false;
    private String username;
    private String password;

    private int idUsuarioSession;
    private String idUsuarioClaveSession;

    private boolean isAdministrador = false;
    private boolean isTecnico = false;
    private boolean isADocente = false;
    private List<ListFullUser> listFullUsers;
    private List<ListFullUser> listFullDocente;
    private ListFullUser nuevoUsuario;
    private UsuarioDAO DAO = new UsuarioDAO();
    private boolean isCreateUser;
    private boolean deshabilitado;

    private String mostrarClave = "";
    private String compararClave;

    private PasswordHashing passwordHashing = new PasswordHashing();

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
    public void verificarSession() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Usuario usuario = (Usuario) facesContext.getExternalContext().getSessionMap().get("usuario");
        idUsuarioSession = usuario.getId();
        idUsuarioClaveSession = usuario.getClave();
        System.out.println("USAURIO LOGUEADO");
        System.out.println(usuario);

        System.out.println("ID USAURIO SESSION");
        System.out.println(idUsuarioSession);

        if (usuario == null) {
            facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath());
//        } else {
//            if (Objects.equals(rolSesion, "DOCENTE")) {
//
//                FacesContext context = FacesContext.getCurrentInstance();
//                String contextPath = context.getExternalContext().getRequestContextPath();
//                contextPath = context.getExternalContext().getRequestContextPath();
//                context.getExternalContext().redirect(contextPath + "/views/dashboard/dashboardDocente.xhtml");
//            } else {
//                facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath());
//            }
        }


    }

    public void verificarSessionAdministrador() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Usuario usuario = (Usuario) facesContext.getExternalContext().getSessionMap().get("usuario");
        if (usuario == null) {
            facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath());
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            String contextPath = context.getExternalContext().getRequestContextPath();
            switch (rolSesion) {
                case "ADMINISTRADOR" -> {
                    contextPath = context.getExternalContext().getRequestContextPath();
                    context.getExternalContext().redirect(contextPath + "/views/dashboard/dashboardAdministrador.xhtml");
                }
                case "TECNICO DE LABORATORIO" -> {
                    contextPath = context.getExternalContext().getRequestContextPath();
                    context.getExternalContext().redirect(contextPath + "/views/dashboard/dashboardTecnico.xhtml");
                }
                case "DOCENTE" -> {
                    contextPath = context.getExternalContext().getRequestContextPath();
                    context.getExternalContext().redirect(contextPath + "/views/dashboard/dashboardDocente.xhtml");
                }
                default -> {
                    System.out.println("Solo números entre 1 y 4");
                    System.out.println(rolSesion);
                }
            }
        }

    }


    public void login() throws IOException, SQLException {

        while (btnLogin) {
            FacesContext context = FacesContext.getCurrentInstance();
            String contextPath = context.getExternalContext().getRequestContextPath();

            switch (rolSesion) {
                case "ADMINISTRADOR" -> {
                    contextPath = context.getExternalContext().getRequestContextPath();
                    context.getExternalContext().redirect(contextPath + "/views/dashboard/dashboardAdministrador.xhtml");
                    isAdministrador = true;
                }
                case "TECNICO DE LABORATORIO" -> {
                    contextPath = context.getExternalContext().getRequestContextPath();
                    context.getExternalContext().redirect(contextPath + "/views/dashboard/dashboardTecnico.xhtml");
                    isTecnico = true;
                }
                case "DOCENTE" -> {
                    contextPath = context.getExternalContext().getRequestContextPath();
                    context.getExternalContext().redirect(contextPath + "/views/dashboard/dashboardDocente.xhtml");
                    isADocente = true;
                }
                default -> {
                    System.out.println("Solo números entre 1 y 4");
                    System.out.println(rolSesion);
                }
            }
            ajustePerfil();

        }
//            FacesContext context = FacesContext.getCurrentInstance();
//            String contextPath = context.getExternalContext().getRequestContextPath();
//            context.getExternalContext().redirect(contextPath+ "/views/dashboard/dashboardAdministrador.xhtml");
        // return "/newPersona.xhtml?faces-redirect=true";


        Usuario usuario = null;
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        usuario = usuarioDAO.getUsuario2(username, password);

        if (usuario != null) {
            btnLogin = true;
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", usuario);
            // Las credenciales son válidas, redirigir al usuario a la página principal
            System.out.println(usuario);
            RolDAO rolDAO = new RolDAO();
            listaRoles = rolDAO.findAllRolesUsuarioByUsername(username);


        } else {
            // Las credenciales son inválidas, mostrar un mensaje de error
            System.out.println(usuario + "Ebert");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Credenciales inválidas", null));
            //return null;
        }
    }

    public void home() throws IOException, SQLException {

        FacesContext context = FacesContext.getCurrentInstance();
        String contextPath = context.getExternalContext().getRequestContextPath();

        switch (rolSesion) {
            case "ADMINISTRADOR" -> {
                contextPath = context.getExternalContext().getRequestContextPath();
                context.getExternalContext().redirect(contextPath + "/views/dashboard/dashboardAdministrador.xhtml");
                isAdministrador = true;
            }
            case "TECNICO DE LABORATORIO" -> {
                contextPath = context.getExternalContext().getRequestContextPath();
                context.getExternalContext().redirect(contextPath + "/views/dashboard/dashboardTecnico.xhtml");
                isTecnico = true;
            }
            case "DOCENTE" -> {
                contextPath = context.getExternalContext().getRequestContextPath();
                context.getExternalContext().redirect(contextPath + "/views/dashboard/dashboardDocente.xhtml");
                isADocente = true;
            }
            default -> {
                System.out.println("Solo números entre 1 y 4");
                System.out.println(rolSesion);
            }
        }


    }

    public void cerrarSesion() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        } catch (Exception ignored) {
        }
    }

    public void logout() throws IOException {
        username = null;
        password = null;
        rolSesion = null;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        //return "/login.xhtml?faces-redirect=true";
        FacesContext context = FacesContext.getCurrentInstance();
        String contextPath = context.getExternalContext().getRequestContextPath();
        context.getExternalContext().redirect(contextPath);


    }

    AjustePerfil ajustePerfil;

    public void ajustePerfil() {

        UsuarioDAO usuarioDAO;
        try {
            usuarioDAO = new UsuarioDAO();
            ajustePerfil = new AjustePerfil();
            ajustePerfil = usuarioDAO.SelectAjustePerfil(username, rolSesion);
        } catch (Exception e) {
            throw e;

        }
    }

    public void actualizarAjustePerfil(AjustePerfil ajustePerfil) {
        DAO.updateAjustePerfil(ajustePerfil);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuario actualizado"));
        PrimeFaces.current().ajax().update("form-Perfil", "msgs");
    }


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

    public String getMostrarClave() {
        return mostrarClave;
    }

    public void setMostrarClave(String mostrarClave) {
        this.mostrarClave = mostrarClave;
    }

    public String getCompararClave() {
        return compararClave;
    }

    public void setCompararClave(String compararClave) {
        this.compararClave = compararClave;
    }

    public void cambiarClave() throws IOException {
        String mayuscula = ".*[A-Z].*";
        String minuscula = ".*[a-z].*";
        String numero = ".*[0-9].*";

        if (compararClave.length() < 8) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "La contraseña debe tener al menos 8 caracteres", null));
            PrimeFaces.current().ajax().update("form-cambiarClave:msgs");
        }
        else if (!compararClave.matches(mayuscula)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "La contraseña debe contener al menos una letra mayúscula", null));
            PrimeFaces.current().ajax().update("form-cambiarClave:msgs");
        }
        else if (!compararClave.matches(minuscula)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "La contraseña debe contener al menos una letra minuscula", null));
            PrimeFaces.current().ajax().update("form-cambiarClave:msgs");
        }

        else if (!compararClave.matches(numero)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "La contraseña debe contener al menos una letra minuscula", null));
            PrimeFaces.current().ajax().update("form-cambiarClave:msgs");
        }

        else {
            if (passwordHashing.verifyPassword(mostrarClave,idUsuarioClaveSession )) {

                String hashedNuevaContrasena = passwordHashing.hashPassword(compararClave);

                DAO.actualizarUsuario(hashedNuevaContrasena, idUsuarioSession);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Contraseña actualizada", null));
                PrimeFaces.current().ajax().update("form-cambiarClave:msgs");
                logout();

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Contraseña no actualizada", null));
                PrimeFaces.current().ajax().update("form-cambiarClave:msgs");
            }
        }
    }

    public void verificarclaveactual() {
        if (passwordHashing.verifyPassword(mostrarClave,idUsuarioClaveSession )) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "La contraseña actual es correcta", null));
            PrimeFaces.current().ajax().update("form-cambiarClave:msgs");

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "La contraseña actual es incorrecta", null));
            PrimeFaces.current().ajax().update("form-cambiarClave:msgs");
        }
    }
}
