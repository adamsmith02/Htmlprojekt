package application.model;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Jarat {
     int id;
     LocalDateTime idopont;
     int akcio;
     int ut_id;
     String repulo_lajstromjel;

    public Jarat(LocalDateTime idopont, int akcio, int ut_id, String repulo_lajstromjel) {
        this.idopont = idopont;
        this.akcio = akcio;
        this.ut_id = ut_id;
        this.repulo_lajstromjel = repulo_lajstromjel;
    }
    public Jarat(int akcio, int ut_id, String repulo_lajstromjel) {
        String str = "2024-12-18 09:15:00.000000";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        this.idopont = LocalDateTime.parse(str, formatter);
        this.akcio = akcio;
        this.ut_id = ut_id;
        this.repulo_lajstromjel = repulo_lajstromjel;
    }

    public Jarat(int id, int akcio, int ut_id, String repulo_lajstromjel) {
        this.id = id;
        String str = "2024-12-18 09:15:00.000000";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        this.idopont = LocalDateTime.parse(str, formatter);
        this.akcio = akcio;
        this.ut_id = ut_id;
        this.repulo_lajstromjel = repulo_lajstromjel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getIdopont() {
        return idopont;
    }

    public void setIdopont(LocalDateTime idopont) {
        this.idopont = idopont;
    }

    public int getAkcio() {
        return akcio;
    }

    public void setAkcio(int akcio) {
        this.akcio = akcio;
    }

    public int getUt_id() {
        return ut_id;
    }

    public void setUt_id(int ut_id) {
        this.ut_id = ut_id;
    }

    public String getRepulo_lajstromjel() {
        return repulo_lajstromjel;
    }

    public void setRepulo_lajstromjel(String repulo_lajstromjel) {
        this.repulo_lajstromjel = repulo_lajstromjel;
    }
}
