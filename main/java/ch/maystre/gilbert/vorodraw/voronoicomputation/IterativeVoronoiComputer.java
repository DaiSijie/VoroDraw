package ch.maystre.gilbert.vorodraw.voronoicomputation;

import java.util.List;
import java.util.Set;

import ch.maystre.gilbert.vorodraw.geometry.BoundedPolygon;
import ch.maystre.gilbert.vorodraw.geometry.Point;

public abstract class IterativeVoronoiComputer {

    /**
     * Return the polygon that would be created if the center is added WITHOUT affecting the current Voronoi
     * diagram. In particular, the neighbor list is NOT updated accordingly.
     *
     * @param potentialCenter The potential center
     *
     * @return The polygon that would be created or null if the point is already in the collection
     */
    public abstract BoundedPolygon peek(Point potentialCenter);

    public abstract void addCenter(Point center);

    public abstract List<Point> getNeighbors(Point center);

    public abstract BoundedPolygon getRegionForCenter(Point center);

    public abstract Set<Point> getCenters();

    public abstract void restart();

}
