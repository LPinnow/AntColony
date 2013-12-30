AntColony
=========

Simulated ant colony written in Java

Sets up a 27x27 grid with the queen ant in the middle.
There are four types of colony ants: queen, forager, soldier, scout.
Enemy ants spawn on the periphery of the grid.

Queen Ant:
Hatches one new ant at the start of every day.
Eats one piece of food each turn.
Does not move out of the nest node.
Can die from old age at 20 years old, enemy ant attack, or starvation.
Ends simulation when killed.

Scout Ant:
Able to move into nodes that are not visible and mark them as visible.
Randomly moves to adjacent nodes.

Soldier Ants:
Attacks enemy ants if in the same node.
Moves into adjacent node if it contains an enemy ant.
Randomly moves into adjacent nodes if no enemy ants are detected.

Forager Ants:
Two Modes of Behavior: Foraging Mode, Return to Nest Mode.
Foraging Mode:
Pick up food if available in current node and enter Return to Nest Mode.
Store movement history.
Move to adjacent node with highest pheromone concentration.
Move randomly if no food or pheromone detected.
Return to Nest Mode:
Follow movement history until reaching the node with the queen's nest.
Drop food in the nest node and re-enter Foraging Mode.

Enemy Ant (Bala):
Attack colony ant in the same node.
Move randomly if not in a node with colony ants.

