package GUI;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

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

	private ClientPanel clientPanel;
	private GameplayPanel gameplayPanel;
	private JPanel chatpanel;
	private Controller controller;
	private static final int DIM = 4;
	
	public Connect4GUI() {
		super("Connect4_3D_View");

		setSize(400, 500);
		setVisible(true);

		init();
		buildGUI();
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
	}
	
	public void buildGUI() {
		
	}
	
	
	public static String getHostAddress() {
		try {
			InetAddress iaddr = InetAddress.getLocalHost();
			return iaddr.getHostName();
		} catch (UnknownHostException e) {
			return "?unknown?";
		}
	}
		
	public Vector getInputbuttonVector(JRadioButton src) {
		return gameplayPanel.getInputbuttonVector(src);
	}
	
	public String getMyMessageText() {
		return clientPanel.getMyMessageText();
	}
	
	
	public String getClientName() {
		return clientPanel.getClientName();
	}
	
	public int getPort() {
		return Integer.parseInt(clientPanel.getPort());
	}
	
	public InetAddress getInetAddress() {
		InetAddress iaddress = null;
		try {
			iaddress = InetAddress.getByName(clientPanel.getHostName());
		} catch (UnknownHostException e) {
			System.out.println("ERROR: Couldn't get address.");
		}
		return iaddress; 
	}
	public boolean equalsResetButton(JButton btn) {
		return btn.equals(gameplayPanel.getResetbutton());
	}
	
	public boolean equalsConnectButton(JButton btn) {
		return  btn.equals(clientPanel.getConnectButton());
	}
	
	public boolean equalsNameField(JTextField field) {
		return field.equals(clientPanel.getNameField());
	}
	
	public void setMyMessageText(String msg) {
		clientPanel.setMyMessageText(msg);
	}
	
	public void enableClientFields(boolean bool) {
		clientPanel.enableFields(bool);
	}
	
	public void setMyMessageFieldEditable(boolean bool) {
		clientPanel.setMyMessageFieldEditable(bool);
	}
	
	public void enableConnectButton(boolean bool) {
		clientPanel.enableConnectButton(bool);
	}
	
	public void addMessage(String msg) {
		clientPanel.addMessage(msg);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Game) {
			Game game = (Game) o;
			if (arg instanceof Game.Ending) {
				gameplayPanel.setStatuslabelText(game.getEnding().toString());
				gameplayPanel.enableResetButton(true);
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

}