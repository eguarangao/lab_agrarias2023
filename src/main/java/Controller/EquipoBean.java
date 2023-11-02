package Controller;

import DAO.EquipoDAO;
import Model.*;
import Model.Laboratorio;
import Model.Aula;
import Model.Usuario;
import Model.CategoriaEquipo;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Data;
import org.primefaces.PrimeFaces;

import java.awt.*;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Data
@Named
@ViewScoped
public class EquipoBean implements Serializable {

    private EquipoDAO DaoEquipo = new EquipoDAO();
    List<Equipo> Listequipos;
    List<Laboratorio> listlaboratorios;
    List<CategoriaEquipo> listCatergoria;
    List<Aula> listaula;
    private Equipo newEquipo;
    private boolean mostrarTablaEquipos = false;
    private boolean botonEquipoDisabled = true;
    private int idUsuarioSession;
    private int idlaboratorioSession;

    @PostConstruct
    public void main() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            Usuario usuario = (Usuario) facesContext.getExternalContext().getSessionMap().get("usuario");
            idUsuarioSession = usuario.getId();
            if(DaoEquipo.VerificadorAdmin(idUsuarioSession)){
                ListarTodosLaboratorios();}
            else if (DaoEquipo.VerificadorTecnic(idUsuarioSession)){
                ListarLaboratorios();}
            else{ System.out.println("Error rol no encontrado"); }

            ListarCategoriaEquipo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void TablaEquiposPorLaboratorio() throws SQLException {
        try {
            this.Listequipos = new ArrayList<>();
            Listequipos = DaoEquipo.listarEquiposPorLaboratorio(idlaboratorioSession);
            mostrarTablaEquipos = !Listequipos.isEmpty();
            botonEquipoDisabled = Listequipos.isEmpty();
            PrimeFaces.current().ajax().update("form-equipo:tablaEquipos", "form-equipo:dt-equipos","form-equipo:botonNewEquipo" );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void ListarTodosLaboratorios() throws SQLException {
        this.listlaboratorios = new ArrayList<>();
        listlaboratorios = DaoEquipo.ListarLosLaboratorios();
        System.out.println(listlaboratorios);
    }

    public void ListarLaboratorios() throws SQLException {
        this.listlaboratorios = new ArrayList<>();
        listlaboratorios = DaoEquipo.listarlaboratoriosPorTecnico(idUsuarioSession);
    }

    public void ListarCategoriaEquipo() throws SQLException {
        this.listCatergoria = new ArrayList<>();
        listCatergoria = DaoEquipo.listarCategorias();
        System.out.println(listCatergoria);
    }

    public void ListarAulaxLaboratorio() {
        try {
            this.listaula = new ArrayList<>();
            listaula = DaoEquipo.ListarAulaPorLaboratorio(idlaboratorioSession);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void SelectLaboratorioTecnico() throws SQLException {
        try {
            TablaEquiposPorLaboratorio();
            ListarAulaxLaboratorio();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void nuevoEquipo() throws SQLException {
        this.newEquipo = new Equipo();
        this.newEquipo.setCategoriaEquipo(new CategoriaEquipo());
        this.newEquipo.setAula(new Aula());
        newEquipo.setEstado(true);
        ListarCategoriaEquipo();
        ListarAulaxLaboratorio();
    }

    public void addEquipo() {
        try {
            if (this.newEquipo.getFechaAdquisicion() == null) {
                DaoEquipo.agregarEquipo(newEquipo);
                Listequipos = DaoEquipo.listarEquiposPorLaboratorio(idlaboratorioSession);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Equipo agregado",null));
            } else {
                DaoEquipo.editarEquipo(newEquipo);
                Listequipos = DaoEquipo.listarEquiposPorLaboratorio(idlaboratorioSession);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Equipo actualizado",null));}
            PrimeFaces.current().executeScript("PF('manageEquipoDialog').hide()");
            PrimeFaces.current().ajax().update("form-equipo:messages");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error al agregar el Equipo",null));
            PrimeFaces.current().ajax().update("form-equipo:messages");
            e.printStackTrace();
        }
    }
    public void eliminarEquipo() {
        try {
            int eliminarEquipoID = newEquipo.getId();
            boolean existeenMantenimiento = DaoEquipo.verificarexisteequipoenmante(eliminarEquipoID);
            boolean existeenAveria = DaoEquipo.verificarexisteequipoenaveria(eliminarEquipoID);

            if(newEquipo.getEstado()==true && existeenMantenimiento==false && existeenAveria==false){
                DaoEquipo.eliminarEquipo(eliminarEquipoID);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Equipo Eliminado",null));
                PrimeFaces.current().executeScript("PF('manageEquipoDialog').hide()");
                PrimeFaces.current().ajax().update("form-equipo:messages", "form-equipo:dt-equipos");
                Listequipos = DaoEquipo.listarEquiposPorLaboratorio(idlaboratorioSession);
            } else if(existeenMantenimiento==true){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"No se puede eliminar equipo se encuentra en mantenimiento",null));
                PrimeFaces.current().executeScript("PF('manageEquipoDialog').hide()");
                PrimeFaces.current().ajax().update("form-equipo:messages", "form-equipo:dt-equipos");}

            else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"No se puede eliminar equipo se encuentra averiado",null));
            PrimeFaces.current().executeScript("PF('manageEquipoDialog').hide()");
            PrimeFaces.current().ajax().update("form-equipo:messages", "form-equipo:dt-equipos");}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}