package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;

import game.Game;
import protocol.Name;
import protocol.command.*;
import util.MessageUI;
import util.exception.protocol.*;



/*
 * Whenever a new client connects to the server their connection will be accepted,
 * and a new ClientHandler thread is made. However only when the client actually 
 * asks to join the server by sending the "Connect"  command and their name, 
 * will they actually put in the active state. Only from this moment on clients 
 * can start games with each other and chat.
 */

public class Server implements Runnable {
	
	public static final String SERVER_NAME = "server";
	
	private final HashMap<ClientHandler, ClientState> clientToState = new HashMap<ClientHandler, ClientState>();
	private final HashMultimap<ClientState, ClientHandler> stateToClient = HashMultimap.create();							// TODO use
	
	private final HashMap<ClientHandler, ServerSideGame> clientToGame = new HashMap<ClientHandler, ServerSideGame>();
	private final HashMultimap<ServerSideGame, ClientHandler> gameToClient = HashMultimap.create();							// TODO use
	
	private final HashSet<ClientHandler> chat = new HashSet<ClientHandler>();
	private final HashBiMap<ClientHandler, String> names = HashBiMap.create();												// TODO look if stll correct
	
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
		while (true) {			// TODO check if this works cleanly
			try {
				Socket socket = serverSocket.accept();
				synchronized (this) {
					ClientHandler client = new ClientHandler(this, socket);
					clientToState.put(client, ClientState.PENDING);
					stateToClient.put(ClientState.PENDING, client);
					new Thread(client).start();
				}
			}
			catch (IOException e) {		// shutdown() function closes socket which makes this exception occur
				console.addMessage("Server shut down succesfully");
				break;
			}
		}
	}


	
	public synchronized void broadcast(String name, String message) {
		for (ClientHandler c : chat) {
			c.sendCommand(Acknowledgement.SAY, name + " " + message);
		}
	}
	
	
	public synchronized void broadcast(ClientHandler client, String message) {
		for (ClientHandler c : chat) {
			c.sendCommand(Acknowledgement.SAY, names.get(client) + " " + message);
		}
	}
	
	
	//////////////////////////////////////////////////////////
	//                                                      //
	//    commands for connected clients to change state    //
	//                                                      //
	//////////////////////////////////////////////////////////
	
	public synchronized void joinChat(ClientHandler client) throws CommandForbiddenException {
		if (!chat.contains(client) && clientToState.get(client) == ClientState.UNREADY) {
			chat.add(client);
		}
		throw new CommandForbiddenException();
	}
	
	
	public synchronized void ready(ClientHandler client) throws CommandForbiddenException {
		if (clientToState.get(client) == ClientState.UNREADY) {
			clientToState.put(client, ClientState.READY);
			stateToClient.remove(ClientState.UNREADY, client);
			stateToClient.put(ClientState.READY, client);
			// TODO start new game if possible
		}
		else {
			throw new CommandForbiddenException();
		}
	}
	
	
	public synchronized void unready(ClientHandler client) throws CommandForbiddenException {
		if (clientToState.get(client) == ClientState.READY) {
			clientToState.put(client, ClientState.UNREADY);
			stateToClient.remove(ClientState.READY, client);
			stateToClient.put(ClientState.UNREADY, client);
		}
		else {
			throw new CommandForbiddenException();
		}
	}
	
	
	public synchronized void connect(ClientHandler client, String name) throws NameUnavailableException, CommandForbiddenException {
		if (clientToState.get(client) == ClientState.PENDING) {
			if (Name.valid(name) && !names.containsValue(name) && !name.equals(SERVER_NAME)) {		// isvalid returns false on null
				clientToState.put(client, ClientState.UNREADY);
				stateToClient.remove(ClientState.PENDING, client);
				stateToClient.put(ClientState.UNREADY, client);
				names.put(client, name);
				broadcast(SERVER_NAME, joinMessage(name));
				console.addMessage(joinMessage(name));
			}
			else {
				throw new NameUnavailableException();
			}
		}
		else {
			throw new CommandForbiddenException();
		}
	}

	
	// TODO move other to unready if was in game, and server not closing
	public synchronized void leave(ClientHandler client) {					//disconnecting is always allowed, so no exceptions are thrown
		if (clientToState.get(client) != ClientState.PENDING) {
			console.addMessage(leaveMessage(names.get(client)));
			broadcast(SERVER_NAME, leaveMessage(names.get(client)));
		}
		stateToClient.remove(clientToState.remove(client), client);		// two removes in one line
		names.remove(client);
		chat.remove(client);
	}
	
	
	public synchronized void move(ClientHandler client, String x, String y) throws CommandForbiddenException, IllegalMoveException {
		if (clientToGame.containsKey(client)) {
			clientToGame.get(client).doMove(client, Integer.parseInt(x), Integer.parseInt(y));
			if (clientToGame.get(client).getEnding() != Game.Ending.NOT_ENDED) {
				finishGame(clientToGame.get(client));
			}
		}
		throw new CommandForbiddenException();
	}
	
	
	/////////////////////////////////////////
	//                                     //
	//    private utility functionality    //
	//                                     //
	/////////////////////////////////////////
	
	private String joinMessage(String name) {
		return "<" + name + " has joined the server>";
	}
	
	
	private String leaveMessage(String name) {
		return "<" + name + " has left the server>";
	}
	
	
	//////////////////////////////////////////
	//                                      //
	//    functionality for shutting down   //
	//                                      //
	//////////////////////////////////////////
	
	
	private synchronized void finishGame(ServerSideGame game) {
		// TODO remove game, put players in unready, 
	}
	
	
	private synchronized void makeGame() {
		// TODO place people in game if more than two are rready, change their state
		// while loop
	}
	
	
	// TODO quit all ongoing games
	public synchronized void shutDown() throws IOException {
		console.addMessage("Shutting down server");
		for (ClientHandler client : clientToState.keySet()) {
			client.sendCommand(Action.DISCONNECT);
			console.addMessage(leaveMessage(names.get(client)));
		}
		serverSocket.close();
	}
	
}
