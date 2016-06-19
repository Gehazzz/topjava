package ru.javawebinar.topjava;

import org.springframework.format.annotation.DateTimeFormat;
import ru.javawebinar.topjava.matcher.ModelMatcher;
import ru.javawebinar.topjava.model.UserMeal;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.javawebinar.topjava.model.BaseEntity.START_SEQ;

/**
 * GKislin
 * 13.03.2015.
 */
public class MealTestData {
    public static final int MEAL_DINNER_ID = START_SEQ;
    public static final int MEAL_SUPPER_ID = START_SEQ + 1;
    public static final int MEAL_BREAKFAST_ID = START_SEQ + 2;

    public static final UserMeal DINNER = new UserMeal(MEAL_DINNER_ID, Timestamp.valueOf("2016-06-18 13:33:37.5168").toLocalDateTime(), "dinner", 1000);
    public static final UserMeal SUPPER = new UserMeal(MEAL_SUPPER_ID, Timestamp.valueOf("2016-06-18 19:20:00.6").toLocalDateTime(), "supper", 1001);
    public static final UserMeal BREAKFAST = new UserMeal(MEAL_BREAKFAST_ID, Timestamp.valueOf("2016-06-17 08:20:10.6").toLocalDateTime(), "breakfast", 400);

    public static final ModelMatcher<UserMeal, String> MATCHER = new ModelMatcher<>(UserMeal::toString);

}
