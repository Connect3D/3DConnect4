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
import protocol.ClientState;
import util.*;
import util.container.DoubleKeyHashMap;
import util.exception.*;



/**
 * 
 * @author Aart Odding
 * 
 * Server is the main class implementing the server side functionality of the the game. 
 * It does not have its own main function, which means some other user interface is necessary
 * to run the server. For each client that connects to the server, a clientHandler is made, 
 * which will handle that client and that client only till either the client disconnects, 
 * or the server shuts down.
 * 
 * @see util.container.DoubleKeyHashMap
 * All Ongoing games are stored in a self-defined container 'DoubleKeyHashMap', which connects 
 * two keys (clients) to one value (game). This provides some very useful functionality, 
 * like getting the other key(opponent) and using any of the two keys to quit the game for 
 * both players.
 * 
 * @see server.TimeOutTimer
 * Both players are limited to a 10 minute time span to send move, if they fail to do this they
 * automatically lose the game, and their opponent wins. This is implemented using a timer, to 
 * which timer tasks can be attached to start the 10 minute duration, and cancelled when a move 
 * is given.
 */

public class Server implements Runnable {
	
	public static final String SERVER_NAME = "server";
	public final TimeOutTimer timer;
	
	private final HashMap<ClientHandler, ClientState> clients;
	private final DoubleKeyHashMap<ClientHandler, ServerSideGame> games;
	
	private final HashSet<ClientHandler> chat;
	private final HashBiMap<ClientHandler, String> names;
	
	private final ServerSocket serverSocket;
	private final MessageUI console;
	
	
	/**
	 * Constructor of the server class. a port number is provided on which the server will be 
	 * attempted to be opened. This can cause an IOException if the port is already being used, 
	 * in which case we do not want to finish creating the server object but instead want to 
	 * throw the exception. An output for messages is also given, so that the server knows 
	 * where to send its output to.
	 * 
	 * @param port				port on which the server will be attempted to be created
	 * @param messageUi			output for messages for the server
	 * @throws IOException		thrown when port is not available
	 */
	/*
	 * @requires messageUi != null;
	 */
	public Server(int port, MessageUI messageUi) throws IOException {
		console = messageUi;
		clients = new HashMap<ClientHandler, ClientState>();
		games = new DoubleKeyHashMap<ClientHandler, ServerSideGame>();
		chat = new HashSet<ClientHandler>();
		names = HashBiMap.create();
		timer = new TimeOutTimer(this);
		serverSocket = new ServerSocket(port);			
	}
	
	
	/////////////////////////////////
	//                             //
	//    Control Functionality    //
	//                             //
	/////////////////////////////////
	
	/**
	 * This function can be run in a separate thread, and is responsible for the accepting of new 
	 * clients on the server. When a new client joins, a new clientHandler object is created to 
	 * handle the client. The initial state of the client is pending until the client issues the 
	 * CONNECT command with a valid name
	 * 
	 * When the server has been shut down, the server socket closes, which causes an IOException
	 * to occur. This will break the main loop, and finish the thread execution.
	 */
	public void run() {
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				synchronized (this) {
					ClientHandler client = new ClientHandler(this, socket);
					clients.put(client, ClientState.PENDING);
					new Thread(client).start();
				}
			} catch (IOException e) {		// shutdown() causes this exception
				console.addMessage("Server shut down succesfully");
				break;
			}
		}
	}
	
	
	/**
	 * Shuts down the server gracefully, should be called by the user interface handling the server.
	 * All clients are informed of the server shutting down, and their sockets are closed. Lastly 
	 * also causes the main loop of the server to break.
	 */
	public synchronized void shutDown() {
		console.addMessage("Shutting down server");
		try {
			for (ClientHandler client : clients.keySet()) {
				client.sendCommand(Action.DISCONNECT);
				client.socket.close();
			}
			serverSocket.close();
		} catch (IOException e) { }
	}
	
	
	///////////////////////////////////////////////////////////////
	//                                                           //
	//    Functionality for clientHandlers to perform actions    //
	//                                                           //
	///////////////////////////////////////////////////////////////
	
	/**
	 * Adds a clientHandler to the chatbox, only allowed when client is in unready state
	 * throws exception if client is already in the chat, or not in unready state.
	 * 
	 * @param client							client to be added to the chat
	 * @throws CommandForbiddenException		thrown if already in chat, or not in right state
	 */
	/*
	 * @requires client != null;
	 */
	public synchronized void joinChat(ClientHandler client) throws CommandForbiddenException {
		if (!chat.contains(client) && clients.get(client) == ClientState.UNREADY) {
			chat.add(client);
		} else {
			throw new CommandForbiddenException();
		}
	}
	
	
	/**
	 * Used by ClientHandlers to signal they are ready to play a game, after this function
	 * returns tryMakeGame() should always be called. this has to happen from outside of this
	 * function, because clientHandlers send an OK when this function returns. If clients are 
	 * already in the ready state, an exception is thrown
	 * 
	 * @param client							client to be made ready
	 * @throws CommandForbiddenException		if client is not in allowed state
	 */
	/*
	 * @requires client != null;
	 */
	public synchronized void ready(ClientHandler client) throws CommandForbiddenException {
		if (clients.get(client) == ClientState.UNREADY) {
			clients.put(client, ClientState.READY);
		} else {
			throw new CommandForbiddenException();
		}
	}
	
	
	/**
	 * Used by clients to move from the ready state back in the unready state, which causes the
	 * client not to be added to new games. throws an exception if client was already in the 
	 * ready state.
	 * 
	 * @param client							client to be unreadied
	 * @throws CommandForbiddenException		if client is not in allowed state
	 */
	/*
	 * @requires client != null;
	 */
	public synchronized void unready(ClientHandler client) throws CommandForbiddenException {
		if (clients.get(client) == ClientState.READY) {
			clients.put(client, ClientState.UNREADY);
		} else {
			throw new CommandForbiddenException();
		}
	}
	
	
	/**
	 * Used by client to formally connect to the server. A name is given, and checked for validity
	 * If the name is valid and the client was not yet registered with a name the name is added.
	 * If the client already had a name, or if it was already taken, a NameUnavailableException is 
	 * thrown. If the client wasn't in the right state to use the command a 
	 * CommandForbiddenException is thrown.
	 * 
	 * @param client							client to be formally connected
	 * @param name								name the client wants to use
	 * @throws NameUnavailableException			in case the name is unavailable
	 * @throws CommandForbiddenException		in case the client is not allowed to connect
	 */
	/*
	 * @requires client != null;
	 * @requires name != null;
	 */
	public synchronized void connect(ClientHandler client, String name) throws 
		NameUnavailableException, 
		CommandForbiddenException {
		if (clients.get(client) == ClientState.PENDING) {
			if (Name.valid(name) && !names.containsValue(name) && !name.equals(SERVER_NAME)) {
				clients.put(client, ClientState.UNREADY);
				names.put(client, name);
				broadcast(SERVER_NAME, joinMessage(name));
				console.addMessage(joinMessage(name));
			} else {
				throw new NameUnavailableException();
			}
		} else {
			throw new CommandForbiddenException();
		}
	}

	
	/**
	 * Used to remove a clients data from the server. Caller should make sure that if he still
	 * is in a game, the game should be forfeited first. Because disconnecting is always allowed, 
	 * no exceptions are thrown.
	 * 
	 * @param client							client to be removed from the server
	 */
	/*
	 * @requires client != null;
	 */
	public synchronized void leave(ClientHandler client) {
		if (clients.get(client) != ClientState.PENDING) {
			console.addMessage(leaveMessage(names.get(client)));
			broadcast(SERVER_NAME, leaveMessage(names.get(client)));
		}
		clients.remove(client);
		names.remove(client);
		chat.remove(client);
	}
	
	
	/**
	 * Move is used for clients to perform a move in the game they are playing. When it is not a 
	 * client's turn to perform a move a CommandForbiddenException is thrown. When the move is not
	 * valid because it is either out of bounds or already taken an IllegalMoveException is thrown.
	 * After this function is called the functions forwardLastMove() and tryFinishGame() should 
	 * also be called. This however can't be done from inside this function, because the function 
	 * needs to return for the clientHandler to send an OK back to the client. Which has to happen 
	 * before these other functions are called.
	 * 
	 * @param client							client performing the move
	 * @param x									x coordinate of the move
	 * @param y									y coordinate of the move
	 * @throws CommandForbiddenException		in case it is not the client's turn
	 * @throws IllegalMoveException				in case the move is not valid
	 */
	/*
	 * @requires client != null;
	 * @requires x != null;
	 * @requires y != null;
	 */
	public synchronized void move(ClientHandler client, String x, String y) throws 
		CommandForbiddenException, 
		IllegalMoveException {
		if (games.hasKey(client)) {
			ServerSideGame game = games.getValue(client);
			game.doMove(client, Integer.parseInt(x), Integer.parseInt(y));
		} else {
			throw new CommandForbiddenException();
		}
	}
	
	
	//////////////////////////////////////////////////////////
	//                                                      //
	//    Functionality to update the state of the server   //
	//                                                      //
	//////////////////////////////////////////////////////////
	
	/**
	 * This function tries to start a new game between to clients. The clients available are the 
	 * clients in the ready state. Because of this this function should always be called after the
	 * ready() function is called, to check if a new game is possible
	 */
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
	
	
	/**
	 * This function retrieves the current game of the calling client (if he is in any), and looks
	 * if it has ended. If the game has ended both clients are notified appropriately, and moved 
	 * back into the unready state. This function should always be called after a client performs
	 * a move.
	 * 
	 * @param client		The calling client (used to find which game to end)
	 */
	/*
	 * @requires client != null;
	 */
	public synchronized void tryFinishGame(ClientHandler client) {
		ServerSideGame game = games.getValue(client);
		ClientHandler opponent = games.getOtherKey(client);
		if (game.getEnding() != Game.Ending.NOT_ENDED) {
			clients.put(client, ClientState.UNREADY);
			clients.put(opponent, ClientState.UNREADY);
			games.remove(game);
			client.sendCommand(game.getEndingFor(client));
			opponent.sendCommand(game.getEndingFor(opponent));
		}
	}
	
	
	/**
	 * This function looks if the client given as an argument is in any game, if he is the game will
	 * be forfeited, where the client given as the parameter loses, and the opponent wins by 
	 * forfeiture.
	 * 
	 * @param client		Client forfeiting the game
	 */
	/*
	 * @requires client != null;
	 */
	public synchronized void tryForfeitGame(ClientHandler client) {
		ServerSideGame game = games.getValue(client);
		ClientHandler opponent = games.getOtherKey(client);
		if (game != null && game.getEnding() == Game.Ending.NOT_ENDED) {
			clients.put(client, ClientState.UNREADY);
			clients.put(opponent, ClientState.UNREADY);
			games.remove(game);
			client.sendCommand(Exit.LOST);
			opponent.sendCommand(Exit.FORFEITURE);
		}
	}
	
	
	/**
	 * This function is used to forward the move one client did to the other client. The move is
	 * retrieved from the game, which always saves the last move, and then send to the other player.
	 * 
	 * @param client		the calling client (used for finding the correct game)
	 */
	/*
	 * @requires client != null;
	 */
	public synchronized void forwardLastMove(ClientHandler client) {
		ServerSideGame game = games.getValue(client);
		if (!game.isSync()) {
			Move move = game.getLastMove();
			ClientHandler other = game.getPlayer(move.mark.opposite());
			other.sendCommand(Action.MOVE, move.column.x + " " + move.column.y);
			game.setSync();
		}
	}
	
		
	///////////////////
	//               //
	//    Utility    //
	//               //
	///////////////////
	
	/**
	 * Sends a message to all clients that have chat functionality enabled.
	 * 
	 * @param name			name of the client asking for a broadcast
	 * @param message		message to be broadcasted
	 */
	/*
	 * @requires name != null;
	 * @requires message != null;
	 */
	public synchronized void broadcast(String name, String message) {
		for (ClientHandler c : chat) {
			c.sendCommand(Acknowledgement.SAY, name + " " + message);
		}
	}
	
	
	/**
	 * Sends a message to all clients that have chat functionality enabled, name is deduced from 
	 * the clientHandler given.
	 * 
	 * @param client		client asking to broadcast
	 * @param message		message to be broadcasted
	 */
	/*
	 * @requires client != null;
	 * @requires message != null;
	 */
	public synchronized void broadcast(ClientHandler client, String message) {
		for (ClientHandler c : chat) {
			c.sendCommand(Acknowledgement.SAY, names.get(client) + " " + message);
		}
	}
	
	
	/**
	 * Used for finding the opponent of a client in a game. Implemented using the DoubleKeyHashMap.
	 * @see DoubleKeyHashMap
	 * 
	 * @param client		client asking for opponent
	 * @return				opponent of the client
	 */
	/*
	 * @requires client != null;
	 * @ensures \result == games.getOtherKey(client);
	 */
	public synchronized ClientHandler getOpponent(ClientHandler client) {
		return games.getOtherKey(client);
	}

	
	/**
	 * Function used for creating the message that is send to all people in chat and the console, 
	 * when a new client formally (By the CONNECT command) joins the server.
	 * 
	 * @param name			name of person joining
	 * @return				message of person joining
	 */
	/*
	 * @requires name != null;
	 * @ensures \result != null;
	 */
	private String joinMessage(String name) {
		return "<" + name + " has joined the server>";
	}
	
	
	/**
	 * Function used for creating the message that is send to all people in chat and the console, 
	 * when a client leaves the server.
	 * 
	 * @param name			name of person leaving
	 * @return				message of person leaving
	 */
	/*
	 * @requires name != null;
	 * @ensures \result != null;
	 */
	private String leaveMessage(String name) {
		return "<" + name + " has left the server>";
	}
	
	
	/**
	 * returns all clients in said certain state.
	 * 
	 * @param state			state to be checked for clients
	 * @return				linkedList of all clients state
	 */
	/*
	 * @requires state != null;
	 * @ensures (\forall ClientHandler c; clients.get(c) == state);
	 */
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
