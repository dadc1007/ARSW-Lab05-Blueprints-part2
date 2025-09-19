/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.persistence.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

/**
 * @author hcadavid
 */
@Repository
public class InMemoryBlueprintPersistence implements BlueprintsPersistence {

  private final ConcurrentHashMap<Tuple<String, String>, Blueprint> blueprints = new ConcurrentHashMap<>();

  public InMemoryBlueprintPersistence() {
    storeInternal(new Blueprint("_authorname_", "_bpname_", new Point[] {new Point(140,140), new Point(115,115)}));
    storeInternal(new Blueprint("Daniel", "bp1", new Point[] {new Point(300,300), new Point(100,100)}));
    storeInternal(new Blueprint("Daniel", "bp2", new Point[] {new Point(225,225), new Point(25,25)}));
    storeInternal(new Blueprint("Vicente", "bp3 ", new Point[] {new Point(7,7), new Point(10,10)}));
  }

  //METODO QUE AYUDA A LA GESTION DEL ARREGLO PRINCIPAL Y LAS COPIAS DE LOS BLUEPRINTS
  private void storeInternal(Blueprint bp) {
    Tuple<String, String> key = new Tuple<>(bp.getAuthor(), bp.getName());
    Blueprint copy = copyBlueprint(bp);
    blueprints.put(key, copy);
  }

  //METODO PARA HACER LAS COPIAS PARA EVITAR COMPARTIR REFERENCIAS
  private Blueprint copyBlueprint(Blueprint bp) {
    List<Point> orig = bp.getPoints();
    List<Point> tempList = new ArrayList<>();

    if (orig != null) {
      for (Point p : orig) {
        tempList.add(new Point(p.getX(), p.getY()));
      }
    }
    Point[] ptsCopy = tempList.toArray(new Point[0]);

    return new Blueprint(bp.getAuthor(), bp.getName(), ptsCopy);
  }

  @Override
  public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException {
    Tuple<String, String> key = new Tuple<>(bp.getAuthor(), bp.getName());
    Blueprint copy = copyBlueprint(bp);
    Blueprint existing = blueprints.putIfAbsent(key, copy);
    if (existing != null) {
      throw new BlueprintPersistenceException("The given blueprint already exists: " + bp);
    }
  }

  @Override
  public Blueprint getBlueprint(String author, String bprintname)
      throws BlueprintNotFoundException {
    Tuple<String, String> key = new Tuple<>(author, bprintname);
    Blueprint bp = blueprints.get(key);
    if (bp == null) {
      throw new BlueprintNotFoundException("The given blueprint does not exist: " + bprintname);
    }
    return copyBlueprint(bp);
  }

  @Override
  public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
    Set<Blueprint> authorBlueprints = new HashSet<>();
    for (Blueprint bp : blueprints.values()) {
      if (bp.getAuthor().equals(author)) {
        authorBlueprints.add(copyBlueprint(bp));
      }
    }
    if (authorBlueprints.isEmpty())
      throw new BlueprintNotFoundException("The given author does not exist");
    return authorBlueprints;
  }

  @Override
  public Set<Blueprint> getAllBlueprints() {
    Set<Blueprint> allBlueprints = new HashSet<>();
    for (Blueprint bp : blueprints.values()){
      allBlueprints.add(copyBlueprint(bp));
    }
    return allBlueprints;
  }

  @Override
  public void updateBlueprint(String author, String name, List<Point> points)
      throws BlueprintNotFoundException {
    Tuple<String, String> key = new Tuple<>(author, name);
    Blueprint existing = blueprints.get(key);
    if (existing == null) {
      throw new BlueprintNotFoundException("The given blueprint does not exist: " + name);
    }
    Point[] ptsArr = new Point[points.size()];
    for (int i = 0; i < points.size(); i++) {
      Point p = points.get(i);
      ptsArr[i] = new Point(p.getX(), p.getY());
    }
    Blueprint updated = new Blueprint(existing.getAuthor(), existing.getName(), ptsArr);
    boolean replaced = blueprints.replace(key, existing, updated);
    if (!replaced){
      throw new BlueprintNotFoundException("The blueprint was modified concurrently: "+ name);
    }
  }
}
