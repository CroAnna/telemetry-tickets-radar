package edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.posluzitelji.radnici;

import java.net.SocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.podaci.PodaciVozila;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.podaci.RedPodaciVozila;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.pomocnici.GpsUdaljenostBrzina;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.posluzitelji.CentralniSustav;

/**
 * Klasa RadnikZaVozila.
 */
public class RadnikZaVozila implements Runnable {
  private AsynchronousSocketChannel klijentovKanal;
  private PodaciVozila podaciVozila;
  private CentralniSustav centralniSustav;

  private Pattern predlozakPokretanjaVozila = Pattern.compile("^VOZILO START (?<id>\\d+)$");
  private Pattern predlozakZaustavljanjaVozila = Pattern.compile("^VOZILO STOP (?<id>\\d+)$");
  private Pattern predlozakZaVozila = Pattern.compile(
      "^VOZILO (?<id>\\d+) (?<broj>\\d+) (?<vrijeme>\\d+) (?<brzina>-?\\d+([.]\\d+)?) (?<snaga>-?\\d+([.]\\d+)?) (?<struja>-?\\d+([.]\\d+)?) (?<visina>-?\\d+([.]\\d+)?) (?<gpsBrzina>-?\\d+([.]\\d+)?) (?<tempVozila>\\d+) (?<postotakBaterija>\\d+) (?<naponBaterija>-?\\d+([.]\\d+)?) (?<kapacitetBaterija>\\d+) (?<tempBaterija>\\d+) (?<preostaloKm>\\d+[.]\\d+) (?<ukupnoKm>\\d+[.]\\d+) (?<gpsSirina>\\d+[.]\\d+) (?<gpsDuzina>\\d+[.]\\d+)$");

  private Matcher poklapanjeVozila;
  private Matcher poklapanjePokretanjeVozila;
  private Matcher poklapanjeZaustavljanjeVozila;

  /**
   * Konstruktor za klasu RadnikZaVozila.
   * 
   * @param klijentovKanal Klijentov kanal za komunikaciju.
   * @param podaciVozila Podaci o vozilu.
   * @param centralniSustav Referenca na centralni sustav.
   */
  public RadnikZaVozila(AsynchronousSocketChannel klijentovKanal, PodaciVozila podaciVozila,
      CentralniSustav centralniSustav) {
    super();
    this.klijentovKanal = klijentovKanal;
    this.podaciVozila = podaciVozila;
    this.centralniSustav = centralniSustav;
  }

  /**
   * Pokreće radnika za vozila.
   */
  @Override
  public void run() {
    SocketAddress adresaUticniceKlijenta;
    try {
      adresaUticniceKlijenta = klijentovKanal.getRemoteAddress();
      try {
        while (true) {
          if ((klijentovKanal != null) && (klijentovKanal.isOpen())) {
            ByteBuffer bafer = ByteBuffer.allocate(2048);
            Future<Integer> citajBafer = klijentovKanal.read(bafer);
            citajBafer.get();
            String primljenaKomanda = new String(bafer.array()).trim();
            bafer.flip();

            if (primljenaKomanda.isEmpty()) {
              // System.out.println("Prazna komanda, prekid.");
              break;
            }

            String odgovor = obradaZahtjeva(primljenaKomanda);

            ByteBuffer odgovorBafer = ByteBuffer.wrap(odgovor.getBytes());
            Future<Integer> pisiBafer = klijentovKanal.write(odgovorBafer);
            pisiBafer.get();
            bafer.clear();
          } else {
            break;
          }
        }
      } finally {
        this.klijentovKanal.close();
      }
    } catch (Exception e) {
      // e.printStackTrace();
    }
  }

  /**
   * Obrada zahtjeva od klijenta.
   * 
   * @param zahtjev Zahtjev koji je primljen od klijenta.
   * @return Odgovor na zahtjev.
   */
  public String obradaZahtjeva(String zahtjev) {
    if (zahtjev == null) {
      return "ERROR 20 Neispravna sintaksa komande. " + zahtjev;
    }
    var odgovor = obradaZahtjevaPokretanja(zahtjev);
    if (odgovor != null) {
      return odgovor;
    }
    odgovor = obradaZahtjevaZaustavljanja(zahtjev);
    if (odgovor != null) {
      return odgovor;
    }
    odgovor = obradaZahtjevaVozila(zahtjev);
    if (odgovor == "ERROR 29") {
      return "ERROR 29 Nešto je pošlo po krivu.";
    } else if (odgovor != null) {
      return odgovor;
    }

    return "ERROR 20 Neispravna sintaksa komande.";
  }

  /**
   * Metoda za obradu zahtjeva pokretanja
   * 
   * @param zahtjev Zahtjev koji sadrži podatke
   * @return Odgovor na zahtjev, ili null ako se podaci ne podudaraju s obrascem.
   */
  public String obradaZahtjevaPokretanja(String zahtjev) {
    this.poklapanjePokretanjeVozila = this.predlozakPokretanjaVozila.matcher(zahtjev);
    var status = poklapanjePokretanjeVozila.matches();
    if (status) {
      var idVozila = Integer.valueOf(this.poklapanjePokretanjeVozila.group("id"));
      var postojiVozilo = provjeriPostojiLiVozilo(idVozila);
      if (postojiVozilo) {
        return "OK";
      } else {
        RedPodaciVozila redVozila = new RedPodaciVozila(idVozila);
        this.centralniSustav.svaVozila.put(idVozila, redVozila);
        return "OK";
      }
    }
    return null;
  }

  /**
   * Metoda za obradu zahtjeva zaustavljanja
   * 
   * @param zahtjev Zahtjev koji sadrži podatke
   * @return Odgovor na zahtjev, ili null ako se podaci ne podudaraju s obrascem.
   */
  public String obradaZahtjevaZaustavljanja(String zahtjev) {
    this.poklapanjeZaustavljanjeVozila = this.predlozakZaustavljanjaVozila.matcher(zahtjev);
    var status = poklapanjeZaustavljanjeVozila.matches();
    if (status) {
      var idVozila = Integer.valueOf(this.poklapanjeZaustavljanjeVozila.group("id"));
      var postojiVozilo = provjeriPostojiLiVozilo(idVozila);
      if (postojiVozilo) {
        this.centralniSustav.svaVozila.remove(idVozila);
        return "OK";
      }
      return "OK";
    }
    return null;
  }

  /**
   * Metoda za provjeru postoji li vozilo u kolekciji
   * 
   * @param zahtjev Id vozila koji se provjerava
   * @return Vrijednost postoji li vozilo u kolekciji
   */
  private boolean provjeriPostojiLiVozilo(int id) {
    if (this.centralniSustav.svaVozila.containsKey(id)) {
      return true;
    }
    return false;
  }


  /**
   * Obrada zahtjeva za podacima o vozilu.
   * 
   * @param zahtjev Zahtjev koji je primljen od klijenta.
   * @return Odgovor na zahtjev ili null ako zahtjev nije ispravan.
   */
  public String obradaZahtjevaVozila(String zahtjev) {
    this.poklapanjeVozila = this.predlozakZaVozila.matcher(zahtjev);
    var statusPoklapanjaVozila = poklapanjeVozila.matches();
    if (statusPoklapanjaVozila) {
      if (provjeriPodatkeOVozilu()) {
        if (provjeriPostojiLiVozilo()) {
          posaljiPodatkeOVozilu(zahtjev);
        } else {
          provjeriDometeRadara(zahtjev);
        }
      } else {
        return "ERROR 29";
      }
    }
    return null;
  }

  private boolean provjeriPodatkeOVozilu() {
    var postavljeni = postaviPodatkeVozila(poklapanjeVozila);
    return postavljeni.equals("OK");
  }

  private boolean provjeriPostojiLiVozilo() {
    int id = Integer.parseInt(poklapanjeVozila.group("id"));
    return provjeriPostojiLiVozilo(id);
  }

  private void posaljiPodatkeOVozilu(String zahtjev) {
    String jsonBody = kreirajJsonPodatkeOVozilu();
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request =
        HttpRequest.newBuilder().uri(URI.create("http://20.24.5.5:8080/nwtis/v1/api/vozila"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody)).build();

    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      int statusCode = response.statusCode();
      if (statusCode == 200) {
        provjeriDometeRadara(zahtjev);
      } else {
        throw new RuntimeException("ERROR 21 Neuspjesan POST za dodavanje pracenih vozila");
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("ERROR: " + e.getMessage());
    }
  }


  /**
   * Metoda za kreiranje JSON formata podataka o vozilu.
   *
   * @return JSON format podataka o vozilu.
   */
  private String kreirajJsonPodatkeOVozilu() {
    return "{\n" + "  \"id\": " + podaciVozila.id() + ",\n" + "  \"broj\": " + podaciVozila.broj()
        + ",\n" + "  \"vrijeme\": " + podaciVozila.vrijeme() + ",\n" + "  \"brzina\": "
        + podaciVozila.brzina() + ",\n" + "  \"snaga\": " + podaciVozila.snaga() + ",\n"
        + "  \"struja\": " + podaciVozila.struja() + ",\n" + "  \"visina\": "
        + podaciVozila.visina() + ",\n" + "  \"gpsBrzina\": " + podaciVozila.gpsBrzina() + ",\n"
        + "  \"tempVozila\": " + podaciVozila.tempVozila() + ",\n" + "  \"postotakBaterija\": "
        + podaciVozila.postotakBaterija() + ",\n" + "  \"naponBaterija\": "
        + podaciVozila.naponBaterija() + ",\n" + "  \"kapacitetBaterija\": "
        + podaciVozila.kapacitetBaterija() + ",\n" + "  \"tempBaterija\": "
        + podaciVozila.tempBaterija() + ",\n" + "  \"preostaloKm\": " + podaciVozila.preostaloKm()
        + ",\n" + "  \"ukupnoKm\": " + podaciVozila.ukupnoKm() + ",\n" + "  \"gpsSirina\": "
        + podaciVozila.gpsSirina() + ",\n" + "  \"gpsDuzina\": " + podaciVozila.gpsDuzina() + "\n"
        + "}";
  }


  /**
   * Postavljanje podataka o vozilu.
   * 
   * @param poklapanjeVozila Matcher koji sadrži podatke o vozilu iz zahtjeva.
   * @return Status postavljanja podataka (OK ili ERROR 29).
   */
  public String postaviPodatkeVozila(Matcher poklapanjeVozila) {
    try {
      int id = Integer.parseInt(poklapanjeVozila.group("id"));
      int tempVozila = Integer.parseInt(poklapanjeVozila.group("tempVozila"));
      int postotakBaterija = Integer.parseInt(poklapanjeVozila.group("postotakBaterija"));
      double naponBaterija = Double.parseDouble(poklapanjeVozila.group("naponBaterija"));
      int kapacitetBaterija = Integer.parseInt(poklapanjeVozila.group("kapacitetBaterija"));
      long vrijeme = Long.parseLong(poklapanjeVozila.group("vrijeme"));
      double brzina = Double.parseDouble(poklapanjeVozila.group("brzina"));
      int tempBaterija = Integer.parseInt(poklapanjeVozila.group("tempBaterija"));
      double visina = Double.parseDouble(poklapanjeVozila.group("visina"));
      double preostaloKm = Double.parseDouble(poklapanjeVozila.group("preostaloKm"));
      double ukupnoKm = Double.parseDouble(poklapanjeVozila.group("ukupnoKm"));
      double gpsSirina = Double.parseDouble(poklapanjeVozila.group("gpsSirina"));
      double gpsDuzina = Double.parseDouble(poklapanjeVozila.group("gpsDuzina"));
      int broj = Integer.parseInt(poklapanjeVozila.group("broj"));
      double snaga = Double.parseDouble(poklapanjeVozila.group("snaga"));
      double struja = Double.parseDouble(poklapanjeVozila.group("struja"));
      double gpsBrzina = Double.parseDouble(poklapanjeVozila.group("gpsBrzina"));

      this.podaciVozila = new PodaciVozila(id, broj, vrijeme, brzina, snaga, struja, visina,
          gpsBrzina, tempVozila, postotakBaterija, naponBaterija, kapacitetBaterija, tempBaterija,
          preostaloKm, ukupnoKm, gpsSirina, gpsDuzina);

      // dodaj podatke i u red jer to treba za 2. zadacu
      /*
       * RedPodaciVozila redPodaciVozila = this.centralniSustav.svaVozila.get(id); if
       * (redPodaciVozila == null) { redPodaciVozila = new RedPodaciVozila(id);
       * this.centralniSustav.svaVozila.put(id, this.podaciVozila ); }
       * redPodaciVozila.dodajPodatakVozila(podaciVozila);
       */
      return "OK";
    } catch (Exception e) {
      return "ERROR 29";
    }
  }

  /**
   * Provjerava domete radara za primljeno vozilo.
   * 
   * @param primljenaKomanda Primljena komanda koja sadrži podatke o vozilu.
   */
  public void provjeriDometeRadara(String primljenaKomanda) {
    for (Map.Entry<Integer, PodaciRadara> ulaz : this.centralniSustav.sviRadari.entrySet()) {
      PodaciRadara vrijednost = ulaz.getValue();

      boolean uDoseguRadara = provjeriJeLiVoziloURadaru(vrijednost.gpsSirina(),
          vrijednost.gpsDuzina(), vrijednost.maksUdaljenost());

      if (uDoseguRadara == true) {
        var komanda = new StringBuilder();
        komanda.append("VOZILO").append(" ").append(this.podaciVozila.id()).append(" ")
            .append(this.podaciVozila.vrijeme()).append(" ").append(this.podaciVozila.brzina())
            .append(" ").append(this.podaciVozila.gpsSirina()).append(" ")
            .append(this.podaciVozila.gpsDuzina());

        MrezneOperacije.posaljiZahtjevPosluzitelju(vrijednost.adresaRadara(),
            vrijednost.mreznaVrataRadara(), komanda.toString());
      }
    }
  }

  /**
   * Provjerava je li vozilo unutar dosega određenog radara na temelju GPS lokacija.
   * 
   * @param gpsSirinaRadara GPS širina radara.
   * @param gpsDuzinaRadara GPS dužina radara.
   * @param maksUdaljenost Maksimalna udaljenost unutar koje se smatra da je vozilo unutar dosega
   *        radara.
   * @return True ako je vozilo unutar dosega radara, inače false.
   */
  public boolean provjeriJeLiVoziloURadaru(double gpsSirinaRadara, double gpsDuzinaRadara,
      int maksUdaljenost) {
    double udaljenostRadaraIVozilaKm = GpsUdaljenostBrzina.udaljenostKm(gpsSirinaRadara,
        gpsDuzinaRadara, this.podaciVozila.gpsSirina(), this.podaciVozila.gpsDuzina());
    double udaljenostRadaraIVozilaM = udaljenostRadaraIVozilaKm * 1000;
    if (udaljenostRadaraIVozilaM <= maksUdaljenost) {
      return true;
    } else {
      return false;
    }
  }
}


