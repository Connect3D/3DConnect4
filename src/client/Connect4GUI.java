package client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import client.GUIPanels.ClientPanel;
import client.GUIPanels.GameplayPanel;
import game.Column;
import game.Game;
import game.Mark;
import game.Move;
import game.player.Player;
import util.MessageUI;
import util.exception.ColumnFullException;



public class Connect4GUI extends JFrame implements Observer, MessageUI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final ClientPanel clientPanel = new ClientPanel();
	public final GameplayPanel gameplayPanel = new GameplayPanel();
	private Controller controller;
	private static final int DIM = 4;
	private Mark currentMark;
	public static final String USAGE = 
			"Run argument: HUMAN, for a human game, else provide no argument.";
	
	public Connect4GUI(String playerType) {
		super("Connect4_3D_View");

		init(playerType.equals("HUMAN") ? true : false);
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

	
	public void init(boolean humanGame) {
		controller = new Controller(humanGame);
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

	
	public void addMessage(String msg) {
		clientPanel.addMessage(msg);
	}

	
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Game && arg instanceof Move) {
			Game game = (Game) o;
			Move move = (Move) arg;
			Column column = move.column;
			Mark mark = move.mark;
			currentMark = mark;
			try {
				TimeUnit.MILLISECONDS.sleep(100);
				gameplayPanel.selectInputbutton(column.x, column.y, false);
				gameplayPanel.setOutputbuttonText(column.x, column.y, 
						game.getColumnHeigth(column) - 1,
						mark.toString());
			} catch (InterruptedException e1) {
			} catch (ColumnFullException e) {
				gameplayPanel.setOutputbuttonText(column.x, column.y, DIM - 1, mark.toString());
			}
			if (game.getBoardState().isColumnFull(column)) {
				gameplayPanel.enableInputbutton(column.x, column.y, false);
			}
		}
	}

	
	public void updateStatusLabel() {
		gameplayPanel.statusLabel.setText(currentMark + "'s turn");
	}

	
	public static String getHostAddress() {
		try {
			InetAddress iaddr = InetAddress.getLocalHost();
			return iaddr.getHostName();
		} catch (UnknownHostException e) {
			return "?unknowns?";
		}
	}

	
	public static void main(String[] args) {
		if (args == null) {
			new Connect4GUI("computer");
		}
		if (args.length == 1) {
			new Connect4GUI(args[0]); 
		} else {
			System.out.println(USAGE);
		}
	}
}