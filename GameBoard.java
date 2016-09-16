import java.util.*;


public class GameBoard {

	private Room[][] board;
	private int boardSize;
	private Random generator;
	private int xCoord;
	private int yCoord;
	
	
	public GameBoard() {
		generator = new Random();
		setBoardSize();
		createBoard();
	}

	public void createBoard() {
		board = new Room[boardSize][boardSize];
		for(int i=0; i<boardSize; i++) {
			for(int j=0; j<boardSize; j++) {
				board[i][j] = new Room();
			}
		}
		initaliseBoard();
	}
	
	public void initaliseBoard() {
		addPlayer(generator.nextInt(boardSize), generator.nextInt(boardSize));
		addWumpus(generator.nextInt(boardSize), generator.nextInt(boardSize));
		addExit(generator.nextInt(boardSize), generator.nextInt(boardSize));
		addObstacles();
		addTreasure(generator.nextInt(boardSize), generator.nextInt(boardSize));
	}
	
	public void addPlayer(int xCoord, int yCoord) {
		if(board[yCoord][xCoord].getPlayer() instanceof Empty) {
			board[yCoord][xCoord].setPlayer(new Player(boardSize));
		} else {
			addPlayer(generator.nextInt(boardSize), generator.nextInt(boardSize));
		}
		this.xCoord = xCoord;
		this.yCoord = yCoord;
	}
	
	public void addObstacles() {
		for(int j=0; j<=numberOfObstacles(); j++) {
			addPit(generator.nextInt(boardSize), generator.nextInt(boardSize));
		}
		for(int k=0; k<=numberOfTrapDoors(); k++) {
			addTrap(generator.nextInt(boardSize), generator.nextInt(boardSize));
		}
		for(int i=0; i<=numberOfObstacles(); i++) {
			addBat(generator.nextInt(boardSize), generator.nextInt(boardSize));
		}
	}

	public int numberOfTrapDoors() {
		return generator.nextInt(10);
	}

	public int numberOfObstacles() {
		if(boardSize < 31) {
			return generator.nextInt(15-10) + 10;
		} else {
			if(boardSize < 41) {
				return generator.nextInt(35-25) + 25;
			} else {
				if(boardSize < 51) {
					return generator.nextInt(55-40) + 40;
				}
			}
		}
		return boardSize;
	}

	public void addTreasure(int xCoord, int yCoord) {
		if(xCoord == boardSize-1 || xCoord == 0 || yCoord == boardSize-1 || yCoord == 0) {
			while(xCoord == 0 || xCoord == boardSize-1) {
				xCoord = generator.nextInt(boardSize);
			}
			while(yCoord == 0 || yCoord == boardSize-1) {
				yCoord = generator.nextInt(boardSize);			
			}
		}
		int[] xCoords = {xCoord+1, xCoord, xCoord-1};
		int[] yCoords = {yCoord+1, yCoord, yCoord-1};
		int count = 0;
		for(int i : xCoords) {
			for(int j: yCoords) {
				if(board[j][i].getcObject() instanceof None || !(board[j][i] instanceof BottomlessPit)) {
					count++;
				}
			}
		}
		if(count > 3) {
			if(board[yCoord][xCoord].getcObject() instanceof None) {
				board[yCoord][xCoord].setcObject(new Treasure());
			} 
		} else {
			addTreasure(generator.nextInt(boardSize), generator.nextInt(boardSize));
		}
	}
	
	public void addPit(int xCoord, int yCoord) {
		if(board[yCoord][xCoord] instanceof Room) {
			if(board[yCoord][xCoord].getgObject() instanceof Empty && board[yCoord][xCoord].getcObject() instanceof None && board[yCoord][xCoord].getPlayer() instanceof Empty) {
				board[yCoord][xCoord] = new BottomlessPit();
			} else {
				addPit(generator.nextInt(boardSize), generator.nextInt(boardSize));
			}
		} 
	}
	
	public void addTrap(int xCoord, int yCoord) {
		if(board[yCoord][xCoord] instanceof Room) {
			if(board[yCoord][xCoord].getgObject() instanceof Empty && board[yCoord][xCoord].getcObject() instanceof None && board[yCoord][xCoord].getPlayer() instanceof Empty) {
				board[yCoord][xCoord] = new TrapDoor();
			} else {
				addTrap(generator.nextInt(boardSize), generator.nextInt(boardSize));
			}
		} 
	}

	public void addBat(int xCoord, int yCoord) {
		if(board[yCoord][xCoord].getgObject() instanceof Empty) {
			board[yCoord][xCoord].setgObject(new Superbat());
		} else {
			addBat(generator.nextInt(boardSize), generator.nextInt(boardSize));
		}		
	}

	public void addExit(int xCoord, int yCoord) {
		if(xCoord == boardSize-1 || xCoord == 0 || yCoord == boardSize-1 || yCoord == 0) {
			while(xCoord == 0 || xCoord == boardSize-1) {
				xCoord = generator.nextInt(boardSize);
			}
			while(yCoord == 0 || yCoord == boardSize-1) {
				yCoord = generator.nextInt(boardSize);			
			}
		}
		int[] xCoords = {xCoord+1, xCoord, xCoord-1};
		int[] yCoords = {yCoord+1, yCoord, yCoord-1};
		int count = 0;
		for(int i : xCoords) {
			for(int j: yCoords) {
				if(board[j][i].getgObject() instanceof Empty || !(board[j][i] instanceof BottomlessPit)) {
					count++;
				}
			}
		}
		if(count >3) {
			if(board[yCoord][xCoord].getgObject() instanceof Empty && board[yCoord][xCoord].getPlayer() instanceof Empty) {
				board[yCoord][xCoord] = new Exit();
			} 
		} else {
			addExit(generator.nextInt(boardSize), generator.nextInt(boardSize));
		}
	}

	public void addWumpus(int xCoord, int yCoord) {
		if(board[yCoord][xCoord].getgObject() instanceof Empty) {
			board[yCoord][xCoord].setgObject(new Wumpus());
		} else {
			addWumpus(generator.nextInt(boardSize), generator.nextInt(boardSize));
		}
		
	}
	
	public String getPositionOfPlayer() {
		String position = "";
		int steps=0;
		int arrows = 0;
		for(int i=0; i<board.length; i++) {
			for(int j=0; j<board[i].length; j++) {
				if(board[i][j].getPlayer() instanceof Player) {
					position = "("+j+","+i+")";
					steps = ((Player)(board[i][j].getPlayer())).getNoOfMoves();
					arrows = ((Player)(board[i][j].getPlayer())).noOfArrows();
				}
			}
		}
		position +="   No. steps remaining: " + steps + "   No. of arrows: " + arrows;
		return position;
	}
	
	public int getYCoordPlayer() {
		return yCoord;
	}

	public int getXCoordPlayer() {
		return xCoord;
	}
	
	public void setYCoordPlayer(int yCoord) {
		this.yCoord = yCoord;
	}
	
	public void setXCoordPlayer(int xCoord) {
		this.xCoord = xCoord;
	}

	public Room[][] getBoard() {
		return board;
	}

	public int getBoardSize() {
		return boardSize;
	}
	
	public void setBoardSize() {
		Random r = new Random();
		boardSize = r.nextInt(50-20) + 20;
	}

	public ArrayList<Integer> getXCoordWumpus() {
		ArrayList<Integer> xCoord = new ArrayList<Integer>();
		for(int i=0; i<boardSize; i++) {
			for(int j=0; j<boardSize; j++) {
				if(board[i][j].getgObject() instanceof Wumpus) {
					xCoord.add(j);
				}
			}
		}
		return xCoord;
	}
	
	public ArrayList<Integer> getYCoordWumpus() {
		ArrayList<Integer> yCoord = new ArrayList<Integer>();
		for(int i=0; i<boardSize; i++) {
			for(int j=0; j<boardSize; j++) {
				if(board[i][j].getgObject() instanceof Wumpus) {
					yCoord.add(i);
				}
			}
		}
		return yCoord;
	}

}
