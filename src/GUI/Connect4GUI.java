package GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import game.Board;
import game.Column;
import game.Controller;
import game.Game;
import game.Mark;
import game.Move;
import game.player.HumanPlayer;
import game.player.Player;
import util.MessageUI;
import util.Vector;
import util.exception.ColumnFullException;

public class Connect4GUI extends JFrame implements Observer, MessageUI {

	public final ClientPanel clientPanel = new ClientPanel();
	public final GameplayPanel gameplayPanel = new GameplayPanel();
	private Controller controller;
	private static final int DIM = 4;
    private final Mark thisPlayerMark = Mark.X;
    
	public Connect4GUI() {
		super("Connect4_3D_View");

		init();
		setSize(900, 500);

		buildGUI();
		gameplayPanel.statusLabel.setText("Go ahead X");

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				e.getWindow().dispose();
			}

			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		});
		setVisible(true);

	}

	public static void main(String[] args) {
		new Connect4GUI();
	}

	public void init() {
		controller = new Controller();
		//Player p1 = new HumanPlayer("Richard", Mark.X, controller);
		//Player p2 = new HumanPlayer("Aart", Mark.O, controller);
		//Game game = new Game(p1, p2);
		//new Thread(game).start();
		//game.addObserver(this);
		//controller.setGame(game);
		controller.setGUI(this);
		clientPanel.build(controller);
		gameplayPanel.build(controller);
	}
	
	public void createGame(Player p1, Player p2) {
		Game game = new Game(p1, p2);
		new Thread(game).start();
		game.addObserver(this);
		controller.setGame(game);
	}

	public void buildGUI() {
		JPanel mainpanel = new JPanel(new FlowLayout());
		BorderLayout blayout = new BorderLayout();
		blayout.setHgap(40);
		JPanel gpanel = new JPanel(blayout);
		gpanel.add(clientPanel, BorderLayout.WEST);
		gpanel.add(gameplayPanel, BorderLayout.EAST);
		mainpanel.add(gpanel);
		Container cc = getContentPane();
		cc.add(mainpanel);
	}

//	TODO Implement (or in client)
	public void enableGameplay() {
		gameplayPanel.enableInputButtons(true);
		gameplayPanel.statusButton.setEnabled(true);
		gameplayPanel.exitButton.setEnabled(true);
	}
//	
//	String text = eventSource.getText();
//	States newState = text.equals(States.READY.toString()) ? States.READY : States.UNREADY;
//	client.setState(newState);
//	mainGUI.gameplayPanel.statusButton.setText(newState.toString());

	
	public void addMessage(String msg) {
		clientPanel.addMessage(msg);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Game) {
			Game game = (Game) o;
			if (arg instanceof Game.Ending) {
				gameplayPanel.statusLabel.setText(game.getEnding().toString());
				gameplayPanel.resetButton.setEnabled(true);
			}
			if (arg instanceof Move) {
				Move move = (Move) arg;
				Column column = move.column;
				Mark mark = move.mark;
				try {
					TimeUnit.MILLISECONDS.sleep(100);
					gameplayPanel.selectInputbutton(column.x, column.y, false);
					gameplayPanel.setOutputbuttonText(column.x, column.y, game.getColumnHeigth(column) - 1,
							mark.toString());
				} catch (InterruptedException e1) {
				} catch (ColumnFullException e) {
					gameplayPanel.setOutputbuttonText(column.x, column.y, DIM - 1, mark.toString());
				}
				if (game.getBoardState().isColumnFull(column)) {
					gameplayPanel.enableInputbutton(column.x, column.y, false);
				}
				gameplayPanel.statusLabel.setText(mark.opposite() + "'s turn");
			}
		}
	}
	


}