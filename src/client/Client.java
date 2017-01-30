package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import game.Controller;
import game.Game;
import game.Mark;
import game.player.HumanPlayer;
import protocol.command.Action;
import util.MessageUI;

/**
 * P2 prac wk4. <br>
 * Client.
 * 
 * @author Theo Ruys
 * @version 2005.02.21
 */
public class Client extends Thread {
	private static final String USAGE = "usage: " + Client.class.getName()
			+ " <name> <address> <port> or <name> <port> ";

	private String clientName;
	private MessageUI mui;
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;
	private String message;
	public States state;
	private Controller controller;
	
	/**
	 * Constructs a Client-object and tries to make a socket connection.
	 */
	public Client(String name, InetAddress host, int port, MessageUI m, Controller controller) throws IOException {
		clientName = name;
		mui = m;
		// try to open a Socket to the server
		try {
			sock = new Socket(host, port);
		} catch (IOException e) {
			mui.addMessage("ERROR: could not create a socket on " + host + " and port " + port);
			System.exit(0);
		}
		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		this.controller = controller;
		setState(States.UNREADY);
	}

	public States getStatus() {
		return state;
	}
	/**
	 * Reads the messages in the socket connection. Each message will be
	 * forwarded to the MessageUI
	 */
	public void run() {
		BufferedReader terminal = new BufferedReader(new InputStreamReader(System.in));
		(new Thread(new Reader())).start();
		setState(States.UNREADY);
		sendMessage(Action.CONNECT + " " + clientName);
	}

	public void setState(States state) {
		this.state = state;
	}

	/** close the socket connection. */
	public void shutdown() {
		//sendMessage("[" + clientName + " has left]");
		sendMessage(Action.DISCONNECT.toString());
		mui.addMessage("Closing socket connection...");
		try {
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** returns the client name. */
	public String getClientName() {
		return clientName;
	}

	public void sendMessage(String message) {
		try {
			out.write(message);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			shutdown();
		}
	}

	//TODO: Listen, parse, send back, or process
	class Reader implements Runnable {
		public void run() {
			try {
				String msg = "";
				while (msg != null) {
					msg = in.readLine();
					controller.readCommand(msg);
				}
			} catch (IOException e) {
				shutdown();
			}
		}
	}
}


