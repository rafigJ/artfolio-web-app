package ru.vsu.cs.artfolio;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import ru.vsu.cs.artfolio.controller.integration.AuthenticationControllerTest;
import ru.vsu.cs.artfolio.controller.unit.FeedControllerUnitTest;
import ru.vsu.cs.artfolio.controller.unit.PostControllerUnitTest;
import ru.vsu.cs.artfolio.service.PostServiceIT;
import ru.vsu.cs.artfolio.service.UserServiceIT;

@Suite
@SelectClasses({
        AuthenticationControllerTest.class, PostServiceIT.class, UserServiceIT.class,
        FeedControllerUnitTest.class, PostControllerUnitTest.class
})
public class TestRunner {
}
