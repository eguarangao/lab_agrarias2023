package Controller;

import DAO.HorarioDAO;
import DAO.LaboratorioDAO;
import Model.*;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Data
@Named
@ViewScoped
public class LaboratorioBean implements Serializable {
    int idLaboratorio;
    List<Laboratorio> laboratorioList;
    HorarioDAO horarioDAO;
    Date fecha;
    List<Horario> horarioListforLaboratorio;
    LaboratorioDAO laboratorioDAO;
    Horario horario;
    List<Item> listaPrueba;

    ///
    String fechaString = "2023-05-01";
    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
    Date fechaEspecifica;

    {
        try {
            fechaEspecifica = formato.parse(fechaString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    public void finAllLaboratorio() throws SQLException {
        laboratorioList = new ArrayList<>();
        LaboratorioDAO laboratorioDAO = new LaboratorioDAO();
        laboratorioList = laboratorioDAO.findAllLaboratorio();
    }

    public void listHoras() {
        horarioListforLaboratorio= new ArrayList<>();
        System.out.println("#########################");
        System.out.println(idLaboratorio);

//        System.out.println(listaPrueba);
//        System.out.println(fechaEspecifica);

        horarioDAO = new HorarioDAO();
        // horarioListforLaboratorio = new ArrayList<>();
        horarioListforLaboratorio = horarioDAO.horarioForLaboratorio(idLaboratorio, fechaString);
        System.out.println("#########################TAMAÑO LISTA");
        System.out.println(horarioListforLaboratorio.size());
        System.out.println(horarioListforLaboratorio);
        for (int i = 0; horarioListforLaboratorio.size() <1; i++) {
            // Boolean iten = String.valueOf(horarioListforLaboratorio.get(i).isJornada1() + " " + horarioListforLaboratorio.get(i).is);

            Item item1 = new Item();
            item1.setId(1);
            item1.setDato(horarioListforLaboratorio.get(i).isJornada1());

            Item item2 = new Item();
            item1.setId(2);
            item1.setDato(horarioListforLaboratorio.get(i).isJornada2());

            Item item3 = new Item();
            item1.setId(3);
            item1.setDato(horarioListforLaboratorio.get(i).isJornada3());

            Item item4 = new Item();
            item1.setId(4);
            item1.setDato(horarioListforLaboratorio.get(i).isJornada4());

            Item item5 = new Item();
            item1.setId(5);
            item1.setDato(horarioListforLaboratorio.get(i).isJornada5());

            listaPrueba.add(item1);
            listaPrueba.add(item2);
            listaPrueba.add(item3);
            listaPrueba.add(item4);
            listaPrueba.add(item5);

            // Hacer algo con el objeto "equipo" en cada iteración


        }
        System.out.println("LISTAAAAAAAAAAAAAAAAAAAAAAAA");
        System.out.println(listaPrueba);


    }
}
