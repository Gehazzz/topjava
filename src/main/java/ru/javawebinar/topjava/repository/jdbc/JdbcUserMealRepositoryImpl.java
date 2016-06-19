package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.repository.UserMealRepository;

import javax.sql.DataSource;
import java.util.List;
import java.time.LocalDateTime;

/**
 * User: gkislin
 * Date: 26.08.2014
 */

@Repository
public class JdbcUserMealRepositoryImpl implements UserMealRepository {

    private static final BeanPropertyRowMapper<UserMeal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(UserMeal.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private SimpleJdbcInsert insertMeal;

    private SimpleJdbcInsert insertUsersMeals;

    @Autowired
    public JdbcUserMealRepositoryImpl(DataSource dataSource) {
        this.insertMeal = new SimpleJdbcInsert(dataSource)
                .withTableName("MEALS")
                .usingGeneratedKeyColumns("id");
        this.insertUsersMeals = new SimpleJdbcInsert(dataSource).withTableName("USERS_MEALS");
    }

    @Override
    public UserMeal save(UserMeal userMeal, int userId) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", userMeal.getId())
                .addValue("dateTime", userMeal.getDateTime())
                .addValue("description", userMeal.getDescription())
                .addValue("calories", userMeal.getCalories());

        if(userMeal.isNew()){
            Number newKey = insertMeal.executeAndReturnKey(map);
            userMeal.setId(newKey.intValue());

            MapSqlParameterSource idMap = new MapSqlParameterSource()
                    .addValue("mealId", userMeal.getId())
                    .addValue("userId", userId);
            insertUsersMeals.execute(idMap);
        } else {
            if(isMealOwnedByUser(userMeal.getId(), userId)) {
                namedParameterJdbcTemplate.update(
                        "UPDATE meals SET date_time=:dateTime, description=:description, calories=:calories WHERE id=:id", map);
            } else {
                return null;
            }
        }
        return userMeal;
    }

    @Override
    public boolean delete(int id, int userId) {
        if(isMealOwnedByUser(id, userId)) {
            return jdbcTemplate.update("DELETE FROM meals WHERE id=?", id) != 0;
        }else {
            return false;
        }
    }

    @Override
    public UserMeal get(int id, int userId) {
        if (isMealOwnedByUser(id, userId)) {
            List<UserMeal> userMeals = jdbcTemplate.query("SELECT * FROM meals WHERE id=?", ROW_MAPPER, id);
            return DataAccessUtils.singleResult(userMeals);
        } else {
            return null;
        }
    }

    @Override
    public List<UserMeal> getAll(int userId) {
        return jdbcTemplate.query("SELECT * FROM meals WHERE id IN (SELECT meal_id FROM users_meals WHERE user_id = ?) ORDER BY date_time DESC", ROW_MAPPER, userId);
    }

    @Override
    public List<UserMeal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return null;
    }

    private boolean isMealOwnedByUser (int id, int userId){
        return userId == jdbcTemplate.queryForObject("SELECT user_id FROM users_meals WHERE meal_id=?",
               Integer.class, id) ? true : false;
    }
}
