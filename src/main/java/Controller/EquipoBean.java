package Controller;

import DAO.EquipoDAO;
import Model.*;
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

@Data
@Named
@SessionScoped
public class EquipoBean implements Serializable {

    private EquipoDAO DaoEquipo = new EquipoDAO();
    private List<Equipo> Listequipos;
    private Equipo newEquipo;
    private int tecnicoID = 2;

    @PostConstruct
    public void main() {
        try {
            this.Listequipos = new ArrayList<>();
            Listequipos = DaoEquipo.listarEquiposPorTecnico(tecnicoID);
            System.out.println(Listequipos);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void nuevoEquipo() {
        this.newEquipo = new Equipo();
        this.newEquipo.setLaboratorio(new Laboratorio());
        this.newEquipo.setCategoriaEquipo(new CategoriaEquipo());

    }

    public void addEquipo() {
        try {
            if (this.newEquipo.getFechaAdquisicion() == null) {
                DaoEquipo.agregarEquipo(newEquipo);
                Listequipos = DaoEquipo.listarEquiposPorTecnico(tecnicoID);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Equipo agregado correctamente"));

            }
            PrimeFaces.current().executeScript("PF('manageEquipoDialog').hide()");
            PrimeFaces.current().ajax().update("form-equipo:messages", "form-equipo:dt-equipos");

        } catch (SQLException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error al agregar el Equipo"));
            e.printStackTrace();
        }
    }

    public void eliminarEquipo() {
        try {
            int EliminarEquipoID = newEquipo.getId();
                DaoEquipo.eliminarEquipo(EliminarEquipoID);
                Listequipos = DaoEquipo.listarEquiposPorTecnico(tecnicoID);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Equipo Eliminado"));
                PrimeFaces.current().executeScript("PF('manageEquipoDialog').hide()");
                PrimeFaces.current().ajax().update("form-equipo:messages", "form-equipo:dt-equipos");


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
