package players;

import core.GameState;
import players.MCTSNode;
import utils.ElapsedCpuTimer;

import java.util.List;
import java.util.Random;

public class MCTS {

    private MCTSNode root;
    private GameState gameState;
    private ElapsedCpuTimer ect;
    private long remaining;
    private int MAX_DEPTH = 10;

    public MCTS(MCTSNode root, GameState gameState) {
        this.root = root;
        this.gameState = gameState;
        ect = new ElapsedCpuTimer();
        ect.setMaxTimeMillis(100);
    }

    public int Decision(){

        int iteration = 0;
        boolean stop = false;

        while (!stop || iteration <1000){

            iteration++;

            if (ect.remainingTimeMillis() < 5){
                stop = true;
            }
            return (int)(Math.random() * 5);
        }

        return 0;
    }

    private MCTSNode treePolicy(GameState state, MCTSNode node) {

        MCTSNode currentNode = node;

        while (!state.isTerminal() && currentNode.getChildIdx() <= MAX_DEPTH) {
            if (noFullExpanded(currentNode)) {
                //return expand(state, currentNode);

            } else {
                //currentNode = playout(state, currentNode);
            }
        }

        return currentNode;
    }

    public boolean noFullExpanded(MCTSNode currentNode){
        List<MCTSNode> noVisitedNode = null;
        for (MCTSNode child : currentNode.getChildren()){
            if (child.getTotalVisit() == 0){
                return true;
            }
        }
        return false;
    }
}
