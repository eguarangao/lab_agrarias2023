package Controller;

import DAO.LaboratorioDAO;
import DAO.TecnicoLaboratorioDAO;
import Model.Aula;
import Model.Laboratorio;
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

@Named
@SessionScoped
@Data
public class TecnicoLaboratorioBean implements Serializable {
    private TecnicoLaboratorioDAO tecnicoDAO;
    private LaboratorioDAO laboratorioDAO;
    private Aula nuevaAula;

    private List<Laboratorio> listLab;
    private List<Persona> listTecnico;
    private List<Aula> listAula;
    private List<Persona> listPersona;
    private int idLaboratorio;
    private int idFacultad;
    private int idPeriodo;
    private int idTecnico;

    private String nombreLaboratorio;
    private boolean rendered;


    @PostConstruct
    public void main() {
        try {
            idFacultad = 0;
            idLaboratorio = 0;
            idPeriodo =0;
            idTecnico=0;
            rendered = true;
            listAula = new ArrayList<>();
            listPersona = new ArrayList<>();
            listTecnico = new ArrayList<>();
            listPersonaTecnico();
            listLabByTecnico();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listLabByFacultad() {

            tecnicoDAO = new TecnicoLaboratorioDAO();
            listLab = tecnicoDAO.listarPorFacultad(this.idFacultad);




    }
    public void msjValidateAddLab(){
        if(!validarSelectFacultad()){
            PrimeFaces.current().executeScript("PF('manageLabDialog').show();");
        }else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"Mensaje de advertencia","Seleccione una facultad"));
        }
        PrimeFaces.current().ajax().update("form:messages");
    }

    public void listLabByTecnico() throws SQLException {

        tecnicoDAO = new TecnicoLaboratorioDAO();
        listPersona = tecnicoDAO.listarTecnicobyLab(idLaboratorio, idPeriodo);
        PrimeFaces.current().ajax().update("form:messages", "dialogs2:dt-facd2");


    }

    public void clearListLabByTecnico() {
        listPersona.clear();
        if(!validarSelectFLaboratorio()){
            PrimeFaces.current().executeScript("PF('manageFacd2Dialog').show();");

        }else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"Mensaje de advertencia","Seleccione un Laboratorio"));
        }
        PrimeFaces.current().ajax().update("form:messages");

    }

    public void listAulaByLab() {

        tecnicoDAO = new TecnicoLaboratorioDAO();

        listAula = tecnicoDAO.listarAulaPorLaboratorio(this.idLaboratorio);
        PrimeFaces.current().ajax().update("form:messages", "form:dt-facd");
    }

    public boolean validarSelectFacultad() {
        return idFacultad == 0;
    }
    public boolean validarSelectFLaboratorio() {
        return idLaboratorio == 0;
    }
    public boolean validarSelectPeriodo() {
        return idPeriodo == 0;
    }
    public boolean validarSelectTecnico() {
        return idTecnico == 0;
    }

    public void openNew() {
        if(!validarSelectFLaboratorio()){
            PrimeFaces.current().executeScript("PF('manageFacdDialog').show();");
            this.nuevaAula = new Aula();
        }else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"Mensaje de advertencia","Seleccione un Laboratorio"));
        }
        PrimeFaces.current().ajax().update("form:messages");
    }

    public void addAula() {
        try {
            if (nuevaAula.getFechaCreacion() == null) {
                tecnicoDAO.insert(idLaboratorio, nuevaAula);
                listAulaByLab();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Aula agregada"));
            } else {
                tecnicoDAO.update(nuevaAula);
                listAulaByLab();
                PrimeFaces.current().ajax().update("form:messages", "form:dt-facd");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Aula actualizada"));
            }

            PrimeFaces.current().executeScript("PF('manageFacdDialog').hide()");
        } catch (Exception e) {
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

    public void listPersonaTecnico() {
        tecnicoDAO = new TecnicoLaboratorioDAO();
        listTecnico = tecnicoDAO.listTecnico();

    }

    public void insertTecnicoByAsig() throws SQLException {
        if(!validarSelectPeriodo()&!validarSelectTecnico()){

            tecnicoDAO = new TecnicoLaboratorioDAO();
            tecnicoDAO.insert(idLaboratorio, idTecnico, idPeriodo);
            listLabByTecnico();
            PrimeFaces.current().ajax().update("form:dt-facd");
        }else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"Mensaje de advertencia","Seleccione un Periodo o un Técnico"));
        }
        PrimeFaces.current().ajax().update("form:messages");



    }
}
