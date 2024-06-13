package edu.unizg.foi.nwtis.askarica20.vjezba_04_dz_1.posluzitelji.radnici;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.posluzitelji.radnici.RadnikZaRadare;

class RadnikZaRadareTest {
  RadnikZaRadare radnikZaRadare;

  @BeforeAll
  static void setUpBeforeClass() throws Exception {}

  @AfterAll
  static void tearDownAfterClass() throws Exception {}

  @BeforeEach
  void setUp() throws Exception {
    radnikZaRadare = new RadnikZaRadare(null, null, null);
  }

  @AfterEach
  void tearDown() throws Exception {}

  @Test
  void testRadnikZaRadare() {}

  @Test
  void testRun() {}
  /*
   * @Test
   * 
   * @Order(1) void testObradaZahtjevaBrzineIspravanZahtjev() { PodaciRadara podaciRadara = new
   * PodaciRadara(1, "localhost", 8080, 100, 120, 200, "localhost", 8081, "localhost", 8082,
   * "Adresa 1", 45.815006, 15.981918);
   * 
   * RadnikZaRadare radnik = new RadnikZaRadare(null, podaciRadara);
   * 
   * String zahtjev = "VOZILO 1 1623061600 100 45.815006 15.981918"; String odgovor =
   * radnik.obradaZahtjevaBrzine(zahtjev); assertEquals("OK", odgovor); }
   * 
   * @Test
   * 
   * @Order(2) void testObradaZahtjevaBrzinePrazanZahtjev() { PodaciRadara podaciRadara = new
   * PodaciRadara(1, "localhost", 8080, 100, 120, 200, "localhost", 8081, "localhost", 8082,
   * "Adresa 1", 45.815006, 15.981918);
   * 
   * RadnikZaRadare radnik = new RadnikZaRadare(null, podaciRadara);
   * 
   * String zahtjev = ""; String odgovor = radnik.obradaZahtjevaBrzine(zahtjev);
   * assertNull(odgovor); }
   * 
   * @Test
   * 
   * @Order(3) void testObradaZahtjevaBrzineNeispravanZahtjev() { PodaciRadara podaciRadara = new
   * PodaciRadara(1, "localhost", 8080, 100, 120, 200, "localhost", 8081, "localhost", 8082,
   * "Adresa 1", 45.815006, 15.981918);
   * 
   * RadnikZaRadare radnik = new RadnikZaRadare(null, podaciRadara);
   * 
   * String zahtjev = "KRIVO 1 1623061600 100 -5 15.981918"; String odgovor =
   * radnik.obradaZahtjevaBrzine(zahtjev); assertNull(odgovor); }
   * 
   * @Test
   * 
   * @Order(4) void testObradaZahtjevaBrzinePrekoracenjeBrzine() { PodaciRadara podaciRadara = new
   * PodaciRadara(1, "adresaRadara", 8000, 120, 3600, 100, "adresaRegistracije", 8001,
   * "adresaKazne", 8002, "postanskaAdresaRadara", 45.815006, 15.981918);
   * 
   * RadnikZaRadare radnik = new RadnikZaRadare(null, podaciRadara);
   * 
   * String zahtjev = "VOZILO 1 1623061600 130 45.815006 15.981918"; String odgovor =
   * radnik.obradaZahtjevaBrzine(zahtjev); assertEquals("OK", odgovor); }
   * 
   * @Test
   * 
   * @Order(5) void testObradaZahtjevaBrzineNeprekoracenjeBrzine() { PodaciRadara podaciRadara = new
   * PodaciRadara(1, "adresaRadara", 8000, 120, 3600, 100, "adresaRegistracije", 8001,
   * "adresaKazne", 8002, "postanskaAdresaRadara", 45.815006, 15.981918);
   * 
   * RadnikZaRadare radnik = new RadnikZaRadare(null, podaciRadara);
   * 
   * String zahtjev = "VOZILO 1 1623061600 100 45.815006 15.981918"; String odgovor =
   * radnik.obradaZahtjevaBrzine(zahtjev); assertEquals("OK", odgovor); }
   * 
   * @Test
   * 
   * @Order(6) void testPrekoracenjeBrzine() { PodaciRadara podaciRadara = new PodaciRadara(1,
   * "localhost", 8080, 100, 120, 200, "localhost", 8081, "localhost", 8082, "Adresa 1", 45.815006,
   * 15.981918);
   * 
   * RadnikZaRadare radnik = new RadnikZaRadare(null, podaciRadara);
   * 
   * assertTrue(radnik.provjeraPrekoracenjaBrzine(150.0)); }
   * 
   * @Test
   * 
   * @Order(7) void testBezPrekoracenjaBrzine() { PodaciRadara podaciRadara = new PodaciRadara(1,
   * "localhost", 8080, 100, 120, 200, "localhost", 8081, "localhost", 8082, "Adresa 1", 45.815006,
   * 15.981918);
   * 
   * RadnikZaRadare radnik = new RadnikZaRadare(null, podaciRadara);
   * 
   * assertFalse(radnik.provjeraPrekoracenjaBrzine(100.0)); }
   * 
   * @Test
   * 
   * @Order(8) void testRazlikeVremena() { RadnikZaRadare radnik = new RadnikZaRadare(null, null);
   * 
   * long vrijemeBrzogVozila = 1000000L; long vrijemeTrenutnogVozila = 1060000L; double rezultat =
   * radnik.dajRazlikuVremena(vrijemeBrzogVozila, vrijemeTrenutnogVozila); assertEquals(60.0,
   * rezultat); }
   * 
   * @Test
   * 
   * @Order(9) void testPripremeKomandeZaPosluziteljaKazni() { PodaciRadara podaciRadara = new
   * PodaciRadara(1, "localhost", 8080, 100, 120, 200, "localhost", 8081, "localhost", 8082,
   * "Adresa 1", 45.815006, 15.981918);
   * 
   * RadnikZaRadare radnik = new RadnikZaRadare(null, podaciRadara); radnik.brzoVozilo = new
   * BrzoVozilo(1, 100, 1623061600, 120.0, 45.815006, 15.981918, true);
   * 
   * long vrijemePocetak = 1623061600L; long vrijemeKraj = 1623061800L; String ocekivanaKomanda =
   * "VOZILO 1 1623061600 1623061800 120.0 45.815006 15.981918 45.815006 15.981918"; String rezultat
   * = radnik.pripremiKomanduZaPosluziteljaKazni(vrijemePocetak, vrijemeKraj);
   * assertEquals(ocekivanaKomanda, rezultat); }
   */
}
