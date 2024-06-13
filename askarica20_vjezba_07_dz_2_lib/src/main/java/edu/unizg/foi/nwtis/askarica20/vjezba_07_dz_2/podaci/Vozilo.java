package edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.podaci;

/**
 * Klasa koja predstavlja vozilo.
 */
public class Vozilo {
  private int id;
  private int broj;
  private long vrijeme;
  private double brzina;
  private double snaga;
  private double struja;
  private double visina;
  private double gpsBrzina;
  private int tempVozila;
  private int postotakBaterija;
  private double naponBaterija;
  private int kapacitetBaterija;
  private int tempBaterija;
  private double preostaloKm;
  private double ukupnoKm;
  private double gpsSirina;
  private double gpsDuzina;

  /**
   * Konstruktor bez parametara.
   */
  public Vozilo() {
    super();
  }

  /**
   * Konstruktor s parametrima.
   *
   * @param id Jedinstveni identifikator vozila.
   * @param broj Broj vozila.
   * @param vrijeme Vrijeme kada su podaci zabilježeni.
   * @param brzina Brzina vozila.
   * @param snaga Snaga vozila.
   * @param struja Struja vozila.
   * @param visina Visina vozila.
   * @param gpsBrzina GPS brzina vozila.
   * @param tempVozila Temperatura vozila.
   * @param postotakBaterija Postotak napunjenosti baterije.
   * @param naponBaterija Napon baterije.
   * @param kapacitetBaterija Kapacitet baterije.
   * @param tempBaterija Temperatura baterije.
   * @param preostaloKm Preostala kilometraža vozila.
   * @param ukupnoKm Ukupna kilometraža vozila.
   * @param gpsSirina GPS širina lokacije vozila.
   * @param gpsDuzina GPS dužina lokacije vozila.
   */
  public Vozilo(int id, int broj, long vrijeme, double brzina, double snaga, double struja,
      double visina, double gpsBrzina, int tempVozila, int postotakBaterija, double naponBaterija,
      int kapacitetBaterija, int tempBaterija, double preostaloKm, double ukupnoKm,
      double gpsSirina, double gpsDuzina) {
    super();
    this.id = id;
    this.broj = broj;
    this.vrijeme = vrijeme;
    this.brzina = brzina;
    this.snaga = snaga;
    this.struja = struja;
    this.visina = visina;
    this.gpsBrzina = gpsBrzina;
    this.tempVozila = tempVozila;
    this.postotakBaterija = postotakBaterija;
    this.naponBaterija = naponBaterija;
    this.kapacitetBaterija = kapacitetBaterija;
    this.tempBaterija = tempBaterija;
    this.preostaloKm = preostaloKm;
    this.ukupnoKm = ukupnoKm;
    this.gpsSirina = gpsSirina;
    this.gpsDuzina = gpsDuzina;
  }

  /**
   * Dohvaća identifikator vozila.
   *
   * @return Identifikator vozila.
   */
  public int getId() {
    return id;
  }

  /**
   * Postavlja identifikator vozila.
   *
   * @param id Identifikator vozila.
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Dohvaća broj vozila.
   *
   * @return Broj vozila.
   */
  public int getBroj() {
    return broj;
  }

  /**
   * Postavlja broj vozila.
   *
   * @param broj Broj vozila.
   */
  public void setBroj(int broj) {
    this.broj = broj;
  }

  /**
   * Dohvaća vrijeme kada su podaci zabilježeni.
   *
   * @return Vrijeme kada su podaci zabilježeni.
   */
  public long getVrijeme() {
    return vrijeme;
  }

  /**
   * Postavlja vrijeme kada su podaci zabilježeni.
   *
   * @param vrijeme Vrijeme kada su podaci zabilježeni.
   */
  public void setVrijeme(long vrijeme) {
    this.vrijeme = vrijeme;
  }

  /**
   * Dohvaća brzinu vozila.
   *
   * @return Brzina vozila.
   */
  public double getBrzina() {
    return brzina;
  }

  /**
   * Postavlja brzinu vozila.
   *
   * @param brzina Brzina vozila.
   */
  public void setBrzina(double brzina) {
    this.brzina = brzina;
  }

  /**
   * Dohvaća snagu vozila.
   *
   * @return Snaga vozila.
   */
  public double getSnaga() {
    return snaga;
  }

  /**
   * Postavlja snagu vozila.
   *
   * @param snaga Snaga vozila.
   */
  public void setSnaga(double snaga) {
    this.snaga = snaga;
  }

  /**
   * Dohvaća struju vozila.
   *
   * @return Struja vozila.
   */
  public double getStruja() {
    return struja;
  }

  /**
   * Postavlja struju vozila.
   *
   * @param struja Struja vozila.
   */
  public void setStruja(double struja) {
    this.struja = struja;
  }

  /**
   * Dohvaća visinu vozila.
   *
   * @return Visina vozila.
   */
  public double getVisina() {
    return visina;
  }

  /**
   * Postavlja visinu vozila.
   *
   * @param visina Visina vozila.
   */
  public void setVisina(double visina) {
    this.visina = visina;
  }

  /**
   * Dohvaća GPS brzinu vozila.
   *
   * @return GPS brzina vozila.
   */
  public double getGpsBrzina() {
    return gpsBrzina;
  }

  /**
   * Postavlja GPS brzinu vozila.
   *
   * @param gpsBrzina GPS brzina vozila.
   */
  public void setGpsBrzina(double gpsBrzina) {
    this.gpsBrzina = gpsBrzina;
  }

  /**
   * Dohvaća temperaturu vozila.
   *
   * @return Temperatura vozila.
   */
  public int getTempVozila() {
    return tempVozila;
  }

  /**
   * Postavlja temperaturu vozila.
   *
   * @param tempVozila Temperatura vozila.
   */
  public void setTempVozila(int tempVozila) {
    this.tempVozila = tempVozila;
  }

  /**
   * Dohvaća postotak napunjenosti baterije.
   *
   * @return Postotak napunjenosti baterije.
   */
  public int getPostotakBaterija() {
    return postotakBaterija;
  }

  /**
   * Postavlja postotak napunjenosti baterije.
   *
   * @param postotakBaterija Postotak napunjenosti baterije.
   */
  public void setPostotakBaterija(int postotakBaterija) {
    this.postotakBaterija = postotakBaterija;
  }

  /**
   * Dohvaća napon baterije.
   *
   * @return Napon baterije.
   */
  public double getNaponBaterija() {
    return naponBaterija;
  }

  /**
   * Postavlja napon baterije.
   *
   * @param naponBaterija Napon baterije.
   */
  public void setNaponBaterija(double naponBaterija) {
    this.naponBaterija = naponBaterija;
  }

  /**
   * Dohvaća kapacitet baterije.
   *
   * @return Kapacitet baterije.
   */
  public int getKapacitetBaterija() {
    return kapacitetBaterija;
  }

  /**
   * Postavlja kapacitet baterije.
   *
   * @param kapacitetBaterija Kapacitet baterije.
   */
  public void setKapacitetBaterija(int kapacitetBaterija) {
    this.kapacitetBaterija = kapacitetBaterija;
  }

  /**
   * Dohvaća temperaturu baterije.
   *
   * @return Temperatura baterije.
   */
  public int getTempBaterija() {
    return tempBaterija;
  }

  /**
   * Postavlja temperaturu baterije.
   *
   * @param tempBaterija Temperatura baterije.
   */
  public void setTempBaterija(int tempBaterija) {
    this.tempBaterija = tempBaterija;
  }

  /**
   * Dohvaća preostalu kilometražu vozila.
   *
   * @return Preostala kilometraža vozila.
   */
  public double getPreostaloKm() {
    return preostaloKm;
  }

  /**
   * Postavlja preostalu kilometražu vozila.
   *
   * @param preostaloKm Preostala kilometraža vozila.
   */
  public void setPreostaloKm(double preostaloKm) {
    this.preostaloKm = preostaloKm;
  }

  /**
   * Dohvaća ukupnu kilometražu vozila.
   *
   * @return Ukupna kilometraža vozila.
   */
  public double getUkupnoKm() {
    return ukupnoKm;
  }

  /**
   * Postavlja ukupnu kilometražu vozila.
   *
   * @param ukupnoKm Ukupna kilometraža vozila.
   */
  public void setUkupnoKm(double ukupnoKm) {
    this.ukupnoKm = ukupnoKm;
  }

  /**
   * Dohvaća GPS širinu lokacije vozila.
   *
   * @return GPS širina lokacije vozila.
   */
  public double getGpsSirina() {
    return gpsSirina;
  }

  /**
   * Postavlja GPS širinu lokacije vozila.
   *
   * @param gpsSirina GPS širina lokacije vozila.
   */
  public void setGpsSirina(double gpsSirina) {
    this.gpsSirina = gpsSirina;
  }

  /**
   * Dohvaća GPS dužinu lokacije vozila.
   *
   * @return GPS dužina lokacije vozila.
   */
  public double getGpsDuzina() {
    return gpsDuzina;
  }

  /**
   * Postavlja GPS dužinu lokacije vozila.
   *
   * @param gpsDuzina GPS dužina lokacije vozila.
   */
  public void setGpsDuzina(double gpsDuzina) {
    this.gpsDuzina = gpsDuzina;
  }
}
