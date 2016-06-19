package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.util.DbPopulator;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

/**
 * Created by Gena on 18.06.2016.
 */
@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class UserMealServiceTest {

    @Autowired
    protected UserMealService service;

    @Autowired
    private DbPopulator dbPopulator;

    @Before
    public void setUp() throws Exception {
        dbPopulator.execute();
    }

    @Test
    public void testSave() throws Exception{
        UserMeal testUserMeal = new UserMeal(null,Timestamp.valueOf("2016-06-18 19:53:41.432").toLocalDateTime(), "dinner", 1000);
        UserMeal created = service.save(testUserMeal, USER_ID);
        testUserMeal.setId(created.getId());
        MATCHER.assertCollectionEquals(Arrays.asList(testUserMeal, BREAKFAST), service.getAll(USER_ID));
       /* MATCHER.assertCollectionEquals(Arrays.asList(SUPPER, DINNER), service.getAll(ADMIN_ID));*/
    }

    @Test
    public void testDelete() throws Exception {
        service.delete(MEAL_DINNER_ID, ADMIN_ID);
        MATCHER.assertCollectionEquals(Collections.singletonList(SUPPER), service.getAll(ADMIN_ID));
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteForeignMeal() throws Exception {
        UserMeal testUserMeal = new UserMeal(null,Timestamp.valueOf("2016-06-18 19:53:41.432").toLocalDateTime(), "dinner", 1000);
        UserMeal created = service.save(testUserMeal, USER_ID);
        service.delete(created.getId(), ADMIN_ID);
    }

    @Test
    public void testGet() throws Exception {
        UserMeal userMeal = service.get(MEAL_BREAKFAST_ID, USER_ID);
        MATCHER.assertEquals(BREAKFAST, userMeal);
    }

    @Test(expected = NotFoundException.class)
    public void testGetForeignMeal() throws Exception {
        service.get(100001, USER_ID);
    }

    @Test
    public void testGetAll() throws Exception {
        Collection<UserMeal> all = service.getAll(ADMIN_ID);
        MATCHER.assertCollectionEquals(Arrays.asList(SUPPER, DINNER), all);
    }

    @Test
    public void testUpdate() throws Exception {
        UserMeal updated = service.get(MEAL_BREAKFAST_ID, USER_ID);
        updated.setDescription("Second breakfast");
        updated.setCalories(540);
        service.update(updated, USER_ID);
        MATCHER.assertEquals(updated, service.get(MEAL_BREAKFAST_ID, USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateForeignUser() throws Exception {
        UserMeal testUserMeal = new UserMeal(null,Timestamp.valueOf("2016-06-18 19:53:41.432").toLocalDateTime(), "dinner", 1000);
        UserMeal created = service.save(testUserMeal, USER_ID);
        testUserMeal.setId(created.getId());
        service.update(testUserMeal, ADMIN_ID);
    }
}
