package edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.podaci.Vozilo;
import jakarta.inject.Inject;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Klijent za REST API simulacije vožnji.
 */
public class RestKlijentSimulator {
  // vezan uz tablicu voznje i VozilaResurs
  /**
   * Konstruktor klase.
   */

  @Inject
  private ServletContext context;

  public RestKlijentSimulator() {}

  /**
   * Dohvaća listu vozila u JSON formatu.
   *
   * @return Lista vozila.
   */
  public List<Vozilo> getVoznjeJSON() {
    RestSimulator rs = new RestSimulator();
    List<Vozilo> vozila = rs.getJSON();
    return vozila;
  }

  /**
   * Vraća vozila u intervalu od do.
   *
   * @param odVremena početak intervala
   * @param doVremena kraj intervala
   * @return vozila
   */
  public List<Vozilo> getVoznjeJSON_od_do(long odVremena, long doVremena) {
    RestSimulator rk = new RestSimulator();
    List<Vozilo> vozila = rk.getJSON_od_do(odVremena, doVremena);

    return vozila;
  }

  /**
   * Vraća voznje za vozilo.
   *
   * @param id id vozila
   * @return vozila
   */
  public List<Vozilo> getVoznjePoIdu(String id) {
    RestSimulator rk = new RestSimulator();
    List<Vozilo> vozila = rk.getPraceneVoznjeId(id);
    return vozila;
  }

  /**
   * Vraća voznje za vozilo u intervalu od do..
   *
   * @param id id vozila
   * @param odVremena početak intervala
   * @param doVremena kraj intervala
   * @return vozila
   */
  public List<Vozilo> getVoznjePoIduInterval(String id, long odVremena, long doVremena) {
    RestSimulator rk = new RestSimulator();
    List<Vozilo> vozila = rk.getPraceneVoznjeIdInterval(id, odVremena, doVremena);
    return vozila;
  }

  /**
   * Dodaje simulaciju vozila u bazu.
   * 
   * @param vozilo Vozilo za koje se dodaje simulacija.
   * @return Odgovor servera na zahtjev za dodavanjem simulacije u bazu.
   */
  public String postDodajSimulacijuUBazu(Vozilo vozilo) {
    RestSimulator rk = new RestSimulator();
    String odg = rk.dodajSimulacijuUBazu(vozilo);
    return odg;
  }

  /**
   * Pokreće simulaciju na temelju zadane datoteke.
   * 
   * @param datotekaZaKoristenje Naziv datoteke koja sadrži podatke za simulaciju.
   * @param idVozila Identifikator vozila za koje se pokreće simulacija.
   * @param trajanjeSek Trajanje simulacije u sekundama.
   * @param trajanjePauze Trajanje pauze između iteracija simulacije.
   * @return Rezultat pokretanja simulacije ili "slanje..." ako dođe do greške.
   */
  public String getPokreniSimulaciju(String datotekaZaKoristenje, int idVozila, int trajanjeSek,
      int trajanjePauze) {
    RestSimulator rk = new RestSimulator();

    try (BufferedReader citac = new BufferedReader(new FileReader(datotekaZaKoristenje))) {
      citac.readLine();

      String redak;
      String prethodniRedak = null;
      int brojRetka = 0;

      while ((redak = citac.readLine()) != null) {
        return redak.toString();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "slanje...";
  }


  /**
   * Priprema podatak o vozilu i sprema ga za slanje.
   *
   * @param redak Podatak o vozilu u CSV formatu.
   * @param brojRetka Redni broj retka u CSV datoteci.
   * @param prethodniRedak Podatak o prethodnom vozilu u CSV formatu.
   * @return Komanda za slanje podataka o vozilu.
   */
  public Vozilo kreirajVoziloIzRetka(String csvRedak, Integer brojRetka, String prethodniRedak,
      int trajanjeSek, int idVozila) {
    String[] dijeloviRetka = csvRedak.split(",");
    String[] dijeloviPrethodnogRetka;
    Vozilo podaciVozila;

    if (prethodniRedak != null) {
      dijeloviPrethodnogRetka = prethodniRedak.split(",");
    } else {
      dijeloviPrethodnogRetka = dijeloviRetka;
    }

    podaciVozila = new Vozilo(idVozila, brojRetka, Long.valueOf(dijeloviRetka[0]),
        Double.valueOf(dijeloviRetka[1]), Double.valueOf(dijeloviRetka[2]),
        Double.valueOf(dijeloviRetka[3]), Double.valueOf(dijeloviRetka[4]),
        Double.valueOf(dijeloviRetka[5]), Integer.valueOf(dijeloviRetka[6]),
        Integer.valueOf(dijeloviRetka[7]), Double.valueOf(dijeloviRetka[8]),
        Integer.valueOf(dijeloviRetka[9]), Integer.valueOf(dijeloviRetka[10]),
        Double.valueOf(dijeloviRetka[11]), Double.valueOf(dijeloviRetka[12]),
        Double.valueOf(dijeloviRetka[13]), Double.valueOf(dijeloviRetka[14]));

    var razlika = dajRazlikuVremenaRetka(Long.valueOf(dijeloviRetka[0]),
        Long.valueOf(dijeloviPrethodnogRetka[0])) * dajKorekcijuVremena(trajanjeSek);

    try {
      Thread.sleep((long) razlika);
    } catch (Exception e) {
      e.getMessage();
    }

    return podaciVozila;
  }

  /**
   * Računa razliku u vremenu između trenutnog i prethodnog vremena u datoteci.
   *
   * @param trenutnoVrijeme Trenutno vrijeme.
   * @param prethodnoVrijeme Prethodno vrijeme.
   * @return Razlika u vremenu između trenutnog i prethodnog vremena.
   */
  private long dajRazlikuVremenaRetka(long trenutnoVrijeme, long prethodnoVrijeme) {
    long vremenskaRazlika = trenutnoVrijeme - prethodnoVrijeme;
    return vremenskaRazlika;
  }

  /**
   * Računa korekciju vremena na temelju postavljenog trajanja i trajanja pauze.
   *
   * @return Korekcija vremena.
   */
  private double dajKorekcijuVremena(int trajanjeSek) {
    return trajanjeSek / 1000.0;
  }



  /**
   * Unutarnja klasa za komunikaciju s REST API-jem simulacije vožnji.
   */
  static class RestSimulator {
    /** web target. */
    private final WebTarget webTarget;

    /** klijent. */
    private final Client client;

    /** konstanta BASE_URI. */
    private static final String BASE_URI = "http://localhost:9080/";

    /**
     * Konstruktor klase.
     */
    public RestSimulator() {
      client = ClientBuilder.newClient();
      webTarget = client.target(BASE_URI).path("nwtis/v1/api/simulacije");
    }

    /**
     * Vraća vozila.
     *
     * @return vozila
     * @throws ClientErrorException iznimka kod poziva klijenta
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
     * Vraća voznje za vozilo.
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
     * Vraća voznje za vozilo u intervalu od do..
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

    public String dodajSimulacijuUBazu(Vozilo vozilo) {
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
