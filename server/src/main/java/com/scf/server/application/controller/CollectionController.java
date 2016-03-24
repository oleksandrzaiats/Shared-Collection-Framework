package com.scf.server.application.controller;

import com.scf.server.application.model.exception.InvalidBeanException;
import com.scf.shared.dto.CollectionDTO;
import com.scf.server.application.processor.CollectionProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller with CRUD operations.
 * Controller contains only request/response logic of API.
 * All "business logic" is in {@link CollectionProcessor}
 */
@RestController
@RequestMapping("/collection")
public class CollectionController extends AbstractController {

    @Autowired
    CollectionProcessor collectionProcessor;

    @RequestMapping(method = RequestMethod.GET, path = "/")
    public
    @ResponseBody
    ResponseEntity<?> getCollections(@RequestParam(value = "shared_key") Optional<String> sharedKey) {
        if (sharedKey.isPresent()) {
            return getSharedCollection(sharedKey.get());
        } else {
            return new ResponseEntity<>(collectionProcessor.getAll(getCurrentUser()), HttpStatus.OK);
        }
    }

    private ResponseEntity<?> getSharedCollection(String sharedKey) {
        return new ResponseEntity<>(collectionProcessor.getBySharedKey(sharedKey), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public
    @ResponseBody
    ResponseEntity<?> get(@PathVariable Long id) {
        if (id == null) {
            throw new InvalidBeanException("Id parameter is null.");
        }
        return new ResponseEntity<>(collectionProcessor.get(id), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/")
    public
    @ResponseBody
    ResponseEntity<?> create(@RequestBody CollectionDTO collectionDTO) {
        collectionDTO.setUser(getCurrentUser().getUser());
        validateBean(collectionDTO);
        return new ResponseEntity<>(collectionProcessor.create(collectionDTO), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/")
    public
    @ResponseBody
    ResponseEntity<?> update(@RequestBody CollectionDTO collectionDTO) {
        validateBean(collectionDTO);
        return new ResponseEntity<>(collectionProcessor.update(collectionDTO, getCurrentUser()), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    public
    @ResponseBody
    ResponseEntity<?> delete(@PathVariable Long id) {
        if (id == null) {
            throw new InvalidBeanException("Id parameter is null.");
        }
        collectionProcessor.delete(id, getCurrentUser());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
