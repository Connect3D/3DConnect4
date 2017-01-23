package game;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.LocalGame;
import game.player.HumanPlayer;

/**
 * @author RichKok
 * GUI with buttons and text notifying the user of the game's status.
 * An Actionlistener is attached to the buttons, calling the private Controller class which in turn issues the Game class.
 * The view is added as an observer to the game class, therefore changes in Game consequently call the update command.
 * Changes of state in the game class are observed 
 */
public class View extends JFrame implements Observer   {

	private Controller controller;
	private JPanel panel;
	private JLabel turn;
	private JButton[] buttons;
	private JButton anotherGame;
	private static final int DIM = 4;
	
	public View(Game game) {
		super("Connect4_3D_View");
		controller = new Controller(game);
		buildGUI(game);
		setSize(300, 300);
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				e.getWindow().dispose();
			}

			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		});
	}
	
	private void buildGUI(Game game) { 
		panel = new JPanel(new FlowLayout());
		JPanel panelNorth = new JPanel(new GridLayout(DIM, DIM));
		
		buttons = new JButton[DIM * DIM];
		for (int i = 0; i < DIM * DIM; i++) {
			JButton button = new JButton("EMPTY");
			buttons[i] = button;
			button.addActionListener(controller);
			panelNorth.add(button);
		}
		
		panel.add(panelNorth, BorderLayout.NORTH);
		JPanel panelSouth = new JPanel(new BorderLayout());
		turn = new JLabel("It is " + game.getCurrentPlayer().getName() + "'s turn.");
		anotherGame = new JButton("Play again");
		anotherGame.addActionListener(controller);
		anotherGame.setEnabled(false);
		
		panelSouth.add(turn);
		panelSouth.add(anotherGame, BorderLayout.SOUTH);
		
		Container cc = getContentPane();
		cc.setLayout(new FlowLayout());
		cc.add(panelNorth);
		cc.add(panelSouth);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Game) {
			
		}
		
	}
	
	public static void main(String[] args) {
		Game game = new Game(new HumanPlayer("Rich", Mark.X, new LocalGame()), new HumanPlayer("Aart", Mark.O, new LocalGame()));
		new View(game);
	}


	class Controller implements ActionListener {

		Game game;
		
		public Controller(Game game) {
			this.game = game;
		}
		
		/**
		 * Receives input from GUI buttons, calls an appropriate command of Game.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
				
		}
		
	}
}
