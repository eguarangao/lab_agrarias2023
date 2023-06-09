package Controller;

import DAO.PersonaDAO;
import DAO.RolDAO;
import DAO.UsuarioDAO;
import Model.AjustePerfil;
import Model.Rol;
import Model.Usuario;
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

@Data
@Named
@SessionScoped
public class UsuarioBean implements Serializable {
    private List<Rol> listaRoles = new ArrayList<>();
    private String rolSesion;

    private Boolean btnLogin = false;
    private String username;
    private String password;

    private boolean isAdministrador = false;
    private boolean isTecnico = false;
    private boolean isADocente = false;


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

        usuario = usuarioDAO.getUsuario(username, password);
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

    public void cerrarSesion() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");

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

}
