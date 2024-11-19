package ch.maystre.gilbert.vorodraw.coloringcomputation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import ch.maystre.gilbert.vorodraw.MainActivity;

import static ch.maystre.gilbert.vorodraw.coloringcomputation.MutableGraph.Node;

public class FiveColorer<T> extends ColoringComputer<T> {

    private Map<Node, Integer> colorMap;
    private final Map<T, Integer> hint;

    public FiveColorer(MutableGraph<T> g, Map<T, Integer> hint){
        super(g);
        this.hint = hint;
        this.colorMap = new HashMap<>();
    }

    @Override
    public void compute(){
        Stack<Operation> decomposed = decompose();
        fillColorMap(decomposed);
    }

    @Override
    public Map<Node, Integer> getNodeColorMap() {
        return colorMap;
    }

    private Stack<Operation> decompose(){
        Stack<Operation> st = new Stack<>();

        // push operations and peel-off until there is no more vertices in the graph.
        while(!graph.isEmpty()){
            // case where there is an easy candidate removal
            Node removeCandidate = graph.findRemoveCandidate();
            if(removeCandidate != null){
                st.push(removeNode(removeCandidate));
                continue;
            }

            // else, it is a contract situation
            Node[] contractCandidates = graph.findContractCandidates();
            st.push(contractNodes(contractCandidates[0], contractCandidates[1], contractCandidates[2]));
        }

        return st;
    }

    private RemoveOperation removeNode(Node node){
        graph.removeNode(node);
        return new RemoveOperation(node);
    }

    private ContractOperation contractNodes(Node x, Node n1, Node n2){
        // remove the nodes from the graph
        graph.removeNode(x);
        graph.removeNode(n1);
        graph.removeNode(n2);

        // add the new contracted node n
        HashSet<Node> newNeighbors = new HashSet<>();
        newNeighbors.addAll(n1.neighbors); // those don't contain x anymore!
        newNeighbors.addAll(n2.neighbors); // those don't contain x anymore!
        Node n = new Node(false, newNeighbors);
        graph.addNode(n);

        return new ContractOperation(x, n1, n2, n);
    }

    private void fillColorMap(Stack<Operation> decomposed){
        while(!decomposed.empty()){
            Operation toTreat = decomposed.pop();

            if(toTreat instanceof FiveColorer.RemoveOperation){
                Node s = ((RemoveOperation) toTreat).n;
                graph.addNode(s);
                colorMap.put(s, getFreeColor(s.neighbors, s));
            }
            else if(toTreat instanceof FiveColorer.ContractOperation){
                Node x = ((ContractOperation) toTreat).x;
                Node n1 = ((ContractOperation) toTreat).n1;
                Node n2 = ((ContractOperation) toTreat).n2;
                Node newNode = ((ContractOperation) toTreat).newNode;

                System.out.println("contract");

                colorMap.put(n1, colorMap.get(newNode));
                colorMap.put(n2, colorMap.get(newNode));

                graph.removeNode(newNode);
                graph.addNode(n1);
                graph.addNode(n2);
                graph.addNode(x);

                colorMap.put(x, getFreeColor(x.neighbors, x));
            }
        }
    }

    private int getFreeColor(Set<Node> neighbors, Node cur){
        HashSet<Integer> neighborColors = new HashSet<>();
        for(Node n : neighbors){
            neighborColors.add(colorMap.get(n));
        }

        if(cur.originalNode){
            T o = graph.representing.get(cur);
            if(hint.containsKey(o)){
                int prevColor = hint.get(o);
                if(!neighborColors.contains(prevColor))
                    return prevColor;
            }
        }

        int color = MainActivity.RANDOM.nextInt(5);
        while(neighborColors.contains(color))
            color = MainActivity.RANDOM.nextInt(5);

        return color;
    }

    private static class Operation{}

    private static class RemoveOperation extends Operation{
        private final Node n;
        private RemoveOperation(Node n){
            this.n = n;
        }
    }

    private static class ContractOperation extends Operation{
        private final Node x, n1, n2, newNode;
        private ContractOperation(Node x, Node n1, Node n2, Node newNode){
            this.x = x;
            this.n1 = n1;
            this.n2 = n2;
            this.newNode = newNode;
        }
    }

}
