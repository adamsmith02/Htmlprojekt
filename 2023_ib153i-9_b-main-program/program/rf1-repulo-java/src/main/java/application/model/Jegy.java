package application.model;

public class Jegy {

    private int id;
    private String felhasznaloFelhasznalonev;
    private String nev;
    private int hely;
    private int jaratId;
    private boolean pluszCsomag;

    // Konstruktorok, Getterek és Setterek
    public Jegy(String felhasznaloFelhasznalonev, String nev, int hely, int jaratId, boolean pluszCsomag) {
        this.felhasznaloFelhasznalonev = felhasznaloFelhasznalonev;
        this.nev = nev;
        this.hely = hely;
        this.jaratId = jaratId;
        this.pluszCsomag = pluszCsomag;
    }

    // Ide jönnek a getterek és setterek...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFelhasznaloFelhasznalonev() {
        return felhasznaloFelhasznalonev;
    }

    public void setFelhasznaloFelhasznalonev(String felhasznaloFelhasznalonev) {
        this.felhasznaloFelhasznalonev = felhasznaloFelhasznalonev;
    }

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public int getHely() {
        return hely;
    }

    public void setHely(int hely) {
        this.hely = hely;
    }

    public int getJaratId() {
        return jaratId;
    }

    public void setJaratId(int jaratId) {
        this.jaratId = jaratId;
    }

    public boolean isPluszCsomag() {
        return pluszCsomag;
    }

    public void setPluszCsomag(boolean pluszCsomag) {
        this.pluszCsomag = pluszCsomag;
    }
}
