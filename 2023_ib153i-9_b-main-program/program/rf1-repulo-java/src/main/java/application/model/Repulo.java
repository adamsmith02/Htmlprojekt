package application.model;

public class Repulo {

    private String lajstromjel;
    private String becenev;
    private String modell;
    private int ulohely;
    private int rakter;
    private String info;
    private int repultUtakSzama;
    public Repulo(String lajstromjel, String becenev, String modell, int ulohely, int rakter, String info, int repultUtakSzama) {
        this.lajstromjel = lajstromjel;
        this.becenev = becenev;
        this.modell = modell;
        this.ulohely = ulohely;
        this.rakter = rakter;
        this.info = info;
        this.repultUtakSzama = repultUtakSzama;
    }

    public Repulo() {}

    public String getLajstromjel() {
        return lajstromjel;
    }

    public void setLajstromjel(String lajstromjel) {
        this.lajstromjel = lajstromjel;
    }

    public String getBecenev() {
        return becenev;
    }

    public void setBecenev(String becenev) {
        this.becenev = becenev;
    }

    public String getModell() {
        return modell;
    }

    public void setModell(String modell) {
        this.modell = modell;
    }

    public int getUlohely() {
        return ulohely;
    }

    public void setUlohely(int ulohely) {
        this.ulohely = ulohely;
    }

    public int getRakter() {
        return rakter;
    }

    public void setRakter(int rakter) {
        this.rakter = rakter;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getRepultUtakSzama() {
        return repultUtakSzama;
    }

    public void setRepultUtakSzama(int repultUtakSzama) {
        this.repultUtakSzama = repultUtakSzama;
    }
}
