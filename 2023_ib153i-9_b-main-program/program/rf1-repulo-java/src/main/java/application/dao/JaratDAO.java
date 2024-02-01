package application.dao;

import java.util.*;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import application.model.Jarat;
import application.model.Repulo;
import application.model.Ut;
import org.apache.tomcat.Jar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
public class JaratDAO extends JdbcDaoSupport {

    @Autowired
    DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JaratDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    public void insertJarat(Jarat jarat) {
        String sql = "INSERT INTO jarat (idopont,akcio, ut_id, repulo_lajstromjel) VALUES (?, ?, ?,?)";
        getJdbcTemplate().update(sql, jarat.getIdopont(), jarat.getAkcio(), jarat.getUt_id(), jarat.getRepulo_lajstromjel());
    }

    public void deleteJarat(int id) {
        String sql = "DELETE FROM jegy WHERE jarat_id = ?";
        getJdbcTemplate().update(sql, id);
        String sql2 = "DELETE FROM jarat WHERE id= ?";
        getJdbcTemplate().update(sql2, id);
    }

    public void updateJarat(int id, int akcio, int utId, String repLajstrom) {
//        String sql = "UPDATE jarat SET akcio = " + akcio + ", ut_id = " + utId +
//                ", repulo_lajstromjel = " + repLajstrom + " WHERE id = " + id + ";";
        String sql = "UPDATE jarat SET akcio = ?, ut_id = ?, repulo_lajstromjel = ? WHERE id = ?;";
        getJdbcTemplate().update(sql, akcio, utId, repLajstrom, id);
    }

    public List<JaratInfo> listJaratok2() {
        String sql = "SELECT j.id AS jarat_id, u.repter_varos, u.megy FROM jarat j INNER JOIN ut u ON j.ut_id = u.id;";
        List<Map<String, Object>> rows;
        assert getJdbcTemplate() != null;
        rows = getJdbcTemplate().queryForList(sql);

        List<JaratInfo> result = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            JaratInfo jaratInfo = new JaratInfo(
                    (Integer) row.get("jarat_id"),
                    (String) row.get("repter_varos"),
                    (boolean) row.get("megy")
            );
            result.add(jaratInfo);
        }

        return result;
    }

    public Map<Jarat,Ut> listJaratok() {
        String sql = "SELECT * FROM jarat";
        String sql2 = "SELECT * FROM ut";
        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql);
        List<Map<String, Object>> rows2 = getJdbcTemplate().queryForList(sql2);
        Map<Jarat,Ut> result = new HashMap<>();
        for (Map<String, Object> row : rows) {
            Jarat jarat = new Jarat(
                    (Integer)row.get("akcio"),
                    (Integer)row.get("ut_id"),
                    (String)row.get("repulo_lajstromjel")

            );
            //akcio add
            jarat.setId( (Integer)row.get("id"));


            Ut ut = null;
            for (Map<String, Object> row2 : rows2) {
                if(jarat.getUt_id()==(Integer)row2.get("id")){
                    ut = new Ut(
                            (Integer)row2.get("id"),
                            (Integer)row2.get("hossz"),
                            (Integer)row2.get("ar"),
                            (Boolean) row2.get("megy"),
                            (String) row2.get("repter_varos") //nem itt a hiba SL
                    );
                }
            }
            result.put(jarat,ut);
        }

        return result;
    }

    public Jarat getJaratById(int id) {
        String sql = "SELECT * FROM jarat WHERE id = ?";
        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql, id);

        if(rows.isEmpty()) {
            return null;
        }

        Map<String, Object> row = rows.get(0);
        Jarat jarat = new Jarat( (int) row.get("id"),(int) row.get("akcio"), (int) row.get("ut_id"), (String) row.get("repulo_lajstromjel"));
        return jarat;
    }

    public class JaratInfo {
        private int jaratId;
        private String repterVaros;
        private boolean megy;

        public JaratInfo(int jaratId, String repterVaros, boolean megy) {
            this.jaratId = jaratId;
            this.repterVaros = repterVaros;
            this.megy = megy;
        }

        public int getJaratId() {
            return jaratId;
        }

        public void setJaratId(int jaratId) {
            this.jaratId = jaratId;
        }

        public String getRepterVaros() {
            return repterVaros;
        }

        public void setRepterVaros(String repterVaros) {
            this.repterVaros = repterVaros;
        }

        public boolean getMegy() {
            return megy;
        }

        public boolean setMegy(boolean megy) {
            return this.megy = megy;
        }


    }

}
