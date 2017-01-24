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
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

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

	private Controller controller;
	private JButton anotherGame;
	private JLabel turn;
	private JRadioButton[][] inputButtons;
	private JButton[] outputButtons;
	private static final int DIM = 4;

	public View() {
		super("Connect4_3D_View");

		init();
		setSize(600, 600);
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
	
	public void init() {
		controller = new Controller();
		Player p1 = new HumanPlayer("Richard", Mark.X, controller);
		Player p2 = new HumanPlayer("Aart", Mark.O, controller);
		Game game = new Game(p1, p2);
		Thread gameThread = new Thread(game);
		gameThread.start();
		game.addObserver(this);
		controller.setGame(game);
		buildGUI(game, controller);
	}

	private void buildGUI(Game game, Controller controller) {
		JPanel mainPanel = new JPanel(new BorderLayout());
		GridLayout glayout = new GridLayout(3, 1);
		glayout.setVgap(0);
		
		JPanel playPanel = new JPanel(glayout);
		playPanel.add(createStatusPanel(game));
		playPanel.add(createInputPanel());

		playPanel.add(createMenuPanel(), BorderLayout.SOUTH);
		
		JPanel mainOutputPanel = new JPanel(new FlowLayout());
		mainOutputPanel.add(createColumnOutputPanel());
		
		mainPanel.add(playPanel, BorderLayout.WEST);
		mainPanel.add(mainOutputPanel, BorderLayout.EAST);
		
		Container cc = getContentPane();
		cc.setLayout(new FlowLayout());
		cc.add(mainPanel);
	}
	
	public JPanel createInputPanel() {
		JPanel input = new JPanel(new GridLayout(DIM, DIM));
		inputButtons = new JRadioButton[Board.WIDTH][Board.DEPTH];
		for (int x = 0; x < Board.WIDTH; x++) {
			for (int y = 0; y < Board.DEPTH; y++) {
				JRadioButton button = new JRadioButton();
				inputButtons[x][y] = button;
				button.addActionListener(controller);
				input.add(button);
			}
		}
		return input;
	}
	
	
	public JPanel createMenuPanel() {
		JPanel menuPanel = new JPanel(new FlowLayout());
		anotherGame = new JButton("Play again");
		anotherGame.addActionListener(controller);
		anotherGame.setEnabled(false);
		menuPanel.add(anotherGame);
		return menuPanel;
	}
	
	public JPanel createStatusPanel(Game game) { 
		JPanel statusPanel = new JPanel(new FlowLayout());
		turn = new JLabel("");
		turn.setText(
				"It is " + game.getCurrentPlayerName() + "'s turn. With mark " + game.getCurrentPlayerMark() + ".");
		statusPanel.add(turn);
		return statusPanel;
	}
	
	public JPanel createColumnOutputPanel() {
		JPanel fpanel = new JPanel(new FlowLayout());
		outputButtons = new JButton[DIM * DIM * DIM];
		GridLayout gLayout = new GridLayout(DIM, 1);
		gLayout.setVgap(20);
		JPanel mgpanel = new JPanel(gLayout);
		for (int i = 0; i < DIM; i++) {
			JPanel gpanel = new JPanel(new GridLayout(DIM, DIM));

			for (int j = 0; j < DIM * DIM; j++) {
				JButton button = new JButton();
				button.setPreferredSize(new Dimension(20, 20));
				outputButtons[i] = button;
				gpanel.add(button);
			}
			mgpanel.add(gpanel);
		}
		fpanel.add(mgpanel);
		return fpanel;
	}

	// TODO: Instantiate classes in a more structured manner. Possible
	// instantiate them in other classes.
	public static void main(String[] args) {
		new View();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Game) {
			Game game = (Game) o;
			if (arg instanceof Game.Ending) {
				turn.setText(game.getEnding().toString());
				anotherGame.setEnabled(true);
			}
			if (arg instanceof Move) {
				Move move = (Move) arg;
				//outputButtons[move.column.x][move.column.y].setText(move.mark.toString());
				if (game.getBoardState().isColumnFull(move.column)) {
					//buttons[move.column.x][move.column.y].setEnabled(false);
				}
				turn.setText("It is " + game.getCurrentPlayerName() + "'s turn. With mark " + move.mark.opposite() + ".");
				}
			}
	}

	public void updateTurn(String playername) {
		turn.setText("It is " + playername + "'s turn.");
	}

	public void resetButtons() {
		for (int x = 0; x < Board.WIDTH; x++) {
			for (int y = 0; y < Board.DEPTH; y++) {
				//JRadioButton button = buttons[x][y];
				//button.setEnabled(true);
				anotherGame.setEnabled(false);
			}
		}
	}

	public Vector getButtonVector(JButton button) {
		for (int x = 0; x < Board.WIDTH; x++) {
			for (int y = 0; y < Board.DEPTH; y++) {
				//if (button.equals(buttons[x][y])) {
					return new Vector(x, y);
				//}
			}
		}
		return null;
	}

	class Controller implements ActionListener, ProvidesMoves {

		private Column column;
		private Game game;

		private void setGame(Game game) {
			this.game = game;
		}

		/**
		 * Receives input from GUI buttons, calls an appropriate command of
		 * Game.
		 */
		@Override
		public synchronized void actionPerformed(ActionEvent e) {
			Object src = e.getSource();
			if (src instanceof JButton) {
				if (src.equals(anotherGame)) {
					//game.resetBoard();
				} else {
					Vector buttonPos = getButtonVector((JButton) src);
					column = new Column(buttonPos.x, buttonPos.y);
					notifyAll();
				}
			}
		}

		@Override
		public synchronized Column waitForMove() {
			try {
				while (column == null) {
					wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Column choice = new Column(column);
			column = null;
			return choice;
		}

	}
}
