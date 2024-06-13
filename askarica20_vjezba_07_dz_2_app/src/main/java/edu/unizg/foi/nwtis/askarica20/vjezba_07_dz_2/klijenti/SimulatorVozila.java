package edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.klijenti;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Future;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.podaci.PodaciVozila;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

/**
 * Klasa SimulatorVozila.
 */
public class SimulatorVozila {
  private String adresaVozila;
  private int mreznaVrataVozila;
  private int trajanjeSek;
  private int trajanjePauze;
  private PodaciVozila podaciVozila;

  private String datotekaCsv;
  private int id;

  /**
   * Glavna metoda programa. Provjerava argumente naredbenog retka i pokreće simulaciju voznje
   * e-vozila.
   *
   * @param args Argumenti naredbenog retka kojih mora biti 3.
   */
  public static void main(String[] args) {
    if (args.length != 3) {
      System.out.println("Broj argumenata nije 3.");
      return;
    }
    try {
      SimulatorVozila simulatorVozila = new SimulatorVozila();
      simulatorVozila.preuzmiPostavke(args);
      simulatorVozila.pokreniKlijenta();
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
      return;
    }
  }

  /**
   * Pokreće klijenta za simulaciju vozila putem kanala.
   *
   * @throws Exception Ako se dogodi greška prilikom pokretanja klijenta.
   */
  private void pokreniKlijenta() throws Exception {
    AsynchronousSocketChannel klijentovKanal = AsynchronousSocketChannel.open();
    SocketAddress serverovaAdresa =
        new InetSocketAddress(this.adresaVozila, this.mreznaVrataVozila);
    Future<Void> result = klijentovKanal.connect(serverovaAdresa);
    result.get();

    try (BufferedReader citac = new BufferedReader(new FileReader(datotekaCsv))) {
      citac.readLine();

      String redak;
      String prethodniRedak = null;
      int brojRetka = 0;

      while ((redak = citac.readLine()) != null) {
        var komanda = spremiPodatakOVozilu(redak, brojRetka, prethodniRedak);
        byte[] podaci = komanda.getBytes(StandardCharsets.UTF_8);
        ByteBuffer bafer = ByteBuffer.wrap(podaci);
        Future<Integer> baferZaPisanje = klijentovKanal.write(bafer);

        Thread.sleep(this.trajanjePauze);
        prethodniRedak = redak;
        brojRetka++;
      }
    }
  }

  /**
   * Priprema podatak o vozilu i sprema ga za slanje.
   *
   * @param redak Podatak o vozilu u CSV formatu.
   * @param brojRetka Redni broj retka u CSV datoteci.
   * @param prethodniRedak Podatak o prethodnom vozilu u CSV formatu.
   * @return Komanda za slanje podataka o vozilu.
   */
  private String spremiPodatakOVozilu(String redak, Integer brojRetka, String prethodniRedak) {
    String[] dijeloviRetka = redak.split(",");
    String[] dijeloviPrethodnogRetka;

    if (prethodniRedak != null) {
      dijeloviPrethodnogRetka = prethodniRedak.split(",");
    } else {
      dijeloviPrethodnogRetka = dijeloviRetka;
    }

    this.podaciVozila = new PodaciVozila(this.id, brojRetka, Long.valueOf(dijeloviRetka[0]),
        Double.valueOf(dijeloviRetka[1]), Double.valueOf(dijeloviRetka[2]),
        Double.valueOf(dijeloviRetka[3]), Double.valueOf(dijeloviRetka[4]),
        Double.valueOf(dijeloviRetka[5]), Integer.valueOf(dijeloviRetka[6]),
        Integer.valueOf(dijeloviRetka[7]), Double.valueOf(dijeloviRetka[8]),
        Integer.valueOf(dijeloviRetka[9]), Integer.valueOf(dijeloviRetka[10]),
        Double.valueOf(dijeloviRetka[11]), Double.valueOf(dijeloviRetka[12]),
        Double.valueOf(dijeloviRetka[13]), Double.valueOf(dijeloviRetka[14]));

    var razlika = dajRazlikuVremenaRetka(Long.valueOf(dijeloviRetka[0]),
        Long.valueOf(dijeloviPrethodnogRetka[0])) * dajKorekcijuVremena();

    try {
      Thread.sleep((long) razlika);
    } catch (Exception e) {
      e.getMessage();
    }

    return dajKomanduZaSlanje();
  }

  /**
   * Sastavlja komandu za slanje podataka o vozilu.
   *
   * @return Komanda za slanje podataka o vozilu.
   */
  public String dajKomanduZaSlanje() {
    var komanda = new StringBuilder();
    komanda.append("VOZILO").append(" ").append(this.podaciVozila.id()).append(" ")
        .append(this.podaciVozila.broj()).append(" ").append(this.podaciVozila.vrijeme())
        .append(" ").append(this.podaciVozila.brzina()).append(" ")
        .append(this.podaciVozila.snaga()).append(" ").append(this.podaciVozila.struja())
        .append(" ").append(this.podaciVozila.visina()).append(" ")
        .append(this.podaciVozila.gpsBrzina()).append(" ").append(this.podaciVozila.tempVozila())
        .append(" ").append(this.podaciVozila.postotakBaterija()).append(" ")
        .append(this.podaciVozila.naponBaterija()).append(" ")
        .append(this.podaciVozila.kapacitetBaterija()).append(" ")
        .append(this.podaciVozila.tempBaterija()).append(" ")
        .append(this.podaciVozila.preostaloKm()).append(" ").append(this.podaciVozila.ukupnoKm())
        .append(" ").append(this.podaciVozila.gpsSirina()).append(" ")
        .append(this.podaciVozila.gpsDuzina());

    return komanda.toString();
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
  private double dajKorekcijuVremena() {
    return trajanjeSek / 1000.0;
  }

  /**
   * Preuzima postavke iz konfiguracijske datoteke i argumenta naredbenog retka.
   *
   * @param args Argumenti naredbenog retka.
   * @throws NeispravnaKonfiguracija Ako konfiguracija nije ispravna.
   * @throws NumberFormatException Ako se broj ne može parsirati.
   * @throws UnknownHostException Ako se ne može dobiti lokalni naziv računala.
   */
  public void preuzmiPostavke(String[] args)
      throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);
    this.adresaVozila = InetAddress.getLocalHost().getHostName();
    this.mreznaVrataVozila = Integer.valueOf(konfig.dajPostavku("mreznaVrataVozila"));
    this.trajanjeSek = Integer.valueOf(konfig.dajPostavku("trajanjeSek"));
    this.trajanjePauze = Integer.valueOf(konfig.dajPostavku("trajanjePauze"));

    this.datotekaCsv = args[1].toString();
    this.id = Integer.valueOf(args[2]);
  }
}
