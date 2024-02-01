package application.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import application.model.User;
import org.springframework.stereotype.Service;


@Service
public class UserDAO extends JdbcDaoSupport {
  @Autowired
  BCryptPasswordEncoder passwordEncoder;

  @Autowired
  DataSource dataSource;

  @PostConstruct
  private void initialize() {
    setDataSource(dataSource);
  }

  public void insertUser(User user) {
    String sql = "INSERT INTO felhasznalo (felhasznalonev, jelszo, vezeteknev,keresztnev, email, telefonszam, bankkartyaszam, aranykartya, admin) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?)";
    //getJdbcTemplate().update(sql, user.getFelhasznalonev(), passwordEncoder.encode(user.getJelszo()), user.getVezetekNev(),user.getKeresztNev(), user.getEmail(), user.getTelefonSzam(), user.getBankkartyaszam(), user.isAranykartya(), user.getRole());
    getJdbcTemplate().update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sql);
      ps.setString(1, user.getFelhasznalonev());
      ps.setString(2, passwordEncoder.encode(user.getJelszo()));
      ps.setString(3, user.getVezetekNev());
      ps.setString(4, user.getKeresztNev());
      ps.setString(5, user.getEmail());
      ps.setString(6, user.getTelefonSzam());
      ps.setString(7, user.getBankkartyaszam());
      ps.setBoolean(8, user.isAranykartya());
      ps.setString(9, user.getRole());
      return ps;
    });
  }

  public User getUserById(int id) {
    String sql = "SELECT * FROM felhasznalo WHERE id = ?";
    List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql, id);
    if (rows.isEmpty()) {
      return null;
    }
    return mapRowToUser(rows.get(0));
  }

  public User findByUsername(String felhasznalonev) {
    String sql = "SELECT * FROM felhasznalo WHERE felhasznalonev = ?";
    List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql, felhasznalonev);
    if (rows.isEmpty()) {
      return null;
    }
    return mapRowToUser(rows.get(0));
  }

  private User mapRowToUser(Map<String, Object> row) {
    User user = new User();
    user.setFelhasznalonev((String) row.get("felhasznalonev"));
    user.setJelszo((String) row.get("jelszo")); // Note: it's the hashed password
    user.setVezetekNev((String) row.get("VezetekNev"));
    user.setKeresztNev((String) row.get("KeresztNev"));
    user.setEmail((String) row.get("email"));
    user.setTelefonSzam((String) row.get("telefonSzam"));
    user.setBankkartyaszam((String) row.get("bankkartyaszam"));
    user.setAranykartya((Boolean) row.get("aranykartya"));
    user.setRole((String) row.get("admin"));
    return user;
  }
  public void deleteUser(String felhasznalonev) {
    String sql = "DELETE FROM felhasznalo WHERE felhasznalonev = ?";
    getJdbcTemplate().update(sql, felhasznalonev);
  }
  public void updateUser(User user) {
    String sql = "UPDATE felhasznalo SET jelszo = ?, vezeteknev = ?, keresztnev = ?, email = ?, telefonszam = ?, bankkartyaszam = ?, aranykartya = ?, admin = ? WHERE felhasznalonev = ?";
    getJdbcTemplate().update(sql,
            passwordEncoder.encode(user.getJelszo()),
            user.getVezetekNev(),
            user.getKeresztNev(),
            user.getEmail(),
            user.getTelefonSzam(),
            user.getBankkartyaszam(),
            user.isAranykartya(),
            user.getRole(),
            user.getFelhasznalonev());
  }

}
