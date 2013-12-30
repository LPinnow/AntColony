import java.util.Stack;

/******************************************************************************************
 * Represents one forager ant turn
 *  Mode 0: 'Forage for Food'= Move to adjacent node with the highest pheromone. If no
 *  					pheromone in adjacent nodes, randomly move. 
 *  Mode 1: 'Return to Nest' = Pick up food and take it back to the queen's node by
 *  					following movement history and then drop food in the queen's node
 ******************************************************************************************/

public class ForagerTurn{
	// Variables to store x,y coordinates of the ant's current location 
	// and the previous two nodes in ant's movement history
	static int x;
	static int y;
	static int prevX = -1;
	static int prevY = -1;
	static int thirdPrevX = -1;
	static int thirdPrevY = -1;
	

	public static void takeTurn(Ant ant) {
		ForagerAnt fAnt = (ForagerAnt) ant;
		
		boolean pheromoneDetected = false;
		
		prevX = -1;
		prevY = -1;
		thirdPrevX = -1;
		thirdPrevY = -1;
		
		// If not at queen's node, check if node is in movement history and store previous
		// two nodes visited to prevent looping over already visited nodes while foraging
		if (AntColony.node[fAnt.getLocationX()][fAnt.getLocationY()] != AntColony.node[13][13] 
				&& fAnt.getMode() == 0)	{
			// Stores the x,y coordinates of the previous node, to prevent looping if not at queen node
			previousNodes(fAnt);
			
			// Calls method to check if current node is already on the stack
			checkForLoop(fAnt);
		}
		
		// If pheromone is detected in adjacent squares, returns true
		// If no pheromone is detected in adjacent squares, returns false
		if(fAnt.getMode() == 0)
			pheromoneDetected = scanAdjNodes(fAnt.getLocationX(), fAnt.getLocationY());
		
		// If the forager ant is in 'return to nest' mode and is not in the queen's node, 
		// the forager will continue following its movement history 
		if (fAnt.getMode() == 1){
			returnToNest(fAnt);
		}
		// Pick up food in current node and enter 'Return to Nest' mode
		else if (AntColony.node[fAnt.getLocationX()][fAnt.getLocationY()].food > 0
				&& AntColony.node[fAnt.getLocationX()][fAnt.getLocationY()] != AntColony.node[13][13]){
			//Set ant to return to nest mode
			fAnt.setMode(1);
			
			//Removes one food from the node
			AntColony.node[fAnt.getLocationX()][fAnt.getLocationY()].setFoodAmount(
					AntColony.node[fAnt.getLocationX()][fAnt.getLocationY()].food - 1);
			
			//Adds 10 pheromone to the node if under 1000
			if (AntColony.node[fAnt.getLocationX()][fAnt.getLocationY()].amtPheromone < 1000)
					AntColony.node[fAnt.getLocationX()][fAnt.getLocationY()].setPheromoneLevel(
							AntColony.node[fAnt.getLocationX()][fAnt.getLocationY()].amtPheromone + 10);
			
			//Deletes current node from movement history
			fAnt.movementHistory.pop();
		}
		// Higher pheromone was detected in an adjacent node, forager moves to the node
		// with the highest pheromone concentration (Ignores concentrations of less than 10)
		else if (pheromoneDetected == true && AntColony.node[x][y].amtPheromone >= 10)	{
			UpdateIcons.removeAntIcon(fAnt);
			
			// Updates the Ants location to the node it moved to
			fAnt.setLocationX(x);
			fAnt.setLocationY(y);
			UpdateIcons.addAntIcon(fAnt);
			
			//Add new location to movement history
			fAnt.movementHistory.push(AntColony.node[fAnt.getLocationX()][fAnt.getLocationY()]);
		}
		// If no pheromone is detected in adjacent nodes, the forager will move randomly
		else {
			RandomMove.moveAnt(fAnt);
			
			//Add new location to movement history
			fAnt.movementHistory.push(AntColony.node[fAnt.getLocationX()][fAnt.getLocationY()]);
		}
		
		// Increments ant age by 1
		fAnt.setAntAge(fAnt.getAntAge() + 1);
						
		// Ant has reached 1 year old and dies from old age
		if (ant.getAntAge() == 3651)
			fAnt.setAlive(false);
	}
	
	
	/**
	 * Return to Nest Mode
	 * Forager Ant follows movement history back to queen's node
	 * @param fAnt
	 */
	private static void returnToNest(ForagerAnt fAnt) {
		ColonyNodeView nodeOnTop;
		
		// Removes ant from current node
		UpdateIcons.removeAntIcon(fAnt);
		
		//Move towards queen using the ant's movement history
		nodeOnTop = fAnt.movementHistory.peek();
		fAnt.setLocationX(nodeOnTop.getXLoc());
		fAnt.setLocationY(nodeOnTop.getYLoc());
		UpdateIcons.addAntIcon(fAnt);
		
		//Deletes current node from movement history
		fAnt.movementHistory.pop();
		
		// If not at queen's node the forager deposits 10 pheromone 
		// At the queen's node, it drops the food and enters forage mode again
		if (AntColony.node[fAnt.getLocationX()][fAnt.getLocationY()] == AntColony.node[13][13])	{
			AntColony.node[13][13].setFoodAmount(
					AntColony.node[fAnt.getLocationX()][fAnt.getLocationY()].food + 1);
			fAnt.setMode(0);
			
			//Clears movement history
			fAnt.movementHistory.clear();
			prevX = -1;
			prevY = -1;
			thirdPrevX = -1;
			thirdPrevY = -1;
			
			// Adds queen node to stack
			fAnt.movementHistory.push(AntColony.node[fAnt.getLocationX()][fAnt.getLocationY()]);
			
		}
		else if (AntColony.node[fAnt.getLocationX()][fAnt.getLocationY()].amtPheromone < 1000)	{
			//Adds 10 pheromone to current node
			AntColony.node[fAnt.getLocationX()][fAnt.getLocationY()].setPheromoneLevel(
					AntColony.node[fAnt.getLocationX()][fAnt.getLocationY()].amtPheromone + 10);
		}
	
	}
	
	
	/**
	 * Scans the adjacent nodes and stores the x,y coordinates of the node with the highest
	 * amount of pheromone and returns true. If no nodes contain pheromone, returns false.
	 * @param scanX
	 * @param scanY
	 * @return
	 */
	private static boolean scanAdjNodes(int scanX, int scanY) {
		// Variables for x,y coordinates around the ant's current node location
		int xLower = scanX - 1;
		int yLower = scanY - 1;
		int xHigher = scanX + 1;
		int yHigher = scanY + 1 ;
		boolean higherPheromone = false;
		
		
		// Boundaries for when ant is at the edge of the grid
		if (scanX == 0)
			xLower = scanX;
		else if (scanX == 26)
			xHigher = scanX;
		
		if (scanY == 0)
			yLower = scanY;
		else if (scanY == 26)
			yHigher = scanY;
		
		//Sets initial node to be compared to the queen's node, because the pheromone will always be zero
		x = 13;
		y = 13;	
		
		// Loops through adjacent nodes
		for (int i = xLower; i <= xHigher; i++)	{
			for (int j = yLower; j <= yHigher; j++)	{
				//Compare pheromone levels and store x,y location if higher
				if (i == scanX && j == scanY){}						//skips starting node
				else if(i == prevX && j == prevY){}					//skips previous node
				else if (i == thirdPrevX && j == thirdPrevY){}		//skips third previous node
				else if (AntColony.node[i][j].isVisible() == false){}//skips nodes not visible
				//Compares pheromone concentrations
				else if (AntColony.node[i][j].amtPheromone > AntColony.node[x][y].amtPheromone)	{
					x = i;
					y = j;
					higherPheromone = true;
				}
				else if ((AntColony.node[i][j].amtPheromone == AntColony.node[x][y].amtPheromone)
						&& AntColony.node[i][j].amtPheromone != 0)	{
					//Randomly chooses which node is chosen if two nodes contain equal pheromone amounts
					int randNum = RandomNumGen.randomNumber(0, 1);
					if (randNum == 0) {
						x = i;
						y = j;
						higherPheromone = true;
					}
				}
			}
		} 
		
		return higherPheromone; // true if higher pheromone detected
	}
	
	/**
	* Examines the ant's movement history stack to prevent looping.
	* Prevents ants from moving into the node they just came from and the one before that
	* Stores x,y coordinates of previous two nodes.
	* @param fAnt
	*/
	private static void previousNodes (ForagerAnt fAnt)	{
		ColonyNodeView startNode;
		ColonyNodeView previousNode;
		ColonyNodeView thirdPrevNode;
		
		// Stores the current node
		startNode = fAnt.movementHistory.peek();
		
		// Removes the current node
		fAnt.movementHistory.pop();
		
		if(fAnt.movementHistory.isEmpty() == false)	{
			// Stores the x,y coordinates of the previous node
			previousNode = fAnt.movementHistory.peek();
			prevX = previousNode.xCoordinate;
			prevY = previousNode.yCoordinate;
			
			fAnt.movementHistory.pop();
			
			// Stores x,y coordinates of the third item in the movement history
			if(fAnt.movementHistory.isEmpty() == false)	{
				thirdPrevNode = fAnt.movementHistory.peek();
				thirdPrevX = thirdPrevNode.getXLoc();
				thirdPrevY = thirdPrevNode.getYLoc();
			}
			
			fAnt.movementHistory.push(previousNode);
		}
		
		// Puts the current node back on the stack
		fAnt.movementHistory.push(startNode);	
	}
	
	/**
	* Checks if the current node is already in the stack and deletes all the nodes between them
	* Prevents large amounts of pheromone from being dropped on nodes that the ants have looped over
	* @param fAnt
	*/
	private static void checkForLoop (ForagerAnt fAnt)	{
		boolean nodeFound = false;
		
		// Sets the node on the top of the stack to be examined
		ColonyNodeView startingNode = fAnt.movementHistory.peek();
		
		// Creates a stack containing the forager ants movement history
		Stack<ColonyNodeView> foragerAntStack = null;
		foragerAntStack = (Stack<ColonyNodeView>) fAnt.movementHistory.clone();
		
		//Removes the top node that the ant is currently in and sets a variable to new top node
		foragerAntStack.pop();
		ColonyNodeView nodeToCompare = null;
		
		// Loops through movement history to find if the current node has already been stored.
		// Deletes the top node until a match is found. If no match is found, movement history
		// stack is kept the same.
		while ((nodeFound == false) && (foragerAntStack.isEmpty() == false)) {
			nodeToCompare = foragerAntStack.peek();
			
			if (nodeToCompare == startingNode)
				nodeFound = true;
			else
				foragerAntStack.pop();
		}
		
		// Sets the foragers ant movement history to the shortened stack if a match was found
		if (nodeFound == true)
			fAnt.movementHistory = foragerAntStack;
	}
}
