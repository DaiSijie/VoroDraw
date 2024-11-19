package ch.maystre.gilbert.vorodraw.geometry;

public class Segment {

    public final Line line;
    public Point start;
    public Point end;

    public Segment(Line line, Point start, Point end){
        this.line = line;
        this.start = start;
        this.end = end;
    }

    /**
     * Compute the intersection between the segment and a line
     *
     * @param other The line to intersect
     * @return the intersection if it exists else null (also if segment and line are parallel)
     */
    public Point intersect(Line other){
        Point intersection = line.intersect(other);
        if(intersection == null)
            return null;

        // let us check that it is within the bounds
        boolean xGood = Math.min(start.x, end.x) <= intersection.x && intersection.x <= Math.max(start.x, end.x);
        boolean yGood = Math.min(start.y, end.y) <= intersection.y && intersection.y <= Math.max(start.y, end.y);

        return (xGood && yGood) ? intersection : null;
    }

}
