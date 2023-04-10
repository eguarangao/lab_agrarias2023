package Controller;

import DAO.UsuarioDAO;
import Model.Usuario;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Data;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Data
@Named
@SessionScoped
public class UsuarioBean implements Serializable {


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





    public String login() {
        Usuario usuario = new Usuario();
        UsuarioDAO usuarioDAO=new UsuarioDAO();
        usuario= usuarioDAO.getUsuario(username);
        if (usuario!= null && usuario.getClave().equals(password)) {
            // Las credenciales son válidas, redirigir al usuario a la página principal
            return "pagina_principal.xhtml?faces-redirect=true";
        } else {
            // Las credenciales son inválidas, mostrar un mensaje de error
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Credenciales inválidas", null));
            return null;
        }
    }
}
