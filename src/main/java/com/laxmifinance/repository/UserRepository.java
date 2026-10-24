package com.laxmifinance.repository;

import com.laxmifinance.form.RegistrationForm;
import com.laxmifinance.model.AppUser;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<String> findPassword(String userName) {
        List<String> rows = jdbc.query("SELECT password FROM user WHERE user_name = ?",
                (rs, rowNum) -> rs.getString("password"), userName);
        return rows.isEmpty() ? Optional.empty() : Optional.ofNullable(rows.get(0));
    }

    public Optional<AppUser> loadUser(String userName) {
        List<AppUser> rows = jdbc.query("""
                        SELECT u.user_id, u.user_name, u.user_phone_number, u.user_address,
                               u.user_father_name, u.sur_name, u.first_name,
                               l.login_date, l.login_time
                        FROM user u
                        LEFT JOIN login_details l
                            ON l.login_id = (SELECT MAX(login_id) FROM login_details WHERE user_id = u.user_id)
                        WHERE u.user_name = ?
                        """,
                (rs, rowNum) -> {
                    AppUser user = new AppUser();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUserName(rs.getString("user_name"));
                    user.setPhoneNumber(rs.getString("user_phone_number"));
                    user.setAddress(rs.getString("user_address"));
                    user.setFatherName(rs.getString("user_father_name"));
                    user.setSurName(rs.getString("sur_name"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLoginDate(rs.getString("login_date"));
                    user.setLoginTime(rs.getString("login_time"));
                    return user;
                }, userName);
        return rows.isEmpty() ? Optional.empty() : Optional.of(rows.get(0));
    }

    public int insertUser(RegistrationForm form, String encryptedPassword) {
        try {
            return jdbc.update("""
                            INSERT INTO user(user_name, user_phone_number, password, user_address,
                                user_father_name, sur_name, first_name)
                            VALUES (?,?,?,?,?,?,?)
                            """,
                    form.getUserName(), form.getPhoneNo(), encryptedPassword, form.getAddress(),
                    form.getFatherName(), form.getSName(), form.getFName());
        } catch (DuplicateKeyException e) {
            return -1;
        }
    }

    public void recordLogin(int userId, String ipAddress, String browser) {
        jdbc.update("""
                        INSERT INTO login_details(user_id, login_date, login_time, login_ip, browser)
                        VALUES (?, CURRENT_DATE, CURRENT_TIME, ?, ?)
                        """, userId, ipAddress, browser);
    }

    public int updatePassword(String userName, String encryptedPassword) {
        return jdbc.update("UPDATE user SET password = ? WHERE user_name = ?", encryptedPassword, userName);
    }

    public boolean userNameExists(String userName) {
        Integer count = jdbc.queryForObject("SELECT count(*) FROM user WHERE user_name = ?", Integer.class, userName);
        return count != null && count > 0;
    }
}
