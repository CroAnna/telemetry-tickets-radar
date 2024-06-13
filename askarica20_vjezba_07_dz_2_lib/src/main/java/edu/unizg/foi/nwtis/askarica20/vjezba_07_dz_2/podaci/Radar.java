package edu.unizg.foi.nwtis.askarica20.vjezba_07_dz_2.podaci;

/**
 * Klasa koja predstavlja radar.
 */
public class Radar {
  private int id;
  private String adresaRadara;
  private int mreznaVrataRadara;
  private int maksBrzina;
  private int maksTrajanje;
  private int maksUdaljenost;
  private String adresaRegistracije;
  private int mreznaVrataRegistracije;
  private String adresaKazne;
  private int mreznaVrataKazne;
  private String postanskaAdresaRadara;
  private double gpsSirina;
  private double gpsDuzina;

  /**
   * Konstruktor bez parametara.
   */
  public Radar() {
    super();
  }

  /**
   * Konstruktor s parametrima.
   *
   * @param id Jedinstveni identifikator radara.
   * @param adresaRadara Adresa radara.
   * @param mreznaVrataRadara Mrežna vrata radara.
   * @param maksBrzina Maksimalna brzina koju radar može zabilježiti.
   * @param maksTrajanje Maksimalno trajanje praćenja.
   * @param maksUdaljenost Maksimalna udaljenost na kojoj radar može pratiti brzinu.
   * @param adresaRegistracije Adresa za registraciju.
   * @param mreznaVrataRegistracije Mrežna vrata za registraciju.
   * @param adresaKazne Adresa za kazne.
   * @param mreznaVrataKazne Mrežna vrata za kazne.
   * @param postanskaAdresaRadara Poštanska adresa radara.
   * @param gpsSirina GPS širina lokacije radara.
   * @param gpsDuzina GPS dužina lokacije radara.
   */
  public Radar(int id, String adresaRadara, int mreznaVrataRadara, int maksBrzina, int maksTrajanje,
      int maksUdaljenost, String adresaRegistracije, int mreznaVrataRegistracije,
      String adresaKazne, int mreznaVrataKazne, String postanskaAdresaRadara, double gpsSirina,
      double gpsDuzina) {
    super();
    this.id = id;
    this.adresaRadara = adresaRadara;
    this.mreznaVrataRadara = mreznaVrataRadara;
    this.maksBrzina = maksBrzina;
    this.maksTrajanje = maksTrajanje;
    this.maksUdaljenost = maksUdaljenost;
    this.adresaRegistracije = adresaRegistracije;
    this.mreznaVrataRegistracije = mreznaVrataRegistracije;
    this.adresaKazne = adresaKazne;
    this.mreznaVrataKazne = mreznaVrataKazne;
    this.postanskaAdresaRadara = postanskaAdresaRadara;
    this.gpsSirina = gpsSirina;
    this.gpsDuzina = gpsDuzina;
  }

  /**
   * Dohvaća identifikator radara.
   *
   * @return Identifikator radara.
   */
  public int getId() {
    return id;
  }

  /**
   * Postavlja identifikator radara.
   *
   * @param id Identifikator radara.
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Dohvaća adresu radara.
   *
   * @return Adresa radara.
   */
  public String getAdresaRadara() {
    return adresaRadara;
  }

  /**
   * Postavlja adresu radara.
   *
   * @param adresaRadara Adresa radara.
   */
  public void setAdresaRadara(String adresaRadara) {
    this.adresaRadara = adresaRadara;
  }

  /**
   * Dohvaća mrežna vrata radara.
   *
   * @return Mrežna vrata radara.
   */
  public int getMreznaVrataRadara() {
    return mreznaVrataRadara;
  }

  /**
   * Postavlja mrežna vrata radara.
   *
   * @param mreznaVrataRadara Mrežna vrata radara.
   */
  public void setMreznaVrataRadara(int mreznaVrataRadara) {
    this.mreznaVrataRadara = mreznaVrataRadara;
  }

  /**
   * Dohvaća maksimalnu brzinu koju radar može zabilježiti.
   *
   * @return Maksimalna brzina.
   */
  public int getMaksBrzina() {
    return maksBrzina;
  }

  /**
   * Postavlja maksimalnu brzinu koju radar može zabilježiti.
   *
   * @param maksBrzina Maksimalna brzina.
   */
  public void setMaksBrzina(int maksBrzina) {
    this.maksBrzina = maksBrzina;
  }

  /**
   * Dohvaća maksimalno trajanje praćenja.
   *
   * @return Maksimalno trajanje.
   */
  public int getMaksTrajanje() {
    return maksTrajanje;
  }

  /**
   * Postavlja maksimalno trajanje praćenja.
   *
   * @param maksTrajanje Maksimalno trajanje.
   */
  public void setMaksTrajanje(int maksTrajanje) {
    this.maksTrajanje = maksTrajanje;
  }

  /**
   * Dohvaća maksimalnu udaljenost na kojoj radar može pratiti brzinu.
   *
   * @return Maksimalna udaljenost.
   */
  public int getMaksUdaljenost() {
    return maksUdaljenost;
  }

  /**
   * Postavlja maksimalnu udaljenost na kojoj radar može pratiti brzinu.
   *
   * @param maksUdaljenost Maksimalna udaljenost.
   */
  public void setMaksUdaljenost(int maksUdaljenost) {
    this.maksUdaljenost = maksUdaljenost;
  }

  /**
   * Dohvaća adresu za registraciju.
   *
   * @return Adresa za registraciju.
   */
  public String getAdresaRegistracije() {
    return adresaRegistracije;
  }

  /**
   * Postavlja adresu za registraciju.
   *
   * @param adresaRegistracije Adresa za registraciju.
   */
  public void setAdresaRegistracije(String adresaRegistracije) {
    this.adresaRegistracije = adresaRegistracije;
  }

  /**
   * Dohvaća mrežna vrata za registraciju.
   *
   * @return Mrežna vrata za registraciju.
   */
  public int getMreznaVrataRegistracije() {
    return mreznaVrataRegistracije;
  }

  /**
   * Postavlja mrežna vrata za registraciju.
   *
   * @param mreznaVrataRegistracije Mrežna vrata za registraciju.
   */
  public void setMreznaVrataRegistracije(int mreznaVrataRegistracije) {
    this.mreznaVrataRegistracije = mreznaVrataRegistracije;
  }

  /**
   * Dohvaća adresu za kazne.
   *
   * @return Adresa za kazne.
   */
  public String getAdresaKazne() {
    return adresaKazne;
  }

  /**
   * Postavlja adresu za kazne.
   *
   * @param adresaKazne Adresa za kazne.
   */
  public void setAdresaKazne(String adresaKazne) {
    this.adresaKazne = adresaKazne;
  }

  /**
   * Dohvaća mrežna vrata za kazne.
   *
   * @return Mrežna vrata za kazne.
   */
  public int getMreznaVrataKazne() {
    return mreznaVrataKazne;
  }

  /**
   * Postavlja mrežna vrata za kazne.
   *
   * @param mreznaVrataKazne Mrežna vrata za kazne.
   */
  public void setMreznaVrataKazne(int mreznaVrataKazne) {
    this.mreznaVrataKazne = mreznaVrataKazne;
  }

  /**
   * Dohvaća poštansku adresu radara.
   *
   * @return Poštanska adresa radara.
   */
  public String getPostanskaAdresaRadara() {
    return postanskaAdresaRadara;
  }

  /**
   * Postavlja poštansku adresu radara.
   *
   * @param postanskaAdresaRadara Poštanska adresa radara.
   */
  public void setPostanskaAdresaRadara(String postanskaAdresaRadara) {
    this.postanskaAdresaRadara = postanskaAdresaRadara;
  }

  /**
   * Dohvaća GPS širinu lokacije radara.
   *
   * @return GPS širina.
   */
  public double getGpsSirina() {
    return gpsSirina;
  }

  /**
   * Postavlja GPS širinu lokacije radara.
   *
   * @param gpsSirina GPS širina.
   */
  public void setGpsSirina(double gpsSirina) {
    this.gpsSirina = gpsSirina;
  }

  /**
   * Dohvaća GPS dužinu lokacije radara.
   *
   * @return GPS dužina.
   */
  public double getGpsDuzina() {
    return gpsDuzina;
  }

  /**
   * Postavlja GPS dužinu lokacije radara.
   *
   * @param gpsDuzina GPS dužina.
   */
  public void setGpsDuzina(double gpsDuzina) {
    this.gpsDuzina = gpsDuzina;
  }
}
