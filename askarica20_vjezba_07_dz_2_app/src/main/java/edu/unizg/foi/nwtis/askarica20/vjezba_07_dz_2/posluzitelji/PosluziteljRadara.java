package edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.posluzitelji;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ThreadFactory;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.posluzitelji.radnici.RadnikZaRadare;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

/**
 * Klasa PosluziteljRadara.
 */
public class PosluziteljRadara {
  private PodaciRadara podaciRadara;
  private ThreadFactory tvornicaDretvi = Thread.ofVirtual().factory();

  /**
   * Metoda za pokretanje poslužitelja radara. Ovisno o broju argumenata pokreće registraciju ili
   * brisanje radara.
   * 
   * @param args Argumenti iz komandne linije kojih mora biti 1 ili 3.
   */
  public static void main(String[] args) {
    if (args.length != 1 && args.length != 3) {
      System.out.println("Broj argumenata nije 1 ili 3.");
      return;
    }

    PosluziteljRadara posluziteljRadara = new PosluziteljRadara();
    try {
      posluziteljRadara.preuzmiPostavke(args);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    if (args.length == 1) {
      try {
        // if (posluziteljRadara.provjeriJeLiPoslKazniAktivan()) {
        posluziteljRadara.registrirajPosluzitelja();
        posluziteljRadara.pokreniPosluzitelja(posluziteljRadara);
        // } else {
        // System.out.println("ERROR 31 Poslužitelj kazni nije aktivan.");
        // TODO kak da se ovo returna kak pise u uputama?
        // return "ERROR 31";
        // }
      } catch (Exception e) {
        System.out.println(e.getMessage());
        return;
      }
    } else if (args.length == 3) {
      if (args[2].matches("\\d+")) {
        int id = Integer.parseInt(args[2]);
        posluziteljRadara.obrisiRadarPoIdu(id);
      } else {
        posluziteljRadara.obrisiSveRadare();
      }
    }
  }

  /**
   * Metoda za provjeru aktivnosti poslužitelja kazni.
   * 
   * @return true ako je poslužitelj kazni aktivan, inače false
   */
  private boolean provjeriJeLiPoslKazniAktivan() {
    try {
      Socket mreznaUticnica =
          new Socket(this.podaciRadara.adresaKazne(), this.podaciRadara.mreznaVrataKazne());
      mreznaUticnica.close();
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Metoda za brisanje svih radara.
   */
  private void obrisiSveRadare() {
    var komanda = new StringBuilder();
    komanda.append("RADAR").append(" ").append("OBRIŠI").append(" ").append("SVE");
    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(this.podaciRadara.adresaRegistracije(),
        this.podaciRadara.mreznaVrataRegistracije(), komanda.toString());
  }

  /**
   * Metoda za resetiranje radara.
   */
  private void resetirajRadare() {
    var komanda = new StringBuilder();
    komanda.append("RADAR").append(" ").append("RESET");
    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(this.podaciRadara.adresaRegistracije(),
        this.podaciRadara.mreznaVrataRegistracije(), komanda.toString());
  }

  /**
   * Metoda za trazenje radara.
   * 
   * @param id ID radara koji se trazi
   */
  private void traziRadarPoIdu(int id) {
    var komanda = new StringBuilder();
    komanda.append("RADAR").append(" ").append(id);
    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(this.podaciRadara.adresaRegistracije(),
        this.podaciRadara.mreznaVrataRegistracije(), komanda.toString());
  }

  /**
   * Metoda za brisanje radara po ID-u.
   * 
   * @param id ID radara koji se briše
   */
  private void obrisiRadarPoIdu(int id) {
    var komanda = new StringBuilder();
    komanda.append("RADAR").append(" ").append("OBRIŠI").append(" ").append(id);
    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(this.podaciRadara.adresaRegistracije(),
        this.podaciRadara.mreznaVrataRegistracije(), komanda.toString());
  }

  /**
   * Metoda za registraciju poslužitelja radara.
   * 
   * @return true ako je registracija uspješna, inače false
   */
  public boolean registrirajPosluzitelja() {
    var komanda = new StringBuilder();
    komanda.append("RADAR").append(" ").append(this.podaciRadara.id()).append(" ")
        .append(this.podaciRadara.adresaRadara()).append(" ")
        .append(this.podaciRadara.mreznaVrataRadara()).append(" ")
        .append(this.podaciRadara.gpsSirina()).append(" ").append(this.podaciRadara.gpsDuzina())
        .append(" ").append(this.podaciRadara.maksUdaljenost());

    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(this.podaciRadara.adresaRegistracije(),
        this.podaciRadara.mreznaVrataRegistracije(), komanda.toString());

    if (odgovor != null) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Metoda za pokretanje poslužitelja radara.
   */
  public void pokreniPosluzitelja(PosluziteljRadara posluziteljRadara) {
    boolean kraj = false;
    try (ServerSocket mreznaUticnicaPosluzitelja =
        new ServerSocket(this.podaciRadara.mreznaVrataRadara())) {
      while (!kraj) {
        var mreznaUticnica = mreznaUticnicaPosluzitelja.accept();
        var radnikZaRadare =
            new RadnikZaRadare(mreznaUticnica, this.podaciRadara, posluziteljRadara);
        var dretva = tvornicaDretvi.newThread(radnikZaRadare);
        dretva.start();
      }
    } catch (NumberFormatException | IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Metoda za preuzimanje postavki iz konfiguracijske datoteke.
   * 
   * @param args Argumenti iz komandne linije
   * @throws NeispravnaKonfiguracija ako konfiguracija nije ispravna
   * @throws NumberFormatException ako dođe do greške prilikom parsiranja brojeva
   * @throws UnknownHostException ako adresa poslužitelja nije poznata
   */
  public void preuzmiPostavke(String[] args)
      throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);
    this.podaciRadara = new PodaciRadara(Integer.valueOf(konfig.dajPostavku("id")),
        InetAddress.getLocalHost().getHostName(),
        Integer.valueOf(konfig.dajPostavku("mreznaVrataRadara")),
        Integer.valueOf(konfig.dajPostavku("maksBrzina")),
        Integer.valueOf(konfig.dajPostavku("maksTrajanje")),
        Integer.valueOf(konfig.dajPostavku("maksUdaljenost")),
        konfig.dajPostavku("adresaRegistracije"),
        Integer.valueOf(konfig.dajPostavku("mreznaVrataRegistracije")),
        konfig.dajPostavku("adresaKazne"), Integer.valueOf(konfig.dajPostavku("mreznaVrataKazne")),
        konfig.dajPostavku("postanskaAdresaRadara"),
        Double.valueOf(konfig.dajPostavku("gpsSirina")),
        Double.valueOf(konfig.dajPostavku("gpsDuzina")));
  }
}
