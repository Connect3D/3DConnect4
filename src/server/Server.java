package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;

import protocol.ErrorCode;
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
	
	public static final String NAME = "server";
	
	private final HashMap<String, ClientHandler> activeConnections = new HashMap<String, ClientHandler>();
	private final HashSet<String> playerNames = new HashSet<String>();
	private final HashSet<String> supportChat = new HashSet<String>();
	private final HashSet<String> ready = new HashSet<String>();
	private final ServerSocket serverSocket;
	private final MessageUI console;
	
	
	public Server(int port, MessageUI messageUi) throws IOException{
		console = messageUi;
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
				synchronized (this) {
					new Thread(new ClientHandler(this, socket)).start();		// TODO what happens if client connects after shutdown??
				}
			}
			catch (IOException e) {		// shutdown() function closes socket which makes this exception occur
				console.addMessage("Server shut down succesful");
				break;
			}
			synchronized (this) {
				exit = serverSocket.isClosed();
			}
		}
	}


	public synchronized void shutdown() {
		console.addMessage("Shutting down server");
		broadcast(ErrorCode.SERVER_SHUTTING_DOWN.toString());
		for (String name : playerNames) {
			remove(name); 					// remove does not actually disconnect the client, just removes it from active list
			console.addMessage(name + " left");
		}
		try {
		    Thread.sleep(1000);            	// wait 2 seconds, so clienthandlers have time to send shut down code
		    serverSocket.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public synchronized void broadcast(String msg) {
		for (String name : supportChat) {
			activeConnections.get(name).send(msg);
		}
	}
	
	
	private synchronized void remove(String name) {
		if (playerNames.contains(name)) {
			unready(name);							// if player was unready nothing happens
			activeConnections.remove(name);
			playerNames.remove(name);		
			supportChat.remove(name);				// if player didn't support chat nothing happens
		}
	}
	
	
	//////////////////////////////////////////////////////////
	//                                                      //
	//    commands for connected clients to change state    //
	//                                                      //
	//////////////////////////////////////////////////////////
	
	public synchronized boolean joinChat(String name) {
		if (playerNames.contains(name)) {
			if (!supportChat.contains(name)) {
				supportChat.add(name);
			}
			return true;
		}
		return false;
	}
	
	
	public synchronized boolean ready(String name) {
		if (playerNames.contains(name)) {
			ready.add(name);
			// TODO start new game
			return true;
		}
		return false;
	}
	
	
	public synchronized boolean unready(String name) {
		if (ready.contains(name)) {
			ready.remove(name);
			return true;
		}
		return false;
	}
	
	
	public synchronized boolean join(String name, ClientHandler client) {
		if (Name.valid(name) && !playerNames.contains(name)) {
			activeConnections.put(name, client);
			playerNames.add(name);
			broadcast("SAY " + NAME + " [ " + name + " has joined the server ]");
			console.addMessage(name + " has joined the server");
			return true;
		}
		return false;
	}

	
	public synchronized boolean leave(String name) {
		if (playerNames.contains(name)) {
			remove(name);
			broadcast("SAY " + NAME + " [ " + name + " has left the server ]");
			console.addMessage(name + " has left the server");
			return true;
		}
		return false;
	}

}
