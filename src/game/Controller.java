package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.InetAddress;

import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import GUI.Connect4GUI;
import client.Client;
import util.ProvidesMoves;
import util.Vector;

public class Controller implements ActionListener, KeyListener, ProvidesMoves {

	private Column column;
	private Game game;
	private Client client;
	private Connect4GUI mainGUI;

	public void setGUI(Connect4GUI gui) {
		mainGUI = gui;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	/**
	 * Receives input from GUI buttons, calls an appropriate command of Game.
	 */
	@Override
	public synchronized void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if ((src instanceof JButton)) {
			JButton eventSource = (JButton) src;
			if (mainGUI.equalsResetButton(eventSource)) {
				// game.resetBoard();
			}
			if (mainGUI.equalsConnectButton(eventSource))
				startConnecting();
		}
		if (src instanceof JRadioButton) {
			Vector buttonPos = mainGUI.getInputbuttonVector((JRadioButton) src);
			column = new Column(buttonPos.x, buttonPos.y);
			notifyAll();
		}

		if (src instanceof JTextField) {
			client.sendMessage(mainGUI.getMyMessageText());
			mainGUI.setMyMessageText("");
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

	public void startConnecting() {
		String name = mainGUI.getClientName();
		int port = mainGUI.getPort();
		InetAddress iaddress = mainGUI.getInetAddress();
		mainGUI.enableClientFields(false);
		try {
			client = new Client(name, iaddress, port, mainGUI);
			client.start();
			mainGUI.setMyMessageFieldEditable(true);
		} catch (IOException e) {
			System.out.println("ERROR: Client could not be created");
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		Object src = e.getSource();
		if (src instanceof JTextField) {
			JTextField srcTextField = (JTextField) src;
			if (mainGUI.equalsNameField(srcTextField)) {
				mainGUI.enableConnectButton(true);
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}
