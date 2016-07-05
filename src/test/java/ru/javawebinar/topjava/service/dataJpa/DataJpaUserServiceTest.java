package ru.javawebinar.topjava.service.dataJpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.UserServiceTest;

/**
 * Created by Gena on 03.07.2016.
 */
@ActiveProfiles(Profiles.ACTIVE_DATA_JPA)
public class DataJpaUserServiceTest extends UserServiceTest {
}
