package edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.posluzitelji.radnici;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.podaci.BrzoVozilo;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.posluzitelji.PosluziteljRadara;

/**
 * Klasa RadnikZaRadare.
 */
public class RadnikZaRadare implements Runnable {
  private Socket mreznaUticnica;
  private PodaciRadara podaciRadara;
  private PosluziteljRadara posluziteljRadara;

  private Matcher poklapanjeVozila;
  private Matcher poklapanjeResetiranjaRadara;
  private Matcher poklapanjeTrazenjaRadara;
  private Pattern predlozakVozila = Pattern.compile(
      "^VOZILO (?<id>\\d+) (?<vrijeme>\\d+) (?<brzina>-?\\d+([.]\\d+)?) (?<gpsSirina>\\d+[.]\\d+) (?<gpsDuzina>\\d+[.]\\d+)$");
  private Pattern predlozakResetiranjaRadara = Pattern.compile("^RADAR RESET$");
  private Pattern predlozakTrazenjaRadaraPoIdu = Pattern.compile("^RADAR (?<id>\\d+)$");


  public volatile BrzoVozilo brzoVozilo = null;
  private static volatile ConcurrentHashMap<Integer, BrzoVozilo> brzaVozilaPodaci =
      new ConcurrentHashMap<Integer, BrzoVozilo>();
  private static volatile int helperBrojPoslanihKazni = 0;

  /**
   * Konstruktor za klasu RadnikZaRadare.
   * 
   * @param mreznaUticnica Mrežna utičnica preko koje se komunicira s klijentom.
   * @param podaciRadara Podaci o radaru.
   */
  public RadnikZaRadare(Socket mreznaUticnica, PodaciRadara podaciRadara,
      PosluziteljRadara posluziteljRadara) {
    super();
    this.mreznaUticnica = mreznaUticnica;
    this.podaciRadara = podaciRadara;
    this.posluziteljRadara = posluziteljRadara;
  }

  /**
   * Pokreće izvršavanje radnika za radare. Radnik čita podatke koje dobiva preko mrežne utičnice,
   * obrađuje ih i šalje odgovor klijentu. Nakon završetka obrade, zatvara mrežnu utičnicu.
   */
  @Override
  public void run() {
    try {
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
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Metoda za obradu zahtjeva od klijenta.
   * 
   * @param zahtjev Zahtjev u obliku komande koji je primljen.
   * @return Odgovor na zahtjev.
   */
  public String obradaZahtjeva(String zahtjev) {
    if (zahtjev == null) {
      return "ERROR 10 Neispravna sintaksa komande.";
    }
    var odgovor = obradaZahtjevaBrzine(zahtjev);
    if (odgovor != null) {
      return odgovor;
    }
    odgovor = obradaZahtjevaRadarId(zahtjev);
    if (odgovor != null) {
      return odgovor;
    }
    odgovor = obradaZahtjevaResetiranjeRadara(zahtjev);
    if (odgovor != null) {
      return odgovor;
    }
    return "ERROR 10 Neispravna sintaksa komande.";
  }

  /**
   * Metoda za obradu podataka o brzini vozila iz zahtjeva.
   * 
   * @param zahtjev Zahtjev koji sadrži podatke o brzini vozila.
   * @return Odgovor na zahtjev, ili null ako se podaci ne podudaraju s obrascem.
   */
  public String obradaZahtjevaBrzine(String zahtjev) {
    this.poklapanjeVozila = this.predlozakVozila.matcher(zahtjev);
    var statusVozila = poklapanjeVozila.matches();
    if (statusVozila) {
      boolean prekoracenje =
          provjeraPrekoracenjaBrzine(Double.valueOf(this.poklapanjeVozila.group("brzina")));

      var idOvogVozila = Integer.parseInt(poklapanjeVozila.group("id"));

      if (prekoracenje == true) {
        obradaPrekoracenja(idOvogVozila);
      } else {
        obradaNeprekoracenja(idOvogVozila);
      }
      return "OK";
    }
    return null;
  }

  /**
   * Metoda za obradu zahtjeva radara po IDu
   * 
   * @param zahtjev Zahtjev koji sadrži podatke
   * @return Odgovor na zahtjev, ili null ako se podaci ne podudaraju s obrascem.
   */
  public String obradaZahtjevaRadarId(String zahtjev) {
    this.poklapanjeTrazenjaRadara = this.predlozakTrazenjaRadaraPoIdu.matcher(zahtjev);
    var status = poklapanjeTrazenjaRadara.matches();
    if (status) {
      int idRadaraZahtjev = Integer.valueOf(this.poklapanjeTrazenjaRadara.group("id"));
      int idRadaraOvaj = this.podaciRadara.id();

      if (idRadaraZahtjev == idRadaraOvaj) {
        try {
          var odgovorPoslKazni = MrezneOperacije.posaljiZahtjevPosluzitelju(
              this.podaciRadara.adresaKazne(), this.podaciRadara.mreznaVrataKazne(), "TEST");
          if ("OK".equals(odgovorPoslKazni)) {
            return "OK";
          } else {
            return "ERROR 34 Posluzitelj kazni nije aktivan";
          }
        } catch (Exception e) {
          return "ERROR 33 id ne odgovara id-u ni jednog radara";
        }
      } else {
        return "ERROR 33 id ne odgovara id-u radara";
      }
    }
    return null;
  }

  /**
   * Metoda za obradu zahtjeva radara i resetiranje
   * 
   * @param zahtjev Zahtjev koji sadrži podatke
   * @return Odgovor na zahtjev, ili null ako se podaci ne podudaraju s obrascem.
   */
  public String obradaZahtjevaResetiranjeRadara(String zahtjev) {
    this.poklapanjeResetiranjaRadara = this.predlozakResetiranjaRadara.matcher(zahtjev);
    var status = poklapanjeResetiranjaRadara.matches();
    if (status) {
      var komanda = new StringBuilder();
      komanda.append("RADAR").append(" ").append(this.podaciRadara.id());

      var odgovor =
          MrezneOperacije.posaljiZahtjevPosluzitelju(this.podaciRadara.adresaRegistracije(),
              this.podaciRadara.mreznaVrataRegistracije(), komanda.toString());

      if ("OK".equals(odgovor)) {
        return "OK";
      } else if ("ERROR 12".equals(odgovor)) {
        var odg = this.posluziteljRadara.registrirajPosluzitelja();
        if ("OK".equals(odg)) {
          return "OK";
        }
      } else {
        return "ERROR 32 PosluziteljZaRegistracijuRadara nije aktivan.";
      }
    }
    return null;
  }

  /**
   * Metoda za obradu prekoračenja brzine vozila.
   * 
   * @param idOvogVozila ID vozila koje je prekoračilo brzinu.
   */
  public void obradaPrekoracenja(int idOvogVozila) {
    if (brzaVozilaPodaci.containsKey(idOvogVozila) == false
        || brzaVozilaPodaci.get(idOvogVozila).status() == false) {

      brzaVozilaPodaci.put(idOvogVozila,
          new BrzoVozilo(Integer.parseInt(poklapanjeVozila.group("id")), -1,
              Long.parseLong(poklapanjeVozila.group("vrijeme")),
              Double.parseDouble(poklapanjeVozila.group("brzina")),
              Double.parseDouble(poklapanjeVozila.group("gpsSirina")),
              Double.parseDouble(poklapanjeVozila.group("gpsDuzina")), true));
    } else {
      double vremRazlika = dajRazlikuVremena(brzaVozilaPodaci.get(idOvogVozila).vrijeme(),
          Long.parseLong(poklapanjeVozila.group("vrijeme")));

      if (vremRazlika > (double) this.podaciRadara.maksTrajanje()
          && vremRazlika < (double) (this.podaciRadara.maksTrajanje() * 2.0)
          && brzaVozilaPodaci.get(idOvogVozila).status() == true) {
        var vrPocetak = brzaVozilaPodaci.get(idOvogVozila).vrijeme();
        var vrKraj = Long.parseLong(poklapanjeVozila.group("vrijeme"));
        this.brzoVozilo = brzaVozilaPodaci.get(idOvogVozila);

        posaljiKaznuPosluzitelju(vrPocetak, vrKraj);
        helperBrojPoslanihKazni++;

        BrzoVozilo brzoVoziloPomocnik = brzaVozilaPodaci.get(idOvogVozila);
        brzoVoziloPomocnik = brzoVoziloPomocnik.postaviStatus(false);
        brzaVozilaPodaci.put(idOvogVozila, brzoVoziloPomocnik);
      }
    }
  }

  /**
   * Metoda za obradu kada vozilo ne prekoračuje brzinu.
   * 
   * @param idOvogVozila ID vozila koje ne prekoračuje brzinu.
   */
  public void obradaNeprekoracenja(int idOvogVozila) {
    BrzoVozilo brzoVoziloPomocnik = brzaVozilaPodaci.get(idOvogVozila);
    if (brzoVoziloPomocnik != null) {
      brzoVoziloPomocnik = brzoVoziloPomocnik.postaviStatus(false);
      brzaVozilaPodaci.put(idOvogVozila, brzoVoziloPomocnik);
    }
  }

  /**
   * Metoda za slanje kazne poslužitelju u slučaju prekoračenja brzine.
   * 
   * @param vrijemePocetak Vrijeme početka prekoračenja brzine.
   * @param vrijemeKraj Vrijeme kraja prekoračenja brzine.
   */
  public void posaljiKaznuPosluzitelju(long vrijemePocetak, long vrijemeKraj) {
    var komanda = pripremiKomanduZaPosluziteljaKazni(vrijemePocetak, vrijemeKraj);
    MrezneOperacije.posaljiZahtjevPosluzitelju(this.podaciRadara.adresaKazne(),
        this.podaciRadara.mreznaVrataKazne(), komanda.toString());
  }

  /**
   * Metoda za pripremu komande za slanje kazne poslužitelju.
   * 
   * @param vrijemePocetak Vrijeme početka prekoračenja brzine.
   * @param vrijemeKraj Vrijeme kraja prekoračenja brzine.
   * @return Pripremljena komanda za slanje kazne poslužitelju.
   */
  public String pripremiKomanduZaPosluziteljaKazni(long vrijemePocetak, long vrijemeKraj) {
    var novaKomanda = new StringBuilder();
    novaKomanda.append("VOZILO").append(" ").append(this.brzoVozilo.id()).append(" ")
        .append(vrijemePocetak).append(" ").append(vrijemeKraj).append(" ")
        .append(this.brzoVozilo.brzina()).append(" ").append(this.brzoVozilo.gpsSirina())
        .append(" ").append(this.brzoVozilo.gpsDuzina()).append(" ")
        .append(this.podaciRadara.gpsSirina()).append(" ").append(this.podaciRadara.gpsDuzina());

    return novaKomanda.toString();
  }

  /**
   * Metoda za izračunavanje razlike u vremenu između dva trenutka.
   * 
   * @param vrijemeBrzogVozila Vrijeme kada je zabilježena brzina brzog vozila.
   * @param vrijemeTrenutnogVozila Trenutno vrijeme kada je zahtjev obrađen.
   * @return Razlika u vremenu između dva trenutka.
   */
  public double dajRazlikuVremena(long vrijemeBrzogVozila, long vrijemeTrenutnogVozila) {
    long vremenskaRazlikaUSekundama = (vrijemeTrenutnogVozila - vrijemeBrzogVozila) / 1000;
    return vremenskaRazlikaUSekundama;
  }

  /**
   * Metoda za provjeru prekoračenja brzine vozila.
   * 
   * @param brzina Brzina vozila koja se provjerava.
   * @return True ako je brzina veća od maksimalne dozvoljene, inače false.
   */
  public boolean provjeraPrekoracenjaBrzine(double brzina) {
    if (brzina > (double) this.podaciRadara.maksBrzina()) {
      return true;
    } else {
      return false;
    }
  }

}
