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
import org.springframework.stereotype.Repository;

/**
 * @author hcadavid
 */
@Repository
public class InMemoryBlueprintPersistence implements BlueprintsPersistence {

  private final Map<Tuple<String, String>, Blueprint> blueprints = new HashMap<>();

  public InMemoryBlueprintPersistence() {
    // load stub data
    Point[] pts = new Point[] {new Point(140, 140), new Point(115, 115)};
    Blueprint bp = new Blueprint("_authorname_", "_bpname_", pts);
    blueprints.put(new Tuple<>(bp.getAuthor(), bp.getName()), bp);

    Point[] pts1 = new Point[] {new Point(300, 300), new Point(100, 100)};
    Blueprint bp1 = new Blueprint("Daniel", "bp1", pts1);
    blueprints.put(new Tuple<>(bp1.getAuthor(), bp1.getName()), bp1);

    Point[] pts2 = new Point[] {new Point(225, 225), new Point(25, 25)};
    Blueprint bp2 = new Blueprint("Daniel", "bp2", pts2);
    blueprints.put(new Tuple<>(bp2.getAuthor(), bp2.getName()), bp2);

    Point[] pts3 = new Point[] {new Point(7, 7), new Point(10, 10)};
    Blueprint bp3 = new Blueprint("Vicente", "bp3 ", pts3);
    blueprints.put(new Tuple<>(bp3.getAuthor(), bp3.getName()), bp3);
  }

  @Override
  public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException {
    if (blueprints.containsKey(new Tuple<>(bp.getAuthor(), bp.getName()))) {
      throw new BlueprintPersistenceException("The given blueprint already exists: " + bp);
    } else {
      blueprints.put(new Tuple<>(bp.getAuthor(), bp.getName()), bp);
    }
  }

  @Override
  public Blueprint getBlueprint(String author, String bprintname)
      throws BlueprintNotFoundException {
    Tuple<String, String> key = new Tuple<>(author, bprintname);
    if (!blueprints.containsKey(key)) {
      throw new BlueprintNotFoundException("The given blueprint does not exist: " + bprintname);
    }
    return blueprints.get(key);
  }

  @Override
  public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
    Set<Blueprint> authorBlueprints = new HashSet<>();
    for (Blueprint bp : blueprints.values()) {
      if (bp.getAuthor().equals(author)) {
        authorBlueprints.add(bp);
      }
    }
    if (authorBlueprints.isEmpty())
      throw new BlueprintNotFoundException("The given author does not exist");
    return authorBlueprints;
  }

  @Override
  public Set<Blueprint> getAllBlueprints() {
    return new HashSet<>(blueprints.values());
  }

  @Override
  public void updateBlueprint(String author, String name, List<Point> points)
      throws BlueprintNotFoundException {
    try {
      Blueprint existing = getBlueprint(author, name);
      existing.setPoints(points);
    } catch (BlueprintNotFoundException e) {
      throw new BlueprintNotFoundException("The given blueprint does not exist: " + name);
    }
  }
}
