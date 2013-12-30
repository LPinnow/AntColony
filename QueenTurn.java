import java.util.Stack;

/*********************************************************************
 * Represents one Queen Ant turn.
 * The queen will not move.
 * One unit of food will be consumed each turn.
 * Queen Ant will hatch a new colony ant at the beginning of each day.
 * 25% will be soldiers, 25% will be scouts, 50% will be foragers
 *********************************************************************/


public class QueenTurn{

	public static void takeTurn(int turn) {
	
		int food = AntColony.node[13][13].food;
		
		// The queen eats one unit of food in node 13,13 per turn
		if (food == 0)
			AntColony.endSim = true;
		else
			AntColony.node[13][13].setFoodAmount(food - 1);
		
		// The queen hatches a new ant once per day (every ten turns)
		if (turn % 10 == 0)	{
			Ant ant;
			ForagerAnt foragerAnt;
			int antIDNum = AntColony.activeAnts.getLast().getIDNum() + 1;
		    int antAge = 0;
		    int antMode = 0;
		    int antLocX = 13;
		    int antLocY = 13;
		    boolean antIsAlive = true;
		    Stack<ColonyNodeView> moveHist = new Stack<ColonyNodeView>();
		    
		    // Creates a new ant: 25% Scout, 25% Soldier, 50% Forager
			int rNum;
			rNum = RandomNumGen.randomNumber(0, 3);
			
			if (rNum == 0) 	{
				ant = new Ant(antIDNum, "scout", antAge, antIsAlive, antMode, antLocX, antLocY);
				AntColony.node[13][13].setScoutCount(AntColony.node[13][13].scouts + 1);
				AntColony.activeAnts.add(ant);
			}
			else if (rNum == 2) {
				ant = new Ant(antIDNum, "soldier", antAge, antIsAlive, antMode, antLocX, antLocY);
				AntColony.node[13][13].setSoldierCount(AntColony.node[13][13].soldiers + 1);
				AntColony.activeAnts.add(ant);
			}
			else {
				foragerAnt = new ForagerAnt(antIDNum, "forager", antAge, antIsAlive, 
						antMode, antLocX, antLocY, moveHist);
				AntColony.node[13][13].setForagerCount(AntColony.node[13][13].foragers + 1);
				AntColony.activeAnts.add(foragerAnt);
				
				foragerAnt.movementHistory.push(AntColony.node
						[foragerAnt.getLocationX()][foragerAnt.getLocationY()]);
			}	
		}
	}
}
