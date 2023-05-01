package Controller;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Data;

import java.io.*;


import java.io.File;

@Data
@Named
@SessionScoped
public class SolicitudBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private String tipoSolicitud="lalalalala";
    private String nombreArchivo;

    public void getTipoSolicitudes(int opcion) {
        if (opcion == 1) {
            tipoSolicitud = "SOLICITUD PRACTICA DE ESTUDIANTE";
        } else if (opcion == 2) {
            tipoSolicitud = "SOLICITUD PRACTICAS DE TESIS";
        } else {
            tipoSolicitud = "SOLICITUD DE INVESTIGACIÓN";
        }
    }


}
