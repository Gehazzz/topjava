package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.to.UserMealWithExceed;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

/**
 * Created by Gena on 10.07.2016.
 */
@Controller
public class UserMealController extends AbstractUserMealController {

    @RequestMapping(value = {"/meals/update", "/meals/create"}, method = RequestMethod.GET)
    public String get(Model model, HttpServletRequest request) {
        if(request.getRequestURI().equals("/meals/create")) {
            model.addAttribute("meal", new UserMeal(LocalDateTime.now().withNano(0).withSecond(0), "", 1000));
        }else {
            model.addAttribute("meal", super.get(getId(request)));
        }
        return "mealEdit";
    }

    @RequestMapping(value = "/meals/delete", method = RequestMethod.GET)
    public String delete(Model model, HttpServletRequest request) {
        super.delete(getId(request));
        return "redirect:/meals";
    }

    @RequestMapping(value = "/meals", method = RequestMethod.GET)
    public String getAll(Model model) {
        model.addAttribute("mealList", super.getAll());
        return "mealList";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String save(Model model, HttpServletRequest request) {
        UserMeal userMeal = new UserMeal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.valueOf(request.getParameter("calories")));
        if (request.getParameter("id").isEmpty()) {
            LOG.info("Create {}", userMeal);
            super.create(userMeal);
        } else {
            LOG.info("Update {}", userMeal);
            super.update(userMeal, getId(request));
        }
        return "redirect:/meals";
    }


   /* public String create(Model model) {
        return "";
    }

*/
   @RequestMapping(value = "/meals/filter", method = RequestMethod.POST)
    public String getBetween(Model model, HttpServletRequest request) {
       LocalDate startDate = TimeUtil.parseLocalDate(resetParam("startDate", request));
       LocalDate endDate = TimeUtil.parseLocalDate(resetParam("endDate", request));
       LocalTime startTime = TimeUtil.parseLocalTime(resetParam("startTime", request));
       LocalTime endTime = TimeUtil.parseLocalTime(resetParam("endTime", request));
       request.setAttribute("mealList", super.getBetween(startDate, startTime, endDate, endTime));
        return "";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }

    private String resetParam(String param, HttpServletRequest request) {
        String value = request.getParameter(param);
        request.setAttribute(param, value);
        return value;
    }
}
