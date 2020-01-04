package GroupK.TD_UCT;

import core.GameState;
import players.Player;
import utils.ElapsedCpuTimer;
import utils.Types;

import java.util.Random;

public class H_MCTSPlayer_TDUCT extends Player {

    public H_MCTSPlayer_TDUCT(long seed, int pId) {
        super(seed, pId);
    }

    @Override
    public Types.ACTIONS act(GameState gs) {

        if (gs.getBoard()[gs.getPosition().x][gs.getPosition().y].getKey() == 4){
            return Types.ACTIONS.ACTION_STOP;
        }
        // TODO update gs
        if (gs.getGameMode().equals(Types.GAME_MODE.TEAM_RADIO)){
            int[] msg = gs.getMessage();
        }

        H_MCTS_TD_UCT agent = new H_MCTS_TD_UCT(200, 12, 10, new Random(seed));
        ElapsedCpuTimer ect = new ElapsedCpuTimer();
        ect.setMaxTimeMillis(100);
        return agent.search(gs, ect);
    }

    @Override
    public int[] getMessage() {
        return new int[0];
    }

    @Override
    public Player copy() {
        return null;
    }

}
