package ch.maystre.gilbert.vorodraw.voronoicomputation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.maystre.gilbert.vorodraw.geometry.BoundedPolygon;
import ch.maystre.gilbert.vorodraw.geometry.HalfPlane;
import ch.maystre.gilbert.vorodraw.geometry.Point;

public class NaiveIterativeVoronoiComputer extends IterativeVoronoiComputer{

    private final Set<Point> points;
    private final Map<Point, List<Point>> neighborsOf;
    private final Map<Point, BoundedPolygon> centerToRegion;
    private final Point lb;
    private final Point rt;

    public NaiveIterativeVoronoiComputer(Point lb, Point rt){
        this.points = new HashSet<>();
        this.neighborsOf = new HashMap<>();
        this.centerToRegion = new HashMap<>();
        this.lb = lb;
        this.rt = rt;
    }

    @Override
    public BoundedPolygon peek(Point potentialCenter) {
        return computeRegion(potentialCenter, points);
    }

    @Override
    public void addCenter(Point center){
        if(points.contains(center))
            return;

        //trim-off old regions
        for(Point c: points)
            centerToRegion.get(c).intersect(new HalfPlane(c, center), center);

        //add new region
        centerToRegion.put(center, peek(center));
        points.add(center);

        //re-compute neighbor graphs
        neighborsOf.clear();
        for(Point c: points)
            neighborsOf.put(c, centerToRegion.get(c).getCreatorNeighbors());
    }

    private BoundedPolygon computeRegion(Point center, Set<Point> points){
        BoundedPolygon toReturn = new BoundedPolygon(this.lb, this.rt);

        //compute the intersection of all bissector planes
        for(Point p : points)
            toReturn.intersect(new HalfPlane(center, p), p);

        return toReturn;
    }

    @Override
    public List<Point> getNeighbors(Point center) {
        return neighborsOf.get(center);
    }

    @Override
    public BoundedPolygon getRegionForCenter(Point center) {
        return centerToRegion.get(center);
    }

    @Override
    public Set<Point> getCenters() {
        return points;
    }

    @Override
    public void restart() {
        points.clear();
        neighborsOf.clear();
        centerToRegion.clear();
    }

}
