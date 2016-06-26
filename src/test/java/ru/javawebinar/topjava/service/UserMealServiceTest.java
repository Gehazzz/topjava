package ru.javawebinar.topjava.service;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class UserMealServiceTest {
    private static final Logger LOG = LoggerFactory.getLogger(UserMealServiceTest.class);

    @Autowired
    protected UserMealService service;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testDelete() throws Exception {
        long start = System.currentTimeMillis();
        service.delete(MealTestData.MEAL1_ID, USER_ID);
        MATCHER.assertCollectionEquals(Arrays.asList(MEAL6, MEAL5, MEAL4, MEAL3, MEAL2), service.getAll(USER_ID));
        long finish = System.currentTimeMillis();
        LOG.info("Test duration " + String.valueOf(finish-start) + " milliseconds");
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteNotFound() throws Exception {
        long start = System.currentTimeMillis();
        service.delete(MEAL1_ID, 1);
        long finish = System.currentTimeMillis();
        LOG.info("Test duration " + String.valueOf(finish-start) + " milliseconds");
    }

    @Test
    public void testSave() throws Exception {
        long start = System.currentTimeMillis();
        UserMeal created = getCreated();
        service.save(created, USER_ID);
        MATCHER.assertCollectionEquals(Arrays.asList(created, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1), service.getAll(USER_ID));
        long finish = System.currentTimeMillis();
        LOG.info("Test duration " + String.valueOf(finish-start) + " milliseconds");
    }

    @Test
    public void testGet() throws Exception {
        long start = System.currentTimeMillis();
        UserMeal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        MATCHER.assertEquals(ADMIN_MEAL, actual);
        long finish = System.currentTimeMillis();
        LOG.info("Test duration " + String.valueOf(finish-start) + " milliseconds");
    }

    @Test(expected = NotFoundException.class)
    public void testGetNotFound() throws Exception {
        long start = System.currentTimeMillis();
        service.get(MEAL1_ID, ADMIN_ID);
        long finish = System.currentTimeMillis();
        LOG.info("Test duration " + String.valueOf(finish-start) + " milliseconds");
    }

    @Test
    public void testUpdate() throws Exception {
        long start = System.currentTimeMillis();
        UserMeal updated = getUpdated();
        service.update(updated, USER_ID);
        MATCHER.assertEquals(updated, service.get(MEAL1_ID, USER_ID));
        long finish = System.currentTimeMillis();
        LOG.info("Test duration " + String.valueOf(finish-start) + " milliseconds");
    }

    @Test(expected = NotFoundException.class)
    public void testNotFoundUpdate() throws Exception {
        long start = System.currentTimeMillis();
        UserMeal item = service.get(MEAL1_ID, USER_ID);
        service.update(item, ADMIN_ID);
        long finish = System.currentTimeMillis();
        LOG.info("Test duration " + String.valueOf(finish-start) + " milliseconds");
    }

    @Test
    public void testGetAll() throws Exception {
        long start = System.currentTimeMillis();
        MATCHER.assertCollectionEquals(USER_MEALS, service.getAll(USER_ID));
        long finish = System.currentTimeMillis();
        LOG.info("Test duration " + String.valueOf(finish-start) + " milliseconds");
    }

    @Test
    public void testGetBetween() throws Exception {
        long start = System.currentTimeMillis();
        MATCHER.assertCollectionEquals(Arrays.asList(MEAL3, MEAL2, MEAL1),
                service.getBetweenDates(LocalDate.of(2015, Month.MAY, 30), LocalDate.of(2015, Month.MAY, 30), USER_ID));
        long finish = System.currentTimeMillis();
        LOG.info("Test duration " + String.valueOf(finish-start) + " milliseconds");
    }
}