package Controller;

import DAO.FacultadDAO;
import DAO.PeriodoDAO;
import Model.Facultad;
import Model.Periodo;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import lombok.Data;
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Named
@SessionScoped
public class PeriodoBean implements Serializable {
    private PeriodoDAO periodoDAO = new PeriodoDAO();
    private Periodo nuevoPeriodo = new Periodo();
    private List<Periodo> periodos;
    @PostConstruct
    public void main() {
        try {
            this.periodos = new ArrayList<>();
            periodos = periodoDAO.listarPeriodos();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void openNew() {
        this.nuevoPeriodo = new Periodo();
    }
    public void delete() {
        try{
            int deletedId = nuevoPeriodo.getId();
            periodoDAO.deletePeriodo(deletedId);
            periodos = periodoDAO.listarPeriodos();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Periodo Removed"));
            PrimeFaces.current().ajax().update("form:messages", "form:dt-facd");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void addPeriodo() {
        try {
            if (this.nuevoPeriodo.getFechaCreacion() ==null) {
                periodoDAO.insertPeriodo(nuevoPeriodo);
                periodos = periodoDAO.listarPeriodos();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Periodo agregado"));
            } else {
                periodoDAO.updatePeriodo(nuevoPeriodo);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Periodo actualizado"));

            }
            PrimeFaces.current().executeScript("PF('manageFacdDialog').hide()");
            PrimeFaces.current().ajax().update("form:messages");

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error al agregar el periodo"));
            e.printStackTrace();
        }
    }
}
