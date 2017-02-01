package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import protocol.ClientState;
import util.MessageUI;



public class Client extends Thread {
	private static final String USAGE = "usage: " + Client.class.getName()
			+ " <name> <address> <port> or <name> <port> ";

	private String clientName;
	private MessageUI mui;
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;
	private ClientState state = ClientState.PENDING;
	private Controller controller;
	
	/**
	 * Constructs a Client-object and tries to make a socket connection.
	 */
	public Client(String name, InetAddress host, int port, MessageUI m, Controller controller) 
			throws IOException {
		clientName = name;
		mui = m;
		// try to open a Socket to the server
		try {
			sock = new Socket(host, port);
		} catch (IOException e) {
			mui.addMessage("ERROR: could not create a socket on " + host + " and port " + port);
			mui.addMessage(USAGE);
			System.exit(0);
		}
		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		this.controller = controller;
	}
	

	public ClientState getClientState() {
		return state;
	}
	
	
	public void setClientState(ClientState cstate) {
		state = cstate;
	}
	
	
	/**
	 * Reads the messages in the socket connection. Each message will be
	 * forwarded to the MessageUI
	 */
	public void run() {
		(new Thread(new Reader())).start();
	}

	
	/** close the socket connection. */
	public void shutdown() {
		//sendMessage("[" + clientName + " has left]");
		mui.addMessage("Closing socket connection...");
		try {
			in.close();
			out.close();
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/** returns the client name. */
	public String getClientName() {
		return clientName;
	}

	
	public synchronized void sendMessage(String msg) {
		try {
			out.write(msg);
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
					controller.readCommand(msg);
				}
			} catch (IOException e) {
				shutdown();
			}
		}
	}
}


