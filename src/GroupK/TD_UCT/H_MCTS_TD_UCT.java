package GroupK.TD_UCT;

import core.GameState;
import players.heuristics.CustomHeuristic;
import players.heuristics.StateHeuristic;
import utils.ElapsedCpuTimer;
import utils.Types;
import utils.Utils;
import utils.Vector2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class H_MCTS_TD_UCT {

    //Parameter
    private int MAX_ITERATION;
    private int MAX_DEPTH;
    private int MAX_ROLLOUT;

    private Random random;
    private StateHeuristic heuristic;
    private GameState state;
    private List<Types.ACTIONS> actionsList = Types.ACTIONS.all();
    private int[] actions = new int[actionsList.size()];
    private int iteration;

    // TD_UCT
    private int numberOfRollOut;
    private double discountFactor = 0.1; // range(0-1) lower the importance of future factor
    private double stepRate = 0.01; // range(0-1)
    private double decayRate = 0.99; // range(0-1)
    private double weight = 0.2; // range(0-1)
    private double c = 0.2; // explore Rate

    // Constructor
    public H_MCTS_TD_UCT(int MAX_ITERATION, int MAX_DEPTH, int MAX_ROLLOUT, Random random) {

        // Set Parameter
        this.MAX_ITERATION = MAX_ITERATION;
        this.MAX_DEPTH = MAX_DEPTH;
        this.MAX_ROLLOUT = MAX_ROLLOUT;
        this.random = random;

        // add index to actions
        for (int i=0; i<actionsList.size(); i++){
            actions[i] = i;
        }
    }

    public Types.ACTIONS search(GameState gs, ElapsedCpuTimer ect){

        GameState rootGs = gs.copy();
        //heuristic = new AdvancedHeuristic(rootGs, random);
        heuristic = new CustomHeuristic(rootGs);

        H_MCTSNode_TD_UCT root = new H_MCTSNode_TD_UCT(true);

        iteration = 0;
        long remaining = ect.remainingTimeMillis();

        while (iteration < MAX_ITERATION && remaining > 0){
            state = gs.copy();
            H_MCTSNode_TD_UCT currentNode = treePolicy(root);
            double delta = rollOut();
            backUp(currentNode, delta);
            iteration++;
            remaining = ect.remainingTimeMillis();
        }

        //return most frequency child
        //return actionsList.get(frequentChild(root)) ;
        //return highest TD value child
        return actionsList.get(highestTDValueChild(root));
    }

    // Tree Policy
    private H_MCTSNode_TD_UCT treePolicy(H_MCTSNode_TD_UCT root){

        H_MCTSNode_TD_UCT currentNode = root;
        while (!state.isTerminal()){
            if (currentNode.noFullyExpanded()){
                return expand(currentNode);
            }
            currentNode = bestChild(currentNode);
        }

        return currentNode;
    }

    // Expansion
    private H_MCTSNode_TD_UCT expand(H_MCTSNode_TD_UCT currentNode){

        if (currentNode.unexploredNodeIsEmpty() && currentNode.exploredNodeIsEmpty()){
            currentNode.addUnexploredNode(actions);
        }

        H_MCTSNode_TD_UCT nextNode = currentNode.getUnexploredNode();
        roll(nextNode);
        return nextNode;
    }

    // Selection
    private H_MCTSNode_TD_UCT bestChild(H_MCTSNode_TD_UCT currentNode){
        double bestValue = -Double.MAX_VALUE;
        H_MCTSNode_TD_UCT bestAction = null;

        if (currentNode.getDepth() == MAX_DEPTH){
            return currentNode;
        }
        for (H_MCTSNode_TD_UCT temp: currentNode.getExploredNode()){
            double value;
            if (currentNode.isRoot()){
                double wVtd = weight * temp.getTdValue();
                double exploitation = (1 - weight) * (temp.getValue()/temp.getTimeSelected());
                double exploration = c*Math.sqrt(2 * Math.log(iteration/(double)temp.getTimeSelected()));
                value = wVtd + exploitation + exploration;
            }else{
                double wVtd = weight * temp.getTdValue();
                double exploitation = (1 - weight) * (temp.getValue()/temp.getTimeSelected());
                double exploration = c*Math.sqrt(2 * Math.log(currentNode.getTimeSelected()/(double)temp.getTimeSelected()));
                value = wVtd + exploitation + exploration;
            }
            value = Utils.normalise(value, Double.MAX_VALUE, -Double.MAX_VALUE);
            if (value > bestValue){
                bestValue = value;
                bestAction = temp;
            }
        }

        double e = 0.001;
        if (e < Math.random()){

        }
        roll(bestAction);
        return bestAction;
    }

    // Simulation (for multiple times)
    private double rollOut(){

        int rollOutTime = 0;

        while ((!state.isTerminal()) && rollOutTime < MAX_ROLLOUT){
            state.next(opponentsAction());
            rollOutTime++;
        }

        numberOfRollOut = rollOutTime;
        return heuristic.evaluateState(state);
    }

    // Simulation (one time)
    private void roll(H_MCTSNode_TD_UCT startNode){
        state.next(opponentsAction(startNode));
    }

    // Creating safer action for opponent and keep self action
    private Types.ACTIONS[] opponentsAction(H_MCTSNode_TD_UCT currentNode){
        int myID = state.getPlayerId() - Types.TILETYPE.AGENT0.getKey();
        Types.ACTIONS[] rollActions = new Types.ACTIONS[4];

        for (int i=0; i<rollActions.length; i++){
            if (i == myID){
                rollActions[i] = actionsList.get(currentNode.getActionIdx());
            }else {
                int[] opponentPosition = opponetPosition(i);
                rollActions[i] = Types.ACTIONS.all().get(safeRandomAction(opponentPosition[0], opponentPosition[1]));
                rollActions[i] = actionsList.get((int)(Math.random()* actionsList.size()));
            }
        }

        return rollActions;
    }

    // find safe action
    private int safeRandomAction()
    {
        Types.TILETYPE[][] board = state.getBoard();
        ArrayList<Types.ACTIONS> actionsToTry = Types.ACTIONS.all();
        int width = board.length;
        int height = board[0].length;

        while(actionsToTry.size() > 0) {

            int nAction = (int)(Math.random()*actionsToTry.size());
            Types.ACTIONS act = actionsToTry.get(nAction);
            Vector2d dir = act.getDirection().toVec();

            Vector2d pos = state.getPosition();
            int x = pos.x + dir.x;
            int y = pos.y + dir.y;

            if (x >= 0 && x < width && y >= 0 && y < height)
                if(board[y][x] != Types.TILETYPE.FLAMES)
                    return nAction;

            actionsToTry.remove(nAction);
        }

        return (int)(Math.random()*actions.length);
    }

    // Creating random action for all player
    private Types.ACTIONS[] opponentsAction(){
        int myID = state.getPlayerId() - Types.TILETYPE.AGENT0.getKey();
        Types.ACTIONS[] rollActions = new Types.ACTIONS[4];

        for (int i=0; i<rollActions.length; i++){
            if (i == myID){
                rollActions[i] = actionsList.get(safeRandomAction());
            }else {
                int[] opponentPosition = opponetPosition(i);
                rollActions[i] = Types.ACTIONS.all().get(safeRandomAction(opponentPosition[0], opponentPosition[1]));
                rollActions[i] = actionsList.get((int)(Math.random()* actionsList.size()));
            }
        }

        return rollActions;
    }

    // location of other people
    private int[] opponetPosition(int agentIndex){
        Types.TILETYPE[][] board = state.getBoard();
        int[] pos = {0, 0};
        int width = board.length;
        int height = board[0].length;
        Types.TILETYPE[] players = {Types.TILETYPE.AGENT0, Types.TILETYPE.AGENT1, Types.TILETYPE.AGENT2, Types.TILETYPE.AGENT3};

        for (int i=0; i<height; i++){
            for (int j=0; j<width; j++){
                if (board[i][j] == players[agentIndex]){
                    pos[0] = i;
                    pos[1] = j;
                    return pos;
                }
            }
        }
        return pos;
    }

    // find safe action
    private int safeRandomAction(int x, int y) {

        ArrayList<Types.ACTIONS> actionsToTry = Types.ACTIONS.all();

        while (actionsToTry.size() > 0) {

            int nAction = (int)(Math.random()*actionsToTry.size());

            Types.ACTIONS act = actionsToTry.get(nAction);
            Vector2d dir = act.getDirection().toVec();

            if (safeAction(x, y, dir))
                return nAction;

            actionsToTry.remove(nAction);
        }

        return  (int)(Math.random()*actions.length);
    }

    // find safe action
    private boolean safeAction(int posX, int posY, Vector2d dir){
        Types.TILETYPE[][] board = state.getBoard();
        int width = board.length;
        int height = board[0].length;

        int x = posX + dir.x;
        int y = posY + dir.y;
        if (x >= 0 && x < width && y >= 0 && y < height)
            if (board[y][x] != Types.TILETYPE.FLAMES)
                return true;

        return false;
    }

    // Backup
    private void backUp(H_MCTSNode_TD_UCT lastNode, double delta){

        double tdError = Math.pow(discountFactor, numberOfRollOut)*delta - lastNode.getTdValue();
        double eligibilityTrace = 1;
        while (!lastNode.isRoot()){
            double tdValue = lastNode.getTdValue() + stepRate * eligibilityTrace * tdError;
            double value = lastNode.getValue() + delta;
            lastNode.update(value, tdValue);
            eligibilityTrace = eligibilityTrace * decayRate * discountFactor;
            lastNode = lastNode.getParent();
        }
    }

    // Most frequent tree policy
    private int frequentChild(H_MCTSNode_TD_UCT root){
        List<H_MCTSNode_TD_UCT> children = root.getExploredNode();
        int frequencyActionIndex = -1;
        int timeSelected = -1;
        for (H_MCTSNode_TD_UCT child: children){
            if (child.getTimeSelected() > timeSelected){
                timeSelected = child.getTimeSelected();
                frequencyActionIndex = child.getActionIdx();
            }
        }
        return frequencyActionIndex;
    }

    // Highest TD value tree policy
    private int highestTDValueChild(H_MCTSNode_TD_UCT root){
        H_MCTSNode_TD_UCT highestValueNode = bestChild(root);
        return highestValueNode.getActionIdx();
    }

}
