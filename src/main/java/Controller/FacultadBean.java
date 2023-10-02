package Controller;

import DAO.FacultadDAO;
import Model.Facultad;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.AsyncResult;
import jakarta.ejb.Asynchronous;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Data;
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Data
@Named
@SessionScoped
public class FacultadBean implements Serializable {
    private FacultadDAO facultadDAO = new FacultadDAO();
    private Facultad nuevaFacultad = new Facultad();
    private List<Facultad> facultades;

    @PostConstruct
    public void main() {
        try {
            this.facultades = new ArrayList<>();
            facultades = facultadDAO.listarFacultades();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void openNew() {
        this.nuevaFacultad = new Facultad();
    }
    public void delete() {
        try{
            int deletedId = nuevaFacultad.getIdFacd();
            facultadDAO.delete(deletedId);
            facultades = facultadDAO.listarFacultades();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Product Removed"));
            PrimeFaces.current().ajax().update("form:messages", "form:dt-facd");
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void addFacultad() {
        try {
            if (this.nuevaFacultad.getFechaCreacion()==null) {
                facultadDAO.insert(nuevaFacultad);
                facultades = facultadDAO.listarFacultades();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Facultad agregada"));
            } else {
                facultadDAO.update(nuevaFacultad);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Product Updated"));

            }
            PrimeFaces.current().executeScript("PF('manageFacdDialog').hide()");
            PrimeFaces.current().ajax().update("form:messages");

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error al agregar la facultad"));
            e.printStackTrace();
        }
    }

}
