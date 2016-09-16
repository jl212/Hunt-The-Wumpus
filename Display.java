import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;


@SuppressWarnings("serial")
public class Display extends Frame implements ActionListener {

	private GamePlay game;
	private Label lblDirection; 
	private TextField tfDirection;
	private TextArea output;
	private Button move;
	private Button hint;
	private Button shoot;
	
	public Display() {
		setLayout(new FlowLayout());
		lblDirection = new Label("Direction");
		(output = new TextArea()).setEditable(false);
		(tfDirection = new TextField(2)).setEditable(true);
		(move = new Button("Move")).addActionListener(this);
		(shoot = new Button("Shoot")).addActionListener(this);
		(hint = new Button("Hint")).addActionListener(this);
		add(lblDirection);
		add(tfDirection);
		add(move);
		add(hint);
		add(shoot);
		add(output);
		setTitle("Hunt the Wumpus");
		setSize(500, 275);
		game = new GamePlay(output, getInstructionsAndLevel());
		output.append("Start position " + game.getBoard().getPositionOfPlayer() + "\n");
		game.checkAdjacent(game.getBoard().getXCoordPlayer(), game.getBoard().getYCoordPlayer());
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
	            dispose();
	         }
		});
		setVisible(true);
	}
	
	@SuppressWarnings("resource")
	public String getInstructionsAndLevel() {
		Scanner scan = new Scanner(System.in);
		Instructions instruct = new Instructions();
		System.out.println(instruct.getInstructions() + "What level would you like to play at?\nEasy or Hard? (Default Level is Easy)");
		String level = scan.next().toLowerCase();
		return checkLevel(level);
	}

	public String checkLevel(String level) {
		if(level != "easy" || level != "hard") {
			return "easy";
		} else {
			return level;
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == move) {
			//get direction and send to move method
			game.getMove(tfDirection.getText().toLowerCase());
			output.append("You are at " + game.getBoard().getPositionOfPlayer()+"\n");
			tfDirection.setText("");
		} else {
			if(event.getSource() == shoot) {
				//get direction and send to shoot method
				game.shotArrow(tfDirection.getText().toLowerCase());
				tfDirection.setText("");
			} else {
				if(event.getSource() == hint) {
					output.append(game.getHintSystem().giveHint() +"\n");
				}
			}
		}
	}

	public GamePlay getGame() {
		return game;
	}

}
