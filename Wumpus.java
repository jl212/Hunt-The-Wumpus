
public class Wumpus extends GameObject{

	private Boolean dead;
	
	public Wumpus() {
		dead = false;
	}
	
	public Boolean isDead() {
		return dead;
	}
	
	public void setToDead() {
		dead = true;
	}

}
