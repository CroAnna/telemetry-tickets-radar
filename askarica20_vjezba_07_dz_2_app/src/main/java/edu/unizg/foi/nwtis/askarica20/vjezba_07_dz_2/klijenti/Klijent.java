package edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.klijenti;

import java.net.InetAddress;
import java.net.UnknownHostException;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

/**
 * Klasa Klijent.
 */
public class Klijent {
  private int mreznaVrataKazne;
  private String adresaKazne;

  /**
   * Glavna metoda programa. Pokreće aplikaciju za obradu kazni ovisno o argumentima naredbenog
   * retka kojih smije biti 2 ili 3.
   *
   * @param args Argumenti naredbenog retka.
   */
  public static void main(String[] args) {
    System.out.println(args.length);
    if (args.length != 4 && args.length != 3) {
      System.out.println("Broj argumenata nije 2 ili 3.");
      return;
    } else {
      Klijent klijent = new Klijent();
      try {
        klijent.preuzmiPostavke(args);
      } catch (Exception e) {
        e.printStackTrace();
      }

      if (args.length == 3) {
        long vrijeme1 = Long.parseLong(args[1]);
        long vrijeme2 = Long.parseLong(args[2]);
        klijent.sveKazneZaVrijeme(vrijeme1, vrijeme2, klijent.adresaKazne,
            klijent.mreznaVrataKazne);
      } else if (args.length == 4) {
        int id = Integer.parseInt(args[1]);
        long vrijeme1 = Long.parseLong(args[2]);
        long vrijeme2 = Long.parseLong(args[3]);
        klijent.sveKazneZaVrijemeZaId(id, vrijeme1, vrijeme2, klijent.adresaKazne,
            klijent.mreznaVrataKazne);
      }
    }
  }

  /**
   * Priprema komandu za dohvaćanje svih kazni za sva vozila u određenom vremenskom razdoblju i
   * šalje je poslužitelju.
   *
   * @param vrijemeOd Početak vremenskog intervala u milisekundama od epohe (Unix vremena).
   * @param vrijemeDo Kraj vremenskog intervala u milisekundama od epohe (Unix vremena).
   * @param adresa Adresa poslužitelja na koja se šalje zahtjev.
   * @param mreznaVrata Mrežna vrata poslužitelja na koja se šalje zahtjev.
   */
  private void sveKazneZaVrijeme(long vrijemeOd, long vrijemeDo, String adresa, int mreznaVrata) {
    var komanda = new StringBuilder();
    komanda.append("STATISTIKA").append(" ").append(vrijemeOd).append(" ").append(vrijemeDo);
    var odgovor =
        MrezneOperacije.posaljiZahtjevPosluzitelju(adresa, mreznaVrata, komanda.toString());
  }

  /**
   * Priprema komandu za dohvaćanje svih kazni za određeno vozilo u određenom vremenskom razdoblju i
   * šalje je poslužitelju. *
   * 
   * @param id Identifikator vozila.
   * @param vrijemeOd Početak vremenskog intervala u milisekundama od epohe (Unix vremena).
   * @param vrijemeDo Kraj vremenskog intervala u milisekundama od epohe (Unix vremena).
   * @param adresa Adresa poslužitelja na koji se šalje zahtjev.
   * @param mreznaVrata Mrežna vrata poslužitelja na koji se šalje zahtjev.
   */
  private void sveKazneZaVrijemeZaId(int id, long vrijemeOd, long vrijemeDo, String adresa,
      int mreznaVrata) {
    var komanda = new StringBuilder();
    komanda.append("VOZILO").append(" ").append(id).append(" ").append(vrijemeOd).append(" ")
        .append(vrijemeDo);
    MrezneOperacije.posaljiZahtjevPosluzitelju(adresa, mreznaVrata, komanda.toString());
  }

  /**
   * Preuzima postavke iz argumenta i postavlja mrežna vrata kazni i adresu kazni.
   *
   * @param args String niz argumenata, od kojih se očekuje da sadrži konfiguracijsku datoteku kao
   *        prvi element.
   * @throws NeispravnaKonfiguracija Ako konfiguracija nije ispravna.
   * @throws NumberFormatException Ako se broj ne može parsirati.
   * @throws UnknownHostException Ako se ne može dobiti lokalni naziv računala.
   */
  public void preuzmiPostavke(String[] args)
      throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);
    this.mreznaVrataKazne = Integer.valueOf(konfig.dajPostavku("mreznaVrataKazne"));
    this.adresaKazne = InetAddress.getLocalHost().getHostName();
  }
}
