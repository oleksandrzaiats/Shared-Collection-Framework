package com.scf.server.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scf.server.application.processor.ArtifactProcessor;
import com.scf.server.application.security.AuthUser;
import com.scf.server.application.security.UserAuthentication;
import com.scf.server.configuration.SpringRootConfig;
import com.scf.shared.dto.ArtifactDTO;
import com.scf.shared.dto.UserDTO;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringRootConfig.class)
@WebAppConfiguration
@EnableWebMvc
public class ArtifactControllerTest extends AbstractControllerTest {

    @Rule
    public final RestDocumentation restDocumentation = new RestDocumentation("build/generated-snippets");

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ArtifactProcessor artifactProcessor;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private RestDocumentationResultHandler document;

    private ArtifactDTO artifactDTO;
    private UserDTO userDTO;

    private AuthUser authUser;

    @Before
    public void setUp() throws IOException {
        this.document = document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(this.document)
                .build();

        userDTO = getUser();
        authUser = new AuthUser(userDTO);

        artifactDTO = getArtifact(userDTO);

        assertNotNull(userDTO.getId());
        assertNotNull(artifactDTO.getId());
    }

    @After
    public void tearDown() {

        artifactProcessor.delete(artifactDTO.getId(), authUser);
        userProcessor.delete(userDTO.getId(), authUser);
    }

    @Test
    public void getAllArtifacts() throws Exception {
        this.document.snippets(
                responseFields(
                        fieldWithPath("[].id").description("The artifact's id"),
                        fieldWithPath("[].name").description("The artifact's name"),
                        fieldWithPath("[].fileName").description("The artifact's filename"),
                        fieldWithPath("[].contentType").description("The artifact's content type"),
                        fieldWithPath("[].user").description("The artifact's user")
                )
        );

        UserAuthentication userAuthentication = new UserAuthentication(authUser);
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);

        this.mockMvc.perform(
                get("/artifact/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getArtifactById() throws Exception {
        this.document.snippets(
                responseFields(
                        fieldWithPath("id").description("The artifact's id"),
                        fieldWithPath("name").description("The artifact's name"),
                        fieldWithPath("fileName").description("The artifact's filename"),
                        fieldWithPath("contentType").description("The artifact's content type"),
                        fieldWithPath("user").description("The artifact's user")
                )
        );

        UserAuthentication userAuthentication = new UserAuthentication(authUser);
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);

        this.mockMvc.perform(
                get("/artifact/" + artifactDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getFile() throws Exception {

        UserAuthentication userAuthentication = new UserAuthentication(authUser);
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);

        this.mockMvc.perform(
                get("/artifact/" + artifactDTO.getId() + "/file")
                        .accept(MediaType.MULTIPART_FORM_DATA)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }

    @Test
    public void createArtifact() throws Exception {
        this.document.snippets(
                responseFields(
                        fieldWithPath("id").description("The artifact's id"),
                        fieldWithPath("name").description("The artifact's name"),
                        fieldWithPath("contentType").description("The artifact's name"),
                        fieldWithPath("fileName").description("The artifact's password"),
                        fieldWithPath("user").description("The artifact's password")
                )
        );

        UserAuthentication userAuthentication = new UserAuthentication(authUser);
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);

        FileInputStream fileInputStream = new FileInputStream(getFile("artifact.jpg"));
        MockMultipartFile file = new MockMultipartFile("file", "1", "multipart/form-data", fileInputStream);

        MvcResult result = mockMvc.perform(fileUpload("/artifact/")
                .file(file)
                .param("name", "test"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ArtifactDTO newArtifactDTO = new ObjectMapper().readValue(content, ArtifactDTO.class);
        artifactProcessor.delete(newArtifactDTO.getId(), authUser);
    }

    @Test
    public void updateArtifact() throws Exception {
        this.document.snippets(
                responseFields(
                        fieldWithPath("id").description("The artifact's id"),
                        fieldWithPath("name").description("The artifact's name"),
                        fieldWithPath("fileName").description("The artifact's filename"),
                        fieldWithPath("contentType").description("The artifact's content type"),
                        fieldWithPath("user").description("The artifact's user")
                )
        );

        UserAuthentication userAuthentication = new UserAuthentication(authUser);
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);

        this.mockMvc.perform(
                put("/artifact/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(artifactDTO)
                        ))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteArtifact() throws Exception {
        ArtifactDTO artifact = getArtifact(userDTO);

        UserAuthentication userAuthentication = new UserAuthentication(authUser);
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);

        this.mockMvc.perform(
                delete("/artifact/" + artifact.getId()))
                .andExpect(status().isNoContent());
    }

    private ArtifactDTO getArtifact(UserDTO userDTO) throws IOException {
        ArtifactDTO artifactDTO = new ArtifactDTO();
        artifactDTO.setName("test1");
        artifactDTO.setContentType("application/json");
        artifactDTO.setFileBytes("265001916915724 FooBar 265001916915724".getBytes());//(getFileBytes("artifact.jpg"));
        artifactDTO.setFileName("test2");
        artifactDTO.setUser(userDTO);

        return artifactProcessor.create(artifactDTO);
    }

    private File getFile(String pathFile) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(pathFile).getFile());
    }

}
