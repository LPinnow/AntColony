import java.util.Stack;

/************************************************************
 * Creates a forager ant of type Ant
 ***********************************************************/
public class ForagerAnt extends Ant{
	Stack<ColonyNodeView> movementHistory = new Stack<ColonyNodeView>();
	
	//Constructor
	public ForagerAnt(int idNum, String antType, int antAge, boolean alive,
			int mode, int locationX, int locationY, Stack<ColonyNodeView> moveHist) {
		super(idNum, antType, antAge, alive, mode, locationX, locationY);
	}
	
	/**
	 * Forager Ant's movement history
	 * @param moveHistory
	 */
	// Setter for movement history
	public void setMoveHistory(Stack<ColonyNodeView> moveHistory) {
		movementHistory = moveHistory;
	}
	
	// Getter for movement history
	public Stack<ColonyNodeView> getMoveHistory() {
		return movementHistory;
	}
}