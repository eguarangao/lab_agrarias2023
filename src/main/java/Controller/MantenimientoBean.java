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
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Data;
import org.primefaces.PrimeFaces;
import org.primefaces.event.ToggleEvent;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Named
@ViewScoped
public class MantenimientoBean implements Serializable {

    private EquipoDAO DaoEquipo = new EquipoDAO();
    private List<Equipo> Listequipos;
    private EquipoBean BeanEquipo = new EquipoBean();
    private MantenimientoDAO DAOmantenimiento = new MantenimientoDAO();
    private List<Mantenimiento> ListMante;
    private List<MantenimientoEquipo> LisMantenimientoEquipo;
    private MantenimientoEquipo newMantenimientoEquipos;
    private Mantenimiento newMantenimiento;
    private List<Equipo> equiposRequeridosMantenimiento = new ArrayList<>();
    private boolean botonManteDisabled = true;
    private boolean mostrarTablaMante = false;
    private int idUsuarioSession;
    private int idlaboratorioSession;
    private Date fechaActual = new Date();
    List<TipoMantenimiento> ListTipoMantenimiento;

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

    public Date getFechaActual() {
        return fechaActual;
    }

    public void listEquiposPorLaboratorio() throws SQLException {
        try {
            this.Listequipos = new ArrayList<>();
            Listequipos = DAOmantenimiento.listarEquiposPorLaboratorioActivos(idlaboratorioSession);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void SelectLaboratorioTecnico() throws SQLException {
        try {
            tablaequiposIDmantenimiento();
            TablaMantePorLaboratorio();
            listEquiposPorLaboratorio();
            equiposRequeridosMantenimiento = new ArrayList<>();
            getFechaActual();
            ListarTiposMantenimientos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void TablaMantePorLaboratorio() throws SQLException {
        try {
            this.ListMante = new ArrayList<>();
            ListMante = DAOmantenimiento.listarMantenimientoPorLaboratorio(idlaboratorioSession);
            mostrarTablaMante = true;
            botonManteDisabled = false;
            PrimeFaces.current().ajax().update("form-Mante:tablaMante", "form-Mante:dt-Mante","form-Mante:botonNewMante" );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void tablaequiposIDmantenimiento() {
        try{
            this.LisMantenimientoEquipo = new ArrayList<>();
            LisMantenimientoEquipo = DAOmantenimiento.listarMantenimientoEquipoPorLaboratorio(22);
        } catch (SQLException e){
            e.printStackTrace();
        }

    }


    public void nuevoMantenimiento() throws SQLException {
        this.newMantenimiento = new Mantenimiento();
        this.newMantenimiento.setMantenimientoEquipo(new MantenimientoEquipo());
        this.newMantenimiento.setAula(new Aula());
        this.newMantenimiento.setEquipo(new Equipo());
        this.newMantenimiento.setTipoMantenimiento(new TipoMantenimiento());

        this.newMantenimientoEquipos = new MantenimientoEquipo();
        this.newMantenimientoEquipos.setMantenimiento(new Mantenimiento());

    }


    public void addManteninimiento() {

        try {
                DAOmantenimiento.agregarMantenimiento(newMantenimiento,equiposRequeridosMantenimiento);
                mostrarTablaMante = !ListMante.isEmpty();
                ListMante = DAOmantenimiento.listarMantenimientoPorLaboratorio(idlaboratorioSession);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Mantenimiento agregado"));
            PrimeFaces.current().executeScript("PF('manageManteDialog').hide()");
            PrimeFaces.current().ajax().update("form-Mante:messages");
        } catch (Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error al agregar el Mantenimiento"));
            e.printStackTrace();
        }
    }

    public void updateManteninimiento() {
        try {
            if(newMantenimiento.getEstado()==false) {
                DAOmantenimiento.editarMantenimiento(newMantenimiento);
                ListMante = DAOmantenimiento.listarMantenimientoPorLaboratorio(idlaboratorioSession);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Mantenimiento actualizado"));
                PrimeFaces.current().executeScript("PF('manageManteRealizDialog').hide()");
                PrimeFaces.current().ajax().update("form-Mante:tablaMante", "form-Mante:dt-Mante", "form-Mante:messages");
            } else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No se puede editar Mantenimiento !Equipo Activo¡"));
                PrimeFaces.current().ajax().update("form-Mante:messages");
                PrimeFaces.current().executeScript("PF('manageManteRealizDialog').hide()");
            }
        } catch (Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error al agregar el Mantenimiento"));
            PrimeFaces.current().ajax().update("form-Mante:messages");
            PrimeFaces.current().executeScript("PF('manageManteRealizDialog').hide()");
            e.printStackTrace();
        }
    }

    public  void ListarTiposMantenimientos() throws  SQLException{
        this.ListTipoMantenimiento = new ArrayList<>();
        ListTipoMantenimiento = DAOmantenimiento.listartipomantenimientos();
        System.out.println(ListTipoMantenimiento);
    }

    public void confirmarmantenimiento() {
        try {
            if(newMantenimiento.getEstado()==false){
                DAOmantenimiento.ConfirmarMantenimientoRealizado(newMantenimiento);
                ListMante = DAOmantenimiento.listarMantenimientoPorLaboratorio(idlaboratorioSession);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Mantenimiento realizado"));
                PrimeFaces.current().executeScript("PF('manageManteDialog').hide()");
                PrimeFaces.current().ajax().update("form-Mante:tablaMante", "form-Mante:dt-Mante","form-Mante:messages" );

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Mantenimiento ya realizado anteriormente"));
                PrimeFaces.current().executeScript("PF('manageManteDialog').hide()");
                PrimeFaces.current().ajax().update("form-Mante:messages");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void confirmarmantenimientoaEquipo() {
        try {
            if(newMantenimientoEquipos.getEstado()==false){
                DAOmantenimiento.ConfirmarMantenimientoRealizadoEquipo(newMantenimientoEquipos,newMantenimiento);
                ListMante = DAOmantenimiento.listarMantenimientoPorLaboratorio(idlaboratorioSession);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Mantenimiento realizado al equipo"));
                PrimeFaces.current().executeScript("PF('ConfirmarManteEquipoDialog').hide()");
                PrimeFaces.current().ajax().update("form-Mante:tablaMante", "form-Mante:dt-Mante","form-Mante:messages" );

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Mantenimiento ya realizado anteriormente a equipo"));
                PrimeFaces.current().executeScript("PF('ConfirmarManteEquipoDialog').hide()");
                PrimeFaces.current().ajax().update("form-Mante:messages");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void eliminarequipodeMantenimiento() {
        try {
            if(newMantenimientoEquipos.getEstado()==false){
                int eliminarMantenimientoID = newMantenimiento.getId();
                int eliminarEquipoID = newMantenimientoEquipos.getEquipo().getId();
                DAOmantenimiento.eliminarelEquipodeMantenimiento(eliminarMantenimientoID, eliminarEquipoID);
                ListMante = DAOmantenimiento.listarMantenimientoPorLaboratorio(idlaboratorioSession);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Equipo eliminado de Mantenimiento"));
                PrimeFaces.current().executeScript("PF('deleteManteDialog').hide()");
                PrimeFaces.current().ajax().update("form-Mante:tablaMante", "form-Mante:dt-Mante","form-Mante:messages" );

                if(DAOmantenimiento.verificarexisteidmante(eliminarMantenimientoID) == false){
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Mantenimiento tambien eliminado"));
                    PrimeFaces.current().executeScript("PF('deleteManteDialog').hide()");
                    PrimeFaces.current().ajax().update("form-Mante:tablaMante", "form-Mante:dt-Mante","form-Mante:messages" );
                }

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No se puede eliminar el equipo de mantenimiento ya realizado"));
                PrimeFaces.current().executeScript("PF('deleteManteDialog').hide()");
                PrimeFaces.current().ajax().update("form-Mante:tablaMante", "form-Mante:dt-Mante","form-Mante:messages" );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarMantenimiento() {
        try {
            if(newMantenimiento.getEstado()==false){
                int eliminarMantenimientoID = newMantenimiento.getId();
                DAOmantenimiento.eliminarMantenimiento(eliminarMantenimientoID);
                ListMante = DAOmantenimiento.listarMantenimientoPorLaboratorio(idlaboratorioSession);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Mantenimiento Eliminado"));
                PrimeFaces.current().executeScript("PF('deleteManteDialog').hide()");
                PrimeFaces.current().ajax().update("form-Mante:tablaMante", "form-Mante:dt-Mante","form-Mante:messages" );

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No se puede eliminar el mantenimiento ya realizado"));
                PrimeFaces.current().executeScript("PF('deleteManteDialog').hide()");
                PrimeFaces.current().ajax().update("form-Mante:tablaMante", "form-Mante:dt-Mante","form-Mante:messages" );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
