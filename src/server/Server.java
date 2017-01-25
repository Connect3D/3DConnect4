package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;

import protocol.Name;
import util.MessageUI;

/*
 * Whenever a new client connects to the server their connection will be accepted,
 * and a new ClientHandler thread is made. However only when the client actually 
 * asks to join the server by sending the "Connect"  command and their name, 
 * will they actually be added to the active clients HashMap.
 * Only from this moment on clients can start games with each other and chat.
 */

public class Server implements Runnable {
	
	private final HashMap<String, ClientHandler> activeConnections;
	private final HashSet<String> playerNames;
	private final ServerSocket serverSocket;
	private final MessageUI console;
	
	
	public Server(int port, MessageUI messageUi) throws IOException{
		console = messageUi;
		activeConnections = new HashMap<String, ClientHandler>();
		playerNames = new HashSet<String>();
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			throw new IOException("ERROR: serversocket could not be created on port " + port);
		}
	}
	
	
	public void run() {
		boolean exit = false;
		while (!exit) {
			try {
				Socket socket = serverSocket.accept();
				new Thread(new ClientHandler(this, socket)).start();
			}
			catch (IOException e) {
				console.addMessage("Server shut down correctly");
				break;
			}
			synchronized (this) {
				exit = serverSocket.isClosed();
			}
		}
	}


	public synchronized void shutdown() {
		console.addMessage("Shutting down server");
		for (String name : playerNames) {
			activeConnections.get(name).terminate();
		}
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	// TODO look at implementation
	public synchronized void broadcast(String msg) {
		for (String name : playerNames) {
			activeConnections.get(name).send(msg);
			console.addMessage(msg);		// maybe remove??
		}
	}

	
	public synchronized boolean join(String name, ClientHandler client) {
		if (Name.nameValid(name) && !playerNames.contains(name)) {
			activeConnections.put(name, client);
			playerNames.add(name);
			return true;
		}
		return false;
	}

	
	public synchronized boolean leave(ClientHandler client) {
		String name = client.getName();
		if (playerNames.contains(name)) {
			activeConnections.remove(name);
			playerNames.remove(name);
			return true;
		}
		return false;
	}

}
