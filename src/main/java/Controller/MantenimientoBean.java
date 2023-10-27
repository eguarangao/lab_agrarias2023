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
import org.primefaces.event.ToggleEvent;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Named
@SessionScoped
public class MantenimientoBean implements Serializable {

    private EquipoDAO DaoEquipo = new EquipoDAO();
    private List<Equipo> Listequipos;
    private EquipoBean BeanEquipo = new EquipoBean();
    private MantenimientoDAO DAOmantenimiento = new MantenimientoDAO();
    private List<Mantenimiento> ListMante;
    private List<MantenimientoEquipo> LisMantenimientoEquipo;
    private MantenimientoEquipo newMante;
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
            mostrarTablaMante = !ListMante.isEmpty();
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
        this.newMante = new MantenimientoEquipo();
        this.newMante.setMantenimiento(new Mantenimiento());
        this.newMante.setAula(new Aula());
        this.newMante.setEquipo(new Equipo());
        this.newMante.setTipoMantenimiento(new TipoMantenimiento());

    }


    public void addManteninimiento() {

        try {
                DAOmantenimiento.agregarMantenimiento(newMante,equiposRequeridosMantenimiento);
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
                System.out.println("voy a editar mantenimiento");
                DAOmantenimiento.editarMantenimiento(newMante);
                ListMante = DAOmantenimiento.listarMantenimientoPorLaboratorio(idlaboratorioSession);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Mantenimiento actualizado"));
            PrimeFaces.current().executeScript("PF('manageManteDialog').hide()");
            PrimeFaces.current().ajax().update("form-Mante:messages");

        } catch (Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error al agregar el Mantenimiento"));
            e.printStackTrace();
        }
    }

    public  void ListarTiposMantenimientos() throws  SQLException{
        this.ListTipoMantenimiento = new ArrayList<>();
        ListTipoMantenimiento = DAOmantenimiento.listartipomantenimientos();
        System.out.println(ListTipoMantenimiento);
    }



}
