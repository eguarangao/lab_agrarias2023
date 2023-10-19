package Controller;

import DAO.AveriaDAO;
import DAO.EquipoDAO;
import DAO.MantenimientoDAO;
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
import java.util.Date;
import java.util.List;

@Data
@Named
@SessionScoped
public class AveriaBean implements Serializable {

    private AveriaDAO DAOaveria = new AveriaDAO();
    private EquipoDAO DaoEquipo = new EquipoDAO();
    private MantenimientoDAO DAOmantenimiento = new MantenimientoDAO();
    private List<Averia> ListAveria;
    private List<Equipo> Listequipos;
    private EquipoBean BeanEquipo = new EquipoBean();
    private Averia newAveria;
    private boolean botonAveriaDisabled = true;
    private boolean mostrarTablaAveria = false;
    private int idUsuarioSession;
    private int idlaboratorioSession;
    private Date fechaActual = new Date();
    private List<Equipo> equiposAveriados = new ArrayList<>();

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

    public void SelectLaboratorioTecnico() throws SQLException {
        try {
            TablaAveriaPorLaboratorio();
            listEquiposPorLaboratorio();
            equiposAveriados = new ArrayList<>();
            getFechaActual();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void TablaAveriaPorLaboratorio() throws SQLException {
        try {
            this.ListAveria = new ArrayList<>();
            ListAveria = DAOaveria.listarAveriasPorLaboratorio(idlaboratorioSession);
            mostrarTablaAveria = !ListAveria.isEmpty();
            botonAveriaDisabled = ListAveria.isEmpty();
            PrimeFaces.current().ajax().update("form-Averia:tablaAveria", "form-Averia:dt-Averia","form-Averia:botonNewAveria" );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void listEquiposPorLaboratorio() throws SQLException {
        try {
            this.Listequipos = new ArrayList<>();
            Listequipos = DAOmantenimiento.listarEquiposPorLaboratorioActivos(idlaboratorioSession);
            System.out.println(Listequipos);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void nuevaAveria() throws SQLException {
        this.newAveria = new Averia();
        this.newAveria.setAula(new Aula());
        this.newAveria.setEquipo(new Equipo());

    }

    public void addAveria() {
        try {
                DAOaveria.agregarAveria(newAveria,equiposAveriados);
                mostrarTablaAveria = ListAveria.isEmpty();
                ListAveria = DAOaveria.listarAveriasPorLaboratorio(idlaboratorioSession);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Averia agregada"));
            PrimeFaces.current().executeScript("PF('manageAveriaDialog').hide()");
            PrimeFaces.current().ajax().update("form-Averia:messages");

        } catch (Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error al agregar la averia"));
            e.printStackTrace();
        }
    }

    public void updateAveria() {
        try {
                System.out.println("voy a editar averia");
                DAOaveria.editarAveria(newAveria);
                ListAveria = DAOaveria.listarAveriasPorLaboratorio(idlaboratorioSession);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Averia actualizada"));
            PrimeFaces.current().executeScript("PF('manageAveriaDialog').hide()");
            PrimeFaces.current().ajax().update("form-Averia:messages");

        } catch (Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error al agregar la averia"));
            e.printStackTrace();
        }
    }


}
