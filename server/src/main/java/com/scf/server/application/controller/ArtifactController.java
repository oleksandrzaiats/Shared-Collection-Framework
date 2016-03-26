package com.scf.server.application.controller;

import com.scf.server.application.model.exception.InvalidBeanException;
import com.scf.server.application.processor.ArtifactProcessor;
import com.scf.shared.dto.ArtifactDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Controller with CRUD operations.
 * Controller contains only request/response logic of API.
 * All "business logic" is in {@link ArtifactProcessor}
 */
@RestController
@RequestMapping("/artifact")
public class ArtifactController extends AbstractController {

    @Autowired
    ArtifactProcessor artifactProcessor;

    @RequestMapping(method = RequestMethod.GET, path = "/")
    public
    @ResponseBody
    ResponseEntity<?> getAll() {
        return new ResponseEntity<>(artifactProcessor.getAll(getCurrentUser()), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public
    @ResponseBody
    ResponseEntity<?> get(@PathVariable Long id) {
        if (id == null) {
            throw new InvalidBeanException("Id parameter is null.");
        }
        return new ResponseEntity<>(artifactProcessor.get(id), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}/file")
    public void getFile(@PathVariable Long id, HttpServletResponse response) throws IOException {
        ArtifactDTO artifactDTO = artifactProcessor.get(id);
        response.setHeader("Content-Disposition", "attachment;filename=\"" + artifactDTO.getFileName() + "\"");
        response.setContentType(artifactDTO.getContentType());
        OutputStream out = response.getOutputStream();
        out.write(artifactDTO.getFileBytes());
        out.close();
    }

    @RequestMapping(method = RequestMethod.POST, path = "/")
    public
    @ResponseBody
    ResponseEntity<?> create(@RequestParam("file") MultipartFile file, @RequestParam("name") String name) throws IOException {
        if (file == null) {
            throw new InvalidBeanException("Request must contain artifact file with key \"file\".");
        }
        if (name == null || name.isEmpty()) {
            throw new InvalidBeanException("Request must contain artifact name with key \"name\".");
        }
        ArtifactDTO artifactDTO = new ArtifactDTO();
        artifactDTO.setName(name);
        artifactDTO.setFileBytes(file.getBytes());
        artifactDTO.setContentType(file.getContentType());
        artifactDTO.setFileName(file.getOriginalFilename());
        artifactDTO.setUser(getCurrentUser().getUser());
        validateBean(artifactDTO);
        return new ResponseEntity<>(artifactProcessor.create(artifactDTO), HttpStatus.OK);
    }

    /**
     * Update artifact's file.
     *
     * @param file file which will replace existing file.
     * @param id   identifier of artifact.
     * @return artifact with new properties.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/{id}/file")
    public
    @ResponseBody
    ResponseEntity<?> updateFile(@RequestParam("file") MultipartFile file, @PathVariable Long id) throws IOException {
        if (file == null) {
            throw new InvalidBeanException("Request must contain artifact file with key \"file\".");
        }
        if (id == null) {
            throw new InvalidBeanException("Request must contain artifact id with key \"id\".");
        }
        ArtifactDTO artifactDTO = artifactProcessor.get(id);
        artifactDTO.setFileBytes(file.getBytes());
        artifactDTO.setContentType(file.getContentType());
        artifactDTO.setFileName(file.getOriginalFilename());
        validateBean(artifactDTO);
        return new ResponseEntity<>(artifactProcessor.update(artifactDTO, getCurrentUser()), HttpStatus.OK);
    }

    /**
     * Update artifact information.
     *
     * @param artifactDTO changed artifact.
     * @return updated artifact.
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/")
    public
    @ResponseBody
    ResponseEntity<?> update(@RequestBody ArtifactDTO artifactDTO) {
        validateBean(artifactDTO);
        return new ResponseEntity<>(artifactProcessor.update(artifactDTO, getCurrentUser()), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    public
    @ResponseBody
    ResponseEntity<?> delete(@PathVariable Long id) {
        if (id == null) {
            throw new InvalidBeanException("Id parameter is null.");
        }
        artifactProcessor.delete(id, getCurrentUser());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
