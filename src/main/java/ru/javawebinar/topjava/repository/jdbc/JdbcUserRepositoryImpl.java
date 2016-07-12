package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: gkislin
 * Date: 26.08.2014
 */

@Repository
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);
    //private static final BeanPropertyRowMapper<Role> ROLE_ROW_MAPPER = BeanPropertyRowMapper.newInstance(Role.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(DataSource dataSource) {
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public User save(User user) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", user.getId())
                .addValue("name", user.getName())
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword())
                .addValue("registered", user.getRegistered())
                .addValue("enabled", user.isEnabled())
                .addValue("caloriesPerDay", user.getCaloriesPerDay());

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(map);
            user.setId(newKey.intValue());
            setRole(user);
        } else {
            namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, " +
                            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", map);
            deleteRole(user.getId());
            setRole(user);
        }
        return user;
    }

    @Override
    public boolean delete(int id) {
        //deleteRole(id);
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        User user = DataAccessUtils.singleResult(users);
        if(user != null)
        user.setRoles(new HashSet<Role>(getRoles(id)));
        return user;
    }

    @Override
    public User getByEmail(String email) {
        User user = jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        user.setRoles(new HashSet<Role>(getRoles(user.getId())));
        return user;
    }

    @Override
    public List<User> getAll() {

        class UserRole {
            int userId;
            Role role;

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public void setRole(Role role) {
                this.role = role;
            }

            public int getUserId() {
                return userId;
            }

            public Role getRole() {
                return role;
            }
        }

        List<UserRole> userRoles = jdbcTemplate.query("SELECT role, user_id FROM user_roles", new RowMapper<UserRole>() {
            @Override
            public UserRole mapRow(ResultSet resultSet, int i) throws SQLException {
                UserRole userRoles = new UserRole();
                userRoles.setUserId(resultSet.getInt("user_id"));
                userRoles.setRole(Role.ROLE_ADMIN.equals(Role.valueOf(resultSet.getString("role"))) ? Role.ROLE_ADMIN : Role.ROLE_USER);
                return userRoles;
            }
        });
        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        for(User user : users){
            Set<Role> roles = new HashSet<>();
            for(UserRole userRole: userRoles){
                if(user.getId() == userRole.getUserId()){
                    roles.add(userRole.getRole());
                }
            }
            user.setRoles(roles);
        }
        return users;
    }

    public List<Role> getRoles(int id){
        return jdbcTemplate.query("SELECT role FROM user_roles WHERE user_id=?", new RowMapper<Role>() {
            @Override
            public Role mapRow(ResultSet resultSet, int i) throws SQLException {
                Role role = Role.valueOf(resultSet.getString("role"));
                return role;
            }
        }, id);
    }

    public void setRole(User user){
        List<Role> roles = new ArrayList<>(user.getRoles());
        //ON DUPLICATE KEY UPDATE не работает в spring
        jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES ( ? , ? )", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1, user.getId());
                preparedStatement.setString(2, roles.get(i).name());
            }

            @Override
            public int getBatchSize() {
                return roles.size();
            }
        });
    }

    public void deleteRole(int id){
        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", id);
    }
}
