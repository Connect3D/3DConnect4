package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import com.google.common.collect.HashBiMap;

import game.Game;
import game.Move;
import protocol.Name;
import protocol.command.*;
import util.*;
import util.exception.*;



/*
 * Whenever a new client connects to the server their connection will be accepted,
 * and a new ClientHandler thread is made. However only when the client actually 
 * asks to join the server by sending the "Connect"  command and their name, 
 * will they actually put in the active state. Only from this moment on clients 
 * can start games with each other and chat.
 */

public class Server implements Runnable {
	
	public static final String SERVER_NAME = "server";
	
	private final HashMap<ClientHandler, ClientState> clients = new HashMap<ClientHandler, ClientState>();
	private final DoubleKeyHashMap<ClientHandler, ServerSideGame> games = new DoubleKeyHashMap<ClientHandler, ServerSideGame>();
	
	private final HashSet<ClientHandler> chat = new HashSet<ClientHandler>();
	private final HashBiMap<ClientHandler, String> names = HashBiMap.create();
	
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
					ClientHandler client = new ClientHandler(this, socket);
					clients.put(client, ClientState.PENDING);
					new Thread(client).start();
				}
			}
			catch (IOException e) {		// shutdown() function closes socket which makes this exception occur
				console.addMessage("Server shut down succesfully");
				break;
			}
		}
	}
	
	// TODO quit all ongoing games
	public synchronized void shutDown() throws IOException {
		console.addMessage("Shutting down server");
		for (ClientHandler client : clients.keySet()) {
			client.sendCommand(Action.DISCONNECT);
			console.addMessage(leaveMessage(names.get(client)));
		}
		serverSocket.close();
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
	
	
	///////////////////////////////////////////////////////////////
	//                                                           //
	//    Functionality for clientHandlers to perform actions    //
	//                                                           //
	///////////////////////////////////////////////////////////////
	
	public synchronized void joinChat(ClientHandler client) throws CommandForbiddenException {
		if (!chat.contains(client) && clients.get(client) == ClientState.UNREADY) {
			chat.add(client);
		}
		else {
			throw new CommandForbiddenException();
		}
	}
	
	
	public synchronized void ready(ClientHandler client) throws CommandForbiddenException {
		if (clients.get(client) == ClientState.UNREADY) {
			clients.put(client, ClientState.READY);
		}
		else {
			throw new CommandForbiddenException();
		}
	}
	
	
	public synchronized void unready(ClientHandler client) throws CommandForbiddenException {
		if (clients.get(client) == ClientState.READY) {
			clients.put(client, ClientState.UNREADY);
		}
		else {
			throw new CommandForbiddenException();
		}
	}
	
	
	public synchronized void connect(ClientHandler client, String name) throws NameUnavailableException, CommandForbiddenException {
		if (clients.get(client) == ClientState.PENDING) {
			if (Name.valid(name) && !names.containsValue(name) && !name.equals(SERVER_NAME)) {		// isvalid returns false on null
				clients.put(client, ClientState.UNREADY);
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
		if (clients.get(client) != ClientState.PENDING) {
			console.addMessage(leaveMessage(names.get(client)));
			broadcast(SERVER_NAME, leaveMessage(names.get(client)));
		}
		clients.remove(client);
		names.remove(client);
		chat.remove(client);
	}
	
	
	public synchronized void move(ClientHandler client, String x, String y) throws CommandForbiddenException, IllegalMoveException {
		if (games.hasKey(client)) {
			ServerSideGame game = games.getValue(client);
			game.doMove(client, Integer.parseInt(x), Integer.parseInt(y));			// TODO moves doorsturen
		}
		else {
			throw new CommandForbiddenException();
		}
	}
	
	
	/////////////////////////////////
	//                             //
	//    Utility functionality    //
	//                             //
	/////////////////////////////////
	
	private String joinMessage(String name) {
		return "<" + name + " has joined the server>";
	}
	
	
	private String leaveMessage(String name) {
		return "<" + name + " has left the server>";
	}
	
	
	public synchronized void tryMakeGame() {
		LinkedList<ClientHandler> ready = getClientsInState(ClientState.READY);
		while (ready.size() > 1) {
			ClientHandler p1 = (ClientHandler) ready.pop();
			ClientHandler p2 = (ClientHandler) ready.pop();
			clients.put(p1, ClientState.INGAME);
			clients.put(p2, ClientState.INGAME);
			games.put(p1, p2, new ServerSideGame(p1, p2));
			p1.sendCommand(Action.START, names.get(p1) + " " + names.get(p2));
			p2.sendCommand(Action.START, names.get(p1) + " " + names.get(p2));
		}
	}
	
	
	public synchronized void tryFinishGame(ClientHandler client) {
		ServerSideGame game = games.getValue(client);
		ClientHandler opponent = games.getOtherKey(client);
		if (game.getEnding() != Game.Ending.NOT_ENDED) {			// game has ended
			clients.put(client, ClientState.UNREADY);
			clients.put(opponent, ClientState.UNREADY);
			games.remove(game);
			client.sendCommand(game.getEndingFor(client));
			opponent.sendCommand(game.getEndingFor(opponent));
		}
	}
	
	
	public synchronized void forwardLastMove(ClientHandler client) {
		ServerSideGame game = games.getValue(client);
		if (!game.isSync()) {
			Move move = game.getLastMove();
			ClientHandler other = game.getPlayer(move.mark.opposite());
			other.sendCommand(Action.MOVE, move.column.x + " " + move.column.y);
			game.setSync();
		}
	}
	
	
	private synchronized LinkedList<ClientHandler> getClientsInState(ClientState state) {
		LinkedList<ClientHandler> result = new LinkedList<ClientHandler>();
		for (ClientHandler c : clients.keySet()) {
			if (clients.get(c) == state) {
				result.add(c);
			}
		}
		return result;
	}

}
