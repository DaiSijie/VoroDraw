package ch.maystre.gilbert.vorodraw.coloringcomputation;

import java.util.HashMap;
import java.util.Map;

import static ch.maystre.gilbert.vorodraw.coloringcomputation.MutableGraph.Node;
import static java.util.Map.Entry;

public abstract class ColoringComputer<T> {

    public final MutableGraph<T> graph;

    private Map<T, Integer> colorMap;

    public ColoringComputer(MutableGraph<T> graph){
        this.graph = graph;
    }

    public abstract void compute();

    public abstract Map<Node, Integer> getNodeColorMap();

    public final Map<T, Integer> getColorMap(){
        if(colorMap == null){
            colorMap = new HashMap<>();
            for(Entry<Node, Integer> e : getNodeColorMap().entrySet()){
                if(e.getKey().originalNode)
                    colorMap.put(graph.representing.get(e.getKey()), e.getValue());
            }
        }
        return colorMap;
    }
}
