package Controller;

import DAO.PersonaDAO;
import Model.Persona;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Data;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Data
@Named
@ViewScoped
public class PersonaBean implements Serializable {

    private Persona persona = new Persona();
    private List<Persona> listaPersonas;

    public void save() throws SQLException {
        PersonaDAO personaDAO;
        try {

            personaDAO = new PersonaDAO();
            personaDAO.save(persona);
        } catch (Exception e) {
            throw e;
        }
    }

    public void findAll() throws SQLException {
        PersonaDAO personaDAO;
        try {
            personaDAO = new PersonaDAO();
            listaPersonas = personaDAO.findAll();
        } catch (Exception e) {
            throw e;
        }
    }

    public void findById(Persona persona) throws SQLException {
        PersonaDAO personaDAO;
        Persona personaTemp = new Persona();
        try {
            personaDAO = new PersonaDAO();
            personaTemp = personaDAO.findById(persona);

            if (personaTemp != null) {
                this.persona = personaTemp;
            }

        } catch (Exception e) {
            throw e;
        }
    }


}
