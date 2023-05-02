package Controller;

import DAO.LaboratorioDAO;
import Model.Laboratorio;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Data
@Named
@ViewScoped
public class LaboratorioBean implements Serializable{
    int idLaboratorio;
    LaboratorioDAO laboratorioDAO;
    List<Laboratorio> laboratorioList ;
    @SneakyThrows
    @PostConstruct
    public void init() {
      System.out.println("PostConstruct");
      laboratorioDAO = new LaboratorioDAO();
      laboratorioList = new ArrayList<>();
      laboratorioList = laboratorioDAO.findAllLaboratorio();
    }

}
