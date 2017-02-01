package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import protocol.ClientState;
import game.Column;
import game.Game;
import game.Mark;
import game.player.HumanPlayer;
import protocol.command.Acknowledgement;
import protocol.command.Action;
import protocol.command.Command;
import protocol.command.Exit;
import protocol.command.Error;
import protocol.CommandParser;
import util.ProvidesMoves;
import util.Util;
import util.container.Pair;
import util.container.Vector;
import util.exception.CommandForbiddenException;
import util.exception.CommandInvalidException;
import util.exception.CommandUnsupportedException;

//ERRORS AND ACKNOWLEDGEMENT
public class Controller implements ActionListener, KeyListener, ProvidesMoves {

	private Column column;
	private Game game;
	private Client client;
	private Connect4GUI mainGUI;
	private final CommandParser commandParser = new CommandParser(Command.Direction.SERVER_TO_CLIENT);
	private Command prevCommand;

	public void setGUI(Connect4GUI gui) {
		mainGUI = gui;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	// TODO: Only enable buttons when it is one's turn.
	public synchronized void readCommand(String cmd) {
		if (cmd != null) {
			mainGUI.addMessage("Server message: " + cmd);
			Pair<Command, String[]> parsedCmd = getParsedCommand(cmd);

			Command command = parsedCmd.first;
			String[] args = parsedCmd.second;
			if (command instanceof Action && !client.getClientState().equals(ClientState.PENDING)) {
				handleAction(command, args);
			}
			if (command instanceof Acknowledgement) {
				handleAck();
			}
			if (command instanceof Error) {
				handleError(command);
			}
			if (command instanceof Exit) {
				handleExit(command);
			}
		}
	}

	private Pair<Command, String[]> getParsedCommand(String cmd) {
		try {
			return commandParser.parse(cmd);
		} catch (CommandUnsupportedException e) {
			client.sendMessage(Error.COMMAND_UNSUPPORTED.toString());
		} catch (CommandForbiddenException e) {
			client.sendMessage(Error.FORBIDDEN.toString());
		} catch (CommandInvalidException e) {
			client.sendMessage(Error.COMMAND_INVALID.toString());
		}
		return null;
	}

	public void handleAction(Command command, String[] args) {
		switch ((Action) command) {
		case START:
			if (client.getClientState() == ClientState.READY) {
				mainGUI.createGame(new HumanPlayer(args[0], Mark.X, this), new HumanPlayer(args[1], Mark.O, this));
				mainGUI.gameplayPanel.resetButton.setEnabled(true);
				mainGUI.gameplayPanel.exitButton.setEnabled(true);
				client.setClientState(ClientState.INGAME);
				if (client.getClientName().equals(args[0])) {
					mainGUI.gameplayPanel.enableInputButtons(true);
				}
				client.sendMessage(Acknowledgement.OK.toString());
			} else {
				client.sendMessage(Error.FORBIDDEN.toString());
			}
			break;
		case MOVE:
			if (client.getClientState() == ClientState.INGAME) {
				column = new Column(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
				if (!game.isColumnFull(column)) {
					notifyAll();
					client.sendMessage(Acknowledgement.OK.toString());
				} else {
					client.sendMessage(Error.ILLEGAL_MOVE.toString());
				}
				mainGUI.gameplayPanel.enableInputButtons(true);
			}
			mainGUI.updateStatusLabel();
			break;
		case SAY:
			mainGUI.addMessage(Util.join(args));
			client.sendMessage(Acknowledgement.OK.toString());
			break;
		default:
			client.sendMessage(Error.COMMAND_UNSUPPORTED.toString());
		}
	}

	public void handleAck() {
		if (prevCommand.equals(Action.CONNECT)) {
			mainGUI.clientPanel.tfHostname.setEnabled(false);
			mainGUI.clientPanel.tfName.setEnabled(false);
			mainGUI.clientPanel.tfPort.setEnabled(false);
			mainGUI.clientPanel.b1Connect.setEnabled(false);
			// mainGUI.clientPanel.tfMyMessage.setEnabled(true);
			mainGUI.gameplayPanel.statusButton.setEnabled(true);
			client.setClientState(ClientState.UNREADY);
			// client.sendMessage(Action.AVAILABLE.toString());
		}
		if (prevCommand.equals(Action.UNREADY)) {
			mainGUI.gameplayPanel.switchStatusButtonText();
			client.setClientState(ClientState.UNREADY);
		}
		if (prevCommand.equals(Action.READY)) {
			client.setClientState(ClientState.READY);
			mainGUI.gameplayPanel.switchStatusButtonText();
		}
		if (prevCommand.equals(Action.MOVE)) {
			mainGUI.gameplayPanel.enableInputButtons(false);
			mainGUI.updateStatusLabel();
			notifyAll();
		}
		mainGUI.clientPanel.errorField.setText(mainGUI.clientPanel.NO_ERROR);
	}

	public void handleError(Command command) {
		mainGUI.clientPanel.errorField.setText(command.toString());
		if (((Error) command).name().equals(Error.SERVER_SHUTTING_DOWN)) {
			client.shutdown();
		}
	}

	public void handleExit(Command command) {
		Exit exitCmd = (Exit) command;
		if (exitCmd.name().equals(Exit.FORFEITURE.toString())) {
			mainGUI.gameplayPanel.statusLabel.setText("Game won");
		}
		if (exitCmd.name().equals(Exit.TIMEOUT.toString())) {
			mainGUI.gameplayPanel.statusLabel.setText("Game lost");
		} else {
			mainGUI.gameplayPanel.statusLabel.setText(exitCmd.name());
		}
		client.setClientState(ClientState.UNREADY);
		mainGUI.gameplayPanel.switchStatusButtonText();
		mainGUI.gameplayPanel.statusButton.setEnabled(false);
		mainGUI.gameplayPanel.enableInputButtons(false);
		mainGUI.gameplayPanel.resetButton.setEnabled(true);
		mainGUI.gameplayPanel.exitButton.setEnabled(true);
	}

	/**
	 * Receives input from GUI buttons, sends the appropriate command.
	 */
	@Override
	public synchronized void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (src instanceof JButton) {
			JButton eventSource = (JButton) src;
			// TODO: Handle exit, reset and disconnect.
			if (mainGUI.gameplayPanel.exitButton.equals(eventSource)) {
				client.sendMessage(Exit.FORFEITURE.toString());
			}
			if (mainGUI.gameplayPanel.resetButton.equals(eventSource)) {
				// game.resetBoard();
				client.sendMessage(Exit.FORFEITURE.toString());
			}
			if (mainGUI.clientPanel.b1Connect.equals(eventSource)) {
				startConnecting();
				prevCommand = Action.CONNECT;
				client.sendMessage(Action.CONNECT + " " + client.getClientName());
			}
			if (mainGUI.gameplayPanel.statusButton.equals(eventSource)) {
				prevCommand = (eventSource.getText() == Action.READY.toString()) ? Action.READY : Action.UNREADY;
				client.sendMessage(prevCommand.toString());
			}
		}
		if (client != null) {
			if (src instanceof JRadioButton) {
				Vector buttonPos = mainGUI.gameplayPanel.getInputbuttonVector((JRadioButton) src);
				column = new Column(buttonPos.x, buttonPos.y);
				client.sendMessage("MOVE" + " " + buttonPos.x + " " + buttonPos.y);
				prevCommand = Action.MOVE;
			}

			if (src instanceof JTextField) {
				// client.sendMessage(Action.SAY +
				// mainGUI.clientPanel.tfMyMessage.getText());
				// mainGUI.clientPanel.tfMyMessage.setText("");
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		Object src = e.getSource();
		if (src instanceof JTextField) {
			JTextField srcTextField = (JTextField) src;
			if (mainGUI.clientPanel.tfName.equals(srcTextField)) {
				mainGUI.clientPanel.b1Connect.setEnabled(true);
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

	public void startConnecting() {
		String name = mainGUI.clientPanel.tfName.getText().trim();
		int port = 0;
		InetAddress iaddress = null;
		try {
			port = Integer.parseInt(mainGUI.clientPanel.tfPort.getText());
		} catch (NumberFormatException e) {
			System.out.println("Given port is not an integer.");
		}
		try {
			iaddress = InetAddress.getByName(mainGUI.clientPanel.tfHostname.getText());
		} catch (UnknownHostException e) {
			System.out.println("Given address is not valid");
		}
		try {
			client = new Client(name, iaddress, port, mainGUI, this);
			client.start();
		} catch (IOException e) {
			System.out.println("ERROR: Client could not be created");
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}
