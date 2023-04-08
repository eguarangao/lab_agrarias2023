package Controller;

import DAO.PersonaDAO;
import Model.Persona;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Data;

import java.sql.SQLException;

@Data
@Named
@ViewScoped
public class PersonaBean {

    private Persona persona=new Persona();

    public void save() throws SQLException {
        PersonaDAO personaDAO;
        try {
            personaDAO = new PersonaDAO();
            personaDAO.save(persona);
        } catch (Exception e) {
            throw e;
        }
    }


}
