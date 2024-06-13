package edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.kontroler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.model.RestKlijentSimulator;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.podaci.Vozilo;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.mvc.binding.BindingResult;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Controller
@Path("simulacije")
@RequestScoped
public class KontrolerSimulacija {
  @Inject
  private Models model;

  @Inject
  private BindingResult bindingResult;

  @Inject
  private ServletContext context;

  @GET
  @Path("ispisSvihSimulacija")
  @View("simulacije.jsp")
  public void json_sim() {
    RestKlijentSimulator k = new RestKlijentSimulator();
    List<Vozilo> simulacije = k.getVoznjeJSON();
    model.put("simulacije", simulacije);
  }

  @POST
  @Path("ispisSimulacijaUIntervalu")
  @View("simulacije.jsp")
  public void json_pi_sim(@FormParam("odVremena") long odVremena,
      @FormParam("doVremena") long doVremena) {
    System.out.println(odVremena + " " + doVremena);
    RestKlijentSimulator k = new RestKlijentSimulator();
    List<Vozilo> simulacije = k.getVoznjeJSON_od_do(odVremena, doVremena);
    model.put("simulacije", simulacije);
  }

  @POST
  @Path("ispisSimulacijaPoIdVozila")
  @View("simulacije.jsp")
  public void json_pi_id_voz_sim(@FormParam("idVozila") int idVozila) {
    RestKlijentSimulator k = new RestKlijentSimulator();
    List<Vozilo> simulacije = k.getVoznjePoIdu(String.valueOf(idVozila));
    model.put("simulacije", simulacije);
  }

  @POST
  @Path("ispisSimulacijaPoIdVozilaInterval")
  @View("simulacije.jsp")
  public void getPrVozPoIduSim(@FormParam("idVozila") int idVozila,
      @FormParam("odVremena") long odVremena, @FormParam("doVremena") long doVremena) {
    RestKlijentSimulator k = new RestKlijentSimulator();
    List<Vozilo> simulacije =
        k.getVoznjePoIduInterval(String.valueOf(idVozila), odVremena, doVremena);
    model.put("simulacije", simulacije);
  }

  @POST
  @Path("pokreniSimulaciju")
  @View("odgovori.jsp")
  public void pokreniSim(@FormParam("nazivDatoteke") String nazivDatoteke,
      @FormParam("idVozila") int idVozila, @FormParam("trajanjeSek") int trajanjeSek,
      @FormParam("trajanjePauze") int trajanjePauze) {
    RestKlijentSimulator k = new RestKlijentSimulator();
    context.setAttribute("datoteka", nazivDatoteke);

    String datotekaZaKoristenje = context.getInitParameter("datoteka1");

    if (nazivDatoteke.equals("NWTiS_DZ1_V1.csv")) {
      datotekaZaKoristenje = context.getInitParameter("datoteka1");
    } else if (nazivDatoteke.equals("NWTiS_DZ1_V2.csv")) {
      datotekaZaKoristenje = context.getInitParameter("datoteka2");
    } else {
      datotekaZaKoristenje = context.getInitParameter("datoteka3");
    }

    String csvPutanja =
        context.getRealPath("/WEB-INF") + java.io.File.separator + datotekaZaKoristenje;

    try (BufferedReader citac = new BufferedReader(new FileReader(csvPutanja))) {
      citac.readLine();
      String redak;
      String prethodniRedak = null;
      int brojRetka = 0;
      RestKlijentSimulator rk = new RestKlijentSimulator();

      while ((redak = citac.readLine()) != null) {
        Vozilo vozilo =
            rk.kreirajVoziloIzRetka(redak, brojRetka, prethodniRedak, trajanjeSek, idVozila);
        rk.postDodajSimulacijuUBazu(vozilo);
        try {
          Thread.sleep(trajanjePauze);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        prethodniRedak = redak;
        brojRetka++;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    model.put("odg", "Simulacija zavrsena. Mozete provjeriti nove podatke u bazi :D");
  }

  @POST
  @Path("dodavanjeSimulacije")
  @View("odgovori.jsp")
  public void postDodajSimulaciju(@FormParam("id") int id, @FormParam("broj") int broj,
      @FormParam("vrijeme") long vrijeme, @FormParam("brzina") double brzina,
      @FormParam("snaga") double snaga, @FormParam("struja") double struja,
      @FormParam("visina") double visina, @FormParam("gpsBrzina") double gpsBrzina,
      @FormParam("tempVozila") int tempVozila, @FormParam("postotakBaterija") int postotakBaterija,
      @FormParam("naponBaterija") double naponBaterija,
      @FormParam("kapacitetBaterija") int kapacitetBaterija,
      @FormParam("tempBaterija") int tempBaterija, @FormParam("preostaloKm") double preostaloKm,
      @FormParam("ukupnoKm") double ukupnoKm, @FormParam("gpsSirina") double gpsSirina,
      @FormParam("gpsDuzina") double gpsDuzina) {

    Vozilo vozilo = new Vozilo(id, broj, vrijeme, brzina, snaga, struja, visina, gpsBrzina,
        tempVozila, postotakBaterija, naponBaterija, kapacitetBaterija, tempBaterija, preostaloKm,
        ukupnoKm, gpsSirina, gpsDuzina);

    RestKlijentSimulator k = new RestKlijentSimulator();
    String odg = k.postDodajSimulacijuUBazu(vozilo);
    model.put("odg", odg);
  }


}


