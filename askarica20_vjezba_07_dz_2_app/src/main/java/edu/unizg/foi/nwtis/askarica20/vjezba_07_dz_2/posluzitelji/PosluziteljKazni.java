package edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.posluzitelji;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.URI;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.podaci.PodaciKazne;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

/**
 * Klasa PosluziteljKazni.
 */
public class PosluziteljKazni {
  private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
  public int mreznaVrata;
  private Pattern predlozakKazna = Pattern.compile(
      "^VOZILO (?<id>\\d+) (?<vrijemePocetak>\\d+) (?<vrijemeKraj>\\d+) (?<brzina>-?\\d+([.]\\d+)?) (?<gpsSirina>\\d+[.]\\d+) (?<gpsDuzina>\\d+[.]\\d+) (?<gpsSirinaRadar>\\d+[.]\\d+) (?<gpsDuzinaRadar>\\d+[.]\\d+)$");
  private Pattern predlozakStatistika =
      Pattern.compile("^STATISTIKA (?<vrijemeOd>\\d+) (?<vrijemeDo>\\d+)$");
  private Pattern predlozakVozilo =
      Pattern.compile("^VOZILO (?<id>\\d+) (?<vrijemeOd>\\d+) (?<vrijemeDo>\\d+)$");
  private Pattern predlozakTestiranja = Pattern.compile("^TEST$");

  private Matcher poklapanjeKazna;
  private Matcher poklapanjeVozilo;
  private Matcher poklapanjeStatistika;
  private Matcher poklapanjeTestiranja;

  private volatile Queue<PodaciKazne> sveKazne = new ConcurrentLinkedQueue<>();
  private static volatile ConcurrentHashMap<Integer, Integer> kaznePoVozilu =
      new ConcurrentHashMap<Integer, Integer>();

  /**
   * Glavna metoda koja pokreće poslužitelja za kazne.
   *
   * @param args Argumenti naredbenog retka kojih smije biti samo 1.
   */
  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Broj argumenata nije 1.");
      return;
    }
    // System.out.println("Posl za kazne pokrenut!");


    PosluziteljKazni posluziteljKazni = new PosluziteljKazni();
    try {
      posluziteljKazni.preuzmiPostavke(args);
      posluziteljKazni.pokreniPosluzitelja();
    } catch (NeispravnaKonfiguracija | NumberFormatException | UnknownHostException e) {
      System.out.println(e.getMessage());
      return;
    }
  }

  /**
   * Metoda za pokretanje poslužitelja za primanje zahtjeva o kaznama.
   */
  public void pokreniPosluzitelja() {
    boolean kraj = false;

    try (ServerSocket mreznaUticnicaPosluzitelja = new ServerSocket(this.mreznaVrata)) {
      while (!kraj) {
        var mreznaUticnica = mreznaUticnicaPosluzitelja.accept();
        BufferedReader citac =
            new BufferedReader(new InputStreamReader(mreznaUticnica.getInputStream(), "utf8"));
        OutputStream out = mreznaUticnica.getOutputStream();
        PrintWriter pisac = new PrintWriter(new OutputStreamWriter(out, "utf8"), true);
        var redak = citac.readLine();

        mreznaUticnica.shutdownInput();
        pisac.println(obradaZahtjeva(redak));

        pisac.flush();
        mreznaUticnica.shutdownOutput();
        mreznaUticnica.close();
      }
    } catch (NumberFormatException | IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Metoda koja obrađuje zahtjev i vraća odgovor.
   *
   * @param zahtjev Zahtjev koji je primljen od klijenta.
   * @return Odgovor na zahtjev.
   */
  public String obradaZahtjeva(String zahtjev) {
    if (zahtjev == null) {
      return "ERROR 40 Neispravna sintaksa komande.-1";
    }
    var odgovor = obradaZahtjevaKazna(zahtjev);
    if (odgovor != null) {
      return odgovor;
    }
    odgovor = obradaZahtjevaStatistika(zahtjev);
    if (odgovor != null) {
      // ystem.out.println(odgovor); // za provjeru u terminalu
      return odgovor;
    }
    odgovor = obradaZahtjevaTest(zahtjev);
    if (odgovor != null) {
      // System.out.println("obradaZahtjevaTest " + odgovor);
      return odgovor;
    }
    odgovor = obradaZahtjevaVozilo(zahtjev);
    if (odgovor != null) {
      if (odgovor != "ERROR 41") {
        return odgovor;
      } else if (odgovor != "ERROR 41") {
        return "ERROR 41 Ne postoji kazna za ovo e-vozilo u zadanom vremenu.";
      }
    }
    return "ERROR 40 Neispravna sintaksa komande."; // ovo dode - znaci nikad ne dode odgovor koji
                                                    // nije null
  }

  /**
   * Metoda za obradu zahtjeva o statistici.
   *
   * @param zahtjev Zahtjev u obliku komande koji je primljen.
   * @return Odgovor na zahtjev o statistici.
   */
  public String obradaZahtjevaStatistika(String zahtjev) {
    this.poklapanjeStatistika = this.predlozakStatistika.matcher(zahtjev);
    var statusStatistika = poklapanjeStatistika.matches();

    if (statusStatistika) {
      var vrijemeOd = Long.valueOf(this.poklapanjeStatistika.group("vrijemeOd"));
      var vrijemeDo = Long.valueOf(this.poklapanjeStatistika.group("vrijemeDo"));
      kaznePoVozilu.clear();

      for (PodaciKazne kazna : sveKazne) {
        if (kazna.vrijemePocetak() >= vrijemeOd && kazna.vrijemeKraj() <= vrijemeDo) {
          int idVozila = kazna.id();
          if (kaznePoVozilu.containsKey(idVozila) == true) {
            int novaVrijednost = kaznePoVozilu.get(idVozila);
            novaVrijednost++;
            kaznePoVozilu.put(idVozila, novaVrijednost);
          } else {
            kaznePoVozilu.put(idVozila, 1);
          }
        }
      }

      StringBuilder rezultat = new StringBuilder("OK ");
      kaznePoVozilu.forEach((idVozila, brojKazni) -> {
        rezultat.append(idVozila).append(" ").append(brojKazni).append("; ");
      });

      return rezultat.toString();
    }
    return null;
  }

  /**
   * Metoda za obradu zahtjeva o vozilu.
   *
   * @param zahtjev Zahtjev u obliku komande koji je primljen od klijenta.
   * @return Odgovor na zahtjev o vozilu.
   */
  public String obradaZahtjevaVozilo(String zahtjev) {
    this.poklapanjeVozilo = this.predlozakVozilo.matcher(zahtjev);
    var statusVozilo = poklapanjeVozilo.matches();
    // System.out.println("statusVozilo " + statusVozilo);
    if (statusVozilo) {
      var najnovijiZapis = najnovijiZapisZaId(Integer.valueOf(this.poklapanjeVozilo.group("id")),
          Long.valueOf(this.poklapanjeVozilo.group("vrijemeOd")),
          Long.valueOf(this.poklapanjeVozilo.group("vrijemeDo")));
      if (najnovijiZapis == null) {
        return "ERROR 41";
      }
      return "OK " + najnovijiZapis.vrijemeKraj() + " " + najnovijiZapis.brzina() + " "
          + najnovijiZapis.gpsSirinaRadar() + " " + najnovijiZapis.gpsDuzinaRadar();
    }
    return null;
  }

  /**
   * Metoda za obradu zahtjeva o kazni koja provjerava komandu i ispisuje kazne ukoliko postoji.
   *
   * @param zahtjev Zahtjev u obliku komande koji je primljen od klijenta.
   * @return Odgovor na zahtjev o kazni.
   */
  public String obradaZahtjevaKazna(String zahtjev) {
    this.poklapanjeKazna = this.predlozakKazna.matcher(zahtjev);
    var statusKazna = poklapanjeKazna.matches();
    if (statusKazna) {
      var kazna = new PodaciKazne(Integer.valueOf(this.poklapanjeKazna.group("id")),
          Long.valueOf(this.poklapanjeKazna.group("vrijemePocetak")),
          Long.valueOf(this.poklapanjeKazna.group("vrijemeKraj")),
          Double.valueOf(this.poklapanjeKazna.group("brzina")),
          Double.valueOf(this.poklapanjeKazna.group("gpsSirina")),
          Double.valueOf(this.poklapanjeKazna.group("gpsDuzina")),
          Double.valueOf(this.poklapanjeKazna.group("gpsSirinaRadar")),
          Double.valueOf(this.poklapanjeKazna.group("gpsDuzinaRadar")));

      this.sveKazne.add(kazna);
      System.out.println("Id: " + kazna.id() + " Vrijeme od: " + sdf.format(kazna.vrijemePocetak())
          + "  Vrijeme do: " + sdf.format(kazna.vrijemeKraj()) + " Brzina: " + kazna.brzina()
          + " GPS: " + kazna.gpsSirina() + ", " + kazna.gpsDuzina());

      var odg = posaljiPodatkeOKazni(kazna);

      if (odg.equals("OK")) {
        return "OK";
      } else if (odg.equals("ERROR 42")) {
        return "ERROR 42 Neuspjesan POST za dodavanje kazne";
      }
    }
    return null;
  }

  /**
   * Metoda za slanje podataka o kazni putem HTTP POST zahtjeva.
   *
   * @param kazna Objekt PodaciKazne koji sadrži informacije o kazni.
   */
  private String posaljiPodatkeOKazni(PodaciKazne kazna) {
    String jsonBody = kreirajJsonPodatkeOKazni(kazna);
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request =
        HttpRequest.newBuilder().uri(URI.create("http://20.24.5.5:8080/nwtis/v1/api/kazne"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody)).build();
    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      int statusCode = response.statusCode();
      if (statusCode == 200) {
        // System.out.println("uspjesno dodana kazna u bazu");
        return "OK";
      } else {
        // System.out.println("ERROR: " + statusCode + " " + response.body());
        return ("ERROR 42");
      }
    } catch (Exception e) {
      e.printStackTrace();
      return ("ERROR 42");
    }
  }

  /**
   * Metoda za kreiranje JSON formata podataka o kazni.
   *
   * @param kazna Objekt PodaciKazne koji sadrži informacije o kazni.
   * @return JSON format podataka o kazni.
   */
  private String kreirajJsonPodatkeOKazni(PodaciKazne kazna) {
    return "{\n" + "  \"brzina\": " + kazna.brzina() + ",\n" + "  \"gpsDuzina\": "
        + kazna.gpsDuzina() + ",\n" + "  \"gpsDuzinaRadar\": " + kazna.gpsDuzinaRadar() + ",\n"
        + "  \"gpsSirina\": " + kazna.gpsSirina() + ",\n" + "  \"gpsSirinaRadar\": "
        + kazna.gpsSirinaRadar() + ",\n" + "  \"id\": " + kazna.id() + ",\n" + "  \"vrijemeKraj\": "
        + kazna.vrijemeKraj() + ",\n" + "  \"vrijemePocetak\": " + kazna.vrijemePocetak() + "\n"
        + "}";
  }

  /**
   * Metoda za obradu zahtjeva za testiranje.
   *
   * @param zahtjev Zahtjev u obliku komande koji je primljen od klijenta.
   * @return Odgovor na zahtjev za testiranje.
   */
  public String obradaZahtjevaTest(String zahtjev) {
    this.poklapanjeTestiranja = this.predlozakTestiranja.matcher(zahtjev);
    var status = poklapanjeTestiranja.matches();
    if (status) {
      // TODO ne znam tocno kaj al eto
      return "OK";
    }
    return null;
  }

  /**
   * Pronalazi najnoviji zapis kazne za vozilo u zadanom vremenskom intervalu.
   *
   * @param id ID vozila.
   * @param vrijemePoc Početak vremenskog intervala.
   * @param vrijemeKraj Kraj vremenskog intervala.
   * @return Najnoviji zapis za vozilo u zadanom vremenskom intervalu.
   */
  public PodaciKazne najnovijiZapisZaId(int id, long vrijemePoc, long vrijemeKraj) {
    PodaciKazne najnoviji = null;
    for (PodaciKazne kazna : sveKazne) {
      if (kazna.id() == id && kazna.vrijemeKraj() >= vrijemePoc
          && kazna.vrijemeKraj() <= vrijemeKraj) {
        najnoviji = kazna;
      }
    }
    return najnoviji;
  }

  /**
   * Preuzima postavke iz konfiguracijske datoteke.
   *
   * @param args Argumenti naredbenog retka.
   * @throws NeispravnaKonfiguracija Ako konfiguracija nije ispravna.
   * @throws NumberFormatException Ako se broj ne može parsirati.
   * @throws UnknownHostException Ako se ne može dobiti lokalni naziv računala.
   */
  public void preuzmiPostavke(String[] args)
      throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);

    this.mreznaVrata = Integer.valueOf(konfig.dajPostavku("mreznaVrataKazne"));
  }
}
