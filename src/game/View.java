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
import game.player.Player;
import util.ProvidesMoves;

/**
 * @author RichKok GUI with buttons and text notifying the user of the game's
 *         status. An Actionlistener is attached to the buttons, calling the
 *         private Controller class which in turn issues the Game class. The
 *         view is added as an observer to the game class, therefore changes in
 *         Game consequently call the update command. Changes of state in the
 *         game class are observed
 */
public class View extends JFrame implements Observer {

	private JPanel panel;
	private JLabel turn;
	private JButton[][] buttons;
	private JButton anotherGame;
	private static final int DIM = 4;

	public View(Controller controller) {
		super("Connect4_3D_View");
		buildGUI(controller);
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

	private void buildGUI(Controller controller) {
		panel = new JPanel(new FlowLayout());
		JPanel panelNorth = new JPanel(new GridLayout(DIM, DIM));

		buttons = new JButton[Board.WIDTH][Board.DEPTH];
		for (int x = 0; x < Board.WIDTH; x++) {
			for (int y = 0; y < Board.DEPTH; y++) {
				JButton button = new JButton("EMPTY");
				buttons[x][y] = button;
				button.addActionListener(controller);
				panelNorth.add(button);
			}
		}

		panel.add(panelNorth, BorderLayout.NORTH);
		JPanel panelSouth = new JPanel(new BorderLayout());
		anotherGame = new JButton("Play again");
		anotherGame.addActionListener(controller);
		anotherGame.setEnabled(false);
		turn = new JLabel("");
		panelSouth.add(turn);
		panelSouth.add(anotherGame, BorderLayout.SOUTH);

		Container cc = getContentPane();
		cc.setLayout(new FlowLayout());
		cc.add(panelNorth);
		cc.add(panelSouth);
	}

	public static void main(String[] args) {
		Controller controller = new Controller();
		Player p1 = new HumanPlayer("Richard", Mark.X, controller);
		Player p2 = new HumanPlayer("Aart", Mark.O, controller);
		Game game = new Game(p1, p2);
		Thread gameThread = new Thread(game);
		gameThread.start();
		View view = new View(controller);
		view.updateTurn(game.getCurrentPlayerName());
		controller.setView(view);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Game && arg instanceof Move) {
			Move move = (Move) arg;
			buttons[move.column.x][move.column.y].setText(move.mark.toString());
			updateTurn(((Game) o).getCurrentPlayerName());
		}
	}

	public void updateTurn(String playername) {
		turn = new JLabel("It is " + playername + "'s turn.");
	}

	public Vector getButtonVector(JButton button) {
		for (int x = 0; x < Board.WIDTH; x++) {
			for (int y = 0; y < Board.DEPTH; y++) {
				if (button.equals(buttons[x][y])) {
					return new Vector(x, y);
				}
			}
		}
		return null;
	}
	
}
