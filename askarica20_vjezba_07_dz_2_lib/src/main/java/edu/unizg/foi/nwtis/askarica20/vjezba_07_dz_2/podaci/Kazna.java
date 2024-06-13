package edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.podaci;

/**
 * Klasa koja predstavlja kaznu za prekršaj brzine.
 */
public class Kazna {
  private int id;
  private long vrijemePocetak;
  private long vrijemeKraj;
  private double brzina;
  private double gpsSirina;
  private double gpsDuzina;
  private double gpsSirinaRadar;
  private double gpsDuzinaRadar;

  /**
   * Konstruktor bez parametara.
   */
  public Kazna() {
    super();
  }

  /**
   * Konstruktor s parametrima.
   *
   * @param id Jedinstveni identifikator kazne.
   * @param vrijemePocetak Vrijeme početka prekršaja u milisekundama.
   * @param vrijemeKraj Vrijeme kraja prekršaja u milisekundama.
   * @param brzina Brzina vozila u trenutku prekršaja.
   * @param gpsSirina GPS širina lokacije prekršaja.
   * @param gpsDuzina GPS dužina lokacije prekršaja.
   * @param gpsSirinaRadar GPS širina lokacije radara koji je zabilježio prekršaj.
   * @param gpsDuzinaRadar GPS dužina lokacije radara koji je zabilježio prekršaj.
   */
  public Kazna(int id, long vrijemePocetak, long vrijemeKraj, double brzina, double gpsSirina,
      double gpsDuzina, double gpsSirinaRadar, double gpsDuzinaRadar) {
    super();
    this.id = id;
    this.vrijemePocetak = vrijemePocetak;
    this.vrijemeKraj = vrijemeKraj;
    this.brzina = brzina;
    this.gpsSirina = gpsSirina;
    this.gpsDuzina = gpsDuzina;
    this.gpsSirinaRadar = gpsSirinaRadar;
    this.gpsDuzinaRadar = gpsDuzinaRadar;
  }

  /**
   * Dohvaća identifikator kazne.
   *
   * @return Identifikator kazne.
   */
  public int getId() {
    return id;
  }

  /**
   * Postavlja identifikator kazne.
   *
   * @param id Identifikator kazne.
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Dohvaća vrijeme početka prekršaja.
   *
   * @return Vrijeme početka prekršaja.
   */
  public long getVrijemePocetak() {
    return vrijemePocetak;
  }

  /**
   * Postavlja vrijeme početka prekršaja.
   *
   * @param vrijemePocetak Vrijeme početka prekršaja.
   */
  public void setVrijemePocetak(long vrijemePocetak) {
    this.vrijemePocetak = vrijemePocetak;
  }

  /**
   * Dohvaća vrijeme kraja prekršaja.
   *
   * @return Vrijeme kraja prekršaja u milisekundama.
   */
  public long getVrijemeKraj() {
    return vrijemeKraj;
  }

  /**
   * Postavlja vrijeme kraja prekršaja.
   *
   * @param vrijemeKraj Vrijeme kraja prekršaja u milisekundama.
   */
  public void setVrijemeKraj(long vrijemeKraj) {
    this.vrijemeKraj = vrijemeKraj;
  }

  /**
   * Dohvaća brzinu vozila u trenutku prekršaja.
   *
   * @return Brzina vozila.
   */
  public double getBrzina() {
    return brzina;
  }

  /**
   * Postavlja brzinu vozila u trenutku prekršaja.
   *
   * @param brzina Brzina vozila.
   */
  public void setBrzina(double brzina) {
    this.brzina = brzina;
  }

  /**
   * Dohvaća GPS širinu lokacije prekršaja.
   *
   * @return GPS širina.
   */
  public double getGpsSirina() {
    return gpsSirina;
  }

  /**
   * Postavlja GPS širinu lokacije prekršaja.
   *
   * @param gpsSirina GPS širina.
   */
  public void setGpsSirina(double gpsSirina) {
    this.gpsSirina = gpsSirina;
  }

  /**
   * Dohvaća GPS dužinu lokacije prekršaja.
   *
   * @return GPS dužina.
   */
  public double getGpsDuzina() {
    return gpsDuzina;
  }

  /**
   * Postavlja GPS dužinu lokacije prekršaja.
   *
   * @param gpsDuzina GPS dužina.
   */
  public void setGpsDuzina(double gpsDuzina) {
    this.gpsDuzina = gpsDuzina;
  }

  /**
   * Dohvaća GPS širinu lokacije radara koji je zabilježio prekršaj.
   *
   * @return GPS širina radara.
   */
  public double getGpsSirinaRadar() {
    return gpsSirinaRadar;
  }

  /**
   * Postavlja GPS širinu lokacije radara koji je zabilježio prekršaj.
   *
   * @param gpsSirinaRadar GPS širina radara.
   */
  public void setGpsSirinaRadar(double gpsSirinaRadar) {
    this.gpsSirinaRadar = gpsSirinaRadar;
  }

  /**
   * Dohvaća GPS dužinu lokacije radara koji je zabilježio prekršaj.
   *
   * @return GPS dužina radara.
   */
  public double getGpsDuzinaRadar() {
    return gpsDuzinaRadar;
  }

  /**
   * Postavlja GPS dužinu lokacije radara koji je zabilježio prekršaj.
   *
   * @param gpsDuzinaRadar GPS dužina radara.
   */
  public void setGpsDuzinaRadar(double gpsDuzinaRadar) {
    this.gpsDuzinaRadar = gpsDuzinaRadar;
  }
}
