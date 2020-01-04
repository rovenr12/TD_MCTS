package players.assignment;

import java.util.LinkedList;
import java.util.List;

public class H_MCTSNode_TD_UCT {

    private H_MCTSNode_TD_UCT parent;
    private List<H_MCTSNode_TD_UCT> unexploredNode = new LinkedList<>();
    private List<H_MCTSNode_TD_UCT> exploredNode = new LinkedList<>();
    private double value = 0;
    private double tdValue = 0.2;
    private int timeSelected = 0;
    private int actionIdx;
    private boolean isRoot = false;
    private int depth;

    //Setting -> Make sure it is same as MCTS(MAX_DEPTH)
    private int MAX_DEPTH = 12;

    public H_MCTSNode_TD_UCT(H_MCTSNode_TD_UCT parent, int actionIdx, int[] actions, int depth) {

        this.parent = parent;
        this.actionIdx = actionIdx;
        this.depth = depth;

    }

    public H_MCTSNode_TD_UCT(boolean isRoot) {
        this.isRoot = isRoot;
    }

    public boolean noFullyExpanded(){
        return ((unexploredNodeIsEmpty() && exploredNodeIsEmpty()) || unexploredNode.size() >0) && depth < MAX_DEPTH;
    }

    public void addUnexploredNode(int[] actions){
        if (noFullyExpanded()){
            for (int i=0; i<actions.length; i++){
                unexploredNode.add(new H_MCTSNode_TD_UCT(this, i, actions, this.depth+1));
            }
        }
    }

    public boolean unexploredNodeIsEmpty(){
        return unexploredNode.isEmpty();
    }
    public boolean exploredNodeIsEmpty(){
        return exploredNode.isEmpty();
    }

    public H_MCTSNode_TD_UCT getUnexploredNode(){
        // Get Node Randomly
        // Possilbe improvement: try to get by choosing best action
        if (!unexploredNodeIsEmpty()){
            H_MCTSNode_TD_UCT temp = unexploredNode.get((int)(Math.random()*unexploredNode.size()));
            unexploredNode.remove(temp);
            exploredNode.add(temp);
            return temp;
        }
        return null;
    }

    public void update(double value, double tdValue){
        this.value = value;
        timeSelected++;
        this.tdValue = tdValue;
    }

    public H_MCTSNode_TD_UCT getParent() {
        return parent;
    }

    public double getValue() {
        return value;
    }

    public int getTimeSelected() {
        return timeSelected;
    }

    public double getTdValue() {
        return tdValue;
    }

    public List<H_MCTSNode_TD_UCT> getExploredNode() {
        return exploredNode;
    }

    public int getActionIdx() {
        return actionIdx;
    }

    public int getDepth() {
        return depth;
    }

    public boolean isRoot() {
        return isRoot;
    }

    @Override
    public String toString() {

        return "H_MCTSNode{" +
                "depth=" + depth +
                ", actionIdx=" + actionIdx +
                ",value=" + value +
                ", timeSelected=" + timeSelected +
                '}';
    }

    public void getReport(){
        if (isRoot){
            System.out.println("Root");
            if (!exploredNode.isEmpty()){
                System.out.println("Root Explored Node:");
                for (H_MCTSNode_TD_UCT node: exploredNode){
                    node.getReport();
                    System.out.println("*************************************************");
                }
            }
            if (!unexploredNode.isEmpty()){
                System.out.println("Root Unexplored Node:");
                for (H_MCTSNode_TD_UCT node: unexploredNode){
                    node.getReport();
                    System.out.println("*************************************************");
                }
            }
        }else {
            System.out.println(this.toString());
            this.getChildDetail();
        }
    }

    public void getChildDetail(){

        if (!exploredNode.isEmpty()){
            System.out.println("D:"+this.getDepth() + " A:" + this.getActionIdx() + " Explored Node:");
            for (H_MCTSNode_TD_UCT node: exploredNode){
                node.getReport();
            }
            System.out.println();
        }

        if (!unexploredNode.isEmpty()){
            System.out.println("D:"+this.getDepth() + " A:" + this.getActionIdx() + " Unexplored Node:");
            for (H_MCTSNode_TD_UCT node: unexploredNode){
                node.getReport();
            }
            System.out.println();
        }

    }
}
