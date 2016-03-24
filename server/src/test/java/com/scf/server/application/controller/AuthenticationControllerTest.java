package com.scf.server.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scf.server.application.InitialValue;
import com.scf.server.configuration.SpringRootConfig;
import com.scf.shared.dto.UserDTO;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringRootConfig.class)
@WebAppConfiguration
public class AuthenticationControllerTest extends AbstractControllerTest {

    @Rule
    public final RestDocumentation restDocumentation = new RestDocumentation("build/generated-snippets");

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private RestDocumentationResultHandler document;

    private UserDTO userDTO;

    @Before
    public void setUp() {
        this.document = document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(this.document)
                .build();

        userDTO = getUser();
    }

    @After
    public void tearDown() {
        userDTO = userProcessor.findUserByLogin(userDTO.getLogin());
        userProcessor.delete(userDTO.getId(), null);
    }

    @Test
    public void createUser() throws Exception {
        this.document.snippets(
                responseFields(
                        fieldWithPath("id").description("The user's id"),
                        fieldWithPath("login").description("The user's password"),
                        fieldWithPath("name").description("The user's name"),
                        fieldWithPath("password").description("The user's password")
                )
        );

        this.mockMvc.perform(
                post("/user")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(userDTO)
                        )).andExpect(status().isOk());
    }

    @Test
    public void loginUser() throws Exception {
        userDTO = userProcessor.create(userDTO);
        userDTO.setPassword("123123");

        this.document.snippets(
                responseFields(
                        fieldWithPath("token").description("The user's token")
                )
        );

        this.mockMvc.perform(
                post("/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(userDTO)
                        )).andExpect(status().isOk());
    }

    @Override
    protected UserDTO getUser() {
        UserDTO userDTO = userConverter.convertToDTO(InitialValue.getUserEntity());
        return userDTO;
    }
}
