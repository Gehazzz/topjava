package ru.javawebinar.topjava.web.meal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.TestUtil;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.service.UserMealService;
import ru.javawebinar.topjava.util.UserMealsUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.MealTestData.MEAL2;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

/**
 * Created by Gena on 18.07.2016.
 */
public class UserMealRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = UserMealRestController.REST_URL + '/';
    //http://stackoverflow.com/questions/13213061/springmvc-requestmapping-for-get-parameters
    private static final String BETWEEN_URL = "between?startDate=2015-05-29&startTime=00:00&endDate=2015-05-30&endTime=23:59";

    @Autowired
    protected UserMealService service;

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + MEAL1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentMatcher(MEAL1));
    }

    @Test
    public void testGetAll() throws Exception {
       mockMvc.perform(get(REST_URL))
               .andExpect(status().isOk())
               .andDo(print())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(EXCEED_MATCHER.contentListMatcher(UserMealsUtil.getWithExceeded(USER_MEALS, UserTestData.USER.getCaloriesPerDay())));
    }

    @Test
    public void testGetBetween() throws Exception{
        //https://techotom.wordpress.com/2013/12/10/help-troubleshooting-spring-mvc-unit-test-failing-with-a-http-400-error-code/
         mockMvc.perform(get(REST_URL + BETWEEN_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(EXCEED_MATCHER.contentListMatcher(UserMealsUtil.getWithExceeded(Arrays.asList(MEAL3, MEAL2, MEAL1), UserTestData.USER.getCaloriesPerDay())));
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isOk());
        MATCHER.assertCollectionEquals(Arrays.asList(MEAL6, MEAL5, MEAL4, MEAL3, MEAL2), service.getAll(USER_ID));
    }

    @Test
    public void testUpdate() throws Exception {
        UserMeal updated = new UserMeal();
        updated.setId(MEAL1_ID);
        updated.setDescription(MEAL1.getDescription());
        updated.setCalories(MEAL1.getCalories());
        updated.setDateTime(MEAL1.getDateTime());
        mockMvc.perform(put(REST_URL + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isOk());

        MATCHER.assertEquals(updated, service.get(MEAL1_ID, USER_ID));
    }

    @Test
    public void testCreate() throws Exception {
        UserMeal expected = MealTestData.getCreated();
        ResultActions action = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected))).andExpect(status().isCreated());

        UserMeal returned = MATCHER.fromJsonAction(action);
        expected.setId(returned.getId());

        MATCHER.assertEquals(expected, returned);
        MATCHER.assertCollectionEquals(Arrays.asList(expected, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1), service.getAll(USER_ID));
    }

}
