package application.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import application.model.Jarat;
import application.model.Repulo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
public class RepuloDAO extends JdbcDaoSupport {

    @Autowired
    DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    public void insertRepulo(Repulo repulo) {
        String sql = "INSERT INTO repulo (lajstromjel, becenev, modell, ulohely, rakter, info, repultutakszama) VALUES (?, ?, ?, ?, ?, ?, ?)";
        getJdbcTemplate().update(sql, repulo.getLajstromjel(), repulo.getBecenev(), repulo.getModell(), repulo.getUlohely(), repulo.getRakter(), repulo.getInfo(), repulo.getRepultUtakSzama());
    }

    public List<Repulo> listRepulok() {
        String sql = "SELECT * FROM repulo";
        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql);

        List<Repulo> result = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            Repulo repulo = new Repulo(
                    (String)row.get("lajstromjel"),
                    (String)row.get("becenev"),
                    (String)row.get("modell"),
                    (Integer)row.get("ulohely"),
                    (Integer)row.get("rakter"),
                    (String)row.get("info"),
                    (Integer)row.get("repultutakszama")
            );
            result.add(repulo);
        }

        return result;
    }

    public Repulo getRepuloByLajstromjel(String lajstromjel) {
        String sql = "SELECT * FROM repulo WHERE lajstromjel = ?";
        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql, lajstromjel);

        if(rows.isEmpty()) {
            return null; // vagy dobhatunk egy kivételt, ha szeretnénk
        }

        Map<String, Object> row = rows.get(0);
        Repulo repulo = new Repulo(
                (String)row.get("lajstromjel"),
                (String)row.get("becenev"),
                (String)row.get("modell"),
                (Integer)row.get("ulohely"),
                (Integer)row.get("rakter"),
                (String)row.get("info"),
                (Integer)row.get("repultutakszama")
        );

        return repulo;
    }

    public void updateRepulo(String lajstromjel,String becenev,String modell,int ulohely,int rakter,String info,int repultU) {
        String sql = "UPDATE repulo SET becenev =  '" + becenev + "', modell = '" + modell + "', ulohely = '" + ulohely + "', " +
                "rakter = '" + rakter + "', info = '" + info + "', repultutakszama = '" + repultU + "' WHERE lajstromjel = '" + lajstromjel + "'";
        getJdbcTemplate().update(sql);
    }

    public void deleteRepulo(String lajstromjel) {
        String sql = "SELECT * FROM jarat";
        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql);
        for (Map<String, Object> row : rows) {
            if(row.get("repulo_lajstromjel").equals(lajstromjel)){
                int futo = (Integer) row.get("id");
                String fsql = "DELETE FROM jegy WHERE jarat_id = ?";
                getJdbcTemplate().update(fsql, futo);
                String fsql2 = "DELETE FROM jarat WHERE id= ?";
                getJdbcTemplate().update(fsql2, futo);
            }
        }


        String sql2 = "DELETE FROM repulo WHERE lajstromjel = '" + lajstromjel + "'";
        getJdbcTemplate().update(sql2);
    }

    public boolean isLajstromjelExists(String lajstromjel) {
        String sql = "SELECT COUNT(*) FROM repulo WHERE lajstromjel = ?";
        int count = getJdbcTemplate().queryForObject(sql, Integer.class, lajstromjel);
        return count > 0;
    }

}
