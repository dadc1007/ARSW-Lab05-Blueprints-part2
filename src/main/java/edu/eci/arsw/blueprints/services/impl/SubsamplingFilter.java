package edu.eci.arsw.blueprints.services.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.services.BluePrintsFilter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SubsamplingFilter implements BluePrintsFilter {
  @Override
  public void applyFilter(Blueprint blueprint) {
    List<Point> points = blueprint.getPoints();
    List<Point> filteredPoints = new ArrayList<>();

    for (int i = 0; i < points.size(); i++) {
      if (i % 2 == 0) {
        filteredPoints.add(points.get(i));
      }
    }

    blueprint.setPoints(filteredPoints);
  }
}
