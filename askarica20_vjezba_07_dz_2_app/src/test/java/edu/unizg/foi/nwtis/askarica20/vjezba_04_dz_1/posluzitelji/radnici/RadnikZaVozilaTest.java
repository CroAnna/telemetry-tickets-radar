package edu.unizg.foi.nwtis.askarica20.vjezba_04_dz_1.posluzitelji.radnici;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.lang.reflect.Method;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.posluzitelji.radnici.RadnikZaVozila;

class RadnikZaVozilaTest {
  RadnikZaVozila radnikZaVozila;

  @BeforeAll
  static void setUpBeforeClass() throws Exception {}

  @AfterAll
  static void tearDownAfterClass() throws Exception {}

  @BeforeEach
  void setUp() throws Exception {
    radnikZaVozila = new RadnikZaVozila(null, null, null);
  }

  @AfterEach
  void tearDown() throws Exception {}

  @Test
  void testRadnikZaVozila() {}

  @Test
  void testRun() {}

  /*
   * @Test
   * 
   * @Order(4) void testObradaZahtjevaNeispravanZahtjev() { RadnikZaVozila radnik = new
   * RadnikZaVozila(null, null, null); assertEquals("ERROR 20 Neispravna sintaksa komande.",
   * radnik.obradaZahtjeva("KRIVAKOMANDA 15 11.0 18484651521 151 54 7.1")); }
   */

  @Test
  @Order(3)
  void testObradaZahtjevaIspravanZahtjevNepokrenutCentralni() {
    RadnikZaVozila radnik = new RadnikZaVozila(null, null, null);

    assertThrows(NullPointerException.class, () -> {
      radnik.obradaZahtjeva(
          "VOZILO 1 1144 1708074177916 19.316 556.4432 15.22 217.2 4.968693 22 75 36.56 5911 25 22.5 818.932 46.29052 16.345083");
    });
  }

  @Test
  @Order(2)
  void testProvjeriJeLiVoziloURadaruUDometu() {
    double gpsSirinaRadara = 45.815399;
    double gpsDuzinaRadara = 15.966568;
    int maksUdaljenost = 100;

    RadnikZaVozila radnikZaVozila = new RadnikZaVozila(null, null, null);

    try {
      Class<?> radnikClass = RadnikZaVozila.class;
      Method metoda = radnikClass.getDeclaredMethod("provjeriJeLiVoziloURadaru", double.class,
          double.class, int.class);
      metoda.setAccessible(true);

      boolean rezultat =
          (boolean) metoda.invoke(radnikZaVozila, gpsSirinaRadara, gpsDuzinaRadara, maksUdaljenost);

      assertEquals(true, rezultat);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @Order(1)
  void testProvjeriJeLiVoziloURadaruIzvanDometa() {
    double gpsSirinaRadara = 45.812399;
    double gpsDuzinaRadara = 15.969568;
    int maksUdaljenost = 100;

    RadnikZaVozila radnikZaVozila = new RadnikZaVozila(null, null, null);

    try {
      Class<?> radnikClass = RadnikZaVozila.class;
      Method metoda = radnikClass.getDeclaredMethod("provjeriJeLiVoziloURadaru", double.class,
          double.class, int.class);
      metoda.setAccessible(true);

      boolean rezultat =
          (boolean) metoda.invoke(radnikZaVozila, gpsSirinaRadara, gpsDuzinaRadara, maksUdaljenost);

      assertEquals(false, rezultat);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
