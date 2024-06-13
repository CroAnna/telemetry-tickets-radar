package edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.kontroler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class SlusacKonteksta implements ServletContextListener {

  private ServletContext context = null;

  /**
   * Ova metoda se poziva kada se inicijalizira kontekst servleta (tj. kada se web aplikacija
   * implementira). Dohvaća parametre konteksta i učitava podatke iz specificiranih datoteka.
   *
   * @param event Događaj koji sadrži kontekst servleta koji se inicijalizira.
   */
  @Override
  public void contextInitialized(ServletContextEvent event) {
    context = event.getServletContext();
    String imeDat = context.getInitParameter("datoteka");
    ucitajDatoteku(1);
    ucitajDatoteku(2);
    ucitajDatoteku(3);
  }

  /**
   * Ova metoda učitava određenu datoteku na temelju broja parametra. Konstruira putanju do datoteke
   * i zatim čita sadržaj datoteke.
   *
   * @param brDat Broj koji označava koju datoteku učitati.
   */
  private void ucitajDatoteku(int brDat) {
    String path = context.getRealPath("/WEB-INF") + java.io.File.separator;
    String datoteka;

    if (brDat == 1) {
      datoteka = context.getInitParameter("datoteka1");
    } else if (brDat == 2) {
      datoteka = context.getInitParameter("datoteka2");
    } else {
      datoteka = context.getInitParameter("datoteka3");
    }

    String csvPutanjaDoDatoteke = path + datoteka;
    procitajDatoteke(csvPutanjaDoDatoteke, brDat);
  }

  /**
   * Ova metoda čita sadržaj CSV datoteke i pohranjuje podatke u mapu. Također postavlja atribut
   * konteksta s pročitanim podacima.
   *
   * @param csvPutanjaDoDatoteke Putanja do CSV datoteke.
   * @param brDat Broj koji označava koju datoteku učitavamo.
   */
  private void procitajDatoteke(String csvPutanjaDoDatoteke, int brDat) {
    Map<String, String> podaciMap = new LinkedHashMap<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(csvPutanjaDoDatoteke))) {
      reader.readLine(); // preskoci prvi
      String line;

      while ((line = reader.readLine()) != null) {
        String[] parts = line.split(",");
        if (parts.length >= 4) {
          String miliseconds = parts[0];
          String speed = parts[1];
          String power = parts[2];
          String ampere = parts[3];
          String value = "Speed: " + speed + ", Power: " + power + ", Ampere: " + ampere;
          podaciMap.put(miliseconds, value);
        }
      }
      if (brDat == 1) {
        context.setAttribute("csvData1", podaciMap);
      } else if (brDat == 2) {
        context.setAttribute("csvData2", podaciMap);
      } else {
        context.setAttribute("csvData3", podaciMap);
      }
    } catch (IOException e) {
    }
  }

  /**
   * Ova metoda se poziva kada se kontekst servleta uništava (tj. kada se web aplikacija
   * deimplementira).
   *
   * @param event Događaj koji sadrži kontekst servleta koji se uništava.
   */
  @Override
  public void contextDestroyed(ServletContextEvent event) {
    context = event.getServletContext();
  }
}
