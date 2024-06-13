package edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.kontroler;

import java.util.List;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.model.RestKlijentVozila;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.podaci.Vozilo;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.mvc.binding.BindingResult;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Controller
@Path("vozila")
@RequestScoped
public class KontrolerVozila {
  @Inject
  private Models model;

  @Inject
  private BindingResult bindingResult;

  @GET
  @Path("ispisPraceneVoznje")
  @View("vozila.jsp")
  public void json() {
    RestKlijentVozila k = new RestKlijentVozila();
    List<Vozilo> vozila = k.getPraceneVoznjeJSON();
    model.put("vozila", vozila);
  }

  @POST
  @Path("ispisPraceneVoznjeUIntervalu")
  @View("vozila.jsp")
  public void json_pi(@FormParam("odVremena") long odVremena,
      @FormParam("doVremena") long doVremena) {
    System.out.println(odVremena + " " + doVremena);
    RestKlijentVozila k = new RestKlijentVozila();
    List<Vozilo> vozila = k.getPraceneVoznjeJSON_od_do(odVremena, doVremena);
    model.put("vozila", vozila);
  }

  @POST
  @Path("ispisPracenihVoznjiPoIdVozila")
  @View("vozila.jsp")
  public void json_pi_id_voz(@FormParam("idVozila") int idVozila) {
    RestKlijentVozila k = new RestKlijentVozila();
    List<Vozilo> vozila = k.getPraceneVoznjePoIdu(String.valueOf(idVozila));
    model.put("vozila", vozila);
  }

  @POST
  @Path("ispisPracenihVoznjiPoIdVozilaInterval")
  @View("vozila.jsp")
  public void getPrVozPoIdu(@FormParam("idVozila") int idVozila,
      @FormParam("odVremena") long odVremena, @FormParam("doVremena") long doVremena) {
    RestKlijentVozila k = new RestKlijentVozila();
    List<Vozilo> vozila =
        k.getPraceneVoznjePoIduInterval(String.valueOf(idVozila), odVremena, doVremena);
    model.put("vozila", vozila);
  }

  @POST
  @Path("startanjeVozila")
  @View("odgovori.jsp")
  public void json_start_voz(@FormParam("idVozila") int idVozila) {
    RestKlijentVozila k = new RestKlijentVozila();
    String odg = k.getStartVozila(idVozila);
    model.put("odg", odg);
  }

  @POST
  @Path("stopanjeVozila")
  @View("odgovori.jsp")
  public void json_stop_voz(@FormParam("idVozila") int idVozila) {
    RestKlijentVozila k = new RestKlijentVozila();
    String odg = k.getStopVozila(idVozila);
    model.put("odg", odg);
  }

  @POST
  @Path("dodavanjeVoznje")
  @View("odgovori.jsp")
  public void postDodajVoznju(@FormParam("id") int id, @FormParam("broj") int broj,
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

    RestKlijentVozila k = new RestKlijentVozila();
    String odg = k.postDodajVoznjuUBazu(vozilo);
    model.put("odg", odg);
  }


}
