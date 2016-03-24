package com.scf.server.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scf.server.application.processor.ArtifactProcessor;
import com.scf.server.application.processor.CollectionProcessor;
import com.scf.server.application.processor.UserProcessor;
import com.scf.server.application.security.AuthUser;
import com.scf.server.application.security.UserAuthentication;
import com.scf.server.application.security.UserRole;
import com.scf.server.configuration.SpringRootConfig;
import com.scf.shared.dto.ArtifactDTO;
import com.scf.shared.dto.CollectionDTO;
import com.scf.shared.dto.UserDTO;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringRootConfig.class)
@WebAppConfiguration
@EnableWebMvc
public class CollectionControllerTest {

    @Rule
    public final RestDocumentation restDocumentation = new RestDocumentation("build/generated-snippets");

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserProcessor userProcessor;

    @Autowired
    private ArtifactProcessor artifactProcessor;

    @Autowired
    private CollectionProcessor collectionProcessor;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private RestDocumentationResultHandler document;

    private UserDTO userDTO;
    private ArtifactDTO artifactDTO;
    private CollectionDTO collectionDTO;

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

        List<ArtifactDTO> artifactDTOs = new ArrayList<>();
        artifactDTOs.add(artifactDTO);
        collectionDTO = getCollection(userDTO, artifactDTOs, new ArrayList<>());

        Assert.assertNotNull(userDTO.getId());
        Assert.assertNotNull(artifactDTO.getId());
        Assert.assertNotNull(collectionDTO.getId());
    }

    @After
    public void tearDown() {
        //TODO: add delete logic here
    }

    @Test
    public void getCollectionBySharedKey() throws Exception {
        this.document.snippets(
                responseFields(
                        fieldWithPath("id").description("Collection's id"),
                        fieldWithPath("name").description("Collection's name"),
                        fieldWithPath("key").description("Collection's key"),
                        fieldWithPath("user").description("Collection's user"),
                        fieldWithPath("collectionList").description("List of collections which are included to the collection"),
                        fieldWithPath("artifactList").description("List of artifacts which are included to the collection")
                )
        );

        UserAuthentication userAuthentication = new UserAuthentication(authUser);
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);

        this.mockMvc.perform(
                get("/collection/")
                        .param("shared_key", collectionDTO.getKey())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getCollectionById() throws Exception {
        this.document.snippets(
                responseFields(
                        fieldWithPath("id").description("Collection's id"),
                        fieldWithPath("name").description("Collection's name"),
                        fieldWithPath("key").description("Collection's key"),
                        fieldWithPath("user").description("Collection's user"),
                        fieldWithPath("collectionList").description("List of collections which are included to the collection"),
                        fieldWithPath("artifactList").description("List of artifacts which are included to the collection")
                )
        );

        UserAuthentication userAuthentication = new UserAuthentication(authUser);
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);

        this.mockMvc.perform(
                get("/collection/" + collectionDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void createCollection() throws Exception {
        this.document.snippets(
                responseFields(
                        fieldWithPath("id").description("Collection's id"),
                        fieldWithPath("name").description("Collection's name"),
                        fieldWithPath("key").description("Collection's key"),
                        fieldWithPath("user").description("Collection's user"),
                        fieldWithPath("collectionList").description("List of collections which are included to the collection"),
                        fieldWithPath("artifactList").description("List of artifacts which are included to the collection")
                )
        );

        UserAuthentication userAuthentication = new UserAuthentication(authUser);
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);

        List<ArtifactDTO> artifactDTOs = new ArrayList<>();
        artifactDTOs.add(artifactDTO);

        List<CollectionDTO> collectionDTOs = new ArrayList<>();
        collectionDTOs.add(collectionDTO);

        CollectionDTO newCollectionDTO = getCollection(userDTO, artifactDTOs,collectionDTOs);

        this.mockMvc.perform(
                post("/collection/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(newCollectionDTO)))
                .andExpect(status().isOk());

        collectionDTO = newCollectionDTO;
    }

    @Test
    public void updateCollection() throws Exception {
        this.document.snippets(
                responseFields(
                        fieldWithPath("id").description("Collection's id"),
                        fieldWithPath("name").description("Collection's name"),
                        fieldWithPath("key").description("Collection's key"),
                        fieldWithPath("user").description("Collection's user"),
                        fieldWithPath("collectionList").description("List of collections which are included to the collection"),
                        fieldWithPath("artifactList").description("List of artifacts which are included to the collection")
                )
        );

        UserAuthentication userAuthentication = new UserAuthentication(authUser);
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);

        collectionDTO.setName("new_name");

        this.mockMvc.perform(
                put("/collection/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(collectionDTO)))
                .andExpect(status().isOk());

    }

    @Test
    public void deleteCollection() throws Exception {

        UserAuthentication userAuthentication = new UserAuthentication(authUser);
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);

        List<ArtifactDTO> artifactDTOs = new ArrayList<>();
        artifactDTOs.add(artifactDTO);

        List<CollectionDTO> collectionDTOs = new ArrayList<>();
        collectionDTOs.add(collectionDTO);

        CollectionDTO newCollectionDTO = getCollection(userDTO, artifactDTOs,collectionDTOs);

        this.mockMvc.perform(
                delete("/collection/" + newCollectionDTO.getId()))
                .andExpect(status().isNoContent());

    }

    private UserDTO getUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("test");
        userDTO.setRole(UserRole.ROLE_USER.toString());
        userDTO.setLogin("test_user_login");
        userDTO.setPassword("test");

        return userProcessor.create(userDTO);
    }
    private CollectionDTO getCollection(UserDTO userDTO, List<ArtifactDTO> artifactDTOs, List<CollectionDTO> collectionDTOs) {
        CollectionDTO collectionDTO = new CollectionDTO();
        collectionDTO.setName("test");
        collectionDTO.setUser(userDTO);
        collectionDTO.setArtifactList(artifactDTOs);
        collectionDTO.setCollectionList(collectionDTOs);
        collectionDTO.setKey("test");

        return collectionProcessor.create(collectionDTO);
    }

    private ArtifactDTO getArtifact(UserDTO userDTO) throws IOException {
        ArtifactDTO artifactDTO = new ArtifactDTO();
        artifactDTO.setName("test2");
        artifactDTO.setContentType("application/json");
        artifactDTO.setFileBytes("265001916915724 FooBar 265001916915724".getBytes());
        artifactDTO.setFileName("test2");
        artifactDTO.setUser(userDTO);

        return artifactProcessor.create(artifactDTO);
    }
}