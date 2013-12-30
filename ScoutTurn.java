
/************************************************************************
 *  Represents one scout ant turn
 * 	Scout ants will move randomly.
 * 	Scouts are the only colony ants allowed to move into not visible nodes.
 * 	Non-visible nodes will be unlocked when a scout ant moves into them.
 *************************************************************************/

public class ScoutTurn{

	public static void takeTurn(Ant ant) {
		// Scout ant moves randomly
		RandomMove.moveAnt(ant);
		
		// Increments ant age by 1
		ant.setAntAge(ant.getAntAge() + 1);
								
		// Ant has reached 1 year old and dies from old age
		if (ant.getAntAge() == 3651)
			ant.setAlive(false);
	}

}
