package Controller;

import DAO.HorarioDAO;
import DAO.LaboratorioDAO;
import Model.Horario;
import Model.Laboratorio;
import Model.Rol;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Data
@Named
@ViewScoped
public class LaboratorioBean implements Serializable{
    int idLaboratorio;
    LaboratorioDAO laboratorioDAO;
    List<Laboratorio> laboratorioList ;
    HorarioDAO horarioDAO ;
    Date fecha;
    List<Horario> horarioListforLaboratorio ;
    Horario horario;
    @SneakyThrows
    @PostConstruct
    public void init() {
      System.out.println("PostConstruct");

      laboratorioDAO = new LaboratorioDAO();
      laboratorioList = new ArrayList<>();
      laboratorioList = laboratorioDAO.findAllLaboratorio();
      System.out.println(fecha);
    }

    public void listHoras() {
        horarioDAO = new HorarioDAO();
        horarioListforLaboratorio = new ArrayList<>();
        horarioListforLaboratorio = horarioDAO.horarioForLaboratorio(idLaboratorio,fecha);

    }
}
