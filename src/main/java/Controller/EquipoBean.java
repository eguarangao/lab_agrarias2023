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
@SessionScoped
public class EquipoBean implements Serializable {

    private EquipoDAO DaoEquipo = new EquipoDAO();
    private List<Equipo> Listequipos;
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
                ListarTodosLaboratorios();
            }
            else if (DaoEquipo.VerificadorTecnic(idUsuarioSession)){
                ListarLaboratorios();
            }
            else{ System.out.println("No existe usuario"); }

            ListarCategoriaEquipo();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void TablaEquiposPorTecnico() throws SQLException {
        try {
            this.Listequipos = new ArrayList<>();
            Listequipos = DaoEquipo.listarEquiposPorTecnico(idUsuarioSession);
            mostrarTablaEquipos = !Listequipos.isEmpty();
            PrimeFaces.current().ajax().update("form-equipo:selectArea", "form-equipo:dt-equipos");
        } catch (SQLException e) {
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

    public void ListarAulasEquipo() {
        try {
            this.listaula = new ArrayList<>();
            listaula = DaoEquipo.ListaAulas();
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
        ListarCategoriaEquipo();
        ListarAulaxLaboratorio();

    }

    public void addEquipo() {
        try {
            if (this.newEquipo.getFechaAdquisicion() == null) {
                DaoEquipo.agregarEquipo(newEquipo);
                Listequipos = DaoEquipo.listarEquiposPorLaboratorio(idlaboratorioSession);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Equipo agregado"));
            } else {
                System.out.println("Entre a la parte de editar");
                DaoEquipo.editarEquipo(newEquipo);
                Listequipos = DaoEquipo.listarEquiposPorLaboratorio(idlaboratorioSession);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Equipo actualizado"));
            }
            PrimeFaces.current().executeScript("PF('manageEquipoDialog').hide()");
            PrimeFaces.current().ajax().update("form-equipo:messages");

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error al agregar el Equipo"));
            e.printStackTrace();
        }
    }
    public void eliminarEquipo() {
        try {
            int eliminarEquipoID = newEquipo.getId();
            DaoEquipo.eliminarEquipo(eliminarEquipoID);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Equipo Eliminado"));
            PrimeFaces.current().executeScript("PF('manageEquipoDialog').hide()");
            PrimeFaces.current().ajax().update("form-equipo:messages", "form-equipo:dt-equipos");

            if(DaoEquipo.VerificadorAdmin(idUsuarioSession)){
                Listequipos = DaoEquipo.listarEquiposPorLaboratorio(idUsuarioSession);
            }
            else if (DaoEquipo.VerificadorTecnic(idUsuarioSession)){
                Listequipos = DaoEquipo.listarEquiposPorTecnico(idlaboratorioSession);
            }
            else{ System.out.println("ERROR"); }



        } catch (Exception e) {
            e.printStackTrace();
        }





    }



}
