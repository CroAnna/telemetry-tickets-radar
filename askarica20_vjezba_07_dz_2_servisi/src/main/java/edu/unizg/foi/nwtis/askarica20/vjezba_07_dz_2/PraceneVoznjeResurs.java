package edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2;

import java.net.UnknownHostException;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.podaci.PracenoVoziloDAO;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.podaci.Vozilo;
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
 * REST Web Service uz korištenje klase Vozilo
 *
 * @author Ana Škarica
 */
@Path("nwtis/v1/api/vozila")
public class PraceneVoznjeResurs extends SviResursi {
  // zapisuje u tablicu pracenevoznje
  private PracenoVoziloDAO pracenoVoziloDAO = null;

  private int mreznaVrataVozila;
  private String adresaVozila;

  /**
   * Metoda koja se izvrsava nakon konstrukcije objekta resursa
   * 
   * Inicijalizira praceno vozilo DAO
   */
  @PostConstruct
  private void pripremiKorisnikDAO() {
    System.out.println("Pokrećem REST: " + this.getClass().getName());
    try {
      var vezaBP = this.vezaBazaPodataka.getVezaBazaPodataka();
      this.pracenoVoziloDAO = new PracenoVoziloDAO(vezaBP);
      preuzmiPostavke("NWTiS_REST_V.txt");
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
  }

  /**
   * Dohvaća sve pracene voznje ili voznje u zadatom intervalu
   * 
   * @param tipOdgovora vrsta MIME odgovora
   * @param odVremena od vremena
   * @param doVremena do vremena
   * @return lista pracenih voznji
   */
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJson(@HeaderParam("Accept") String tipOdgovora,
      @QueryParam("od") long odVremena, @QueryParam("do") long doVremena) {
    if (odVremena > 0 || doVremena > 0) {
      return Response.status(Response.Status.OK)
          .entity(
              pracenoVoziloDAO.dohvatiSvaPracenaVozilaUIntervalu(odVremena, doVremena).toArray())
          .build();
    } else {
      return Response.status(Response.Status.OK)
          .entity(pracenoVoziloDAO.dohvatiSvaPracenaVozila().toArray()).build();
    }
  }

  /**
   * Dodaje novo praceno vozilo.
   *
   * @param tipOdgovora vrsta MIME odgovora
   * @param novoVozilo podaci novog pracenog vozila
   * @return OK ako je praceno vozilo uspješno upisano ili INTERNAL_SERVER_ERROR ako nije
   */
  @POST
  @Produces({MediaType.APPLICATION_JSON})
  public Response posttJsonDodajPracenoVozilo(@HeaderParam("Accept") String tipOdgovora,
      Vozilo novoVozilo) {

    var odgovor = pracenoVoziloDAO.dodajPracenoVozilo(novoVozilo);
    if (odgovor) {
      return Response.status(Response.Status.OK).build();
    } else {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity("Neuspješni upis pracenog vozila u bazu podataka.").build();
    }
  }

  /**
   * Pokrece praceno vozilo s odredenim identifikacijskim brojem.
   * 
   * @param tipOdgovora vrsta MIME odgovora
   * @param id identifikacijski broj vozila
   * @return odgovor pokretanja pracenog vozila
   */
  @Path("/vozilo/{id}/start")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonVoznjeZaVoziloStart(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("id") int id) {

    var komanda = new StringBuilder();
    komanda.append("VOZILO").append(" ").append("START").append(" ").append(id);

    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(this.adresaVozila,
        this.mreznaVrataVozila, komanda.toString());
    return Response.status(Response.Status.OK).entity("{\"odgovor\":\"" + odgovor + "\"}").build();
  }

  /**
   * Zaustavlja praceno vozilo s odredenim identifikacijskim brojem.
   * 
   * @param tipOdgovora vrsta MIME odgovora
   * @param id identifikacijski broj vozila
   * @return odgovor zaustavljanja pracenog vozila
   */
  @Path("/vozilo/{id}/stop")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonStopVozilo(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("id") int id) {
    var komanda = new StringBuilder();
    komanda.append("VOZILO").append(" ").append("STOP").append(" ").append(id);

    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(this.adresaVozila,
        this.mreznaVrataVozila, komanda.toString());
    return Response.status(Response.Status.OK).entity("{\"odgovor\":\"" + odgovor + "\"}").build();
  }

  /**
   * Dohvaća vožnje za praćeno vozilo s određenim identifikacijskim brojem.
   * 
   * @param tipOdgovora vrsta MIME odgovora
   * @param id identifikacijski broj vozila
   * @param odVremena početno vrijeme intervala
   * @param doVremena završno vrijeme intervala
   * @return lista vožnji za praćeno vozilo
   */
  @Path("/vozilo/{id}")
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonVoznjeZaPracenoVozilo(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("id") int id, @QueryParam("od") long odVremena, @QueryParam("do") long doVremena) {

    if (odVremena > 0 && doVremena > 0) {
      return getJsonVoznjeZaPracenoVoziloInterval(tipOdgovora, id, odVremena, doVremena);
    } else {
      return getJsonVoznjeZaPracenoVozilo(tipOdgovora, id);
    }
  }

  /**
   * Dohvaća vožnje za praćeno vozilo s određenim identifikacijskim brojem u zadanom vremenskom
   * intervalu.
   * 
   * @param tipOdgovora vrsta MIME odgovora
   * @param id identifikacijski broj vozila
   * @param odVremena početno vrijeme intervala
   * @param doVremena završno vrijeme intervala
   * @return lista vožnji za praćeno vozilo u intervalu
   */
  private Response getJsonVoznjeZaPracenoVozilo(@HeaderParam("Accept") String tipOdgovora, int id) {
    return Response.status(Response.Status.OK)
        .entity(pracenoVoziloDAO.dohvatiVoznjeZaPracenoVozilo(id)).build();
  }

  /**
   * Dohvaća vožnje za praćeno vozilo s određenim identifikacijskim brojem u zadanom vremenskom
   * intervalu.
   * 
   * @param tipOdgovora vrsta MIME odgovora
   * @param id identifikacijski broj vozila
   * @param odVremena početno vrijeme intervala
   * @param doVremena završno vrijeme intervala
   * @return odgovor s listom vožnji za praćeno vozilo u intervalu
   */
  private Response getJsonVoznjeZaPracenoVoziloInterval(@HeaderParam("Accept") String tipOdgovora,
      int id, long odVremena, long doVremena) {
    return Response.status(Response.Status.OK)
        .entity(pracenoVoziloDAO.dohvatiVoznjeZaPracenoVoziloUIntervalu(id, odVremena, doVremena))
        .build();
  }

  private void preuzmiPostavke(String nazivDat)
      throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(nazivDat);
    this.mreznaVrataVozila = Integer.valueOf(konfig.dajPostavku("mreznaVrataVozila"));
    this.adresaVozila = konfig.dajPostavku("adresaVozila");
  }


}
