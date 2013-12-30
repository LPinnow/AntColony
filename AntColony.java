
import java.awt.event.*;
import java.util.*;
import javax.swing.JOptionPane;

/***************************************************************************
 *  Main class: Runs the simulated ant colony.
 ***************************************************************************/
public class AntColony implements SimulationEventListener, ActionListener {
	
	/**
	 *  Variable Declarations
	 */
	
	// Initiates a new GUI
	AntSimGUI simGUI = new AntSimGUI();
	
	// Sets up a grid that is 27 squares by 27 squares, represents the colony area
	ColonyView gridView = new ColonyView(27,27);
	public static ColonyNodeView[][] node = new ColonyNodeView[27][27];
	public static ColonyNodeView currentNode;
	private boolean gridInitialized = false;
	
	// Variables for Simulation Events
    SimulationEventListener simListener;
    ActionListener actionListener;
    javax.swing.Timer timer;

    // Variables for setting the time on the GUI
    private int turn;
    private int year;
    private int day;
    private int turnByDay;
    private String time = "Turn: 0  Day: 0  Year: 0 ";

    //Variable set to true when conditions to end simulation are met
    public static boolean endSim = false;
    
    // Variable for storing a random number
    private int randNum;	
    
    //Variables for creating Ant Objects
    Ant ant;
    ForagerAnt fAnt;
    int antIDNum = 0;
    String antType = "";
    int antAge = 0;
    int antMode = 0;
    int antLocX = 0;
    int antLocY = 0;
    boolean antIsAlive = false;
    Stack<ColonyNodeView> moveHist = new Stack<ColonyNodeView>();
    
    //Linked List to store Colony Ants
    protected static LinkedList<Ant> activeAnts = new LinkedList<Ant>();
    
    //Linked List to store Bala Ants
    protected static LinkedList<Ant> balaAnts = new LinkedList<Ant>();
    
 
    /****************************************************
     * Constructor
     * Sets up GUI and initializes grid nodes
     ****************************************************/
    public AntColony() {    
          // Sets up a 27x27 grid, creating an instance of each node
          for (int x = 0; x <= 26; x++) {
             for (int y = 0; y <= 26; y++) {
                 node[x][y] = new ColonyNodeView();
                 gridView.addColonyNodeView(node[x][y], x, y);
             }
          }
  
          simGUI.initGUI(gridView);
          
          simGUI.setTime(time);
          
          simGUI.addSimulationEventListener(this);
     }

    
    /***************************************************************************
     * Simulation Event Button Click Occurs
     * @see SimulationEventListener#simulationEventOccurred(SimulationEvent)
     * Initializes starting conditions and grid based on Simulation Event chosen
     ***************************************************************************/
	@Override
	public void simulationEventOccurred(SimulationEvent simEvent){
	
		if (simEvent.getEventType() == SimulationEvent.NORMAL_SETUP_EVENT){
			initializeGrid();
			
			normalSetUp();
	      }
		else if (simEvent.getEventType() == SimulationEvent.QUEEN_TEST_EVENT){
			initializeGrid();
			
			queenTest();
		}
		else if (simEvent.getEventType() == SimulationEvent.SOLDIER_TEST_EVENT){
			initializeGrid();
			
			soldierTest();
		}
		else if (simEvent.getEventType() == SimulationEvent.FORAGER_TEST_EVENT){
			initializeGrid();
			
			foragerTest();
		}
		else if (simEvent.getEventType() == SimulationEvent.SCOUT_TEST_EVENT){
			initializeGrid();
			
			scoutTest();
		}
		
		
		// Starts the simulation in either continuous run mode or step mode
		// Warning message if grid has not been set up
		if(simEvent.getEventType() == 5 && gridInitialized == true)
			start();
		else if (simEvent.getEventType() == 6 && gridInitialized == true)
			startSimStepMode();
		else if (gridInitialized == false)
			JOptionPane.showMessageDialog (
					null, "Please initialize the grid.", "Grid Not Setup", JOptionPane.WARNING_MESSAGE );
	}
	
	/******************************************************
	* Initializes all nodes on the 27x27 grid 
	* Clears any data from a previous simulation
	******************************************************/
	private void initializeGrid() {
		endSim = false;
		gridInitialized = true;
		
		//Clear list of colony ants and bala ants
		activeAnts.clear();
		balaAnts.clear();
		
		//Initializes the turn counter to 0 and resets the time
		turn = 0;
		time = "Turn: 0  Day: 0  Year: 0 ";
		simGUI.setTime(time);
		
		// Sets up nodes with all nodes containing zero ants or food
		for (int x = 0; x < 27; x++) {		//loops through x coordinates
			for (int y = 0; y < 27; y++) {	//loops through y coordinates
				//Sets the current nodes id to the x,y location on the grid
				currentNode = node[x][y];
				currentNode.setID(x + "," + y);
				
				//Stores x and y coordinates for the node
				currentNode.setXLoc(x);
				currentNode.setYLoc(y);
				
				//Sets the current number of ants in the node to 0
				currentNode.setQueen(false);
				currentNode.setForagerCount(0);
				currentNode.setScoutCount(0);
				currentNode.setSoldierCount(0);
				currentNode.setBalaCount(0);
				
				currentNode.setPheromoneLevel(0);
				
				//Sets nodes surrounding queen to visible and hides all other nodes
				if ((x >= 12 && x <= 14) && (y >= 12 && y <= 14))
					currentNode.showNode();
				else {
					currentNode.visible = false;
					currentNode.hideNode();
				}
				
				randNum = RandomNumGen.randomNumber(0, 3);
				
				// 25% chance food in the node, amount set between 500-1000 units
				if (randNum == 0)
					currentNode.setFoodAmount(RandomNumGen.randomNumber(500, 1000));
				// 75% chance of no food in node
				else
					currentNode.setFoodAmount(0);
				
				//Hides icons from previous colony simulations
				currentNode.hideForagerIcon();
				currentNode.hideBalaIcon();
				currentNode.hideScoutIcon();
				currentNode.hideSoldierIcon();
				
			} 
		}
	}
	

	/********************************************
	 * Colony Ants turn
	 ********************************************/
	private void antTurn() {
		// Queen Ants takes her turn first
		QueenTurn.takeTurn(turn);
		
		ListIterator<Ant> itr = activeAnts.listIterator(0);
		
		// Iterates through the activeAnts Linked List letting each ant in the list take a turn
		while (itr.hasNext()) {
			ant  = (Ant) itr.next();
			
			if (ant.isAlive() == false)
				activeAnts.remove(ant);
			else if (ant.getAntType()== "forager")
				ForagerTurn.takeTurn(ant);
			else if (ant.getAntType() == "scout")
				ScoutTurn.takeTurn(ant);
			else if (ant.getAntType() == "soldier")
				SoldierTurn.takeTurn(ant);
			
			// Removes ant if it is killed during its turn
			if (ant.isAlive() == false)	{
				UpdateIcons.removeAntIcon(ant);
				itr.remove();
			}
		}	
	}
	
	/***************************************
	 * Bala Ants Turn
	 ***************************************/
	 private void balaTurn() {
		 // 3% Chance a new Bala Ant is added to the linked list every turn
		 randNum = RandomNumGen.randomNumber(1, 100);
		 
		 if (randNum == 3 || randNum == 6 || randNum == 9) {
			 antAge = 0;
		     antMode = 0;
			 antIsAlive = true;
			 
			 randNum = RandomNumGen.randomNumber(0, 3);
			 
			 // Randomly picks a node on the edges of the grid to place the new bala ant
			 if (randNum == 1) {
				 antLocX = 0;
				 antLocY = RandomNumGen.randomNumber(0, 26);
			 }
			 else if (randNum == 2) {
				 antLocX = 26;
				 antLocY = RandomNumGen.randomNumber(0, 26);
			 }
			 else if (randNum == 3)	{
				 antLocY = 0;
				 antLocX =  RandomNumGen.randomNumber(0, 26);
			 }
			 else {
				 antLocY = 26;
				 antLocX =  RandomNumGen.randomNumber(0, 26);
			 }
			 
			 //Creates a new bala ant and places it in the randomly determined node on the grid's edge
			 if (balaAnts.isEmpty())
				 ant = new Ant(0, "bala", antAge, antIsAlive, antMode, antLocX, antLocY);
			 else {
				 antIDNum = AntColony.balaAnts.getLast().getIDNum() + 1;
				 ant = new Ant(antIDNum, "bala", antAge, antIsAlive, antMode, antLocX, antLocY);
			 }
			 
			 //Adds the new bala to balaList and updates node
			 balaAnts.add(ant);
			 UpdateIcons.addAntIcon(ant);
		 }
		 
		ListIterator<Ant> itr = balaAnts.listIterator(0);
		 
		// Iterates through the linked list of bala ants, allowing each one to take a turn
		while (itr.hasNext()) {
			ant  = (Ant) itr.next();
			
			BalaTurn.takeTurn(ant);
			
			// Removes the bala ant if it dies during its turn
			if (ant.isAlive() == false)	{
				UpdateIcons.removeAntIcon(ant);
				itr.remove();
			}
		}
	 }

	
	/***********************************
	 * Starts Simulation in Step Mode
	 ************************************/
	public void startSimStepMode() {
	
		if (turn < 73000  && endSim == false) {
			year = turn / 3650;
			day = turn / 10;
			turnByDay = turn % 10;
			
			// Updates the time on the GUI
			time = "Turn: " + turnByDay + " Day: " + day + " Year: " + year;
			simGUI.setTime(time);
			
			// Colony ants take their turn first
			antTurn();
			// Bala ants take their turn second
			balaTurn();
			
			// Decreases pheromone by half in each node every new day
			if (turnByDay == 0)	{
				for (int x = 0; x < 27; x++) {		
					for (int y = 0; y < 27; y++) {	
						node[x][y].setPheromoneLevel(node[x][y].amtPheromone / 2);
					}
				}
			}
			
			if (endSim == true)
				endSimulation();
			
			turn++;
		}
	}
	
	
	/*******************************************
	 * Starts Simulation in Continuous Run Mode
	 * Runs until queen dies
	 *******************************************/
	@Override
	public void actionPerformed(ActionEvent e) {
		if (turn < 73000 && endSim == false) {
			startSimStepMode();
		}
		else if (turn == 73000)	{
			timer.stop();
			endSimulation();
		}
		else {
			timer.stop();
			gridInitialized = false;
		}
	}

	/**
	 * Starts Timer
	 */
	public void start() {  
        timer = new javax.swing.Timer(1000, this);
        timer.start();
    }
	
	
	/**************************************
	 * Ends Simulation
	 **************************************/
	public void endSimulation() {  
		if (activeAnts.getFirst().isAlive() == false)
			JOptionPane.showMessageDialog (
					null, "The Queen Ant has died from a Bala Attack.", "End of Simulation", JOptionPane.INFORMATION_MESSAGE );
		else if (node[13][13].getFoodAmount() !=0)
			JOptionPane.showMessageDialog (
					null, "The Queen Ant has died of old age.", "End of Simulation", JOptionPane.INFORMATION_MESSAGE );
		else
			JOptionPane.showMessageDialog (
					null, "The Queen Ant has died of starvation.", "End of Simulation", JOptionPane.INFORMATION_MESSAGE );
		
		endSim = true;
		gridInitialized = false;
		
	 }
	
	/********************************************************
	 * Normal grid set up
	 * Queen node is set to 13,13
	 * Start with 4 scouts, 10 soldiers, 50 foragers
	 ********************************************************/
	private void normalSetUp() {
		currentNode = node[13][13];
		currentNode.showNode();
		
		currentNode.setQueen(true);
		currentNode.showQueenIcon();
		
		currentNode.setFoodAmount(1000);
		
		//Initializes Ant variables for the Queen Ant
		antType = "queen";
		antIDNum = 0;
		antIsAlive = true;
		antAge = 0;
		antMode = 0;
		antLocX = 13;
		antLocY = 13;
		
		//Creates the Queen Ant object and adds it to the linked list
		ant = new Ant(antIDNum, antType, antAge, antIsAlive, antMode, antLocX, antLocY);
		activeAnts.add(ant);
		
		antIDNum = AntColony.activeAnts.getLast().getIDNum() + 1;
		
		// Adds 4 Scouts to the activeAnts list
		for(int i = 0; i < 4; i++) {
			ant = new Ant(antIDNum, "scout", antAge, antIsAlive, antMode, antLocX, antLocY);
			activeAnts.add(ant);
			UpdateIcons.addAntIcon(ant);
		}
		
		// Adds 10 Soldiers to the activeAnts list
		for (int i = 0; i < 10; i++) {
			ant = new Ant(antIDNum, "soldier", antAge, antIsAlive, antMode, antLocX, antLocY);
			activeAnts.add(ant);
			UpdateIcons.addAntIcon(ant);
		}
		
		//Adds 50 Forager Ants to the activeAnts list
		for (int i = 0; i < 50; i++) {
			fAnt  = new ForagerAnt(antIDNum, "forager", antAge, antIsAlive, 
					antMode, antLocX, antLocY, moveHist);
			AntColony.activeAnts.add(fAnt);
			UpdateIcons.addAntIcon(fAnt);
			
			// Adds current node to Forager's movement history stack
			fAnt.movementHistory.push(AntColony.node
					[fAnt.getLocationX()][fAnt.getLocationY()]);
		}
	}
	

	/********************************************************* 
	 * Sets up various test events 
	 *********************************************************/
	
	/**
	 *  Queen Test Event
	 *  Queen ant hatches a new ant at the start of every new day.
	 *  Eats one food per turn and dies when the food runs out.
	 */
	private void queenTest() {
		currentNode = node[13][13];
		currentNode.showNode();
		
		currentNode.setQueen(true);
		currentNode.showQueenIcon();
		
		currentNode.setFoodAmount(31);
		
		//Initializes Ant variables for the Queen Ant
		antType = "queen";
		antIDNum = 0;
		antIsAlive = true;
		antAge = 0;
		antMode = 0;
		antLocX = 13;
		antLocY = 13;
		
		//Creates the Queen Ant object and adds it to the linked list
		ant = new Ant(antIDNum, antType, antAge, antIsAlive, antMode, antLocX, antLocY);
		activeAnts.add(ant);
	}
	
	/**
	 *  Forager Test Event
	 *  Creates a pheromone trail for the forager ant to follow with food at the end.
	 *  Forager ant will not move into a previous node even if the previous node has the highest
	 *  concentration of pheromone.
	 */
	private void foragerTest() {
		currentNode = node[13][13];
		currentNode.showNode();
		currentNode.setQueen(true);
		currentNode.showQueenIcon();
		currentNode.setFoodAmount(1000);
		 
		//Initializes Ant variables for the Queen Ant
		antType = "forager";
		antIDNum = 1;
		antIsAlive = true;
		antAge = 0;
		antMode = 0;
		antLocX = 13;
		antLocY = 13;
		
		//Creates the Queen Ant object and adds it to the linked list
		ant = new Ant(0, "queen", antAge, antIsAlive, antMode, antLocX, antLocY);
		activeAnts.add(ant);
		
		//Creates the Forager Ant and adds it to the linked list
		fAnt  = new ForagerAnt(1, "forager", antAge, antIsAlive, 
				antMode, antLocX, antLocY, moveHist);
		UpdateIcons.addAntIcon(fAnt);
		AntColony.activeAnts.add(fAnt);
		
		// Adds current node to Forager's movement history stack
		fAnt.movementHistory.push(AntColony.node
				[fAnt.getLocationX()][fAnt.getLocationY()]);
		
		// Sets up a pheromone trail for the forager ant to follow with food at the end
		node[12][12].setPheromoneLevel(100);
		node[12][12].setFoodAmount(0);
		node[11][11].showNode();
		node[11][11].setPheromoneLevel(500);
		node[11][11].setFoodAmount(0);
		node[10][10].showNode();
		node[10][10].setPheromoneLevel(300);
		node[10][10].setFoodAmount(0);
		node[9][9].showNode();
		node[9][9].setPheromoneLevel(400);
		node[9][9].setFoodAmount(500);
	}
	
	
	/**
	 * Soldier Test Event
	 * Creates an ant at the queen's node and a bala ant in an adjacent node
	 * Soldier ant will move to bala ant. Bala ant attacks the solider and removes it
	 * if attack is successful. If not successful, solider ant will attack on the next turn.
	 */
	private void soldierTest() {
		currentNode = node[13][13];
		currentNode.setQueen(true);
		currentNode.showQueenIcon();
		currentNode.showNode();
		currentNode.setFoodAmount(1000);
		 
		//Initializes Ant variables for the Queen Ant
		antType = "soldier";
		antIDNum = 1;
		antIsAlive = true;
		antAge = 0;
		antMode = 0;
		antLocX = 13;
		antLocY = 13;
		
		//Creates the Queen Ant object and adds it to the linked list
		ant = new Ant(0, "queen", antAge, antIsAlive, antMode, antLocX, antLocY);
		activeAnts.add(ant);
		
		//Creates the Soldier Ant and adds it to the linked list
		ant = new Ant(antIDNum, antType, antAge, antIsAlive, antMode, antLocX, antLocY);
		activeAnts.add(ant);
		UpdateIcons.addAntIcon(ant);
		
		//Creates a bala and adds it to the bala list
		ant = new Ant (antIDNum, "bala", 0, true, 0, 12, 12);
		balaAnts.add(ant);
		UpdateIcons.addAntIcon(ant);
	}
	
	/**
	 * Scout Test Event
	 * Creates a scout ant and the scout will randomly move and unlock hidden nodes
	 */
	private void scoutTest() {
		currentNode = node[13][13];
		currentNode.setQueen(true);
		currentNode.showQueenIcon();
		currentNode.showNode();
		currentNode.setFoodAmount(100);
		 
		//Initializes Ant variables for the Queen Ant
		antType = "scout";
		antIDNum = 1;
		antIsAlive = true;
		antAge = 0;
		antMode = 0;
		antLocX = 13;
		antLocY = 13;
	
		//Creates the Queen Ant object and adds it to the linked list
		ant = new Ant(0, "queen", antAge, antIsAlive, antMode, antLocX, antLocY);
		activeAnts.add(ant);
				
		//Creates the Scout Ant and adds it to the linked list
		ant = new Ant(antIDNum, antType, antAge, antIsAlive, antMode, antLocX, antLocY);
		activeAnts.add(ant);
		UpdateIcons.addAntIcon(ant);	
	}
}
