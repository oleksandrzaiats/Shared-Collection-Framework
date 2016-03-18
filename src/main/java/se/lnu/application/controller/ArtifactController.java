package se.lnu.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import se.lnu.application.dto.ArtifactDTO;
import se.lnu.application.processor.ArtifactProcessor;

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
        return new ResponseEntity<>(artifactProcessor.getAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public
    @ResponseBody
    ResponseEntity<?> get(@PathVariable Long id) {
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
        ArtifactDTO artifactDTO = new ArtifactDTO();
        artifactDTO.setName(name);
        artifactDTO.setFileBytes(file.getBytes());
        artifactDTO.setContentType(file.getContentType());
        artifactDTO.setFileName(file.getOriginalFilename());
        return new ResponseEntity<>(artifactProcessor.create(artifactDTO), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/")
    public
    @ResponseBody
    ResponseEntity<?> update(@RequestBody ArtifactDTO artifactDTO) {
        return new ResponseEntity<>(artifactProcessor.update(artifactDTO), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    public
    @ResponseBody
    ResponseEntity<?> delete(@PathVariable Long id) {
        artifactProcessor.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
