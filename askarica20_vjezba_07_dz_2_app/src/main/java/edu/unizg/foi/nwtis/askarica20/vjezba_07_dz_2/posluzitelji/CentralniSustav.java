package edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.posluzitelji;

import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.podaci.RedPodaciVozila;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

/**
 * Klasa CentralniSustav.
 */
public class CentralniSustav {
  public int mreznaVrataRadara;
  public int mreznaVrataVozila;
  public int mreznaVrataNadzora;
  public int maksVozila;
  private ThreadFactory tvornicaDretvi = Thread.ofVirtual().factory();
  public ConcurrentHashMap<Integer, PodaciRadara> sviRadari =
      new ConcurrentHashMap<Integer, PodaciRadara>();
  public ConcurrentHashMap<Integer, RedPodaciVozila> svaVozila =
      new ConcurrentHashMap<Integer, RedPodaciVozila>();

  /**
   * Glavna metoda programa. Provjerava argumente naredbenog retka i pokreće centralni sustav.
   *
   * @param args Argumenti naredbenog retka kojih smije biti samo 1.
   */
  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Broj argumenata nije 1.");
      return;
    }

    CentralniSustav centralniSustav = new CentralniSustav();
    try {
      centralniSustav.preuzmiPostavke(args);
      centralniSustav.pokreniPosluzitelja();

    } catch (NeispravnaKonfiguracija | NumberFormatException | UnknownHostException e) {
      System.out.println(e.getMessage());
      return;
    }
  }

  /**
   * Pokreće poslužitelje za registraciju radara i vožnje vozila.
   */
  public void pokreniPosluzitelja() {
    PosluziteljZaRegistracijuRadara posluziteljZaRegistracijuRadara =
        new PosluziteljZaRegistracijuRadara(mreznaVrataRadara, this);
    Thread dretvaPosluziteljaRadara = tvornicaDretvi.newThread(posluziteljZaRegistracijuRadara);
    dretvaPosluziteljaRadara.start();

    PosluziteljZaVozila posluziteljZaVozila = new PosluziteljZaVozila(mreznaVrataVozila, this);
    Thread dretvaPosluziteljaZaVozila = tvornicaDretvi.newThread(posluziteljZaVozila);
    dretvaPosluziteljaZaVozila.start();

    try {
      dretvaPosluziteljaRadara.join();
      dretvaPosluziteljaZaVozila.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
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

    this.mreznaVrataRadara = Integer.valueOf(konfig.dajPostavku("mreznaVrataRadara"));
    this.mreznaVrataVozila = Integer.valueOf(konfig.dajPostavku("mreznaVrataVozila"));
    this.mreznaVrataNadzora = Integer.valueOf(konfig.dajPostavku("mreznaVrataNadzora"));
    this.maksVozila = Integer.valueOf(konfig.dajPostavku("maksVozila"));
  }
}
