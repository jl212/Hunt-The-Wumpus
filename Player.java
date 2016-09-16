import java.util.ArrayList;


public class Player extends GameObject{

	private Treasure treasure;
	private ArrayList<Arrow> quiver = new ArrayList<Arrow>();
	private Boolean killedWumpus;
	private int noOfMoves;
	
	public Player(int boardSize) {
		treasure = null;
		giveArrows();
		setNoOfMoves(boardSize);
	}

	private void giveArrows() {
		for(int i=0; i<5; i++){
			quiver.add(new Arrow());
		}
	}
	
	public int noOfArrows() {
		return quiver.size();
	}
	
	public void removeArrow(Room[][] board) {
		int i= quiver.size()-1;
		int choiceNumber;
		int choiceNumber2;
        choiceNumber = (int) (20 * Math.random());
		choiceNumber2 = (int) (20*Math.random());
		board[choiceNumber][choiceNumber2].setcObject(quiver.get(i));
		quiver.remove(i);
	}

	public void setTreasure(Treasure treasure) {
		this.treasure = treasure;
	}

	public Treasure getTreasure() {
		return treasure;
	}

	public void addArrow(Arrow arrow) {
		quiver.add(arrow);
		
	}

	public Boolean getKilledWumpus() {
		return killedWumpus;
	}
	
	public void setKilledWumpus(Boolean killedWumpus) {
		this.killedWumpus = killedWumpus;
	}

	public int getNoOfMoves() {
		return noOfMoves;
	}
	
	public void removeStep() {
		noOfMoves--;
	}

	public void setNoOfMoves(int boardSize) {
		noOfMoves = boardSize*5;
	}

}
