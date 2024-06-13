package edu.unizg.foi.nwtis.askarica20.vjezba_04_dz_1.posluzitelji;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.io.File;
import java.lang.reflect.Field;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.podaci.PodaciKazne;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.posluzitelji.PosluziteljKazni;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PosluziteljKazniTest {
  PosluziteljKazni posluziteljKazni;

  @BeforeAll
  static void setUpBeforeClass() throws Exception {}

  @AfterAll
  static void tearDownAfterClass() throws Exception {}

  @BeforeEach
  void setUp() throws Exception {
    posluziteljKazni = new PosluziteljKazni();
  }

  @AfterEach
  void tearDown() throws Exception {
    posluziteljKazni = null;
  }

  /*
   * @Test
   * 
   * @Order(10) void testMain() { var status = false; var mreznaVrata = 8020;
   * this.posluziteljKazni.mreznaVrata = mreznaVrata; try { InetSocketAddress isa = new
   * InetSocketAddress("localhost", this.posluziteljKazni.mreznaVrata); Socket s = new Socket();
   * s.connect(isa, 70); s.close(); } catch (Exception e) { status = true; } assertTrue(status);
   * 
   * var nazivDatoteke = "PosluziteljKazni.txt"; String[] argumenti = {nazivDatoteke};
   * this.posluziteljKazni.mreznaVrata = mreznaVrata; try { Konfiguracija k =
   * KonfiguracijaApstraktna.preuzmiKreirajKonfiguraciju(nazivDatoteke);
   * k.spremiPostavku("mreznaVrataKazne", Integer.toString(this.posluziteljKazni.mreznaVrata));
   * k.spremiKonfiguraciju(); } catch (NeispravnaKonfiguracija | NumberFormatException e) {
   * e.printStackTrace(); }
   * 
   * var dretva = Thread.ofVirtual().factory().newThread(() -> PosluziteljKazni.main(argumenti));
   * dretva.start();
   * 
   * status = true; this.posluziteljKazni.mreznaVrata = mreznaVrata; try { Thread.sleep(100);
   * InetSocketAddress isa = new InetSocketAddress("localhost", this.posluziteljKazni.mreznaVrata);
   * Socket s = new Socket(); s.connect(isa, 70); s.close(); } catch (Exception e) { status = false;
   * } assertTrue(status); dretva.interrupt(); this.obrisiDatoteku(nazivDatoteke); }
   */
  @Test
  @Order(9)
  void testPokreniPosluzitelja() {

  }

  @Test
  @Order(8)
  void testNajnovijiZapisZaIdViseRadara() throws NoSuchFieldException, IllegalAccessException {
    PosluziteljKazni posluziteljKazni = new PosluziteljKazni();
    Field field = PosluziteljKazni.class.getDeclaredField("sveKazne");
    field.setAccessible(true);
    ConcurrentLinkedQueue<PodaciKazne> sveKazne =
        (ConcurrentLinkedQueue<PodaciKazne>) field.get(posluziteljKazni);

    sveKazne.add(new PodaciKazne(1, 1618588800000L, 1618587300000L, 60.0, 45.0, 15.0, 46.0, 16.0));
    sveKazne.add(new PodaciKazne(1, 1618588900000L, 1618592401000L, 70.0, 46.0, 16.0, 47.0, 17.0));
    sveKazne.add(new PodaciKazne(2, 1618588900000L, 1618592401000L, 70.0, 46.0, 16.0, 47.0, 17.0));

    PodaciKazne zapis = posluziteljKazni.najnovijiZapisZaId(1, 1618588900000L, 1618592402000L);

    assertNotNull(zapis);
    assertEquals(1, zapis.id());
    assertEquals(1618588900000L, zapis.vrijemePocetak());
    assertEquals(1618592401000L, zapis.vrijemeKraj());
    assertEquals(70.0, zapis.brzina());
    assertEquals(46.0, zapis.gpsSirina());
    assertEquals(16.0, zapis.gpsDuzina());
    assertEquals(47.0, zapis.gpsSirinaRadar());
    assertEquals(17.0, zapis.gpsDuzinaRadar());
  }

  @Test
  @Order(7)
  void testNajnovijiZapisZaId() throws NoSuchFieldException, IllegalAccessException {
    PosluziteljKazni posluziteljKazni = new PosluziteljKazni();
    Field field = PosluziteljKazni.class.getDeclaredField("sveKazne");
    field.setAccessible(true);
    ConcurrentLinkedQueue<PodaciKazne> sveKazne =
        (ConcurrentLinkedQueue<PodaciKazne>) field.get(posluziteljKazni);

    sveKazne.add(new PodaciKazne(1, 1618588800000L, 1618587300000L, 60.0, 45.0, 15.0, 46.0, 16.0));
    sveKazne.add(new PodaciKazne(1, 1618588900000L, 1618592401000L, 70.0, 46.0, 16.0, 47.0, 17.0));

    PodaciKazne zapis = posluziteljKazni.najnovijiZapisZaId(1, 1618588900000L, 1618592402000L);

    assertNotNull(zapis);
    assertEquals(1, zapis.id());
    assertEquals(1618588900000L, zapis.vrijemePocetak());
    assertEquals(1618592401000L, zapis.vrijemeKraj());
    assertEquals(70.0, zapis.brzina());
    assertEquals(46.0, zapis.gpsSirina());
    assertEquals(16.0, zapis.gpsDuzina());
    assertEquals(47.0, zapis.gpsSirinaRadar());
    assertEquals(17.0, zapis.gpsDuzinaRadar());
  }


  @Test
  @Order(6)
  void testObradaZahtjevaVozilo() throws NoSuchFieldException, IllegalAccessException {
    PosluziteljKazni posluziteljKazni = new PosluziteljKazni();
    Field field = PosluziteljKazni.class.getDeclaredField("sveKazne");
    field.setAccessible(true);
    ConcurrentLinkedQueue<PodaciKazne> sveKazne =
        (ConcurrentLinkedQueue<PodaciKazne>) field.get(posluziteljKazni);

    sveKazne.add(new PodaciKazne(1, 1618588800000L, 1618592400000L, 60.0, 45.0, 15.0, 46.0, 16.0));
    sveKazne.add(new PodaciKazne(2, 1618588800000L, 1618592400000L, 70.0, 46.0, 16.0, 47.0, 17.0));

    String zahtjev = "VOZILO 1 1618588800000 1618592400000";
    String odgovor = posluziteljKazni.obradaZahtjevaVozilo(zahtjev);

    assertEquals("OK 1618592400000 60.0 46.0 16.0", odgovor);
  }


  @Test
  @Order(5)
  void testObradaZahtjevaStatistika() throws NoSuchFieldException, IllegalAccessException {
    PosluziteljKazni posluziteljKazni = new PosluziteljKazni();
    Field field = PosluziteljKazni.class.getDeclaredField("sveKazne");
    field.setAccessible(true);
    ConcurrentLinkedQueue<PodaciKazne> sveKazne =
        (ConcurrentLinkedQueue<PodaciKazne>) field.get(posluziteljKazni);
    sveKazne.add(new PodaciKazne(1, 1618588800000L, 1618592400000L, 60.0, 45.0, 15.0, 46.0, 16.0));
    sveKazne.add(new PodaciKazne(2, 1618588800000L, 1618592400000L, 70.0, 46.0, 16.0, 47.0, 17.0));

    String zahtjev = "STATISTIKA 1618588800000 1618592400000";
    String odgovor = posluziteljKazni.obradaZahtjevaStatistika(zahtjev);

    assertEquals("OK 1 1; 2 1; ", odgovor);
  }

  /*
   * @Test
   * 
   * @Order(4) void testObradaZahtjeva() { PosluziteljKazni posluziteljKazni = new
   * PosluziteljKazni();
   * 
   * String zahtjevNeispravnaSintaksa = "KRIVA KOMANDA 15.0"; String odgovorNeispravnaSintaksa =
   * posluziteljKazni.obradaZahtjeva(zahtjevNeispravnaSintaksa);
   * assertEquals("ERROR 40 Neispravna sintaksa komande.", odgovorNeispravnaSintaksa);
   * 
   * String zahtjevKazna =
   * "VOZILO 1 1708074572040 1708074580410 17.383 46.306103 16.33664 46.2995 16.33001"; String
   * odgovorKazna = posluziteljKazni.obradaZahtjeva(zahtjevKazna); assertEquals("OK", odgovorKazna);
   * }
   */
  @Test
  @Order(3)
  void testObradaZahtjevaKaznaKrivaKomanda() {
    PosluziteljKazni posluziteljKazni = new PosluziteljKazni();

    String zahtjev = "KAZNA 1 1618588800000 1618592400000 60.0 45.0 15.0 46.0 16.0";
    String odgovor = posluziteljKazni.obradaZahtjevaKazna(zahtjev);

    assertNull(odgovor);
  }

  /*
   * @Test
   * 
   * @Order(2) void testObradaZahtjevaKazna() { PosluziteljKazni posluziteljKazni = new
   * PosluziteljKazni();
   * 
   * String zahtjev =
   * "VOZILO 1 1708074572040 1708074580410 17.383 46.306103 16.33664 46.2995 16.33001"; String
   * odgovor = posluziteljKazni.obradaZahtjevaKazna(zahtjev);
   * 
   * assertNotNull(odgovor); assertEquals("OK", odgovor); }
   */
  @Test
  @Order(1)
  void testPreuzmiPostavke() {
    var nazivDatoteke = "PosluziteljKazni.txt";
    try {
      Konfiguracija k = KonfiguracijaApstraktna.preuzmiKreirajKonfiguraciju(nazivDatoteke);
      k.spremiPostavku("mreznaVrataKazne", "8020");
      k.spremiKonfiguraciju();
      String[] argumenti = {nazivDatoteke};
      this.posluziteljKazni.preuzmiPostavke(argumenti);
      assertEquals(Integer.valueOf(k.dajPostavku("mreznaVrataKazne")).intValue(),
          this.posluziteljKazni.mreznaVrata);
    } catch (NeispravnaKonfiguracija | NumberFormatException | UnknownHostException e) {
      e.printStackTrace();
    }
    this.obrisiDatoteku(nazivDatoteke);
  }

  private boolean obrisiDatoteku(String nazivDatoteke) {
    File f = new File(nazivDatoteke);

    if (!f.exists()) {
      return true;
    } else if (f.exists() && f.isFile()) {
      f.delete();
      if (!f.exists()) {
        return true;
      }
    }
    return false;
  }
}
