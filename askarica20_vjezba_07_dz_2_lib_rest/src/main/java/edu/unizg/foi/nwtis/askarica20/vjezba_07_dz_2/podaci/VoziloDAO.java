package edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.podaci;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Klasa VoziloDAO za rad s tablicom Voznje
 *
 * @author Ana Skarica
 */
public class VoziloDAO {

  /** Veza na bazu podataka. */
  private Connection vezaBP;

  /**
   * Instancira novi vozila DAO.
   *
   * @param vezaBP veza na bazu podataka
   */
  public VoziloDAO(Connection vezaBP) {
    super();
    this.vezaBP = vezaBP;
  }

  /**
   * Dohvati sva vozila.
   *
   * @return lista vozila
   */
  public List<Vozilo> dohvatiSvaVozila() {
    String upit =
        "SELECT id, broj, vrijeme, brzina, snaga, struja, visina, gpsBrzina, tempVozila, postotakBaterija, naponBaterija, kapacitetBaterija, tempBaterija, preostaloKm, ukupnoKm, gpsSirina, gpsDuzina "
            + "FROM voznje"; // valjda je to ta tablica - voznje

    List<Vozilo> vozila = new ArrayList<>();

    try (PreparedStatement s = this.vezaBP.prepareStatement(upit)) {
      ResultSet rs = s.executeQuery();

      while (rs.next()) {
        var vozilo = kreirajObjektVozilo(rs);
        vozila.add(vozilo);
      }
    } catch (SQLException ex) {
      Logger.getLogger(VoziloDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return vozila;
  }

  /**
   * Dohvati sva vozila u zadanom vremenskom intervalu.
   *
   * @param odVremena početno vrijeme
   * @param doVremena krajnje vrijeme
   * @return lista vozila
   */
  public List<Vozilo> dohvatiSvaVozilaUIntervalu(long odVremena, long doVremena) {
    String upit =
        "SELECT id, broj, vrijeme, brzina, snaga, struja, visina, gpsBrzina, tempVozila, postotakBaterija, naponBaterija, kapacitetBaterija, tempBaterija, preostaloKm, ukupnoKm, gpsSirina, gpsDuzina "
            + "FROM voznje WHERE vrijeme >= ? AND vrijeme <= ?";
    List<Vozilo> vozila = new ArrayList<>();

    try (PreparedStatement s = this.vezaBP.prepareStatement(upit)) {
      s.setLong(1, odVremena);
      s.setLong(2, doVremena);
      ResultSet rs = s.executeQuery();

      while (rs.next()) {
        var voznja = kreirajObjektVozilo(rs);
        vozila.add(voznja);
      }
    } catch (SQLException ex) {
      Logger.getLogger(KaznaDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return vozila;
  }

  /**
   * Dodaj voznju/vozilo.
   *
   * @param vozilo vozilo
   * @return true, ako je uspješno dodavanje
   */
  public boolean dodajVozilo(Vozilo vozilo) {
    String upit =
        "INSERT INTO voznje (id, broj, vrijeme, brzina, snaga, struja, visina, gpsBrzina, tempVozila, postotakBaterija, naponBaterija, kapacitetBaterija, tempBaterija, preostaloKm, ukupnoKm, gpsSirina, gpsDuzina) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (PreparedStatement s = this.vezaBP.prepareStatement(upit)) {

      s.setInt(1, vozilo.getId());
      s.setLong(2, vozilo.getBroj());
      s.setLong(3, vozilo.getVrijeme());
      s.setDouble(4, vozilo.getBrzina());
      s.setDouble(5, vozilo.getSnaga());
      s.setDouble(6, vozilo.getStruja());
      s.setDouble(7, vozilo.getVisina());
      s.setDouble(8, vozilo.getGpsBrzina());
      s.setDouble(9, vozilo.getTempVozila());
      s.setDouble(10, vozilo.getPostotakBaterija());
      s.setDouble(11, vozilo.getNaponBaterija());
      s.setDouble(12, vozilo.getKapacitetBaterija());
      s.setDouble(13, vozilo.getTempBaterija());
      s.setDouble(14, vozilo.getPreostaloKm());
      s.setDouble(15, vozilo.getUkupnoKm());
      s.setDouble(16, vozilo.getGpsSirina());
      s.setDouble(17, vozilo.getGpsDuzina());

      int brojAzuriranja = s.executeUpdate();

      return brojAzuriranja == 1;

    } catch (Exception ex) {
      System.out.println(ex);
      Logger.getLogger(VoziloDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return false;
  }

  /**
   * Dohvati vožnje za vozilo po ID-u.
   *
   * @param id identifikator vozila
   * @return lista vožnji za vozilo
   */
  public List<Vozilo> dohvatiVoznjeZaVozilo(int id) {
    String upit =
        "SELECT id, broj, vrijeme, brzina, snaga, struja, visina, gpsBrzina, tempVozila, postotakBaterija, naponBaterija, kapacitetBaterija, tempBaterija, preostaloKm, ukupnoKm, gpsSirina, gpsDuzina "
            + "FROM voznje WHERE id = ?";
    List<Vozilo> vozila = new ArrayList<>();

    try (PreparedStatement s = this.vezaBP.prepareStatement(upit)) {
      s.setInt(1, id);
      ResultSet rs = s.executeQuery();

      while (rs.next()) {
        var voznja = kreirajObjektVozilo(rs);
        vozila.add(voznja);
      }
    } catch (SQLException ex) {
      Logger.getLogger(VoziloDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return vozila;
  }

  /**
   * Dohvati vožnje za vozilo u zadanom vremenskom intervalu.
   *
   * @param id identifikator vozila
   * @param odVremena početno vrijeme
   * @param doVremena krajnje vrijeme
   * @return lista vožnji za vozilo
   */
  public List<Vozilo> dohvatiVoznjeZaVoziloUIntervalu(int id, long odVremena, long doVremena) {
    String upit =
        "SELECT id, broj, vrijeme, brzina, snaga, struja, visina, gpsBrzina, tempVozila, postotakBaterija, naponBaterija, kapacitetBaterija, tempBaterija, preostaloKm, ukupnoKm, gpsSirina, gpsDuzina "
            + "FROM voznje WHERE id = ? AND vrijeme >= ? AND vrijeme <= ?";
    List<Vozilo> vozila = new ArrayList<>();

    try (PreparedStatement s = this.vezaBP.prepareStatement(upit)) {
      s.setInt(1, id);
      s.setLong(2, odVremena);
      s.setLong(3, doVremena);
      ResultSet rs = s.executeQuery();

      while (rs.next()) {
        var voznja = kreirajObjektVozilo(rs);
        vozila.add(voznja);
      }
    } catch (SQLException ex) {
      Logger.getLogger(KaznaDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return vozila;
  }

  /**
   * Kreira objekt Vozilo.
   *
   * @param rs rezultat upita
   * @return objekt Vozilo
   * @throws SQLException ako dođe do pogreške pri čitanju podataka iz ResultSet-a
   */
  private Vozilo kreirajObjektVozilo(ResultSet rs) throws SQLException {
    int id = rs.getInt("id");
    int broj = rs.getInt("broj");
    long vrijeme = rs.getLong("vrijeme");
    double brzina = rs.getDouble("brzina");
    double snaga = rs.getDouble("snaga");
    double struja = rs.getDouble("struja");
    double visina = rs.getDouble("visina");
    double gpsBrzina = rs.getDouble("gpsBrzina");
    int tempVozila = rs.getInt("tempVozila");
    int postotakBaterija = rs.getInt("postotakBaterija");
    double naponBaterija = rs.getDouble("naponBaterija");
    int kapacitetBaterija = rs.getInt("kapacitetBaterija");
    int tempBaterija = rs.getInt("tempBaterija");
    double preostaloKm = rs.getDouble("preostaloKm");
    double ukupnoKm = rs.getDouble("ukupnoKm");
    double gpsSirina = rs.getDouble("gpsSirina");
    double gpsDuzina = rs.getDouble("gpsDuzina");

    Vozilo v = new Vozilo(id, broj, vrijeme, brzina, snaga, struja, visina, gpsBrzina, tempVozila,
        postotakBaterija, naponBaterija, kapacitetBaterija, tempBaterija, preostaloKm, ukupnoKm,
        gpsSirina, gpsDuzina);
    return v;
  }

}
