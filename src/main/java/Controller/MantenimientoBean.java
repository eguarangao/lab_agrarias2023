package Controller;

import DAO.EquipoDAO;
import DAO.MantenimientoDAO;
import Model.*;
import Model.Mantenimiento;
import Model.TipoMantenimiento;

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
public class MantenimientoBean implements Serializable {

    private EquipoDAO DaoEquipo = new EquipoDAO();
    private EquipoBean BeanEquipo = new EquipoBean();
    private MantenimientoDAO DAOmantenimiento = new MantenimientoDAO();
    private List<MantenimientoEquipo> ListMante;
    private MantenimientoEquipo newMante;
    private boolean botonManteDisabled = true;
    private boolean mostrarTablaMante = false;
    private int idUsuarioSession;
    private int idlaboratorioSession;

    @PostConstruct
    public void main() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            Usuario usuario = (Usuario) facesContext.getExternalContext().getSessionMap().get("usuario");
            idUsuarioSession = usuario.getId();

            if(DaoEquipo.VerificadorAdmin(idUsuarioSession)){
                System.out.println("Soy Admin");
            }
            else if (DaoEquipo.VerificadorTecnic(idUsuarioSession)){
                BeanEquipo.ListarLaboratorios();
            }
            else{ System.out.println("No existe usuario"); }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void SelectLaboratorioTecnico() throws SQLException {
        try {
            TablaMantePorLaboratorio();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void TablaMantePorLaboratorio() throws SQLException {
        try {
            this.ListMante = new ArrayList<>();
            ListMante = DAOmantenimiento.listarMantenimientoPorLaboratorio(idlaboratorioSession);
            mostrarTablaMante = !ListMante.isEmpty();
            botonManteDisabled = ListMante.isEmpty();
            PrimeFaces.current().ajax().update("form-Mante:tablaMante", "form-Mante:dt-Mante","form-Mante:botonNewMante" );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void nuevoMantenimiento() throws SQLException {
        this.newMante = new MantenimientoEquipo();
        this.newMante.setAula(new Aula());

    }


    public void addManteninimiento() {
        try {
            if(this.newMante.getMantenimiento().getFechaRegistro()==null){
                DAOmantenimiento.agregarMantenimiento(newMante);
                ListMante = DAOmantenimiento.listarMantenimientoPorLaboratorio(idlaboratorioSession);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Mantenimiento agregado"));
            } else {
                System.out.println("voy a editar mantenimiento");
                DAOmantenimiento.editarMantenimiento(newMante);
                ListMante = DAOmantenimiento.listarMantenimientoPorLaboratorio(idlaboratorioSession);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Mantenimiento actualizado"));
            }
            PrimeFaces.current().executeScript("PF('manageManteDialog').hide()");
            PrimeFaces.current().ajax().update("form-Mante:messages");

        } catch (Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error al agregar el Mantenimiento"));
            e.printStackTrace();
        }
    }



}
