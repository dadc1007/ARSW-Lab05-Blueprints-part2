package edu.eci.arsw.blueprints.services.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.services.BluePrintsFilter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RedundancyFilter implements BluePrintsFilter {
  @Override
  public void applyFilter(Blueprint blueprint) {
    List<Point> points = blueprint.getPoints();
    List<Point> filteredPoints = new ArrayList<>();

    if (!points.isEmpty()) {
      filteredPoints.add(points.get(0));
      for (int i = 1; i < points.size(); i++) {
        Point previous = points.get(i - 1);
        Point current = points.get(i);

        if (!current.equals(previous)) {
          filteredPoints.add(current);
        }
      }

      blueprint.setPoints(filteredPoints);
    }
  }
}
