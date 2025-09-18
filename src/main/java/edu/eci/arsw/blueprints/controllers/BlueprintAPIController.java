/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.controllers;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author hcadavid
 */
@RestController
@RequestMapping(value = "/blueprints")
@AllArgsConstructor
public class BlueprintAPIController {
  private final BlueprintsServices blueprintsServices;

  @GetMapping
  public ResponseEntity<?> getBlueprints() {
    try {
      return new ResponseEntity<>(blueprintsServices.getAllBlueprints(), HttpStatus.ACCEPTED);
    } catch (Exception e) {
      Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, e);
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/{author}")
  public ResponseEntity<?> getBlueprintsByAuthor(@PathVariable String author) {
    try {
      return new ResponseEntity<>(
          blueprintsServices.getBlueprintsByAuthor(author), HttpStatus.ACCEPTED);
    } catch (BlueprintNotFoundException e) {
      Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, e);
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/{author}/{bpname}")
  public ResponseEntity<?> getBlueprintsByAuthorAndBPName(
      @PathVariable String author, @PathVariable String bpname) {
    try {
      return new ResponseEntity<>(
          blueprintsServices.getBlueprint(author, bpname), HttpStatus.ACCEPTED);
    } catch (BlueprintNotFoundException e) {
      Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, e);
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping
  public ResponseEntity<?> createBlueprint(@RequestBody Blueprint blueprint) {
    try {
      blueprintsServices.addNewBlueprint(blueprint);
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (BlueprintPersistenceException e) {
      Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, e);
      return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }
  }

  @PutMapping("/{author}/{bpname}")
  public ResponseEntity<?> updateBlueprint(
      @PathVariable String author, @PathVariable String bpname, @RequestBody List<Point> points) {
    try {
      blueprintsServices.updateBlueprint(author, bpname, points);
      return new ResponseEntity<>(HttpStatus.ACCEPTED);
    } catch (BlueprintNotFoundException e) {
      Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, e);
      return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }
  }
}
