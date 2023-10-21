package Controller;

import DAO.RolDAO;
import Model.Rol;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Data;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

@Data
@Named
@ViewScoped
public class RolBean  implements Serializable {
    private String username;
    private List<Rol> listaRoles;
    public void findAllRolesUsuarioByUsername() throws SQLException {
        RolDAO rolDAO;
        try {
            rolDAO = new RolDAO();
            listaRoles = rolDAO.findAllRolesUsuarioByUsername(username);
        } catch (Exception e) {
            throw e;
        }
    }
}
