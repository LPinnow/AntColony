
/*************************************************************************
 *  Represents one soldier ant turn
 *  A soldier ant will move into an adjacent node if it contains a bala ant.
 *  The soldier ant will attack a bala ant if in the same node.
 *  50% chance to kill a bala ant
 *  Soldier will move randomly if no bala ants are detected.
 **************************************************************************/
public class SoldierTurn {

	static int x;
	static int y;
	
	public static void takeTurn(Ant ant) {
	
		boolean balaScan = false;
		
		x = ant.getLocationX();
		y = ant.getLocationY();
		
		balaScan = scanAdjNodes(x, y);
		
		// If there is a bala ant in the same node, soldier attacks the bala
		// If no balas are in the same node, scan for balas in adjacent nodes
		// Otherwise, move randomly
		if (AntColony.node[ant.getLocationX()][ant.getLocationY()].balas != 0)
			attackBala(ant);	
		else if (balaScan == true) {
			UpdateIcons.removeAntIcon(ant);
			
			// Updates the Ants location to the node it moved to
			ant.setLocationX(x);
			ant.setLocationY(y);
			UpdateIcons.addAntIcon(ant);
		}
		else
			RandomMove.moveAnt(ant);
		
		// Increments ant age by 1
		ant.setAntAge(ant.getAntAge() + 1);
								
		// Ant has reached 1 year old and dies from old age
		if (ant.getAntAge() == 3651)
			ant.setAlive(false);
	}

	/**
	 * 50% Chance to kill a bala ant in the same node
	 * @param ant
	 */
	
	private static void attackBala(Ant ant) {
	
		int XLoc = ant.getLocationX();
		int YLoc = ant.getLocationY();
		
		int randNum = RandomNumGen.randomNumber(0, 3);
		
		// 50% Chance of killing a Bala ant in the same node as the soldier ant
		if (randNum == 1 || randNum == 3) {
			// Iterates through the linked list of bala ants until it finds the first ant
			// whose location is in the same node as the attacking soldier ant
			for (Ant balaAnt : AntColony.balaAnts)	{
				if (balaAnt.getLocationX() == XLoc && balaAnt.getLocationY() == YLoc) {
					UpdateIcons.removeAntIcon(balaAnt);
					AntColony.balaAnts.remove(balaAnt);
					return;
				}
			}
		}
	}

	/**
	 * Scans adjacent nodes for bala ants
	 * If bala ants are in adjacent nodes, x and y coordinates are stored and returns true
	 * 
	 * @param scanX
	 * @param scanY
	 * @return
	 */
	private static boolean scanAdjNodes(int scanX, int scanY) {
	
		// Variables that store coordinates of surrounding nodes
		int xLower = scanX - 1;
		int yLower = scanY - 1;
		int xHigher = scanX + 1;
		int yHigher = scanY + 1 ;
		
		if (x == 0)
			xLower = x;
		else if (x == 26)
			xHigher = x;
		
		// Doesn't allow coordinates outside of the edges of the grid
		if (y == 0)
			yLower = y;
		else if (y == 26)
			yHigher = y;
		
		for (int i = xLower; i <= xHigher; i++)	{
			for (int j = yLower; j <= yHigher; j++)	{
				// Stores x,y coordinates if node is set to visible and contains a bala ant
				if (AntColony.node[i][j].balas != 0 && AntColony.node[i][j].visible == true) {
					x = i;
					y = j;
					return true; 
				}
			}
		}
		
		return false; // No bala ants in visible adjacent nodes
	}
	
}
