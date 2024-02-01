package application.dao;

import application.model.Jegy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Repository
public class JegyDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JegyDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Jegy> findAll() {
        String sql = "SELECT * FROM jegy";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void save(Jegy jegy) {
        String sql = "INSERT INTO jegy (felhasznalo_felhasznalonev, nev, hely, jarat_id, pluszcsomag) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, jegy.getFelhasznaloFelhasznalonev(), jegy.getNev(), jegy.getHely(), jegy.getJaratId(), jegy.isPluszCsomag());
    }

    private RowMapper<Jegy> rowMapper = (rs, rowNum) -> new Jegy(
            rs.getString("felhasznalo_felhasznalonev"),
            rs.getString("nev"),
            rs.getInt("hely"),
            rs.getInt("jarat_id"),
            rs.getBoolean("pluszcsomag")
    );


    // Update (Módosítás)
    public void updateJegy(Jegy jegy) {
        String sql = "UPDATE jegy SET felhasznalo_felhasznalonev = ?, nev = ?, hely = ?, jarat_id = ?, pluszcsomag = ? WHERE id = ?";
        jdbcTemplate.update(sql, jegy.getFelhasznaloFelhasznalonev(), jegy.getNev(), jegy.getHely(), jegy.getJaratId(), jegy.isPluszCsomag(), jegy.getId());
    }

    // Delete (Törlés)
    public void deleteJegy(int id) {
        String sql = "DELETE FROM jegy WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }


    // Read (Lekérdezés egy adott jegyre)
    public Jegy findJegyById(int id) {
        String sql = "SELECT * FROM jegy WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, rowMapper);
    }

    public List<Jegy> findJegyByFelhasznalonev(String felhasznalonev) {
        String sql = "SELECT * FROM jegy WHERE felhasznalo_felhasznalonev = ?";
        return jdbcTemplate.query(sql, new Object[]{felhasznalonev}, rowMapper);
    }

    public List<Map<String, Object>> findJegyByFelhasznalonevWithUtInfo(String felhasznalonev) {
        String sql = "SELECT j.*, u.repter_varos, u.megy,jar.idopont " +
                "FROM jegy j " +
                "JOIN jarat jar ON j.jarat_id = jar.id " +
                "JOIN ut u ON jar.ut_id = u.id " +
                "WHERE j.felhasznalo_felhasznalonev = ?";

        return jdbcTemplate.queryForList(sql, new Object[]{felhasznalonev});
    }

    public List<Map<String, Object>> findAllWithUtInfo() {
        String sql = "SELECT j.*, u.repter_varos, u.megy,jar.idopont  " +
                "FROM jegy j " +
                "JOIN jarat jar ON j.jarat_id = jar.id " +
                "JOIN ut u ON jar.ut_id = u.id";
        return jdbcTemplate.queryForList(sql);
    }

}


