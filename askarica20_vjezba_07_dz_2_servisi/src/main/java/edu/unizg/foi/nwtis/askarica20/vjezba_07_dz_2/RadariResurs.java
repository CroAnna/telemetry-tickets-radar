package edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2;

import java.net.UnknownHostException;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST Web Service uz korištenje klase Radar
 *
 * @author Ana Škarica
 */
@Path("nwtis/v1/api/radari")
public class RadariResurs extends SviResursi {

  private int mreznaVrataRegistracije;
  private String adresaRegistracije;

  @PostConstruct
  private void pripremiKorisnikDAO() {
    System.out.println("Pokrećem REST: " + this.getClass().getName());
    try {
      preuzmiPostavke("NWTiS_REST_R.txt");
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
  }

  /**
   * Dohvaća sve radare.
   * 
   * @param tipOdgovora vrsta MIME odgovora
   * @return odgovor s podacima o svim radarima
   */
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJson(@HeaderParam("Accept") String tipOdgovora) {
    var komanda = new StringBuilder();
    komanda.append("RADAR").append(" ").append("SVI");

    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(this.adresaRegistracije,
        this.mreznaVrataRegistracije, komanda.toString());

    var noviOdgovor = pretvoriUJsonOblik(odgovor);
    return Response.status(Response.Status.OK).entity(noviOdgovor).build();
  }

  /**
   * Pretvara odgovor u JSON format.
   * 
   * @param odgovor odgovor u formatu Stringa
   * @return JSON format odgovora
   */
  private String pretvoriUJsonOblik(String odgovor) {
    if (odgovor.startsWith("OK ")) {
      odgovor = odgovor.substring(3);
    }

    odgovor = odgovor.replace("}", "").replace("{", "").replace("[", "").replace("]", "");
    String[] parts = odgovor.split(",");

    StringBuilder jsonResponse = new StringBuilder("[");
    for (String part : parts) {
      String[] attributes = part.trim().split(" ");
      jsonResponse.append("{").append("\"id\":").append(attributes[0]).append(",")
          .append("\"adresaRadara\":\"").append(attributes[1]).append("\",")
          .append("\"mreznaVrataRadara\":").append(attributes[2]).append(",")
          .append("\"gpsSirina\":").append(attributes[3]).append(",").append("\"gpsDuzina\":")
          .append(attributes[4]).append(",").append("\"maksUdaljenost\":").append(attributes[5])
          .append("},");
    }

    if (jsonResponse.charAt(jsonResponse.length() - 1) == ',') {
      jsonResponse.deleteCharAt(jsonResponse.length() - 1); // makni zarez ak je bil zadnji element
    }

    jsonResponse.append("]");

    return jsonResponse.toString();
  }

  /**
   * Dohvaća podatke o odabranom radaru.
   * 
   * @param tipOdgovora vrsta MIME odgovora
   * @param id identifikator radara
   * @return odgovor s podacima o odabranom radaru
   */
  @Path("/{id}")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonRadarId(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("id") int id) {
    var komanda = new StringBuilder();
    komanda.append("RADAR").append(" ").append("SVI");

    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(this.adresaRegistracije,
        this.mreznaVrataRegistracije, komanda.toString());
    var odabraniRadar = pretvoriUJsonOblikSamoTajRadar(id, odgovor);

    if (odabraniRadar == "prazno") {
      return Response.status(Response.Status.OK)
          .entity("{\"odgovor\":\"" + "ERROR 33 Nema ovog radara u kolekciji" + "\"}").build();
    } else {
      return Response.status(Response.Status.OK).entity(odabraniRadar).build();
    }

  }

  /**
   * Pretvara odgovor u JSON format.
   * 
   * @param odgovor odgovor u formatu Stringa
   * @param id identifikator radara
   * @return JSON format odgovora
   */
  private String pretvoriUJsonOblikSamoTajRadar(int id, String odgovor) {
    if (odgovor == null || odgovor.isEmpty()) {
      return null;
    }
    if (odgovor.startsWith("OK ")) {
      odgovor = odgovor.substring(3);
    }
    odgovor = odgovor.replace("}", "").replace("{", "").replace("[", "").replace("]", "");
    String[] parts = odgovor.split(",");
    boolean naden = false;

    StringBuilder jsonResponse = new StringBuilder("");
    for (String part : parts) {
      String[] attributes = part.trim().split(" ");

      if (attributes[0].equals(String.valueOf(id))) {
        naden = true;
        jsonResponse.append("{").append("\"id\":").append(attributes[0]).append(",")
            .append("\"adresaRadara\":\"").append(attributes[1]).append("\",")
            .append("\"mreznaVrataRadara\":").append(attributes[2]).append(",")
            .append("\"gpsSirina\":").append(attributes[3]).append(",").append("\"gpsDuzina\":")
            .append(attributes[4]).append(",").append("\"maksUdaljenost\":").append(attributes[5])
            .append("}");
      }
    }
    if (naden) {
      return jsonResponse.toString();
    } else {
      return "prazno";
    }
  }

  /**
   * Provjerava status odabranog radara.
   * 
   * @param tipOdgovora vrsta MIME odgovora
   * @param id identifikator radara
   * @return odgovor s provjerenim statusom odabranog radara
   */
  @Path("/{id}/provjeri")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonRadarIdProvjeri(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("id") int id) {
    var komanda = new StringBuilder();
    komanda.append("RADAR").append(" ").append(id);

    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(this.adresaRegistracije,
        this.mreznaVrataRegistracije, komanda.toString());
    return Response.status(Response.Status.OK).entity("{\"odgovor\":\"" + odgovor + "\"}").build();
  }

  /**
   * Resetira odabrane radare.
   * 
   * @param tipOdgovora vrsta MIME odgovora
   * @return odgovor s informacijom o rezultatu resetiranja
   */
  @Path("/reset")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonRadarReset(@HeaderParam("Accept") String tipOdgovora) {
    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(this.adresaRegistracije,
        this.mreznaVrataRegistracije, "RADAR RESET");
    return Response.status(Response.Status.OK).entity("{\"odgovor\":\"" + odgovor + "\"}").build();
  }

  /**
   * Briše sve radare.
   * 
   * @param tipOdgovora vrsta MIME odgovora
   * @return odgovor s informacijom o rezultatu brisanja
   */
  @Path("/")
  @DELETE
  @Produces({MediaType.APPLICATION_JSON})
  public Response deleteJson(@HeaderParam("Accept") String tipOdgovora) {
    // TODO
    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(this.adresaRegistracije,
        this.mreznaVrataRegistracije, "RADAR OBRIŠI SVE");
    return Response.status(Response.Status.OK).entity("{\"odgovor\":\"" + odgovor + "\"}").build();
  }

  /**
   * Briše odabrani radar.
   * 
   * @param tipOdgovora vrsta MIME odgovora
   * @param id identifikator radara
   * @return odgovor s informacijom o rezultatu brisanja odabranog radara
   */
  @Path("/{id}")
  @DELETE
  @Produces({MediaType.APPLICATION_JSON})
  public Response deleteJsonId(@HeaderParam("Accept") String tipOdgovora, @PathParam("id") int id) {
    var komanda = new StringBuilder();
    komanda.append("RADAR").append(" ").append("OBRIŠI").append(" ").append(id);

    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(this.adresaRegistracije,
        this.mreznaVrataRegistracije, komanda.toString());
    return Response.status(Response.Status.OK).entity("{\"odgovor\":\"" + odgovor + "\"}").build();
  }

  private void preuzmiPostavke(String nazivDat)
      throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(nazivDat);
    this.mreznaVrataRegistracije = Integer.valueOf(konfig.dajPostavku("mreznaVrataRegistracije"));
    this.adresaRegistracije = konfig.dajPostavku("adresaRegistracije");
  }

}
