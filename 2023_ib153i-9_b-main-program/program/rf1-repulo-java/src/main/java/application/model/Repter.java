package application.model;

import java.io.Serializable;

public class Repter implements Serializable {

  private static final long serialVersionUID = 1L;


  String varos;
  String nev;

  public Repter(String varos, String nev) {
    this.varos = varos;
    this.nev = nev;
  }


  public Object getVaros() {
    return varos;
  }

  public void setVaros(String varos) {
    this.varos = varos;
  }

  public Object getNev() {
    return nev;
  }

  public void setNev(String nev) {
    this.nev = nev;
  }

  @Override
  public String toString() {
    return "Repter [Varos=" + varos + ", neve=" + nev + "]";
  }

}