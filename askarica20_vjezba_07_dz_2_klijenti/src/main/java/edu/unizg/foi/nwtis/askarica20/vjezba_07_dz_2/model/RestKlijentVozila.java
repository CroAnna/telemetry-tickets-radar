package edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.podaci.Vozilo;
import jakarta.json.bind.JsonbBuilder;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Klijent za REST API praćenih vožnji.
 */
public class RestKlijentVozila {
  // vezan uz tablicu pracenevoznje i PraceneVoznjeResurs
  /**
   * Konstruktor bez parametara.
   */
  public RestKlijentVozila() {}

  /**
   * Dohvaća listu praćenih vožnji u JSON formatu.
   *
   * @return Lista objekata tipa Vozilo.
   */
  public List<Vozilo> getPraceneVoznjeJSON() {
    RestVozila rk = new RestVozila();
    List<Vozilo> vozilo = rk.getJSON();
    return vozilo;
  }

  /**
   * Vraća vozila u intervalu od do.
   *
   * @param odVremena početak intervala
   * @param doVremena kraj intervala
   * @return vozila
   */
  public List<Vozilo> getPraceneVoznjeJSON_od_do(long odVremena, long doVremena) {
    RestVozila rk = new RestVozila();
    List<Vozilo> vozila = rk.getJSON_od_do(odVremena, doVremena);

    return vozila;
  }


  /**
   * Vraća pracene voznje za vozilo.
   *
   * @param id id vozila
   * @return vozila
   */
  public List<Vozilo> getPraceneVoznjePoIdu(String id) {
    RestVozila rk = new RestVozila();
    List<Vozilo> vozila = rk.getPraceneVoznjeId(id);
    return vozila;
  }

  /**
   * Vraća pracene voznje za vozilo u intervalu od do..
   *
   * @param id id vozila
   * @param odVremena početak intervala
   * @param doVremena kraj intervala
   * @return vozila
   */
  public List<Vozilo> getPraceneVoznjePoIduInterval(String id, long odVremena, long doVremena) {
    RestVozila rk = new RestVozila();
    List<Vozilo> vozila = rk.getPraceneVoznjeIdInterval(id, odVremena, doVremena);

    return vozila;
  }

  /**
   * Pokreće vozilo prema ID-u.
   *
   * @param id ID vozila koje se pokreće.
   * @return Poruka o uspješnom pokretanju vozila.
   */
  public String getStartVozila(int id) {
    RestVozila rk = new RestVozila();
    return rk.startajVozilo(id);
  }

  /**
   * Zaustavlja vozilo prema ID-u.
   *
   * @param id ID vozila koje se zaustavlja.
   * @return Poruka o uspješnom zaustavljanju vozila.
   */
  public String getStopVozila(int id) {
    RestVozila rk = new RestVozila();
    return rk.stopajVozilo(id);
  }

  public String postDodajVoznjuUBazu(Vozilo vozilo) {
    RestVozila rk = new RestVozila();
    String odg = rk.dodajVoznjuUBazu(vozilo);
    return odg;
  }


  /**
   * Unutarnja klasa za komunikaciju s REST API-jem vozila.
   */
  static class RestVozila {
    /** web target. */
    private final WebTarget webTarget;

    /** klijent. */
    private final Client client;

    /** konstanta BASE_URI. */
    private static final String BASE_URI = "http://localhost:9080/";

    /**
     * Konstruktor koji inicijalizira klijent i web target.
     */
    public RestVozila() {
      client = ClientBuilder.newClient();
      webTarget = client.target(BASE_URI).path("nwtis/v1/api/vozila");
    }

    /**
     * Dohvaća sva praćena vozila u JSON formatu.
     *
     * @return Lista vozila.
     * @throws ClientErrorException u slučaju greške kod poziva klijenta.
     */
    public List<Vozilo> getJSON() throws ClientErrorException {
      WebTarget resource = webTarget;
      List<Vozilo> vozila = new ArrayList<Vozilo>();

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var pvozila = jb.fromJson(odgovor, Vozilo[].class);
        vozila.addAll(Arrays.asList(pvozila));
      }

      return vozila;
    }

    /**
     * Vraća vozila u intervalu od do.
     *
     * @param odVremena početak intervala
     * @param doVremena kraj intervala
     * @return vozila
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public List<Vozilo> getJSON_od_do(long odVremena, long doVremena) throws ClientErrorException {
      WebTarget resource = webTarget;
      List<Vozilo> vozila = new ArrayList<Vozilo>();
      resource = resource.queryParam("od", odVremena);
      resource = resource.queryParam("do", doVremena);
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var pkazne = jb.fromJson(odgovor, Vozilo[].class);
        vozila.addAll(Arrays.asList(pkazne));
      }

      return vozila;
    }


    /**
     * Vraća pracene voznje za vozilo.
     *
     * @param id id vozila
     * @return vozila
     * @throws ClientErrorException iznimka kod poziva klijentaon
     */
    public List<Vozilo> getPraceneVoznjeId(String id) throws ClientErrorException {
      WebTarget resource = webTarget;
      List<Vozilo> vozila = new ArrayList<Vozilo>();

      resource = resource.path(java.text.MessageFormat.format("vozilo/{0}", new Object[] {id}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var pkazne = jb.fromJson(odgovor, Vozilo[].class);
        vozila.addAll(Arrays.asList(pkazne));
      }

      return vozila;
    }

    /**
     * Vraća pracene voznje za vozilo u intervalu od do..
     *
     * @param id id vozila
     * @param odVremena početak intervala
     * @param doVremena kraj intervala
     * @return vozila
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public List<Vozilo> getPraceneVoznjeIdInterval(String id, long odVremena, long doVremena)
        throws ClientErrorException {
      WebTarget resource = webTarget;
      List<Vozilo> vozila = new ArrayList<Vozilo>();

      resource = resource.path(java.text.MessageFormat.format("vozilo/{0}", new Object[] {id}));
      resource = resource.queryParam("od", odVremena);
      resource = resource.queryParam("do", doVremena);
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var pkazne = jb.fromJson(odgovor, Vozilo[].class);
        vozila.addAll(Arrays.asList(pkazne));
      }

      return vozila;
    }

    /**
     * Pokreće vozilo prema ID-u.
     *
     * @param id ID vozila koje se pokreće.
     * @return Poruka o uspješnom pokretanju vozila.
     * @throws ClientErrorException iznimka u slučaju neuspješnog pokretanja vozila
     */
    public String startajVozilo(int id) throws ClientErrorException {
      WebTarget resource = webTarget;
      resource =
          resource.path(java.text.MessageFormat.format("vozilo/{0}/start", new Object[] {id}));

      Response response = resource.request().get();
      if (response.getStatus() != 200) {
        throw new ClientErrorException("Neuspjesno startano vozilo", response.getStatus());
      }
      return response.readEntity(String.class);
    }

    /**
     * Zaustavlja vozilo prema ID-u.
     *
     * @param id ID vozila koje se zaustavlja.
     * @return Poruka o uspješnom zaustavljanju vozila.
     * @throws ClientErrorException iznimka u slučaju neuspješnog zaustavljanja vozila
     */
    public String stopajVozilo(int id) throws ClientErrorException {
      WebTarget resource = webTarget;
      resource =
          resource.path(java.text.MessageFormat.format("vozilo/{0}/stop", new Object[] {id}));

      Response response = resource.request().get();
      if (response.getStatus() != 200) {
        throw new ClientErrorException("Neuspjesno stopano vozilo", response.getStatus());
      }
      return response.readEntity(String.class);
    }

    public String dodajVoznjuUBazu(Vozilo vozilo) {
      WebTarget resource = webTarget;

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response response = request.post(Entity.entity(vozilo, MediaType.APPLICATION_JSON));

      if (response.getStatus() == 200) {
        return "Uspješno dodano";
      } else {
        return "Greška prilikom dodavanja";
      }
    }

  }
}
