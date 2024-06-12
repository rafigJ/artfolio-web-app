package ru.vsu.cs.artfolio;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import ru.vsu.cs.artfolio.controller.AuthenticationControllerTest;
import ru.vsu.cs.artfolio.service.PostServiceIT;

@Suite
@SelectClasses({AuthenticationControllerTest.class, PostServiceIT.class})
public class TestRunner {
}
