/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.kontroler;

import java.util.ArrayList;
import java.util.List;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.model.RestKlijentKazne;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.podaci.Kazna;
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
import jakarta.ws.rs.QueryParam;

/**
 *
 * @author NWTiS
 */
@Controller
@Path("kazne")
@RequestScoped
public class Kontroler {

  @Inject
  private Models model;

  @Inject
  private BindingResult bindingResult;

  @GET
  @Path("pocetak")
  @View("index.jsp")
  public void pocetak() {}

  @GET
  @Path("ispisKazni")
  @View("kazne.jsp")
  public void json() {
    RestKlijentKazne k = new RestKlijentKazne();
    List<Kazna> kazne = k.getKazneJSON();
    model.put("kazne", kazne);
  }

  @GET
  @Path("ispisKazniPoId")
  @View("kazne.jsp")
  public void json_id(@QueryParam("id") int id) {
    RestKlijentKazne k = new RestKlijentKazne();
    List<Kazna> kazne = k.getKazneJSON_vozilo(String.valueOf(id));
    model.put("kazne", kazne);
  }

  @POST
  @Path("pretrazivanjeKazni")
  @View("kazne.jsp")
  public void json_pi(@FormParam("odVremena") long odVremena,
      @FormParam("doVremena") long doVremena) {
    System.out.println(odVremena + " " + doVremena);
    RestKlijentKazne k = new RestKlijentKazne();
    List<Kazna> kazne = k.getKazneJSON_od_do(odVremena, doVremena);
    model.put("kazne", kazne);
  }

  @POST
  @Path("pretrazivanjeKaznePoIdu")
  @View("kazne.jsp")
  public void json_pi_id(@FormParam("idKazne") int idKazne) {
    RestKlijentKazne k = new RestKlijentKazne();
    Kazna kazna = k.getKaznaJSON_rb(String.valueOf(idKazne));
    List<Kazna> kazne = new ArrayList<>();
    if (kazna != null) {
      kazne.add(kazna);
    }
    model.put("kazne", kazne);
  }

  @POST
  @Path("ispisKazniPoIdVozila")
  @View("kazne.jsp")
  public void json_pi_id_voz(@FormParam("idVozila") int idVozila) {
    RestKlijentKazne k = new RestKlijentKazne();
    List<Kazna> kazne = k.getKazneJSON_vozilo(String.valueOf(idVozila));
    model.put("kazne", kazne);
  }

  @POST
  @Path("ispisKazniPoIdVozilaInterval")
  @View("kazne.jsp")
  public void json_pi(@FormParam("idVozila") int idVozila, @FormParam("odVremena") long odVremena,
      @FormParam("doVremena") long doVremena) {
    System.out.println(odVremena + " " + doVremena);
    RestKlijentKazne k = new RestKlijentKazne();
    List<Kazna> kazne = k.getKazneJSON_vozilo_od_do(String.valueOf(idVozila), odVremena, doVremena);
    model.put("kazne", kazne);
  }
}
