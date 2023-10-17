package Controller;

import DAO.LaboratorioDAO;
import DAO.TecnicoLaboratorioDAO;
import Model.Aula;
import Model.Laboratorio;
import Model.ListFullUser;
import Model.Persona;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import lombok.Data;
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Named
@SessionScoped
@Data
public class TecnicoLaboratorioBean implements Serializable {
    private TecnicoLaboratorioDAO tecnicoDAO;
    private List<Laboratorio> listLab;
    private List<Aula> listAula;
    private List<Persona> listPersona;
    private int idLaboratorio;
    private int idFacultad;
    private int idPeriodo;
    private String nombreLaboratorio;
    private boolean rendered;
    private Aula nuevaAula;
    private LaboratorioDAO laboratorioDAO;


    @PostConstruct
    public void main() {
        try {
            rendered = true;
            listAula = new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listLabByFacultad() {
        tecnicoDAO = new TecnicoLaboratorioDAO();
        listLab = tecnicoDAO.listarPorFacultad(this.idFacultad);
    }
    public void listLabByTecnico() throws SQLException {
        tecnicoDAO = new TecnicoLaboratorioDAO();
        listPersona = tecnicoDAO.listarTecnicobyLab(this.idLaboratorio, this.idPeriodo);
    }
    public void listAulaByLab() {

        tecnicoDAO = new TecnicoLaboratorioDAO();

        listAula = tecnicoDAO.listarAulaPorLaboratorio(this.idLaboratorio);
        PrimeFaces.current().ajax().update("form:messages", "form:dt-facd");
    }
    public void openNew() {
        this.nuevaAula = new Aula();

    }
    public void addAula(){
        try{
             if(nuevaAula.getFechaCreacion()==null){
                 tecnicoDAO.insert(idLaboratorio,nuevaAula);
                 listAulaByLab();
                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Aula agregada"));
             }else {
                 tecnicoDAO.update(nuevaAula);
                 listAulaByLab();
                 PrimeFaces.current().ajax().update("form:messages", "form:dt-facd");
                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Aula actualizada"));
            }

            PrimeFaces.current().executeScript("PF('manageFacdDialog').hide()");
        }catch (Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error al agregar Aula"));
            e.printStackTrace();
        }
    }
    public void delete() {
        try {

            tecnicoDAO.deleteLaboratorioAulaAndAula(nuevaAula);
            listAulaByLab();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Aula eliminada"));
            PrimeFaces.current().ajax().update("form:messages", "form:dt-facd");
            PrimeFaces.current().ajax().update("formUser:messages", "formUser:dt-facd");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void saveLabByFacultad() throws SQLException {
        laboratorioDAO = new LaboratorioDAO();
        laboratorioDAO.saveLaboratoriobyFacultad(idFacultad, nombreLaboratorio);
        PrimeFaces.current().ajax().update("form:messages", "form:lab");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Laboratorio agregado"));
        listLabByFacultad();
        PrimeFaces.current().executeScript("PF('manageLabDialog').hide()");
    }
}
