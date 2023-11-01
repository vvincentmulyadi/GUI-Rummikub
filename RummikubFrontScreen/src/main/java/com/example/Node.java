import java.util.List;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.tree.TreeNode;

import com.example.rummikubfrontscreen.setup.Board;

public class Node {
    private Board board;
    private Node parent;
    private Node[] children;
    private int visits;
    private double totalValue;
    static Random r = new Random();
    // arbitraty
    static int nActions = 5;
    //select whether the play is going to be a draw,put runs or rows or manipulate runs and rows
    private void selectAction(){
        List<Node> visited = new LinkedList<Node>();
        Node current = this;
        visited.add(this);
        while (!current.isLeaf()) {
            current = current.select();
            // System.out.println("Adding: " + cur);
            visited.add(current);
        }
        current.expand();
        Node newNode = current.select();
        visited.add(newNode);
        double value = rollOut(newNode);
        for (Node node : visited) {
            // would need extra logic for n-player game
            // System.out.println(node);
            node.updateStats(value);
        }
    }
    public void expand() {
        children = new Node[nActions];
        for (int i=0; i<nActions; i++) {
            children[i] = new Node();
        }
    }
    private Node select()
    {
        Node selected=null;
        double bestValue=0;
        for (Node child : children) {
            double uctValue= child.totalValue/(child.visits + epsilon) +
            Math.sqrt(Math.log(visits+1) / (child.visits + epsilon)) +
            r.nextDouble() * epsilon;
            if (uctValue > bestValue) {
                selected = child;
                bestValue = uctValue;
            }
        }
        return selected;

    }
    
    private Node getChildren() 
    {
        return children;
    }
    private void setChildren(List<Node> children)
    {
        this.children = children;
    }
    private double getTotalValue()
    {
        return totalValue;
    }


}
