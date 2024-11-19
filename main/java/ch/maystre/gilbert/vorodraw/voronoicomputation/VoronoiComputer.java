package ch.maystre.gilbert.vorodraw.voronoicomputation;

import java.util.List;
import java.util.Set;

import ch.maystre.gilbert.vorodraw.geometry.BoundedPolygon;
import ch.maystre.gilbert.vorodraw.geometry.Point;

public abstract class VoronoiComputer {

    public final Set<Point> centers;

    public VoronoiComputer(Set<Point> centers){
        this.centers = centers;
    }

    public abstract void compute();

    public abstract List<Point> getNeighbors(Point center);

    public abstract BoundedPolygon getRegionForCenter(Point center);

}
