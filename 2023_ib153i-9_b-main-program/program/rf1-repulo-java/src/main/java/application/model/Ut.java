package application.model;

public class Ut {
    private int id;
    private int hossz;
    private int ar;
    private boolean megy;
    private String repterVaros;

    public Ut( int hossz, int ar, boolean megy, String repterVaros) {

        this.hossz = hossz;
        this.ar = ar;
        this.megy = megy;
        this.repterVaros = repterVaros;
    }

    public Ut(int id, int hossz, int ar, boolean megy, String repterVaros) {
        this.id = id;
        this.hossz = hossz;
        this.ar = ar;
        this.megy = megy;
        this.repterVaros = repterVaros;
    }


    public Ut(){}

    public Ut(int id, String repterVaros) {
        this.id = id;
        this.repterVaros = repterVaros;
    }


// Getters and setters...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHossz() {
        return hossz;
    }

    public void setHossz(int hossz) {
        this.hossz = hossz;
    }

    public int getAr() {
        return ar;
    }

    public void setAr(int ar) {
        this.ar = ar;
    }

    public boolean isMegy() {
        return megy;
    }

    public void setMegy(boolean megy) {
        this.megy = megy;
    }

    public String getRepterVaros() {
        return repterVaros;
    }

    public void setRepterVaros(String repterVaros) {
        this.repterVaros = repterVaros;
    }
}