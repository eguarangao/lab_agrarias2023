package Controller;

import DAO.RolDAO;
import DAO.UsuarioDAO;
import Model.AjustePerfil;
import Model.Rol;
import Model.Usuario;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import lombok.Data;
import lombok.Getter;
import org.primefaces.PrimeFaces;
import utils.PasswordHashing;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Data
@Named
@SessionScoped
public class AccesoBean implements Serializable {
    private List<Rol> listaRoles = new ArrayList<>();
    private String rolSesion;
    private Boolean btnLogin = false;
    private String username;
    private String password;
    private int idUsuarioSession;
    private String idUsuarioClaveSession;
    private AjustePerfil ajustePerfil;
    private boolean isAdministrador = false;
    private boolean isTecnico = false;
    private boolean isADocente = false;
    @Getter
    private String mostrarClave = "";
    @Getter
    private String compararClave;
    private UsuarioDAO DAO = new UsuarioDAO();
    private boolean campocontraseña = false;
    private PasswordHashing passwordHashing = new PasswordHashing();

    public void verificarSession() throws IOException
    {
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
    public void actualizarAjustePerfil(AjustePerfil ajustePerfil) {
        DAO.updateAjustePerfil(ajustePerfil);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuario actualizado"));
        PrimeFaces.current().ajax().update("form-Perfil", "msgs");
    }

    public void setMostrarClave(String mostrarClave) {
        this.mostrarClave = mostrarClave;
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
        else if (campocontraseña == true) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "La contraseña actual es incorrecta", null));
            PrimeFaces.current().ajax().update("form-cambiarClave:msgs");
        }
        else if (mostrarClave == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ingresa la contraseña actual", null));
            PrimeFaces.current().ajax().update("form-cambiarClave:msgs");
        }

        else {
            if (passwordHashing.verifyPassword(mostrarClave,idUsuarioClaveSession )) {

                String hashedNuevaContrasena = passwordHashing.hashPassword(compararClave);

                DAO.actualizarUsuario(hashedNuevaContrasena, idUsuarioSession);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Contraseña actualizada", null));
                PrimeFaces.current().ajax().update("form-cambiarClave:msgs");
                //logout();

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
            campocontraseña = true;
        }
    }
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
