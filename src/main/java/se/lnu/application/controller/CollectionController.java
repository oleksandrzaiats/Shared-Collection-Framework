package se.lnu.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.lnu.application.dto.CollectionDTO;
import se.lnu.application.processor.CollectionProcessor;

/**
 * Controller with CRUD operations.
 * Controller contains only request/response logic of API.
 * All "business logic" is in {@link CollectionProcessor}
 */
@RestController
@RequestMapping("/collection")
public class CollectionController {

    @Autowired
    CollectionProcessor collectionProcessor;

    @RequestMapping(method = RequestMethod.GET, path = "/")
    public
    @ResponseBody
    ResponseEntity<?> getAll() {
        return new ResponseEntity<>(collectionProcessor.getAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public
    @ResponseBody
    ResponseEntity<?> get(@PathVariable Long id) {
        return new ResponseEntity<>(collectionProcessor.get(id), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/")
    public
    @ResponseBody
    ResponseEntity<?> create(@RequestBody CollectionDTO collectionDTO) {
        return new ResponseEntity<>(collectionProcessor.create(collectionDTO), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/")
    public
    @ResponseBody
    ResponseEntity<?> update(@RequestBody CollectionDTO collectionDTO) {
        return new ResponseEntity<>(collectionProcessor.update(collectionDTO), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    public
    @ResponseBody
    ResponseEntity<?> delete(@PathVariable Long id) {
        collectionProcessor.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
