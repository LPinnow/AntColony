/*******************************************
 * Creates an ant object
 *******************************************/
public class Ant {
	
	private int idNum;
	private int antAge;
	private boolean alive;
	private String antType;
	private int mode;
	private int locationX;
	private int locationY;
	
	
	// Seven argument constructor
	public Ant (int idNum, String antType, int antAge, boolean alive, int mode, int locationX, int locationY)	{
		this.idNum = idNum;
		this.antType = antType;
		this.antAge = antAge;
		this.setAlive(alive);
		this.setMode(mode);
		this.setLocationX(locationX);
		this.setLocationY(locationY);
	}
	
	/*************************************
	 * Ants id number
	 * @param idNum
	 *************************************/
	//Setter for an Ants ID Number
	public void setIDNum (int idNum) {
		this.idNum = idNum;
	}
	
	//Getter for an Ants ID Number
	public int getIDNum () {
		return idNum;
	}
	
	/**************************************
	 * Type of ant
	 * @param antType
	 *************************************/
	//Setter for an Ants Type
	public void setAntType (String antType)	{
		this.antType = antType;
	}
	
	//Getter for an Ants Type
	public String getAntType()	{
		return antType;
	}
	
	/***************************************
	 * Number of turns since ant was created
	 * @param antAge
	 ***************************************/
	//Setter for an Ant's Age
	public void setAntAge (int antAge)	{
		this.antAge = antAge;
	}
	
	//Getter for an Ant's Age
	public int getAntAge ()	{
		return antAge;
	}
	
	/***************************************
	 * Ant is alive until old age or killed
	 * @param alive
	 ***************************************/
	//Setter for if an Ant is alive
	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	//Getter for if an Ant is alive
	public boolean isAlive() {
		return alive;
	}
	
	/**********************************************
	 * Mode for setting different types of behavior
	 * @param mode
	 **********************************************/
	//Setter for an Ant's Mode
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	//Getter for an Ant's Mode
	public int getMode() {
		return mode;
	}
	
	/**********************************************
	 * X Coordinate of current node on grid
	 * @param locationX
	 **********************************************/
	//Setter for an Ant's X Location on the grid
	public void setLocationX(int locationX) {
		this.locationX = locationX;
	}
	
	//Getter for an Ant's X Location on the grid
	public int getLocationX() {
		return locationX;
	}

	/*******************************************
	 * Y Coordinate of current node on grid
	 * @param locationY
	 *******************************************/
	//Setter for an Ant's Y Location on the grid
	public void setLocationY(int locationY) {
		this.locationY = locationY;
	}
	
	//Getter for an Ant's Y Location on the grid
	public int getLocationY() {
		return locationY;
	}

	/********************************************
	 * Returns a string
	 * Ant id, type, age, x,y location displayed
	 ********************************************/
	public String toString () {
		String antDescription;
		antDescription = "Ant #: " + idNum + "  Type: " + antType + "  Ant Age: " + antAge + 
				" Location: " + locationX + ", " + locationY;
		
		return antDescription;
	}

}
