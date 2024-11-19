package ch.maystre.gilbert.vorodraw.geometry;

public class Line {

    public final double a;
    public final double b;
    public final double c;

    public Line(double a, double b, double c){
        this.a = a;
        this.b = b;
        this.c = c;
    }

    /**
     * Constructs the line going through two points
     *
     * @param p1 the first point
     * @param p2 the second point
     */
    public Line(Point p1, Point p2){
        // look for vertical case first.
        if(p1.x == p2.x){
            this.a = 1;
            this.b = 0;
            this.c = -p1.x;
            return;
        }

        double m = (p1.y - p2.y)/(p1.x - p2.x);
        double h = p1.y - p1.x * m;
        this.a = m;
        this.b = -1;
        this.c = h;
    }

    /**
     * Check to see if the boundaries are matching
     *
     * @param other another plane
     * @return true iff the boundaries are the same
     */
    public boolean equivalent(Line other){
        if(this.b == 0 && other.b == 0){// both are vertical.
            return this.c/this.a == other.c/other.a;
        }
        else if(this.b != 0 && other.b != 0){
            return this.toFunction().equals(other.toFunction());
        }
        return false;
    }

    /**
     * Return the boundary of the plane as a function y = mx + h
     *
     * @return the function in the form (m, h). Warning: if the plane is vertical, the return is non-specified (but most
     * likely some infinity stuff)
     */
    public Point toFunction(){
        return new Point(-a/b, -c/b);
    }

    /**
     * Compute the intersection between two lines
     *
     * @param other the other line
     * @return the intersection or null if the lines are parallel
     */
    public Point intersect(Line other){
        Point func1 = this.toFunction();
        Point func2 = other.toFunction();

        // check carefully vertical cases
        if(this.b == 0 && other.b == 0){ //both are vertical: return null
            return null;
        }
        else if(this.b == 0){
            double x = -this.c/this.a;
            double y = func2.x * x + func2.y;
            return new Point(x, y);
        }
        else if(other.b == 0){
            double x = -other.c/other.a;
            double y = func1.x * x + func1.y;
            return new Point(x, y);
        }

        // else, we have the intersection of two non-vertical points!
        if(func1.x == func2.x) // both are parallel: return null
            return null;

        double x = (func2.y - func1.y)/(func1.x - func2.x);
        double y = func1.x * x + func1.y;
        return new Point(x, y);
    }

}
