# Monte-Carlo-Tree-Search-with-temporal-difference-enhancement-in-Pommerman

Monte Carlo Tree Search (MCTS) is a popular technology in the field of real-time games and has good performance comparing with the other agents in
pommerman. Recently, few studies introduce using the conceptsin reinforcement learning to enhance the performance in MCTS. 

By combining the concept of temporal difference (TD) in the tree policy, it gives a better result in the twoplayer game. 

# Agent

There are two agents in the file of [group k](https://github.com/rovenr12/Monte-Carlo-Tree-Search-with-temporal-difference-enhancement-in-Pommerman/tree/master/src/GroupK)

- Opponent Enhacement Agent using original MCTS

The oppoent enhancement agent assumes every agent will make a rational action in every turn. Therefore, it predices the opponent's action by picking one of possible safe action.


- Agent using TD MCTS

The agents using TD-MCTS is combines the search methods and reinforcement learning.


# Results

TD-UCT agent does not have significant improvement comparing with original MCTS agents while the opponent model can increase the performance.
The methodology and test results can be seen in [Group_k.pdf](https://github.com/rovenr12/Monte-Carlo-Tree-Search-with-temporal-difference-enhancement-in-Pommerman/blob/master/GroupK_Report.pdf)
