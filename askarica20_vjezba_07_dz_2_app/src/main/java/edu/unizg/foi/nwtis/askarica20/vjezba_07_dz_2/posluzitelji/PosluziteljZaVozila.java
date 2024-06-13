package edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.posluzitelji;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.podaci.PodaciVozila;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.posluzitelji.radnici.RadnikZaVozila;

/**
 * Klasa PosluziteljZaVozila.
 */
public class PosluziteljZaVozila implements Runnable {
  private int mreznaVrata;
  private CentralniSustav centralniSustav;

  volatile AtomicInteger brojaktivnihDretvi = new AtomicInteger(0);
  volatile List<Future<Integer>> odgovoriZaDretve = new ArrayList<Future<Integer>>();

  private PodaciVozila podaciVozila;
  static ExecutorService izvodac;

  /**
   * Konstruktor klase PosluziteljZaVozila.
   * 
   * @param mreznaVrata Mrežna vrata na kojima poslužitelj osluškuje dolazne veze.
   * @param centralniSustav Referenca na centralni sustav koji upravlja poslužiteljem.
   */
  public PosluziteljZaVozila(int mreznaVrata, CentralniSustav centralniSustav) {
    super();
    this.mreznaVrata = mreznaVrata;
    this.centralniSustav = centralniSustav;
  }

  /**
   * Pokreće poslužitelja za vozila. Stvara novu instancu klase PosluziteljZaVozila i pokreće njegov
   * poslužitelj.
   */
  @Override
  public void run() {
    PosluziteljZaVozila posluziteljZaVozila = new PosluziteljZaVozila(mreznaVrata, centralniSustav);
    try {
      posluziteljZaVozila.pokreniPosluzitelja();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Pokreće poslužitelja za prihvaćanje veza klijenata i stvaranje radnika za svakog klijenta.
   * 
   * @throws Exception ako dođe do greške prilikom pokretanja poslužitelja.
   */
  private void pokreniPosluzitelja() throws Exception {
    final AsynchronousServerSocketChannel posluzitelj =
        AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(this.mreznaVrata));
    try {
      izvodac = Executors.newVirtualThreadPerTaskExecutor();
      while (true) {
        AsynchronousSocketChannel klijentovKanal = posluzitelj.accept().get();
        odgovoriZaDretve.add(izvodac.submit(() -> obradiKlijenta(klijentovKanal)));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    posluzitelj.close();
  }

  /**
   * Obradi klijenta stvarajući radnika za vožnje i pokretanjem njegove dretve.
   * 
   * @param klijentovKanal Asinkroni kanal za komunikaciju s klijentom.
   * @return Broj aktivnih dretvi nakon što se obrada završi.
   */
  Integer obradiKlijenta(AsynchronousSocketChannel klijentovKanal) {
    var broj = brojaktivnihDretvi.incrementAndGet();
    try {
      var obrada = new RadnikZaVozila(klijentovKanal, podaciVozila, centralniSustav);
      var dretva = Thread.startVirtualThread(obrada);
      dretva.join();
    } catch (Exception e) {
      e.printStackTrace();
    }
    brojaktivnihDretvi.decrementAndGet();
    return broj;
  }
}


