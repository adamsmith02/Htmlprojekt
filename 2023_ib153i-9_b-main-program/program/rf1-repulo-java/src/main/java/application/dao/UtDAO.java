package application.dao;

import application.model.Repter;
import application.model.Ut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class UtDAO extends JdbcDaoSupport {

    @Autowired
    DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }
    public List<Repter> getAllRepterek() {
        String sql = "SELECT * FROM reptér";
        return getJdbcTemplate().query(sql, (rs, rowNum) ->
                new Repter(
                        rs.getString("Város"),
                        rs.getString("Név")
                ));
    }

    public void addUt(Ut ut) {
        String sql = "INSERT INTO Ut (Hossz, Ár, Megy, Reptér_Város) VALUES (?, ?, ?, ?)";
        getJdbcTemplate().update(sql, ut.getHossz(), ut.getAr(), ut.isMegy() ? 1 : 0, ut.getRepterVaros());
    }
// ... meglévő UtDAO kód ...

    public void updateUt(Ut ut) {
        String sql = "UPDATE Ut SET Hossz = ?, Ár = ?, Megy = ?, Reptér_Város = ? WHERE id = ?";
        getJdbcTemplate().update(sql, ut.getHossz(), ut.getAr(), ut.isMegy(), ut.getRepterVaros(), ut.getId());
    }

    public void deleteUt(int id) {
        String sql = "DELETE FROM Ut WHERE id = ?";
        getJdbcTemplate().update(sql, id);
    }

    public Ut getUtById(int id) {
        String sql = "SELECT * FROM Ut WHERE id = ?";
        return getJdbcTemplate().queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                new Ut(
                        rs.getInt("id"),
                        rs.getInt("Hossz"),
                        rs.getInt("Ár"),
                        rs.getBoolean("Megy"),
                        rs.getString("Reptér_Város")
                ));
    }

    public List<Ut> getUtByVaros(String varos) {
        String sql = "SELECT * FROM Ut WHERE repter_varos = ?";
        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql);

        List<Ut> result = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            Ut ut = new Ut(
                    (Integer)row.get("id"),
                    (Integer)row.get("hossz"),
                    (Integer)row.get("ar"),
                    (boolean)row.get("megy"),
                    (String)row.get("repter_varos")
            );
            result.add(ut);
        }

        return result;
    }

    public List<Ut> listUt() {
        String sql = "SELECT * FROM ut";
        List<Map<String, Object>> rows;
        assert getJdbcTemplate() != null;
        rows = getJdbcTemplate().queryForList(sql);

        List<Ut> result = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            Ut ut = new Ut(
                    (Integer) row.get("id"),
                    (Integer) row.get("hossz"),
                    (Integer) row.get("ar"),
                    (Boolean) row.get("megy"),
                    (String) row.get("repter_varos")
                    );
            result.add(ut);
        }
        return result;
    }
}
