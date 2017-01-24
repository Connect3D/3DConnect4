package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import game.Column;
import game.Game;
import game.Mark;
import game.Move;
import game.player.HumanPlayer;
import game.player.Player;
import util.ProvidesMoves;
import util.Vector;
import util.exception.ColumnFullException;

public class Connect4GUI extends JFrame implements Observer {

	private JPanel serverPanel;
	private GameplayPanel gameplayPanel;
	private JPanel chatpanel;
	private Controller controller;
	private static final int DIM = 4;
	
	public Connect4GUI() {
		super("Connect4_3D_View");

		init();
		setSize(400, 500);
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
	    new Thread(game).start();
		game.addObserver(this);
		controller.setGame(game);
		buildGUI();
	}
	
	public void buildGUI() {
		
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Game) {
			Game game = (Game) o;
			if (arg instanceof Game.Ending) {
				gameplayPanel.setStatuslabelText(game.getEnding().toString());
				gameplayPanel.enableAnothergameButton(true);
			}
			if (arg instanceof Move) {
				Move move = (Move) arg;
				Column column = move.column;
				Mark mark = move.mark;
				try {
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (InterruptedException e1) {
				}
				gameplayPanel.selectInputbutton(column.x, column.y, false); 
				try {
					gameplayPanel.setOutputbuttonText(column.x, column.y, game.getColumnHeigth(column) - 1, mark.toString());
				} catch (ColumnFullException e) {
					gameplayPanel.setOutputbuttonText(column.x, column.y, DIM - 1, mark.toString());
				}
				if (game.getBoardState().isColumnFull(column)) {
					gameplayPanel.enableInputbutton(column.x, column.y, false); 
				}
				gameplayPanel.setStatuslabelText(mark + "'s turn");
			}
		}
	}

	public class Controller implements ActionListener, ProvidesMoves {

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
			if (src.equals(gameplayPanel.getAnothergameButton())) {
				// game.resetBoard();
			}
			if (src instanceof JRadioButton) {
				Vector buttonPos = gameplayPanel.getInputbuttonVector((JRadioButton) src);
				column = new Column(buttonPos.x, buttonPos.y);
				notifyAll();
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