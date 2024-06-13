package edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.podaci.Radar;
import jakarta.json.bind.JsonbBuilder;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Klijent za REST API radara.
 */
public class RestKlijentRadari {
  /**
   * Konstruktor bez parametara.
   */
  public RestKlijentRadari() {}

  /**
   * Dohvaća listu radara u JSON formatu.
   *
   * @return Lista radara.
   */
  public List<Radar> getRadariJSON() {
    RestRadari rk = new RestRadari();
    List<Radar> radari = rk.getJSON();
    return radari;
  }

  /**
   * Dohvaća radar prema ID-u u JSON formatu.
   *
   * @param id ID radara.
   * @return Radar objekt.
   */
  public Radar getRadariJSONId(String id) {
    RestRadari rk = new RestRadari();
    Radar radar = rk.getJSON_id(id);
    return radar;
  }

  /**
   * Briše radar prema ID-u.
   *
   * @param id ID radara koji se briše.
   */
  public void deleteRadarById(int id) {
    RestRadari rk = new RestRadari();
    rk.deleteRadar(String.valueOf(id));
  }

  /**
   * Briše sve radare.
   */
  public void deleteRadari() {
    RestRadari rk = new RestRadari();
    rk.deleteRadarSve();
  }

  /**
   * Resetira sve radare.
   */
  public void resetRadari() {
    RestRadari rk = new RestRadari();
    rk.resetRadariSve();
  }

  /**
   * Provjerava status radara prema ID-u.
   *
   * @param id ID radara čiji se status provjerava.
   * @return Status radara.
   */
  public String getProvjeraRadara(int id) {
    RestRadari rk = new RestRadari();
    return rk.provjeriRadar(id);
  }


  /**
   * Unutarnja klasa za komunikaciju s REST API-jem radara.
   */
  static class RestRadari {
    /** web target. */
    private final WebTarget webTarget;

    /** klijent. */
    private final Client client;

    /** konstanta BASE_URI. */
    private static final String BASE_URI = "http://localhost:9080/";

    /**
     * Konstruktor koji inicijalizira klijent i web target.
     */
    public RestRadari() {
      client = ClientBuilder.newClient();
      webTarget = client.target(BASE_URI).path("nwtis/v1/api/radari");
    }

    /**
     * Vraća radare.
     *
     * @return radari
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public List<Radar> getJSON() throws ClientErrorException {
      WebTarget resource = webTarget;
      List<Radar> radari = new ArrayList<Radar>();

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var pradari = jb.fromJson(odgovor, Radar[].class);
        radari.addAll(Arrays.asList(pradari));
      }
      return radari;
    }

    /**
     * Vraća radar.
     *
     * @param rb redni broj kazne
     * @return kazna
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public Radar getJSON_id(String id) throws ClientErrorException {
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("{0}", new Object[] {id}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var radar = jb.fromJson(odgovor, Radar.class);
        return radar;
      }
      return null;
    }

    /**
     * Briše radar prema ID-u.
     *
     * @param id ID radara koji se briše.
     * @throws ClientErrorException iznimka u slučaju neuspješnog brisanja radara
     */
    public void deleteRadar(String id) throws ClientErrorException {
      WebTarget resource = webTarget.path(id);
      Response response = resource.request().delete();
      if (response.getStatus() != 200) {
        throw new ClientErrorException("Neuspjesno obrisan radar", response.getStatus());
      }
    }

    /**
     * Briše sve radare.
     *
     * @throws ClientErrorException iznimka u slučaju neuspješnog brisanja radara
     */
    public void deleteRadarSve() throws ClientErrorException {
      WebTarget resource = webTarget;
      Response response = resource.request().delete();
      if (response.getStatus() != 200) {
        throw new ClientErrorException("Neuspjesno obrisani radari", response.getStatus());
      }
    }

    /**
     * Resetira sve radare.
     *
     * @throws ClientErrorException iznimka u slučaju neuspješnog resetiranja radara
     */
    public void resetRadariSve() throws ClientErrorException {
      WebTarget resource = webTarget.path("reset");
      Response response = resource.request().get();
      if (response.getStatus() != 200) {
        throw new ClientErrorException("Neuspjesno resetirani radari", response.getStatus());
      }
    }

    /**
     * Provjerava status radara prema ID-u.
     *
     * @param id ID radara čiji se status provjerava.
     * @return Status radara.
     * @throws ClientErrorException iznimka u slučaju neuspješne provjere statusa radara
     */
    public String provjeriRadar(int id) throws ClientErrorException {
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("{0}/provjeri", new Object[] {id}));

      Response response = resource.request().get();
      if (response.getStatus() != 200) {
        throw new ClientErrorException("Neuspjesno provjeren radar", response.getStatus());
      }
      return response.readEntity(String.class);
    }

  }
}
