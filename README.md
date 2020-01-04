# Monte-Carlo-Tree-Search-with-temporal-difference-enhancement-in-Pommerman

Monte Carlo Tree Search (MCTS) is a popular technology in the field of real-time games and has good performance comparing with the other agents in
pommerman. Recently, few studies introduce using the conceptsin reinforcement learning to enhance the performance in MCTS. 

By combining the concept of temporal difference (TD) in the tree policy, it gives a better result in the twoplayer game. 


# Pommerman

The test platform of this game is created by GAIGResearch - (java-pommerman)[https://github.com/GAIGResearch/java-pommerman], and the method to install game can be found under that repositories.


# Agent

There are two agents in the file of [group k](https://github.com/rovenr12/Monte-Carlo-Tree-Search-with-temporal-difference-enhancement-in-Pommerman/tree/master/src/GroupK)

- Opponent Enhacement Agent using original MCTS

The oppoent enhancement agent assumes every agent will make a rational action in every turn. Therefore, it predices the opponent's action by picking one of possible safe action.


- Agent using TD MCTS

The agents using TD-MCTS is combines the search methods and reinforcement learning.

# Agent Implement

In order to use the agents, the group_k folder needs to put under the src folder. It can be used in run.java and test.java.
The following code shows how to implement in run.java and test.java.

To add a Opponent Enhacement Agent to 'Run.JAVA' and ‘Test.JAVA’ please use the following format:

Run.java:

                        MCTSParams mctsParams1 = new MCTSParams();
                        mctsParams1.stop_type = mctsParams1.STOP_ITERATIONS;
                        mctsParams1.num_iterations = 200;
                        mctsParams1.rollout_depth = 12;

                        mctsParams1.heuristic_method = mctsParams1.CUSTOM_HEURISTIC;
                        p = new MCTSPlayer1(seed, playerID++, mctsParams1);
                        playerStr[i-4] = "MCTSPlayer1";
                        

Test.java:

	       		players.add(new MCTSPlayer1(seed, playerID++,mctsParams));

To add a TD_UCT agent to 'Run.JAVA' and ‘Test.JAVA’ please use the following format:

Run,java:

	       		p = new H_MCTSPlayer_TDUCT(seed, playerID++);
               		playerStr[i-4] = "H_MCTSPlayer_TDUCT";
Test.java:

	       		players.add(new H_MCTSPlayer_TDUCT(seed, playerID++));

# Results

TD-UCT agent does not have significant improvement comparing with original MCTS agents while the opponent model can increase the performance.
The methodology and test results can be seen in [Group_k.pdf](https://github.com/rovenr12/Monte-Carlo-Tree-Search-with-temporal-difference-enhancement-in-Pommerman/blob/master/GroupK_Report.pdf)
