package Controller;

import DAO.UsuarioDao;
import Model.Usuario;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Data;

import java.io.Serializable;

@Data
@Named
@ViewScoped
public class UsuarioController implements Serializable {
    private Usuario usuario;


    public void save(){
        UsuarioDao usuarioDao;
        try {
            usuarioDao= new UsuarioDao();
            usuarioDao.save();

        }catch (Exception e){
            throw e;
        }
    }

}
