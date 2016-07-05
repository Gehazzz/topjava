package ru.javawebinar.topjava.service.jpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.UserMealServiceTest;

/**
 * Created by Gena on 03.07.2016.
 */
@ActiveProfiles(Profiles.ACTIVE_JPA)
public class JpaUserMealServiceTest extends UserMealServiceTest{
}
