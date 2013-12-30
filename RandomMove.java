/**************************************************
 * Picks a random adjacent node for ants to move to
 **************************************************/
public class RandomMove {

	public static void moveAnt (Ant ant) {
	
		int startX = ant.getLocationX();
		int startY = ant.getLocationY();
		int newXLoc = startX;
		int newYLoc = startY;
		int incrementNum = 0;
		
			// Loops until a new node is chosen that is valid
			// Soldier and forager ants loop until a node that is already visible is chosen
			while (startX == newXLoc && startY == newYLoc)	{
				// Random X coordinate is chosen and prevents choosing a number off the grid
				if (startX == 0) {
					incrementNum = RandomNumGen.randomNumber(0, 1);
					newXLoc = newXLoc + incrementNum;
				}
				else if (startX == 26)	{
					incrementNum = RandomNumGen.randomNumber(-1, 0);
					newXLoc = newXLoc + incrementNum;
				}
				else {
					incrementNum = RandomNumGen.randomNumber(-1, 1);
					newXLoc = newXLoc + incrementNum;
				}
				
				// Random Y coordinate is chosen and prevents choosing a number off the grid
				if (startY == 0) {
					incrementNum = RandomNumGen.randomNumber(0, 1);
					newYLoc = newYLoc + incrementNum;
				}
				else if (startY == 26)	{
					incrementNum = RandomNumGen.randomNumber(-1, 0);
					newYLoc = newYLoc + incrementNum;
				}
				else {
				incrementNum = RandomNumGen.randomNumber(-1, 1);
				newYLoc = newYLoc + incrementNum;
				}
				
				// If node has not been discovered by scouts and set to visible, ant stays
				// at it's current node and the while loop iterates again
				if ((ant.getAntType() == "forager" || ant.getAntType() == "soldier") 
						&& AntColony.node[newXLoc][newYLoc].visible == false) {
					newXLoc = startX;
					newYLoc = startY;
				}
				
			}
		
		//Updates ant count and icons in the starting node
		UpdateIcons.removeAntIcon(ant);		
		
		// Updates the Ants location to the node it moved to
		ant.setLocationX(newXLoc);
		ant.setLocationY(newYLoc);
		UpdateIcons.addAntIcon(ant);	
	}
}
