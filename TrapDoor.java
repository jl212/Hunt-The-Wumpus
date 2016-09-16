
public class TrapDoor extends Room {

	private Boolean triggered;
	
	public TrapDoor() {
		super();
		triggered = false;
	}
	
	public Boolean hasTriggered() {
		return triggered;
	}
	
	public void triggerTrap() {
		triggered = true;
	}
}
