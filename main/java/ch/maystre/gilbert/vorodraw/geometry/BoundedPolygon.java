package ch.maystre.gilbert.vorodraw.geometry;

import java.util.ArrayList;
import java.util.List;

public class BoundedPolygon {

    private PolygonEdge head;

    /**
     * Constructs a new rectangle polygon
     *
     * @param lb The lower bottom coordinate of the polygon
     * @param rt the right top coordinate of the polygon
     */
    public BoundedPolygon(Point lb, Point rt){
        Point lt = new Point(lb.x, rt.y);
        Point rb = new Point(rt.x, lb.y);

        PolygonEdge eLeft = new PolygonEdge(new Segment(new Line(lt, lb), lt, lb));
        PolygonEdge eBottom = new PolygonEdge(new Segment(new Line(lb, rb), lb, rb));
        PolygonEdge eRight = new PolygonEdge(new Segment(new Line(rb, rt), rb, rt));
        PolygonEdge eTop = new PolygonEdge(new Segment(new Line(rt, lt), rt, lt));

        eLeft.next = eBottom;
        eBottom.next = eRight;
        eRight.next = eTop;
        eTop.next = eLeft;

        this.head = eLeft;
    }

    public List<Point> getPolygonNodes(){
        ArrayList<Point> toReturn = new ArrayList<>();

        toReturn.add(head.segment.start);
        PolygonEdge current = head.next;
        while(current != head){
            toReturn.add(current.segment.start);
            current = current.next;
        }

        return toReturn;
    }

    public List<Point> getCreatorNeighbors(){
        ArrayList<Point> toReturn = new ArrayList<>();

        if(head.createdBy != null) // null means it's a border
            toReturn.add(head.createdBy);

        PolygonEdge current = head.next;
        while(current != head){
            if(current.createdBy != null)
                toReturn.add(current.createdBy);
            current = current.next;
        }

        return toReturn;
    }

    /**
     * Should not make the plane empty!
     * @param plane
     */
    public void intersect(HalfPlane plane, Point creator){
        if(matchesBoundary(plane))
            return;

        // step 1: find all intersections between the polygon and the boundary of the plane
        ArrayList<Point> intersections = new ArrayList<>();
        ArrayList<PolygonEdge> crossers = new ArrayList<>();
        PolygonEdge current = this.head;
        do{
            Point p = current.intersect(plane.getBoundary());
            if(p != null) {
                intersections.add(p);
                crossers.add(current);
            }
            current = current.next;
        } while(current != this.head);

        // step 2: interpret the intersection to find end and start of intersection path
        if(intersections.size() < 2)
            return;
        Point int1 = intersections.get(0);
        Point int2 = intersections.get(intersections.size() - 1);
        PolygonEdge e1 = crossers.get(0);
        PolygonEdge e2 = crossers.get(crossers.size() - 1);

        // step 3: modify the polygon accordingly
        if(plane.isIn(e1.segment.start)){
            e1.segment.end = int1;
            e2.segment.start = int2;
            PolygonEdge n = new PolygonEdge(new Segment(plane.getBoundary(), e1.segment.end, e2.segment.start), creator);
            e1.next = n;
            n.next = e2;
            this.head = n;
        }
        else{
            e1.segment.start = int1;
            e2.segment.end = int2;
            PolygonEdge n = new PolygonEdge(new Segment(plane.getBoundary(), e2.segment.end, e1.segment.start), creator);
            n.next = e1;
            e2.next = n;
            this.head = n;
        }
    }

    private boolean matchesBoundary(HalfPlane plane){
        PolygonEdge current = head;
        do{
            if(current.segment.line.equivalent(plane.getBoundary()))
                return true;
            current = current.next;
        }while(current != head);
        return false;
    }

    private static class PolygonEdge{

        public final Point createdBy;
        private final Segment segment;
        private PolygonEdge next;

        private PolygonEdge(Segment segment){
            this.segment = segment;
            this.createdBy = null;
        }

        private PolygonEdge(Segment segment, Point createdBy){
            this.segment = segment;
            this.createdBy = createdBy;
        }

        private Point intersect(Line line){
            return segment.intersect(line);
        }

    }

}
