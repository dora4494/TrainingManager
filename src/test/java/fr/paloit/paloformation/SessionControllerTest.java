package fr.paloit.paloformation;

import fr.paloit.paloformation.controller.SessionController;
import fr.paloit.paloformation.model.Session;
import fr.paloit.paloformation.service.SessionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;


@WebMvcTest
public class SessionControllerTest {

@Autowired
    private MockMvc mvc;

@MockBean
    private SessionService sessionService;









}
