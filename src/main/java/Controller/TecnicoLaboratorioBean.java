package Controller;

import DAO.TecnicoLaboratorioDAO;
import Model.Aula;
import Model.Laboratorio;
import Model.ListFullUser;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
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
    private List<Laboratorio> listLab;
    private List<Aula> listAula;
    private int idLaboratorio;
    private int idFacultad;
    private boolean rendered;
    private Aula nuevaAula;

    @PostConstruct
    public void main() {
        try {
            rendered = true;
            listAula = new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listLabByFacultad() {
        tecnicoDAO = new TecnicoLaboratorioDAO();
        listLab = tecnicoDAO.listarPorFacultad(this.idFacultad);
    }

    public void listAulaByLab() {

        tecnicoDAO = new TecnicoLaboratorioDAO();

        listAula = tecnicoDAO.listarAulaPorLaboratorio(this.idLaboratorio);
        PrimeFaces.current().ajax().update("form:messages", "form:dt-facd");
    }
    public void openNew() {
        this.nuevaAula = new Aula();

    }


}
