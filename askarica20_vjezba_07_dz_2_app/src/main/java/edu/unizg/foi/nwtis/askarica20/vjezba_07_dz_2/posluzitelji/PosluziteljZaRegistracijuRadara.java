package edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.posluzitelji;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.pomocnici.MrezneOperacije;

/**
 * Klasa PosluziteljZaRegistracijuRadara.
 */
public class PosluziteljZaRegistracijuRadara implements Runnable {
  private int mreznaVrata;
  private CentralniSustav centralniSustav;

  private Pattern predlozakRegistracijeRadara = Pattern.compile(
      "^RADAR (?<id>\\d+) (?<adresa>\\w+) (?<mreznaVrata>\\d+) (?<gpsSirina>\\d+[.]\\d+) (?<gpsDuzina>\\d+[.]\\d+) (?<maksUdaljenost>-?\\d+)$");
  private Pattern predlozakBrisanjaRadaraPoIdu = Pattern.compile("^RADAR OBRIŠI (?<id>\\d+)$");
  private Pattern predlozakBrisanjaSvihRadara = Pattern.compile("^RADAR OBRIŠI SVE$");
  private Pattern predlozakResetiranjaRadara = Pattern.compile("^RADAR RESET$");
  private Pattern predlozakTrazenjaRadaraPoIdu = Pattern.compile("^RADAR (?<id>\\d+)$");
  private Pattern predlozakPripremanjaSvihRadara = Pattern.compile("^RADAR SVI$");

  private Matcher poklapanjeRegistracijeRadara;
  private Matcher poklapanjeBrisanjaSvihRadara;
  private Matcher poklapanjeBrisanjaRadaraPoIdu;
  private Matcher poklapanjeResetiranjaRadara;
  private Matcher poklapanjeTrazenjaRadara;
  private Matcher poklapanjePripremanjaSvihRadara;

  /**
   * Konstruktor za inicijalizaciju poslužitelja.
   * 
   * @param mreznaVrata Mrežna vrata na kojima će poslužitelj slušati zahtjeve.
   * @param centralniSustav Referenca na centralni sustav za pristup podacima o radarima.
   */
  public PosluziteljZaRegistracijuRadara(int mreznaVrata, CentralniSustav centralniSustav) {
    super();
    this.mreznaVrata = mreznaVrata;
    this.centralniSustav = centralniSustav;
  }

  /**
   * Metoda za pokretanje poslužitelja u odvojenoj dretvi. Prihvaća dolazne zahtjeve, obrađuje ih i
   * vraća odgovore.
   */
  @Override
  public void run() {
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
   * Metoda za obradu dolaznih zahtjeva. Provjerava zahtjev, određuje njegovu vrstu i poziva
   * odgovarajuću metodu za obradu.
   * 
   * @param zahtjev Dolazni zahtjev u obliku komandu koji treba obraditi.
   * @return Odgovor na zahtjev ili poruka o grešci ako je zahtjev neispravan.
   */
  public String obradaZahtjeva(String zahtjev) {
    if (zahtjev == null) {
      return "ERROR 10 Neispravna sintaksa komande.";
    }

    String odgovor;
    odgovor = obradaZahtjevaRegistracijeRadara(zahtjev);
    if (odgovor != null) {
      return odgovor;
    }
    odgovor = obradaZahtjevaBrisanjaSvihRadara(zahtjev);
    if (odgovor != null) {
      return odgovor;
    }
    odgovor = obradaZahtjevaBrisanjaRadaraPoIdu(zahtjev);
    if (odgovor != null) {
      return odgovor;
    }
    odgovor = obradaZahtjevaResetiranjeRadara(zahtjev);
    if (odgovor != null) {
      return odgovor;
    }
    odgovor = obradaZahtjevaTrazenjeRadara(zahtjev);
    if (odgovor != null) {
      return odgovor;
    }
    odgovor = obradaZahtjevaPripremanjeRadara(zahtjev);
    if (odgovor != null) {
      return odgovor;
    }
    return "ERROR 10 Neispravna sintaksa komande.";
  }


  /**
   * Metoda za obradu zahtjeva za brisanje svih radara. Ako je zahtjev ispravan, briše sve radare iz
   * centralnog sustava.
   * 
   * @param zahtjev Zahtjev u obliku komande za brisanje svih radara,
   * @return Odgovor na zahtjev ili null ako zahtjev nije odgovarajućeg formata,
   */
  public String obradaZahtjevaBrisanjaSvihRadara(String zahtjev) {
    this.poklapanjeBrisanjaSvihRadara = this.predlozakBrisanjaSvihRadara.matcher(zahtjev);
    var status = poklapanjeBrisanjaSvihRadara.matches();
    if (status) {
      this.centralniSustav.sviRadari.clear();
      return "OK";
    }
    return null;
  }

  /**
   * Metoda za obradu zahtjeva za brisanje radara po ID-u. Ako je zahtjev ispravan, briše radar s
   * odgovarajućim ID-om iz centralnog sustava.
   * 
   * @param zahtjev Zahtjev u obliku komande za brisanje radara po ID-u
   * @return Odgovor na zahtjev ili null ako zahtjev nije odgovarajućeg formata
   */
  public String obradaZahtjevaBrisanjaRadaraPoIdu(String zahtjev) {
    this.poklapanjeBrisanjaRadaraPoIdu = this.predlozakBrisanjaRadaraPoIdu.matcher(zahtjev);
    var status = poklapanjeBrisanjaRadaraPoIdu.matches();
    if (status) {
      this.centralniSustav.sviRadari
          .remove(Integer.valueOf(this.poklapanjeBrisanjaRadaraPoIdu.group("id")));
      return "OK";
    }
    return null;
  }

  /**
   * Metoda za obradu zahtjeva za resetiranje radara. Ako je zahtjev ispravan, resetira radar.
   *
   * @param zahtjev Zahtjev u obliku komande za brisanje radara po ID-u.
   * @return Odgovor na zahtjev ili null ako zahtjev nije odgovarajućeg formata.
   */
  public String obradaZahtjevaResetiranjeRadara(String zahtjev) {
    this.poklapanjeResetiranjaRadara = this.predlozakResetiranjaRadara.matcher(zahtjev);
    var status = poklapanjeResetiranjaRadara.matches();
    int brojRadaraPrije = this.centralniSustav.sviRadari.size();
    int brojNeaktivnihRadara = 0;

    if (status) {
      for (Map.Entry<Integer, PodaciRadara> r : this.centralniSustav.sviRadari.entrySet()) {
        PodaciRadara radar = r.getValue();
        var aktivan = provjeraJelRadarAktivan(radar);
        if (!aktivan) {
          brojNeaktivnihRadara++;
          deregistracijaRadara(radar);
        }
      }

      return "OK " + brojRadaraPrije + " " + brojNeaktivnihRadara;
    }
    return null;
  }

  /**
   * Provjerava je li radar aktivan slanjem komande radaru i čekanjem na odgovor.
   *
   * @param radar Objekt tipa PodaciRadara koji predstavlja radar kojeg treba provjeriti.
   * @return true ako je radar aktivan, inače false.
   */
  public boolean provjeraJelRadarAktivan(PodaciRadara radar) {
    // TODO
    var komanda = new StringBuilder();
    komanda.append("RADAR").append(" ").append(radar.id());

    try {
      var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(radar.adresaRadara(),
          radar.mreznaVrataRadara(), komanda.toString());
      if ("OK".equals(odgovor)) {
        return true;
      } else {
        return false;
      }
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Deregistrira radar uklanjanjem iz kolekcije radara.
   *
   * @param radar Objekt tipa PodaciRadara koji predstavlja radar kojeg treba deregistrirati.
   */
  public void deregistracijaRadara(PodaciRadara radar) {
    this.centralniSustav.sviRadari.remove(radar.id());
  }


  /**
   * Metoda za obradu zahtjeva za traženje radara po ID-u.
   *
   * @param zahtjev Zahtjev u obliku komande za traženje radara po ID-u.
   * @return "OK" ako je radar pronađen, "ERROR 12" ako radar nije pronađen, ili null ako zahtjev
   *         nije odgovarajućeg formata.
   */
  public String obradaZahtjevaTrazenjeRadara(String zahtjev) {
    this.poklapanjeTrazenjaRadara = this.predlozakTrazenjaRadaraPoIdu.matcher(zahtjev);
    var status = poklapanjeTrazenjaRadara.matches();
    if (status) {
      int radarId = Integer.parseInt(this.poklapanjeTrazenjaRadara.group("id"));
      if (this.centralniSustav.sviRadari.containsKey(radarId)) {
        PodaciRadara radar = this.centralniSustav.sviRadari.get(radarId);
        return "OK";
      } else {
        return "ERROR 12";
      }
    }
    return null;
  }

  /**
   * Metoda za obradu zahtjeva za pripremanje svih radara.
   *
   * @param zahtjev Zahtjev u obliku komande za pripremanje svih radara.
   * @return "OK" sa svim radarima u odgovarajućem formatu ili null ako zahtjev nije odgovarajućeg
   *         formata.
   */
  public String obradaZahtjevaPripremanjeRadara(String zahtjev) {
    this.poklapanjePripremanjaSvihRadara = this.predlozakPripremanjaSvihRadara.matcher(zahtjev);
    var status = poklapanjePripremanjaSvihRadara.matches();
    if (status) {
      StringBuilder sviRadari = new StringBuilder();
      boolean prviElement = true;
      for (Map.Entry<Integer, PodaciRadara> r : this.centralniSustav.sviRadari.entrySet()) {
        if (!prviElement) {
          sviRadari.append(", ");
        }
        PodaciRadara radar = r.getValue();
        sviRadari.append("[" + r.getKey()).append(" ").append(radar.adresaRadara()).append(" ")
            .append(radar.mreznaVrataRadara()).append(" ").append(radar.gpsSirina()).append(" ")
            .append(radar.gpsDuzina()).append(" ").append(radar.maksUdaljenost()).append("]");
        prviElement = false;
      }
      return "OK {" + sviRadari.toString() + "}";
    }
    return null;
  }

  /**
   * Metoda za obradu zahtjeva za registraciju radara. Ako je zahtjev ispravan, registrira novi
   * radar u centralnom sustavu.
   * 
   * @param zahtjev Zahtjev u obliku komande koji sadrži podatke o radaru koji se registrira.
   * @return Odgovor na zahtjev ili null ako zahtjev nije odgovarajućeg formata.
   */
  public String obradaZahtjevaRegistracijeRadara(String zahtjev) {
    this.poklapanjeRegistracijeRadara = this.predlozakRegistracijeRadara.matcher(zahtjev);
    var status = poklapanjeRegistracijeRadara.matches();
    if (status) {
      var radar = new PodaciRadara(Integer.valueOf(this.poklapanjeRegistracijeRadara.group("id")),
          this.poklapanjeRegistracijeRadara.group("adresa"),
          Integer.valueOf(this.poklapanjeRegistracijeRadara.group("mreznaVrata")), -1, -1,
          Integer.valueOf(this.poklapanjeRegistracijeRadara.group("maksUdaljenost")), null, -1,
          null, -1, null, Double.valueOf(this.poklapanjeRegistracijeRadara.group("gpsSirina")),
          Double.valueOf(this.poklapanjeRegistracijeRadara.group("gpsDuzina")));
      this.centralniSustav.sviRadari.put(radar.id(), radar);
      return "OK";
    }
    return null;
  }


}
