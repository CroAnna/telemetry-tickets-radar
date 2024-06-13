package edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.kontroler;

import java.util.ArrayList;
import java.util.List;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.model.RestKlijentRadari;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.podaci.Radar;
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

@Controller
@Path("radari")
@RequestScoped
public class KontrolerRadara {

  @Inject
  private Models model;

  @Inject
  private BindingResult bindingResult;

  @GET
  @Path("ispisRadara")
  @View("radari.jsp")
  public void json() {
    RestKlijentRadari k = new RestKlijentRadari();
    List<Radar> radari = k.getRadariJSON();
    model.put("radari", radari);
  }

  @GET
  @Path("ispisRadaraPoId")
  @View("radari.jsp")
  public void json_id(@QueryParam("id") int id) {
    RestKlijentRadari k = new RestKlijentRadari();
    Radar radar = k.getRadariJSONId(String.valueOf(id));
    List<Radar> radari = new ArrayList<>();
    if (radar != null) {
      radari.add(radar);
    }
    model.put("radari", radari);
  }

  @POST
  @Path("ispisRadaraPoIdu")
  @View("radari.jsp")
  public void json_pi_id(@FormParam("idRadara") int idRadara) {
    RestKlijentRadari k = new RestKlijentRadari();
    Radar radar = k.getRadariJSONId(String.valueOf(idRadara));
    List<Radar> radari = new ArrayList<>();
    if (radar != null) {
      radari.add(radar);
    }
    model.put("radari", radari);
  }

  @POST
  @Path("provjeravanjeRadara")
  @View("odgovori.jsp")
  public void json_provjera_radara(@FormParam("idRadara") int idRadara) {
    RestKlijentRadari k = new RestKlijentRadari();
    String odg = k.getProvjeraRadara(idRadara);
    model.put("odg", odg);
  }

  @POST
  @Path("brisanjeRadaraPoId")
  @View("radari.jsp")
  public void deleteRadarById(@FormParam("idRadara") int idRadara) {
    RestKlijentRadari k = new RestKlijentRadari();
    k.deleteRadarById(idRadara);
    List<Radar> radari = k.getRadariJSON();
    model.put("radari", radari);
  }

  @POST
  @Path("brisanjeSvihRadara")
  @View("radari.jsp")
  public void deleteRadarSve() {
    RestKlijentRadari k = new RestKlijentRadari();
    k.deleteRadari();
    List<Radar> radari = k.getRadariJSON();
    model.put("radari", radari);
  }

  @POST
  @Path("resetiranjeRadara")
  @View("radari.jsp")
  public void getRadarReset() {
    RestKlijentRadari k = new RestKlijentRadari();
    k.resetRadari();
    List<Radar> radari = k.getRadariJSON();
    model.put("radari", radari);
  }
}
