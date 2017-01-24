package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import game.Game;
import game.Mark;
import game.player.HumanPlayer;
import util.MessageUI;
import util.View;

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

	/**
	 * Constructs a Client-object and tries to make a socket connection.
	 */
	public Client(String name, InetAddress host, int port, MessageUI m) throws IOException {
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
	}

	/**
	 * Reads the messages in the socket connection. Each message will be
	 * forwarded to the MessageUI
	 */
	public void run() {
		BufferedReader terminal = new BufferedReader(new InputStreamReader(System.in));
		(new Thread(new Reader())).start();
		sendMessage(clientName);
	}

	/** close the socket connection. */
	public void shutdown() {
		sendMessage("[" + clientName + " has left]");

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

	
	//ASK WHETHER THIS IS A GOOD SOLUTION OR THE OBSERVER PATTERN SHOULD BE APPLIED.
	public void sendMessage(String message) {
		try {
			out.write(message);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			shutdown();
		}
	}

	class Reader implements Runnable {
		public void run() {
			try {
				String msg = "";
				while (msg != null) {
					msg = in.readLine();
					mui.addMessage(msg);
				}
			} catch (IOException e) {
				shutdown();
			}
		}
	}
}


