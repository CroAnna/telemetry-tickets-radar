package edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2;

import java.net.UnknownHostException;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.podaci.Vozilo;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.podaci.VoziloDAO;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Resurs za upravljanje vozilima.
 */
@Path("nwtis/v1/api/simulacije")
public class VozilaResurs extends SviResursi {
  // zapisuje u tablicu voznje
  private VoziloDAO voziloDAO = null;

  private int mreznaVrataVozila;
  private String adresaVozila;


  /**
   * Inicijalizira VoziloDAO objekt za pristup bazi podataka. Provjerava vezu s bazom podataka i
   * kreira VoziloDAO objekt za daljnje korištenje.
   * 
   * @throws Exception ako ne uspije uspostaviti vezu s bazom podataka ili kreirati VoziloDAO objekt
   */
  @PostConstruct
  private void pripremiKorisnikDAO() {
    System.out.println("Pokrećem REST: " + this.getClass().getName());
    try {
      var vezaBP = this.vezaBazaPodataka.getVezaBazaPodataka();
      this.voziloDAO = new VoziloDAO(vezaBP);
      preuzmiPostavke("NWTiS_REST_S.txt");
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
  }


  /**
   * Dohvaća sva vozila ili vozila u određenom vremenskom intervalu.
   * 
   * @param tipOdgovora tip odgovora
   * @param odVremena početak vremenskog intervala
   * @param doVremena kraj vremenskog intervala
   * @return odgovor s vozilima
   */
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJson(@HeaderParam("Accept") String tipOdgovora,
      @QueryParam("od") long odVremena, @QueryParam("do") long doVremena) {
    if (odVremena > 0 || doVremena > 0) {
      return Response.status(Response.Status.OK)
          .entity(voziloDAO.dohvatiSvaVozilaUIntervalu(odVremena, doVremena).toArray()).build();
    } else {
      return Response.status(Response.Status.OK).entity(voziloDAO.dohvatiSvaVozila().toArray())
          .build();
    }
  }

  /**
   * Dodaje novo vozilo.
   *
   * @param tipOdgovora vrsta MIME odgovora
   * @param novoVozilo podaci novog vozila
   * @return OK ako je vozilo uspješno upisano ili INTERNAL_SERVER_ERROR ako nije
   */
  @POST
  @Produces({MediaType.APPLICATION_JSON})
  public Response posttJsonDodajVozilo(@HeaderParam("Accept") String tipOdgovora,
      Vozilo novoVozilo) {
    var komanda = dajKomanduZaSlanje(novoVozilo);
    var odgovorSlanje = MrezneOperacije.posaljiZahtjevPosluzitelju(this.adresaVozila,
        this.mreznaVrataVozila, komanda.toString());
    // upisuje podatke u bazu u tablicu voznje

    var odgovorUpis = voziloDAO.dodajVozilo(novoVozilo);

    if (odgovorUpis) {
      return Response.status(Response.Status.OK).build();
    } else {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity("Neuspješni upis vozila u bazu podataka.").build();
    }
  }

  /**
   * Sastavlja komandu za slanje podataka o vozilu.
   *
   * @return Komanda za slanje podataka o vozilu.
   */
  public String dajKomanduZaSlanje(Vozilo podaciVozila) {
    var komanda = new StringBuilder();
    komanda.append("VOZILO").append(" ").append(podaciVozila.getId()).append(" ")
        .append(podaciVozila.getBroj()).append(" ").append(podaciVozila.getVrijeme()).append(" ")
        .append(podaciVozila.getBrzina()).append(" ").append(podaciVozila.getSnaga()).append(" ")
        .append(podaciVozila.getStruja()).append(" ").append(podaciVozila.getVisina()).append(" ")
        .append(podaciVozila.getGpsBrzina()).append(" ").append(podaciVozila.getTempVozila())
        .append(" ").append(podaciVozila.getPostotakBaterija()).append(" ")
        .append(podaciVozila.getNaponBaterija()).append(" ")
        .append(podaciVozila.getKapacitetBaterija()).append(" ")
        .append(podaciVozila.getTempBaterija()).append(" ").append(podaciVozila.getPreostaloKm())
        .append(" ").append(podaciVozila.getUkupnoKm()).append(" ")
        .append(podaciVozila.getGpsSirina()).append(" ").append(podaciVozila.getGpsDuzina());

    return komanda.toString();
  }


  /**
   * Dohvaća vožnje za određeno vozilo.
   * 
   * @param tipOdgovora tip odgovora
   * @param id identifikator vozila
   * @param odVremena početak vremenskog intervala
   * @param doVremena kraj vremenskog intervala
   * @return odgovor s vožnjama za vozilo
   */
  @Path("/vozilo/{id}")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonVoznjeZaVozilo(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("id") int id, @QueryParam("od") long odVremena, @QueryParam("do") long doVremena) {

    if (odVremena > 0 && doVremena > 0) {
      return getJsonVoznjeZaVoziloInterval(tipOdgovora, id, odVremena, doVremena);
    } else {
      return getJsonVoznjeZaVozilo(tipOdgovora, id);
    }
  }

  /**
   * Dohvaća vožnje za određeno vozilo.
   * 
   * @param tipOdgovora tip odgovora
   * @param id identifikator vozila
   * @return odgovor s vožnjama za vozilo
   */
  private Response getJsonVoznjeZaVozilo(@HeaderParam("Accept") String tipOdgovora, int id) {
    return Response.status(Response.Status.OK).entity(voziloDAO.dohvatiVoznjeZaVozilo(id)).build();
  }

  /**
   * Dohvaća vožnje za vozilo u određenom vremenskom intervalu.
   * 
   * @param tipOdgovora tip odgovora
   * @param id identifikator vozila
   * @param odVremena početak vremenskog intervala
   * @param doVremena kraj vremenskog intervala
   * @return odgovor s vožnjama za vozilo u intervalu
   */
  private Response getJsonVoznjeZaVoziloInterval(@HeaderParam("Accept") String tipOdgovora, int id,
      long odVremena, long doVremena) {

    return Response.status(Response.Status.OK)
        .entity(voziloDAO.dohvatiVoznjeZaVoziloUIntervalu(id, odVremena, doVremena)).build();
  }

  private void preuzmiPostavke(String nazivDat)
      throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(nazivDat);
    this.mreznaVrataVozila = Integer.valueOf(konfig.dajPostavku("mreznaVrataVozila"));
    this.adresaVozila = konfig.dajPostavku("adresaVozila");
  }


}
