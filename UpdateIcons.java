/*************************************************************************
 * Updates the GUI
 * Removes icons and lowers number of ants displayed for a node when an ant
 * moves out of a node or dies
 * Adds icons and raises number of ants displayed when an ant is born or
 * moves into a node
 **************************************************************************/
public class UpdateIcons {

	/**
	 * Removes ant icon and decrements ant count displayed
	 * For when an ant dies or moves out of a node
	 * @param ant
	 */
	public static void removeAntIcon(Ant ant){
	
		int x = ant.getLocationX();
		int y = ant.getLocationY();
		
		if (ant.getAntType() == "scout") {
			AntColony.node[x][y].setScoutCount(AntColony.node[x][y].scouts - 1);
			// Hides Scout image icon if no Scout ants are left in the node
			if (AntColony.node[x][y].scouts == 0)
				AntColony.node[x][y].hideScoutIcon();
		}
		else if (ant.getAntType() == "soldier")	{
			//Decrements amount of soldier ants in starting node by one
			AntColony.node[x][y].setSoldierCount(AntColony.node[x][y].soldiers - 1);
			// Hides soldier image icon if no soldier ants are left in the node
			if (AntColony.node[x][y].soldiers == 0)
				AntColony.node[x][y].hideSoldierIcon();
		}
		else if (ant.getAntType() == "forager")	{
			//Drop food if forager is in return to nest mode when it is killed
			if (ant.getMode() == 1 && ant.isAlive() == false)
				AntColony.node[x][y].setFoodAmount(AntColony.node[x][y].food + 1);
			//Decrements amount of forager ants in starting node by one
			AntColony.node[x][y].setForagerCount(AntColony.node[x][y].foragers - 1);
			//Hides forager icon if no foragers are left in the node
			if (AntColony.node[x][y].foragers == 0)
				AntColony.node[x][y].hideForagerIcon();
		}
		else if (ant.getAntType() == "bala") {
			//Decrements amount of bala ants in starting node by one
			AntColony.node[x][y].setBalaCount(AntColony.node[x][y].balas - 1);
			// Hides bala image icon if no bala ants are left in the node
			if (AntColony.node[x][y].balas == 0)
				AntColony.node[x][y].hideBalaIcon();
			
		}
	}
	
	/**
	 * Increments ant counter and adds an icon to the node
	 * A new ant is born or an ant moves into a node
	 * @param ant
	 */
	public static void addAntIcon(Ant ant)	{
	
		int newXLoc = ant.getLocationX();
		int newYLoc = ant.getLocationY();
				
		if (ant.getAntType() == "scout") {
			//Scout sets Node to visible
			AntColony.node[newXLoc][newYLoc].showNode();
			// Show Scout image icon in the new node
			AntColony.node[newXLoc][newYLoc].showScoutIcon();
			//Increments amount of scout in the node by 1
			AntColony.node[newXLoc][newYLoc].setScoutCount(AntColony.node[newXLoc][newYLoc].scouts + 1);
		}
		else if (ant.getAntType() == "soldier")	{
			// Show soldier image icon in the new node
			AntColony.node[newXLoc][newYLoc].showSoldierIcon();
			//Increments amount of soldiers in the node by 1
			AntColony.node[newXLoc][newYLoc].setSoldierCount(AntColony.node[newXLoc][newYLoc].soldiers + 1);
		}
		else if (ant.getAntType() == "forager")	{
			//Show Forager Ant icon in the node
			AntColony.node[newXLoc][newYLoc].showForagerIcon();
			//Increments amount of foragers in the node by 1
			AntColony.node[newXLoc][newYLoc].setForagerCount(AntColony.node[newXLoc][newYLoc].foragers + 1);
		}
		else if (ant.getAntType() == "bala") {
			//Increments amount of bala ants in the new node by 1
			AntColony.node[newXLoc][newYLoc].setBalaCount(AntColony.node[newXLoc][newYLoc].balas + 1);
			// Show bala image icon in the new node
			AntColony.node[newXLoc][newYLoc].showBalaIcon();
		}
	}
}
