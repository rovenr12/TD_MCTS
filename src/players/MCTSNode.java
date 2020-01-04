package players;

import utils.Types;

import java.util.List;

public class MCTSNode {

    private MCTSNode parent;
    private List<MCTSNode> children;
    private double qValue;
    private int totalVisit;
    private int actionIdx;
    private int childIdx;

    public MCTSNode() {
        childIdx = 0;
    }

    public MCTSNode(int actionIdx, MCTSNode parent) {
        this.actionIdx = actionIdx;
        totalVisit = 0;
        qValue = 0;
        this.parent = parent;
        childIdx = parent.childIdx + 1;
        for (int i=0; i< Types.ACTIONS.all().size(); i++){
            children.add(new MCTSNode(i, this));
        }
    }

    public int getChildIdx() {
        return childIdx;
    }

    public int getActionIdx() {
        return actionIdx;
    }

    public MCTSNode getParent() {
        return parent;
    }

    public void setParent(MCTSNode parent) {
        this.parent = parent;
    }

    public List<MCTSNode> getChildren() {
        return children;
    }

    public void addChildren(MCTSNode child) {
        children.add(child);
    }

    public double getQValue() {
        return qValue;
    }

    public void updateValue(double reward) {
        this.qValue = qValue + (reward - qValue)/ totalVisit;
        totalVisit++;
    }

    public int getTotalVisit() {
        return totalVisit;
    }

    public MCTSNode mostVisitChild(){
        MCTSNode bestAction = null;
        int maxVisit = -1;
        for (MCTSNode child: children){
            if (child.totalVisit > maxVisit){
                maxVisit = child.totalVisit;
                bestAction = child;
            }
        }

        return bestAction;
    }

}
