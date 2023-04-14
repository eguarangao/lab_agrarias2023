package Controller;

import DAO.RolDAO;
import DAO.UsuarioDAO;
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

@Data
@Named
@SessionScoped
public class UsuarioBean implements Serializable {
    private List<Rol> listaRoles = new ArrayList<>();
    private String rolSesion;

    private Boolean btnLogin = false;
    private String username;
    private String password;

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
    if(usuario==null){
        facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath());
    }else if(rolSesion =="ADMINISTRADOR"){
        facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath()+ "/views/dashboard/dashboardAdministrador.xhtml");

    }else{
        FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts().add("location.reload();");

    }

}
    public void verificarSessionAdministrador() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Usuario usuario = (Usuario) facesContext.getExternalContext().getSessionMap().get("usuario");
        if(usuario==null &&(rolSesion.equals("Administrador"))){
            facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath()+ "/views/dashboard/dashboardAdministrador.xhtml");

        }

    }



    public void login() throws IOException, SQLException {

        while(btnLogin){
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
            System.out.println(usuario+"Ebert");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Credenciales inválidas", null));
            //return null;
        }
    }
}
