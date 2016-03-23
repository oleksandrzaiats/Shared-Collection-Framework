package se.lnu.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;
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
import se.lnu.application.dto.UserDTO;
import se.lnu.application.processor.UserProcessor;
import se.lnu.application.security.UserRole;
import se.lnu.application.utils.PasswordHelper;
import se.lnu.configuration.SpringRootConfig;

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
public class AuthenticationControllerTest {

    @Rule
    public final RestDocumentation restDocumentation = new RestDocumentation("build/generated-snippets");

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserProcessor userProcessor;

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
        userDTO.setPassword("test");

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

    private UserDTO getUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("test");
        userDTO.setRole(UserRole.ROLE_USER.toString());
        userDTO.setLogin("test_user");
        userDTO.setPassword(PasswordHelper.encodePassword("test"));

        return userDTO;
    }
}
