package fr.paloit.paloformation.ihm;

import fr.paloit.paloformation.controller.IndexController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(IndexController.class)
@AutoConfigureMockMvc
public class IndexTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testIndex() throws Exception {
        this.mvc.perform(get(""))
                .andExpect(status().isOk())
                .andExpect(view().name("formations"));
    }

}
