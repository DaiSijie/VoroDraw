package ch.maystre.gilbert.vorodraw.voronoicomputation;

import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.maystre.gilbert.vorodraw.geometry.BoundedPolygon;
import ch.maystre.gilbert.vorodraw.geometry.HalfPlane;
import ch.maystre.gilbert.vorodraw.geometry.Point;

/**
 * A simple O(n^3) implementation where each Voronoi region is simply computed using a naive
 * half-space intersection algorithm
 */
public class NaiveVoronoiComputer extends VoronoiComputer{

    private final Map<Point, List<Point>> neighborsOf;
    private final Map<Point, BoundedPolygon> centerToRegion;
    private final Point lb;
    private final Point rt;

    public NaiveVoronoiComputer(Set<Point> centers, Point lb, Point rt){
        super(centers);
        this.neighborsOf = new HashMap<>();
        this.centerToRegion = new HashMap<>();
        this.lb = lb;
        this.rt = rt;
    }

    @Override
    public void compute() {
        // step 1: compute all regions
        for(Point center: centers){
            ArrayList<Point> cpy = new ArrayList<>(centers);
            cpy.remove(center);
            centerToRegion.put(center, computeRegion(center, cpy));
        }

        //step 2: compute the graph
        for(Point center: centers)
            neighborsOf.put(center, centerToRegion.get(center).getCreatorNeighbors());
    }

    @Override
    public List<Point> getNeighbors(Point center) {
        return neighborsOf.get(center);
    }

    @Override
    public BoundedPolygon getRegionForCenter(Point center) {
        return centerToRegion.get(center);
    }

    private BoundedPolygon computeRegion(Point center, List<Point> neighbors){
        BoundedPolygon toReturn = new BoundedPolygon(this.lb, this.rt);

        //compute the intersection of all bissector planes
        for(Point p : neighbors)
            toReturn.intersect(new HalfPlane(center, p), p);

        return toReturn;
    }
}