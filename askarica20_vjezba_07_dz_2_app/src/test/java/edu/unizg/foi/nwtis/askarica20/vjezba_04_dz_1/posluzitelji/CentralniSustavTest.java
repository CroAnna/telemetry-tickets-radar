package edu.unizg.foi.nwtis.askarica20.vjezba_04_dz_1.posluzitelji;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.File;
import java.net.UnknownHostException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.posluzitelji.CentralniSustav;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

class CentralniSustavTest {
  CentralniSustav centralniSustav;

  @BeforeAll
  static void setUpBeforeClass() throws Exception {}

  @AfterAll
  static void tearDownAfterClass() throws Exception {}

  @BeforeEach
  void setUp() throws Exception {
    centralniSustav = new CentralniSustav();
  }

  @AfterEach
  void tearDown() throws Exception {}

  @Test
  @Order(3)
  void testMainArgumenata() {
    String[] args1 = {};
    CentralniSustav.main(args1);
    assertTrue(true);
  }

  @Test
  @Order(2)
  void testPokreniPosluzitelja() {}


  @Test
  @Order(1)
  void testPreuzmiPostavke() {
    var nazivDatoteke = "CentralniSustav.txt";
    try {
      Konfiguracija k = KonfiguracijaApstraktna.preuzmiKreirajKonfiguraciju(nazivDatoteke);
      k.spremiPostavku("mreznaVrataRadara", "8000");
      k.spremiPostavku("mreznaVrataVozila", "8001");
      k.spremiPostavku("mreznaVrataNadzora", "8002");
      k.spremiPostavku("maksVozila", "5");

      k.spremiKonfiguraciju();
      String[] argumenti = {nazivDatoteke};
      this.centralniSustav.preuzmiPostavke(argumenti);

      assertEquals(Integer.valueOf(k.dajPostavku("mreznaVrataRadara")).intValue(),
          this.centralniSustav.mreznaVrataRadara);
      assertEquals(Integer.valueOf(k.dajPostavku("mreznaVrataVozila")).intValue(),
          this.centralniSustav.mreznaVrataVozila);
      assertEquals(Integer.valueOf(k.dajPostavku("mreznaVrataNadzora")).intValue(),
          this.centralniSustav.mreznaVrataNadzora);
      assertEquals(Integer.valueOf(k.dajPostavku("maksVozila")).intValue(),
          this.centralniSustav.maksVozila);
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
