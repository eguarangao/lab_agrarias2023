package Controller;

import DAO.AveriaDAO;
import DAO.EquipoDAO;
import DAO.MantenimientoDAO;
import Model.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.PrimeFaces;


import java.io.*;
import java.sql.SQLException;
import java.util.*;
@Data
@Named
@SessionScoped
public class AveriaBean implements Serializable {

    private AveriaDAO DAOaveria = new AveriaDAO();
    private EquipoDAO DaoEquipo = new EquipoDAO();
    private MantenimientoDAO DAOmantenimiento = new MantenimientoDAO();
    private List<Averia> ListAveria;
    private List<ReporteAverias> ListReporteAveria;
    private List<Equipo> Listequipos;
    private EquipoBean BeanEquipo = new EquipoBean();
    private Averia newAveria;
    private boolean botonAveriaDisabled = true;
    private boolean botonReporteDisabled = true;
    private boolean mostrarTablaAveria = false;
    private int idUsuarioSession;
    private int idlaboratorioSession;
    private Date fechaActual = new Date();
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
            nuevaAveria();
            ListAveria = DAOaveria.listarAveriasPorLaboratorio(idlaboratorioSession);
            TablaAveriaPorLaboratorio();
            listEquiposPorLaboratorio();
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
            botonReporteDisabled = ListAveria.isEmpty();
            PrimeFaces.current().ajax().update("form-Averia:tablaAveria", "form-Averia:dt-Averia","form-Averia:botonNewAveria", "form-Averia:btnReporteAveria");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void listEquiposPorLaboratorio() throws SQLException {
        try {
            this.Listequipos = new ArrayList<>();
            Listequipos = DAOaveria.listarEquiposPorLaboratorioAverias(idlaboratorioSession);

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
                DAOaveria.agregarAveria(newAveria);
                ListAveria = DAOaveria.listarAveriasPorLaboratorio(idlaboratorioSession);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Averia agregada"));
                PrimeFaces.current().executeScript("PF('manageAveriaDialog').hide()");
                PrimeFaces.current().ajax().update("form-Averia:tablaAveria" ,"form-Averia:dt-Averia","form-Averia:messages" );

        } catch (Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error al agregar la averia"));
            PrimeFaces.current().executeScript("PF('manageAveriaDialog').hide()");
            PrimeFaces.current().ajax().update("form-Averia:messages");
            e.printStackTrace();
        }
    }

    public void updateAveria() {
        try {
            boolean equipoEstadoAveria = DAOaveria.verificarEstadoAveriaEquipo(newAveria);
            if(newAveria.getEnabled()==true && equipoEstadoAveria==true) {
                System.out.println("voy a editar averia");
                DAOaveria.editarAveria(newAveria);
                ListAveria = DAOaveria.listarAveriasPorLaboratorio(idlaboratorioSession);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Averia actualizada"));
                PrimeFaces.current().executeScript("PF('manageAveriaeditDialog').hide()");
                PrimeFaces.current().ajax().update("form-Averia:tablaAveria", "form-Averia:dt-Averia", "form-Averia:messages");
            }
            else if (equipoEstadoAveria==false){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No se puede editar averia !Equipo Dañado¡ "));
                PrimeFaces.current().executeScript("PF('manageAveriaeditDialog').hide()");
                PrimeFaces.current().ajax().update("form-Averia:tablaAveria", "form-Averia:dt-Averia", "form-Averia:messages");
            }

            else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No se puede editar averia !Equipo reparado¡ "));
                PrimeFaces.current().executeScript("PF('manageAveriaeditDialog').hide()");
                PrimeFaces.current().ajax().update("form-Averia:tablaAveria", "form-Averia:dt-Averia", "form-Averia:messages");
            }
        } catch (Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error al agregar la averia"));
            PrimeFaces.current().executeScript("PF('manageAveriaDialog').hide()");
            PrimeFaces.current().ajax().update("form-Averia:messages");
            e.printStackTrace();
        }
    }

    public void confirmaraveria() {
        try {
            boolean equipoEstadoAveria = DAOaveria.verificarEstadoAveriaEquipo(newAveria);

            if(newAveria.getEnabled()==true && equipoEstadoAveria==true){
                DAOaveria.ConfirmarAveriaRealizada(newAveria);
                ListAveria = DAOaveria.listarAveriasPorLaboratorio(idlaboratorioSession);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Equipo reparado"));
                PrimeFaces.current().executeScript("PF('manageAveriaRealizadaDialog').hide()");
                PrimeFaces.current().ajax().update("form-Averia:tablaAveria", "form-Averia:dt-Averia","form-Averia:messages" );

            } else if (equipoEstadoAveria==false){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Equipo dañado"));
                PrimeFaces.current().executeScript("PF('ConfirmarAveriaDialog').hide()");
                PrimeFaces.current().ajax().update("form-Averia:messages");

            }
                else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Equipo reparado ya anteriormente"));
                PrimeFaces.current().executeScript("PF('ConfirmarAveriaDialog').hide()");
                PrimeFaces.current().ajax().update("form-Averia:messages");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void ReportPDF() throws IOException, JRException, SQLException {
        this.ListReporteAveria = new ArrayList<>();
        ListReporteAveria = DAOaveria.listarAveriasPorLaboratorioString(idlaboratorioSession);

        // es una peticion para descargar
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        // Cabecera de la respuesta.
        ec.responseReset();
        // formato PDF
        ec.setResponseContentType("application/pdf");
        // Nombre para descargar el Archivo
        ec.setResponseHeader("Content-disposition", String.format("attachment; filename=Repor.pdf"));


        // tomamos el stream para llenarlo con el pdf.
        try (OutputStream stream = ec.getResponseOutputStream()) {

            // Parametros para el reporte.
            Map<String, Object> parametros = new HashMap<String, Object>();

            //parametros.put("fecha_registro", newAveria.getFecha_registro());
            //parametros.put("descripcion", newAveria.getEquipo().getDescripcion());


            String reportePDF = "C:\\Users\\Equipo\\Documents\\GitHub\\lab_agrarias2023\\target\\jsfdemolab\\resources\\reportes\\reporteAveriaA4.jrxml";

            JasperReport jasperReport = JasperCompileManager.compileReport(reportePDF);


            // llenamos la plantilla con los datos.
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JRBeanCollectionDataSource(this.ListReporteAveria));

            // exportamos a pdf.
            JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
            //JasperExportManager.exportReportToXmlStream(jasperPrint, outputStream);

            stream.flush();
            stream.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            // enviamos la respuesta.
            fc.responseComplete();
        }

    }

}
