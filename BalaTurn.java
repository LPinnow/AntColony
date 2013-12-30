
/***************************************************************
 *  Represents one bala ant turn
 *  Bala will attack if in the same node as a colony ant,
 *  otherwise the bala ant will move randomly to another node.
 *  50% chance to kill a colony ant when bala attacks
 ***************************************************************/

public class BalaTurn{

	public static void takeTurn(Ant ant) {
		ColonyNodeView currentNode = AntColony.node[ant.getLocationX()][ant.getLocationY()];
		
		// If the bala is in a node with a colony ant, bala attacks ant. Otherwise, moves randomly.
		if (currentNode == AntColony.node[13][13])
			attackColonyAnt(ant);
		else if (currentNode.scouts != 0 || currentNode.soldiers != 0 || currentNode.foragers != 0)
			attackColonyAnt(ant);
		else
			RandomMove.moveAnt(ant);
		
		// Increments ant age by 1
		ant.setAntAge(ant.getAntAge() + 1);
				
		// Ant has reached 1 year old and dies from old age
		if (ant.getAntAge() == 3651)
			ant.setAlive(false);
	}
	
	/**
	 * Bala Ant attacks a colony ant, 50% chance to kill the colony ant
	 * @param ant
	 */
	private static void attackColonyAnt(Ant ant) {
		int XLoc = ant.getLocationX();
		int YLoc = ant.getLocationY();
		
		int randNum = RandomNumGen.randomNumber(0, 3);
		
		// 50% Chance of killing an ant in the same node as the bala
		// If the number 1 or 3 is generated, the attack is successful and an ant is killed
		if (randNum == 1 || randNum == 3) {
			// Iterates through the linked list of colony ants until it find the first ant
			// whose location is in the same node as the attacking bala ant
			for (Ant colonyAnt : AntColony.activeAnts)	{
				// Bala Ant kills queen and ends sim if in the same node
				if (XLoc == 13 && YLoc == 13) {
					AntColony.activeAnts.getFirst().setAlive(false);
					AntColony.endSim = true;
					return;
				}
				// Bala Ant kills a colony ant
				else if (colonyAnt.getLocationX() == XLoc && colonyAnt.getLocationY() == YLoc)	{
					colonyAnt.setAlive(false);
					UpdateIcons.removeAntIcon(colonyAnt);
					AntColony.activeAnts.remove(colonyAnt);
					return;
				}
			}//end for loop
		}
		return;
	}
}
