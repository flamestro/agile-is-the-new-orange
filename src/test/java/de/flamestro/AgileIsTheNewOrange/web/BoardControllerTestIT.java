package de.flamestro.AgileIsTheNewOrange.web;

import de.flamestro.AgileIsTheNewOrange.DataProvider;
import de.flamestro.AgileIsTheNewOrange.util.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class BoardControllerTestIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenCreateBoardWithEmptyName_thenReturnHttpStatusBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/board")
                .param("name", "")
                .param("userId", DataProvider.generateRandomString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(equalTo("{\"status\":\"INVALID_NAME\"}")));
    }

    @Test
    void whenCreateBoard_thenReturnHttpStatusOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/board")
                .param("name", DataProvider.generateRandomString())
                .param("userId", DataProvider.generateRandomString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}