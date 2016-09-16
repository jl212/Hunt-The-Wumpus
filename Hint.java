
import java.util.*;

//remove wumpus as an avoidable object and instead have hints specifically for it? give option wumpus/treasure/exit?
public class Hint {
	
	private GameBoard board;
	private Map<Integer, ArrayList<Integer>> avoidablePits;
	private Map<Integer, ArrayList<Integer>> avoidableWumpus;
	private int treasureX;
	private int treasureY;
	private int exitX;
	private int exitY;
	
	public Hint(GameBoard board) {
		avoidablePits = new HashMap<Integer, ArrayList<Integer>>();
		avoidableWumpus = new HashMap<Integer, ArrayList<Integer>>();
		setBoard(board);
		searchBoard();
	}
	
	public String giveHint() {
		String hint = "";
		Boolean direct = false;
		int xCoord = board.getXCoordPlayer();
		int yCoord = board.getYCoordPlayer();
		int xDirection;
		int yDirection;
		if(((Player)board.getBoard()[yCoord][xCoord].getPlayer()).getTreasure() instanceof Treasure) {
			xDirection = xCoord - exitX;
			yDirection = yCoord - exitY;
		} else {
			xDirection = xCoord - treasureX; // if +ve go west if -ve go east
			yDirection = yCoord - treasureY; // if +ve go south if -ve go north
			// if either is zero stay on that line!!
		}
		if(xDirection > 0) {
			if(yDirection > 0) {
				//head SW - check to see if avoidable object in path?
				hint = "south-west";
				direct = checkAvoidableObjects(xCoord-1, yCoord-1);
			} else {
				if(yDirection < 0) {
					//head NW
					hint = "north-west";
					direct = checkAvoidableObjects(xCoord-1, yCoord+1);
				} else {
					if(yDirection == 0) {
						//head W
						hint = "west";
						direct = checkAvoidableObjects(xCoord-1, yCoord);
					}
				}
			}
		}
		if(xDirection < 0) {
			if(yDirection > 0) {
				//head SE - check to see if avoidable object in path?
				hint = "south-east";
				direct = checkAvoidableObjects(xCoord+1, yCoord-1);
			} else {
				if(yDirection < 0) {
					//head NE
					hint = "north-east";
					direct = checkAvoidableObjects(xCoord+1, yCoord+1);
				} else {
					if(yDirection == 0) {
						//head E
						hint = "east";
						direct = checkAvoidableObjects(xCoord+1, yCoord);
					}
				}
			}
		}
		if(xDirection == 0) {
			if(yDirection > 0) {
				//head S - check to see if avoidable object in path?
				hint = "south";
				direct = checkAvoidableObjects(xCoord, yCoord-1);
			} else {
				if(yDirection < 0) {
					//head N
					hint = "north";
					direct = checkAvoidableObjects(xCoord, yCoord+1);
				}
			}
		}
		if(direct == true) {
			return ("You should head " +hint+ "! I wouldn't go directly though!");
		} else {
			return ("You should head " +hint+ "!");
		}
	}
	
	public Boolean checkAvoidableObjects(int xCoord, int yCoord) {
		Iterator<Map.Entry<Integer, ArrayList<Integer>>> iteratorPits = avoidablePits.entrySet().iterator();
		Iterator<Map.Entry<Integer, ArrayList<Integer>>> iteratorWumpus = avoidableWumpus.entrySet().iterator() ;
        while(iteratorPits.hasNext()){
        	Map.Entry<Integer, ArrayList<Integer>> objectEntry = iteratorPits.next();
            if(yCoord == objectEntry.getKey()) {
            	for(int i=0; i<objectEntry.getValue().size(); i++) {
        			if(objectEntry.getValue().get(i) == xCoord) {
        				return true;
        			}
        		}
            }
        }
        while(iteratorWumpus.hasNext()){
        	Map.Entry<Integer, ArrayList<Integer>> objectEntry = iteratorWumpus.next();
            if(yCoord == objectEntry.getKey()) {
            	for(int i=0; i<objectEntry.getValue().size(); i++) {
        			if(objectEntry.getValue().get(i) == xCoord) {
        				return true;
        			}
        		}
            }
        }
        return false;
	}
	
	public void searchBoard() {
		for(int i=0; i<getBoardSize(); i++){
			searchRow(board.getBoard()[i], i);
		}
		searchForObjects();
	}
	
	public void searchRow(Room[] row, int yCoord) {
		for(int xCoord = 0; xCoord<getBoardSize(); xCoord++) {
			if(row[xCoord] instanceof BottomlessPit) {
				recordPits(xCoord, yCoord);
			}
			if(row[xCoord].getgObject() instanceof Wumpus) {
				recordWumpus(xCoord, yCoord);
			}
		}
	}
	
	public void recordWumpus(int xCoord, int yCoord) {
		if(avoidableWumpus.containsKey(yCoord)) {
			avoidableWumpus.get(yCoord).add(xCoord);
		} else {
			ArrayList<Integer> list = new ArrayList<Integer>();
			list.add(xCoord);
			avoidableWumpus.put(yCoord, list);
		}
	}

	public void recordPits(int xCoord, int yCoord) {
		if(avoidablePits.containsKey(yCoord)) {
			avoidablePits.get(yCoord).add(xCoord);
		} else {
			ArrayList<Integer> list = new ArrayList<Integer>();
			list.add(xCoord);
			avoidablePits.put(yCoord, list);
		}
	}

	public void searchForObjects() {
		for(int i=0; i<getBoardSize(); i++){
			for(int j=0; j<getBoardSize(); j++) {
				if(board.getBoard()[i][j].getcObject() instanceof Treasure) {
					setTreasureX(j);
					setTreasureY(i);
				}
				if(board.getBoard()[i][j] instanceof Exit) {
					setExitX(j);
					setExitY(i);
				}
			}
		}
	}
	
	public void checkWumpusLocation() {
		ArrayList<Integer> xCoord = board.getXCoordWumpus();
		ArrayList<Integer> yCoord = board.getXCoordWumpus();
		Iterator<Map.Entry<Integer, ArrayList<Integer>>> iteratorWumpus = avoidableWumpus.entrySet().iterator() ;
		int count=0;
		while(iteratorWumpus.hasNext()){
			 for(int j=0; j<xCoord.size(); j++) {
				 Map.Entry<Integer, ArrayList<Integer>> objectEntry = iteratorWumpus.next();
				 if(yCoord.get(j) == objectEntry.getKey()) {
					 if(objectEntry.getValue().contains(xCoord)) {
						 count++;
					 }
				 } 
			 }	
		 }
		if(!(count == xCoord.size())) {
			avoidableWumpus.clear();
			for(int i=0; i<xCoord.size(); i++) {
				recordWumpus(xCoord.get(i), yCoord.get(i));
			}
		}
	}

	public int getBoardSize() {
		return board.getBoardSize();
	}

	public void setBoard(GameBoard board) {
		this.board = board;
	}

	public void setTreasureX(int treasureX) {
		this.treasureX = treasureX;
	}

	public void setTreasureY(int treasureY) {
		this.treasureY = treasureY;
	}

	public void setExitX(int exitX) {
		this.exitX = exitX;
	}

	public void setExitY(int exitY) {
		this.exitY = exitY;
	}
	
}
