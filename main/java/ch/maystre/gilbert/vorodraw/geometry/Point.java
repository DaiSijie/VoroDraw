package ch.maystre.gilbert.vorodraw.geometry;

import android.os.Parcel;
import android.os.Parcelable;

public class Point{

    public final double x;
    public final double y;

    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o){
        if(o == null || !(o instanceof Point))
            return false;
        Point p2 = (Point) o;
        return x == p2.x && y == p2.y;
    }

    @Override
    public int hashCode(){
        return (int) (x * y);
    }

    @Override
    public String toString(){
        return "(" + x + ", " + y + ")";
    }

}
