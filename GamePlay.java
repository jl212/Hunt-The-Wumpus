
import java.awt.TextArea;
import java.util.*;

public class GamePlay {

	private GameBoard board;
	private Hint hintSystem;
	private TextArea output;
	private String level;
	
	public GamePlay(TextArea output, String level) {
		board = new GameBoard();
		hintSystem = new Hint(board);
		this.output = output;
		setLevel(level);
	}

	public GameBoard getBoard() {
		return board;
	}
	
	public int checkCoord(int coord) {
		int length = (board.getBoard().length - 1);
		if(coord<0) {
			coord = coord % length;
			coord = coord + length;
			coord = coord % length + 1;
		} else {
			if(coord>length) {
				coord = coord % length - 1;
			}
		}
		return coord;
	}
	
	public void shotArrow(String ans) {
		int yCoord = board.getYCoordPlayer();
		int xCoord = board.getXCoordPlayer();
		switch (ans) {
		case "n" : 
			shoot(xCoord, checkCoord(yCoord+1));
			break;
		case "e" :
			shoot(checkCoord(xCoord+1), yCoord);
			break;
		case "s" : 
			shoot(xCoord, checkCoord(yCoord-1));
			break;
		case "w" :
			shoot(checkCoord(xCoord-1), yCoord);
			break;
		case "ne" : 
			shoot(checkCoord(xCoord+1), checkCoord(yCoord+1));
			break;
		case "nw" :
			shoot(checkCoord(xCoord-1), checkCoord(yCoord+1));
			break;
		case "se" :
			shoot(checkCoord(xCoord+1), checkCoord(yCoord-1));
			break;
		case "sw" :
			shoot(checkCoord(xCoord-1), checkCoord(yCoord-1));
			break;
		default : output.append("This direction does not exist\n");
		}
		hintSystem.checkWumpusLocation();
	}
	
	public void shoot(int xCoord, int yCoord) {
		Room[][] board = this.board.getBoard();
		Player player = (Player) board[this.board.getYCoordPlayer()][this.board.getXCoordPlayer()].getPlayer();
		if(player.noOfArrows() > 0) {
			if(board[yCoord][xCoord].getgObject() instanceof Wumpus) {
				if(!((Wumpus) board[yCoord][xCoord].getgObject()).isDead()) {
					((Wumpus)board[yCoord][xCoord].getgObject()).setToDead();
					output.append("You killed the wumpus!\n");
					player.setKilledWumpus(true);
				}
			} else {
				output.append("Your arrow skitters across the ground..\n");
				moveWumpus();
			}
			player.removeArrow(board);
		} else {
			output.append("You do not have enough arrows!");
		}
		
	
	}

	public void moveWumpus() {
		ArrayList<Integer> xCoord = board.getXCoordWumpus();
		ArrayList<Integer> yCoord = board.getYCoordWumpus();
		Random generator = new Random();
		int Low = -1;
		int High = 1;
		for(int i=0; i<xCoord.size(); i++) {
			int randomX = generator.nextInt(High-Low) + Low;
			int randomY = generator.nextInt(High-Low) + Low;
			if(randomX == 0 && randomY == 0) {
				moveWumpus();
			} else {
				board.getBoard()[checkCoord(yCoord.get(i) + randomY)][checkCoord(xCoord.get(i) + randomX)].setgObject(board.getBoard()[yCoord.get(i)][xCoord.get(i)].getgObject());
				board.getBoard()[yCoord.get(i)][xCoord.get(i)].setgObject(new Empty());
			}
		}
	}

	public void getMove(String ans) {
		int yCoord = board.getYCoordPlayer();
		int xCoord = board.getXCoordPlayer();
		int yNorth = checkCoord(yCoord + 1);
		int ySouth = checkCoord(yCoord - 1);
		int xEast = checkCoord(xCoord + 1);
		int xWest = checkCoord(xCoord - 1);
		switch (ans) {
			case "n" : 
				move(xCoord, yNorth);
				break;
			case "e" :
				move(xEast, yCoord);
				break;
			case "s" : 
				move(xCoord, ySouth);
				break;
			case "w" :
				move(xWest, yCoord);
				break;
			case "ne" : 
				move(xEast, yNorth);
				break;
			case "nw" :
				move(xWest, yNorth);
				break;
			case "se" :
				move(xEast, ySouth);
				break;
			case "sw" :
				move(xWest, ySouth);
				break;
			default : output.append("This direction does not exist\n");
		}
	}

	public void move(int xCoord, int yCoord) {
		Room[][] board = this.board.getBoard();
		Player player = (Player) board[this.board.getYCoordPlayer()][this.board.getXCoordPlayer()].getPlayer();
		if(player.getNoOfMoves() > 0) {
			player.removeStep();
			if(board[yCoord][xCoord] instanceof BottomlessPit || board[yCoord][xCoord] instanceof Exit || board[yCoord][xCoord] instanceof TrapDoor
					|| !(board[yCoord][xCoord].getcObject() instanceof None) || !(board[yCoord][xCoord].getgObject() instanceof Empty)) {
				checkObject(xCoord, yCoord);
			} else {
				movePlayer(xCoord, yCoord);
			}
		} else {
			output.append("You have ran out of steps!");
			gameOver();
		}
	}

	public void checkObject(int xCoord, int yCoord) {
		Room[][] board = this.board.getBoard();
		checkRoom(board, xCoord, yCoord);
		if(!(board[yCoord][xCoord].getgObject() instanceof Empty)) {
			checkGameObject(board, xCoord, yCoord);
		}
		if(!(board[yCoord][xCoord].getcObject() instanceof None)) {
			checkCollectableObject(board, xCoord, yCoord);
		}
	}
	
	public void checkCollectableObject(Room[][] board, int xCoord, int yCoord) {
		Player player = (Player) board[this.board.getYCoordPlayer()][this.board.getXCoordPlayer()].getPlayer();
		if(board[yCoord][xCoord].getcObject() instanceof Treasure) {
			player.setTreasure((Treasure) board[yCoord][xCoord].getcObject());
			board[yCoord][xCoord].setcObject(new None());
			movePlayer(xCoord, yCoord);
			output.append("You found the treasure!!\n");
		}
		if(board[yCoord][xCoord].getcObject() instanceof Arrow){
			player.addArrow((Arrow)board[yCoord][xCoord].getcObject());
			board[yCoord][xCoord].setcObject(new None());
			movePlayer(xCoord, yCoord);
		}
	}

	public void checkGameObject(Room[][] board, int xCoord, int yCoord) {
		if(board[yCoord][xCoord].getgObject() instanceof Wumpus) {
			if(!((Wumpus) board[yCoord][xCoord].getgObject()).isDead()) {
				output.append("You were killed by the Wumpus!\n");
				gameOver();
			} else {
				movePlayer(xCoord, yCoord);
			}
		}
		if(board[yCoord][xCoord].getgObject() instanceof Superbat) {
			superbatMove();
			output.append("You were picked up by a superbat and moved to a random location!\n");
		}
	}

	public void checkRoom(Room[][] board, int xCoord, int yCoord) {
		if(board[yCoord][xCoord] instanceof BottomlessPit) {
			output.append("You fell to your death down a bottomless pit!\n");
			gameOver();
		}
		if(board[yCoord][xCoord] instanceof Exit) {
			foundExit(xCoord, yCoord);
		}
		if(board[yCoord][xCoord] instanceof TrapDoor) {
			steppedOnTrap(xCoord, yCoord);
		}
	}
	
	public void steppedOnTrap(int xCoord, int yCoord) {
		Random r = new Random();
		int x = r.nextInt(board.getBoardSize()+1);
		int y = r.nextInt(board.getBoardSize()+1);
		board.addWumpus(checkCoord(x+xCoord), checkCoord(y+yCoord));
	}

	public void movePlayer(int xCoord, int yCoord) {
		Room[][] board = this.board.getBoard();
		int playerX = this.board.getXCoordPlayer();
		int playerY = this.board.getYCoordPlayer();
		board[yCoord][xCoord].setPlayer(board[this.board.getYCoordPlayer()][this.board.getXCoordPlayer()].getPlayer());
		board[playerY][playerX].setPlayer(new Empty());
		this.board.setYCoordPlayer(yCoord);
		this.board.setXCoordPlayer(xCoord);
		checkAdjacent(this.board.getXCoordPlayer(), this.board.getYCoordPlayer());
	}
	
	public void foundExit(int xCoord, int yCoord) {
		output.append("You have found the Exit!\n");
		movePlayer(xCoord, yCoord);
		Player player = (Player) this.board.getBoard()[yCoord][xCoord].getPlayer();
		if(player.getTreasure() != null) {
			if(getLevel().equals("hard")) {
				if(player.getKilledWumpus()) {
					gameOver();
				}
			} else {
				gameOver();
			}
		} else {
			output.append("You cannot leave the cave as you haven't found the treasure!\n");
		}
	}

	public void superbatMove() {
		Random xGenerator = new Random();
		Random yGenerator = new Random();
		int xCoord = xGenerator.nextInt(board.getBoardSize());
		int yCoord = yGenerator.nextInt(board.getBoardSize());
		if(board.getBoard()[yCoord][xCoord].getPlayer() instanceof Empty) {
			board.getBoard()[yCoord][xCoord].setPlayer(board.getBoard()[this.board.getYCoordPlayer()][this.board.getXCoordPlayer()].getPlayer());
			board.getBoard()[this.board.getYCoordPlayer()][this.board.getXCoordPlayer()].setPlayer(new Empty());
			this.board.setYCoordPlayer(yCoord);
			this.board.setXCoordPlayer(xCoord);
			checkAdjacent(this.board.getXCoordPlayer(), this.board.getYCoordPlayer());
		} else {
			superbatMove();
		}
	}

	public void gameOver() {
		output.append("Game Over!");
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			
		} finally {
			System.exit(0);
		}
	}
	
	public void checkAdjacent(int xCoord, int yCoord) {
		int yNorth = checkCoord(yCoord + 1);
		int ySouth = checkCoord(yCoord - 1);
		int xEast = checkCoord(xCoord + 1);
		int xWest = checkCoord(xCoord - 1);
		String output = "";
		if (board.getBoard()[yNorth][xCoord].getgObject() instanceof Wumpus || board.getBoard()[ySouth][xCoord].getgObject() instanceof Wumpus ||
				board.getBoard()[yCoord][xEast].getgObject() instanceof Wumpus || board.getBoard()[yCoord][xWest].getgObject() instanceof Wumpus) {
				output += "You can smell a wumpus close-by.\n";
		} else {
			if (board.getBoard()[yNorth][xCoord].getcObject() instanceof Treasure || board.getBoard()[ySouth][xCoord].getcObject() instanceof Treasure ||
					board.getBoard()[yCoord][xEast].getcObject() instanceof Treasure || board.getBoard()[yCoord][xWest].getcObject() instanceof Treasure) {
					output += "You can see something glistening.\n";
			} else {
				if (board.getBoard()[yNorth][xCoord] instanceof BottomlessPit || board.getBoard()[ySouth][xCoord] instanceof BottomlessPit ||
						board.getBoard()[yCoord][xEast] instanceof BottomlessPit || board.getBoard()[yCoord][xWest] instanceof BottomlessPit) {
					output += ("You feel a breeze.\n");
				}	
			}
		}
		if (board.getBoard()[yNorth][xWest].getgObject() instanceof Wumpus || board.getBoard()[yNorth][xEast].getgObject() instanceof Wumpus || 
				board.getBoard()[ySouth][xEast].getgObject() instanceof Wumpus || board.getBoard()[ySouth][xWest].getgObject() instanceof Wumpus) {
			if(!output.contains("You can smell a wumpus close-by.\n")) {
				output += "You can smell a wumpus close-by.\n";
			}
		} else {
			if (board.getBoard()[yNorth][xWest].getcObject() instanceof Treasure || board.getBoard()[yNorth][xEast].getcObject() instanceof Treasure || 
					board.getBoard()[ySouth][xEast].getcObject() instanceof Treasure || board.getBoard()[ySouth][xWest].getcObject() instanceof Treasure) {
				if(!output.contains("You can see something glistening.\n")) {
					output += "You can see something glistening.\n";
				}
			} else {
				if(board.getBoard()[yNorth][xWest] instanceof BottomlessPit || board.getBoard()[yNorth][xEast] instanceof BottomlessPit || 
						board.getBoard()[ySouth][xEast] instanceof BottomlessPit || board.getBoard()[ySouth][xWest] instanceof BottomlessPit) {
					if(!output.contains("You feel a breeze.\n")) {
						output += "You feel a breeze.\n";
					}
				}
			}
		}
		this.output.append(output);
	}

	public Hint getHintSystem() {
		return hintSystem;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

}
