package application.dao;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import application.model.Jarat;
import application.model.Repter;
import application.model.Ut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
public class RepterDAO extends JdbcDaoSupport {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public RepterDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    DataSource dataSource;



    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    public void insertRepter(Repter repter) {
        String sql = "INSERT INTO repter(varos, nev) VALUES (?, ?)";

        getJdbcTemplate().update(sql, new Object[]{
                repter.getVaros(), repter.getNev()
        });
    }


    public List<Repter> listRepterek() {
        String sql = "SELECT * FROM repter ORDER BY varos";
        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql);

        List<Repter> result = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            Repter repter = new Repter((String) row.get("varos"), (String) row.get("nev"));
            result.add(repter);
        }

        return result;
    }

    public Repter getRepterbyVaros(String varos) {
        String sql = "SELECT * FROM repter WHERE varos= '" + varos + "'";
        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql);

        List<Repter> result = new ArrayList<Repter>();
        for (Map<String, Object> row : rows) {
            Repter repter = new Repter((String) row.get("varos"), (String) row.get("nev"));
            result.add(repter);
        }

        return result.get(0);

    }

    public void deleteRepter(String varos) {
        String sql = "SELECT * FROM jarat";
        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql);

        List<Jarat> result = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            Jarat jarat = new Jarat(
                    (Integer) row.get("akcio"),
                    (Integer) row.get("ut_id"),
                    (String) row.get("repulo_lajstromjel")
            );
            jarat.setId((Integer) row.get("id"));
            result.add(jarat);
        }

        sql = "SELECT * FROM Ut WHERE repter_varos = '" + varos + "'";
        List<Map<String, Object>> rows2 = getJdbcTemplate().queryForList(sql);

        List<Ut> result2 = new ArrayList<>();
        for (Map<String, Object> row : rows2) {
            Ut ut = new Ut(
                    (Integer) row.get("id"),
                    (String) row.get("repter_varos")
            );
            result2.add(ut);
        }

        List<Integer> valList = new ArrayList<>();  // Initialize the list

        for (Jarat row : result) {
            for (Ut col : result2) {
                if (Objects.equals(col.getId(), row.getUt_id())) {
                    valList.add(row.getId());
                }
            }
        }
        for (Integer re : valList) {
            sql = "DELETE FROM jegy WHERE jarat_id =" + re;
            getJdbcTemplate().update(sql);
            sql = "DELETE FROM jarat WHERE id=" + re;
            getJdbcTemplate().update(sql);
        }
        sql = "DELETE FROM ut WHERE repter_varos= '" + varos + "'";
        getJdbcTemplate().update(sql);
        sql = "DELETE FROM repter WHERE varos= '" + varos + "'";
        getJdbcTemplate().update(sql);
    }


    public void updateRepter(String varos, String nev) {
        String sql = "UPDATE repter SET varos = '" + varos + "',  nev = '" + nev + "' WHERE varos='" + varos + "'";
        getJdbcTemplate().update(sql);
    }

}