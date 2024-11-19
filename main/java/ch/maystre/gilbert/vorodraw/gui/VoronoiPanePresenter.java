package ch.maystre.gilbert.vorodraw.gui;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.maystre.gilbert.vorodraw.coloringcomputation.FiveColorer;
import ch.maystre.gilbert.vorodraw.coloringcomputation.MutableGraph;
import ch.maystre.gilbert.vorodraw.geometry.BoundedPolygon;
import ch.maystre.gilbert.vorodraw.geometry.Point;
import ch.maystre.gilbert.vorodraw.voronoicomputation.IterativeVoronoiComputer;
import ch.maystre.gilbert.vorodraw.voronoicomputation.NaiveIterativeVoronoiComputer;

public class VoronoiPanePresenter {

    private final VoronoiPane view;

    // computers
    private final IterativeVoronoiComputer computer;
    private FiveColorer<Point> colorer;

    // state
    private boolean currentlyTracking = false;
    private float currentX;
    private float currentY;
    private Map<Point, Integer> prevColoring = new HashMap<>();

    public VoronoiPanePresenter(VoronoiPane view, int screenWidth, int screenHeight){
        this.view = view;
        this.computer = new NaiveIterativeVoronoiComputer(new Point(-30, -30), new Point(screenWidth + 30, screenHeight + 30));
    }

    public Set<Point> getCenters(){
        return computer.getCenters();
    }

    public BoundedPolygon getRegionForCenter(Point center){
        return computer.getRegionForCenter(center);
    }

    public int getColorForCenter(Point center){
       return prevColoring.get(center);
    }

    public boolean peekMode(){
        return currentlyTracking;
    }

    public Point getPeekPoint(){
        return new Point(currentX, currentY);
    }

    public BoundedPolygon getPeekRegion(){
        return computer.peek(new Point(currentX, currentY));
    }

    public boolean onTouchEvent(MotionEvent e){
        currentX = e.getX();
        currentY = e.getY();
        if(e.getAction() == MotionEvent.ACTION_DOWN){
            currentlyTracking = true;
        }
        else if(e.getAction() == MotionEvent.ACTION_UP){
            currentlyTracking = false;
            computer.addCenter(new Point(currentX, currentY));

            // compute a coloring
            MutableGraph.Builder<Point> builder = new MutableGraph.Builder<>();
            for(Point center : computer.getCenters()){
                builder.addNode(center);
                for(Point n : computer.getNeighbors(center))
                    builder.addEdge(center, n);
            }

            // step 4: compute a coloring of the graph
            colorer = new FiveColorer<>(builder.build(), prevColoring);
            colorer.compute();
            prevColoring = new HashMap<>();
            for(Map.Entry<Point, Integer> ee : colorer.getColorMap().entrySet()){
                prevColoring.put(ee.getKey(), ee.getValue());
            }
        }
        view.invalidate();
        return true;
    }

    public void restart(){
        computer.restart();
        view.invalidate();
    }

    public Parcelable saveState(){
        Bundle b = new Bundle();

        // step 1: store centers
        ArrayList<Parcelable> centerParcels = new ArrayList<>();
        for(Point center : computer.getCenters()){
            Bundle inner = new Bundle();
            inner.putDouble("x", center.x);
            inner.putDouble("y", center.y);
            centerParcels.add(inner);
        }
        b.putParcelableArrayList("centers", centerParcels);

        // step 2: store currentColoring
        ArrayList<Parcelable> colorParcels = new ArrayList<>();
        for(Map.Entry<Point, Integer> e : prevColoring.entrySet()){
            Bundle inner = new Bundle();
            inner.putDouble("x", e.getKey().x);
            inner.putDouble("y", e.getKey().y);
            inner.putInt("color", e.getValue());
            colorParcels.add(inner);
        }
        b.putParcelableArrayList("colors", colorParcels);
        return b;
    }

    public void restoreState(Parcelable state){
        if(!(state instanceof Bundle)) {
            Log.d("SAVE:", "something bad happened");
            return;
        }

        Bundle b = (Bundle) state;

        // restore centers
        ArrayList<Parcelable> centerParcels = b.getParcelableArrayList("centers");
        for(Parcelable center : centerParcels) {
            Bundle inner = (Bundle) center;
            computer.addCenter(new Point(inner.getDouble("x"), inner.getDouble("y")));
        }

        ArrayList<Parcelable> colorParcels = b.getParcelableArrayList("colors");
        for(Parcelable colorEntry : colorParcels){
            Bundle inner = (Bundle) colorEntry;
            prevColoring.put(new Point(inner.getDouble("x"), inner.getDouble("y")), inner.getInt("color"));
        }

        view.invalidate();
    }

}
