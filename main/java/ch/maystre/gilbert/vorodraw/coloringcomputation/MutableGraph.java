package ch.maystre.gilbert.vorodraw.coloringcomputation;

import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MutableGraph<T> {

    public final Map<Node, T> representing;
    public final Set<Node> nodes;

    private MutableGraph(Map<Node, T> representing, Set<Node> nodes){
        this.representing = representing;
        this.nodes = nodes;
    }

    public boolean isEmpty(){
        return nodes.isEmpty();
    }

    /**
     * Removes symmetrically a node
     *
     * @param node the node (with correct neighbors)
     */
    public void removeNode(Node node){
        nodes.remove(node);
        for(Node neighbor : node.neighbors){
            if(node == neighbor){
                Log.d("FUU", "problem!");
            }
            neighbor.neighbors.remove(node);
        }

    }

    /**
     * Adds symmetrically a new node
     *
     * @param node the new node (already containing the correct neighbors)
     */
    public void addNode(Node node){
        nodes.add(node);
        for(Node n : node.neighbors)
            n.neighbors.add(node);
    }

    /**
     * Finds a node candidate for removal or null if none exits
     *
     * @return a removal candidate
     */
    public Node findRemoveCandidate(){
        for(Node node : nodes){
            if(node.neighbors.size() < 5)
                return node;
        }
        return null;
    }

    /**
     * Finds a candidate for contraction or null if none exists
     *
     * @return [x, n1, n2]
     */
    public Node[] findContractCandidates(){
        for(Node x : nodes){
            if(x.neighbors.size() == 5){
                Node n1 = null;
                for(Node neighbor : x.neighbors){
                    if(neighbor.neighbors.size() <= 7){
                        if(n1 == null){
                            n1 = neighbor;
                        }
                        else{
                            return new Node[]{x, n1, neighbor};
                        }
                    }
                }
            }
        }

        return null;
    }

    public void print(){
        for(Node node : nodes){
            System.out.println("For node: " + node);
            System.out.print("   ");
            for(Node nn : node.neighbors){
                System.out.print(nn + " ");
            }
            System.out.println("");
        }
    }

    public static class Builder<T>{

        private final Map<T, Node> objectToNode;
        private final Set<Node> nodes;

        public Builder(){
            this.objectToNode = new HashMap<>();
            this.nodes = new HashSet<>();
        }

        public void addNode(T o){
            if(!objectToNode.keySet().contains(o)){
                Node n = new Node(true, new HashSet<Node>());
                nodes.add(n);
                objectToNode.put(o, n);
            }
        }

        public void addEdge(T o1, T o2){
            if(!objectToNode.containsKey(o1))
                objectToNode.put(o1, new Node(true, new HashSet<Node>()));
            if(!objectToNode.containsKey(o2))
                objectToNode.put(o2, new Node(true, new HashSet<Node>()));

            Node n1 = objectToNode.get(o1);
            Node n2 = objectToNode.get(o2);

            nodes.add(n1);
            nodes.add(n2);

            n1.neighbors.add(n2);
            n2.neighbors.add(n1);
        }

        public MutableGraph<T> build(){
            // invert the map
            Map<Node, T> representing = new HashMap<>();
            for(Map.Entry<T, Node> e : objectToNode.entrySet())
                representing.put(e.getValue(), e.getKey());

            return new MutableGraph<T>(representing, nodes);
        }
    }

    public static class Node{

        public final boolean originalNode;
        public final Set<Node> neighbors;

        public Node(boolean originalNode, Set<Node> neighbors){
            this.originalNode = originalNode;
            this.neighbors = neighbors;
        }

    }

}
