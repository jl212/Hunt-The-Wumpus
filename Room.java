
public class Room {

	private GameObject gObject;
	private CollectableObject cObject;
	private GameObject player;
	
	public Room() {
		gObject = new Empty();
		cObject = new None();
		player = new Empty();
	}

	public GameObject getgObject() {
		return gObject;
	}

	public void setgObject(GameObject gObject) {
		this.gObject = gObject;
	}

	public CollectableObject getcObject() {
		return cObject;
	}

	public void setcObject(CollectableObject cObject) {
		this.cObject = cObject;
	}

	public GameObject getPlayer() {
		return player;
	}

	public void setPlayer(GameObject player) {
		this.player = player;
	}
	
	public void removePlayer() {
		player = null;
	}
	
	public void removeCObject() {
		cObject = null;
	}
	
	public void removeGObject() {
		gObject = null;
	}
	
}
