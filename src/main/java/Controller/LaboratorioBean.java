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
//    boolean seleccionadoLaboratorio = false;
    int idHorario;
    int idItems;

    ///
    Date fechaDate= new Date();
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
//    public boolean isLaboratorio()
//    {
//        return seleccionadoLaboratorio = true;
//    }


    public void finAllLaboratorio() throws SQLException {
        laboratorioList = new ArrayList<>();
        LaboratorioDAO laboratorioDAO = new LaboratorioDAO();
        laboratorioList = laboratorioDAO.findAll();
    }

    public void listHoras() {
        horarioListforLaboratorio = new ArrayList<>();
        listaPrueba = new ArrayList<>();
        System.out.println("#########################");
        System.out.println(idLaboratorio);

//        System.out.println(listaPrueba);
//        System.out.println(fechaEspecifica);

        horarioDAO = new HorarioDAO();
        // horarioListforLaboratorio = new ArrayList<>();
        horarioListforLaboratorio = horarioDAO.horarioForLaboratorio(idLaboratorio, formato.format(fechaDate));
        System.out.println("#########################TAMAÑO LISTA");
        System.out.println(horarioListforLaboratorio.size());
        System.out.println(horarioListforLaboratorio);
        System.out.println("#########################ID LA ORATORIO");
        System.out.println(idLaboratorio);
        // Boolean iten = String.valueOf(horarioListforLaboratorio.get(i).isJornada1() + " " + horarioListforLaboratorio.get(i).is);

        Item item1 = new Item();
        item1.setId(1);
        item1.setDato(horarioListforLaboratorio.get(0).isJornada1());
        item1.setFecha("08:00 am / 10:00 am");

        Item item2 = new Item();
        item2.setId(2);
        item2.setDato(horarioListforLaboratorio.get(0).isJornada2());
        item2.setFecha("10:00 am / 12:00 pm");

        Item item3 = new Item();
        item3.setId(3);
        item3.setDato(horarioListforLaboratorio.get(0).isJornada3());
        item3.setFecha("12:00 pm / 14:00 pm");

        Item item4 = new Item();
        item4.setId(4);
        item4.setDato(horarioListforLaboratorio.get(0).isJornada4());
        item4.setFecha("14:00 pm / 16:00 pm");

        Item item5 = new Item();
        item5.setId(5);
        item5.setDato(horarioListforLaboratorio.get(0).isJornada5());
        item5.setFecha("16:00 pm / 18:00 pm");

        listaPrueba.add(item1);
        listaPrueba.add(item2);
        listaPrueba.add(item3);
        listaPrueba.add(item4);
        listaPrueba.add(item5);

        // Hacer algo con el objeto "equipo" en cada iteración


        System.out.println("LISTAAAAAAAAAAAAAAAAAAAAAAAA");
        System.out.println(listaPrueba.size());
        System.out.println(listaPrueba);


    }
}
