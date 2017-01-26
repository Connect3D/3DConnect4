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
 * will they actually put in the active state. Only from this moment on clients 
 * can start games with each other and chat.
 */

public class Server implements Runnable {
	
	public static final String NAME = "server";	// TODO rename to SERVER_NAME
	
	private final HashMap<ClientHandler, ClientState> clients = new HashMap<ClientHandler, ClientState>();
	private final HashMap<ClientHandler, String> names = new HashMap<ClientHandler, String>();
	private final HashSet<ClientHandler> chat = new HashSet<ClientHandler>();
	
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
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				synchronized (this) {
					try { new Thread(new ClientHandler(this, socket)).start(); } 
					catch (IOException e) { }
				}
			}
			catch (IOException e) {		// shutdown() function closes socket which makes this exception occur
				console.addMessage("Server shut down succesful");
				break;
			}
		}
	}


	public synchronized void shutdown() {
		console.addMessage("Shutting down server");
		chat.clear();			// makes sure noone gets bothered by eachothers disconnect messages
		for (ClientHandler client : clients.keySet()) {
			client.terminate();
		}
		try { serverSocket.close(); }
		catch(IOException e) { }
	}
	
	
	public synchronized void broadcast(String msg) {
		for (ClientHandler client : chat) {
			client.send(msg);
		}
	}
	
	
	public synchronized String getName(ClientHandler client) {
		return names.get(client);
	}
	
	
	public synchronized ClientState getState(ClientHandler client) {
		return clients.get(client);
	}
	
	
	//////////////////////////////////////////////////////////
	//                                                      //
	//    commands for connected clients to change state    //
	//                                                      //
	//////////////////////////////////////////////////////////
	
	public synchronized boolean joinChat(ClientHandler client) {
		if (clients.get(client) == ClientState.UNREADY) {
			chat.add(client);
			return true;
		}
		return false;
	}
	
	
	// TODO start new game if possible
	public synchronized boolean ready(ClientHandler client) {
		if (clients.get(client) == ClientState.UNREADY) {
			clients.put(client, ClientState.READY);
			return true;
		}
		return false;
	}
	
	
	public synchronized boolean unready(ClientHandler client) {
		if (clients.get(client) == ClientState.READY) {
			clients.put(client, ClientState.UNREADY);
			return true;
		}
		return false;
	}
	
	
	// TODO implement rejoin
	public synchronized boolean join(ClientHandler client, String name) {
		if (clients.get(client) == ClientState.PENDING && Name.valid(name) && !names.containsValue(name)) {
			clients.put(client, ClientState.UNREADY);
			names.put(client, name);
			broadcast("SAY " + NAME + joinMessage(name));
			console.addMessage(joinMessage(name));
			return true;
		}
		return false;
	}

	
	// TODO make leaving automatic when client disconnects
	// TODO make sure if in game other person is put in (un)ready, unless server closing
	public synchronized void leave(ClientHandler client) {
		if (clients.get(client) != ClientState.PENDING) {
			broadcast("SAY " + NAME + leaveMessage(names.get(client)));
			console.addMessage(leaveMessage(names.get(client)));
		}
		remove(client);
	}

	
	/////////////////////////////////////////
	//                                     //
	//    private utility functionality    //
	//                                     //
	/////////////////////////////////////////
	
	private synchronized void remove(ClientHandler client) {
		clients.remove(client);
		names.remove(client);
		chat.remove(client);
	}
	
	
	private String joinMessage(String name) {
		return " <" + name + " has joined the server>";
	}
	
	
	private String leaveMessage(String name) {
		return " <" + name + " has left the server>";
	}
	
}
