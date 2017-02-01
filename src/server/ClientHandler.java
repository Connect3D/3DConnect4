package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Observable;

import protocol.*;
import protocol.command.*;
import protocol.command.Error;			// needed to specify which error class we need
import util.*;
import util.container.Pair;
import util.exception.*;


/**
 * 
 * @author Aart Odding
 * 
 * Client handler thread communicating between client and server.
 * After the initial connection between the client and the server has been established,
 * the server passes the control over to a clienthandler thread
 * to communicate over the socket with the client. 
 * The clienthandler then receives messages from the client over his socket input stream, 
 * informs the server through method calls and sends information back.
 * All information over the socket is send in the form of commands. 
 * Either action commands, exit codes, acknowledgment
 * s or received commands from the client or error commands.
 * All commands from the client that are not valid throw exceptions and are catched in 
 * the run method, the client handler informs the client with error codes according to 
 * the exceptions that have been thrown
 * 
 */
public class ClientHandler extends Observable implements Runnable {
	
	public final Socket socket; 		// socket over which a bufferedreader/writer communicate.
									    // public so the server can close the clienthandler
								    	// invariant: socket != null
	private final Server server; 	    // server that passes his connectio over
									    // invariant: server != null
	private final BufferedReader in;    // buffer for the incoming socket connection
									    // invariant: in != null
	private final BufferedWriter out; 	// buffer for the outgoing socket connection
										// invariant: out != null
	private final CommandParser parser; // checks wether commands oblige to the given patterns
										// invariant: parser != null
	

	/** 
	 * Creates a clienthandler with a server, socket,
	 * and bufferer reader for in and outgoing stream and a command parser.
	 * @param serverArg
	 * @param sockArg
	 * @throws IOException
	 */
	/* @requires serverArg != null;
	 * 			 sockArg != null;
	 * @ensures  server = serverArg;
	 * 			 socket = sockArg;
	 * 			 in != null;
	 * 			 out != null;
	 * 			 parser != null;
	 * 		     parser.direction == client to server.; */
	public ClientHandler(Server serverArg, Socket sockArg) throws IOException {
		server = serverArg;
		socket = sockArg;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		parser = new CommandParser(Command.Direction.CLIENT_TO_SERVER);
	}
	
	
	//////////////////////////////////////
	//                                  //
	//  		Public Commands		    //
	//                                  //
	//////////////////////////////////////
	
	
	/**
	 * Executing incoming commands.
	 * Keeps running to receive commands from the client over the socket connection.
	 * Parses the incoming string to see wether they hold true to the protocol's 
	 * syntactic requirements and seperates each string in a command 
	 * and string array for the arguments.
	 * Afterwards a distinction is made between the possible commands
	 * (Action, Exit, Acknowledgement and Error) and handled accordingly.
	 * All thrown errors are handled in this method and the client is notified accordingly 
	 * with a matching error code as stated by the protocol.
	 */
	public void run() {	
		while (true) {
			try {
				Pair<Command, String[]> command = parser.parse(in.readLine());
				if (command.first instanceof Action) {
					runAction((Action) command.first, command.second);
				} else if (command.first instanceof Exit) {
					runExit((Exit) command.first, command.second);
				} else if (command.first instanceof Acknowledgement) {
					runAcknowledgement((Acknowledgement) command.first, command.second);
				}
			} catch (CommandInvalidException e) {
				sendCommand(Error.COMMAND_INVALID);			// TODO dependent on timing of command
			} catch (CommandUnsupportedException e) {
				sendCommand(Error.COMMAND_UNSUPPORTED);
			} catch (CommandForbiddenException e) {
				sendCommand(Error.FORBIDDEN);
			} catch (NameUnavailableException e) {
				sendCommand(Error.NAME_UNAVAILABLE);
			} catch (IllegalMoveException e) {
				sendCommand(Error.ILLEGAL_MOVE);
			} catch (IOException e) {
				server.tryForfeitGame(this);
				server.leave(this);
				break;
			}
		}
	}
	
	
	//////////////////////////////////////
	//  								//
	// 		   Private Commands		    //
	// 								    //
	//////////////////////////////////////
	

	/**
	 * Handles the received action commands from the client and throws exceptions, 
	 * catched by the run method, accordingly.
	 * 
	 * @param action					    the action commands send from the client
	 * @param args						    the arguments given with the command
	 * @throws NameUnavailableException     if the client uses an unavailable name
	 * @throws CommandForbiddenException   if a start function is received which is a server action
	 * @throws IllegalMoveException  	   if a move is send that is not possible
	 */
	
	/* @require action != null;
	 *		    args.length < 2; */
	private void runAction(Action action, String[] args) throws 
		NameUnavailableException, 
		CommandForbiddenException, 
		IllegalMoveException {
		
		switch (action) {
		
			case CONNECT:
				server.connect(this, args[0]);			// will throw error if name is invalid
				sendCommand(Acknowledgement.OK);		// will only run if no error was thrown
				break;
				
			case DISCONNECT:							// unacknowledged
				try {
					in.close();
					out.close();
					socket.close();
				} catch (IOException e) { }				// no catch is necessary, if server 
				server.tryForfeitGame(this);
				server.leave(this);
				break;
				
			case READY:
				server.ready(this);							// throws commandForbidden
				sendCommand(Acknowledgement.OK);
				server.tryMakeGame();
				break;
				
			case UNREADY:									// throws commandForbidden
				server.unready(this);
				sendCommand(Acknowledgement.OK);
				break;
				
			case START:
				throw new CommandForbiddenException();		// server only command
				
			case MOVE:
				server.move(this, args[0], args[1]);
				sendCommand(Acknowledgement.OK);
				server.forwardLastMove(this);
				server.tryFinishGame(this);
				if (server.getOpponent(this) != null) {
					server.timer.start(TimeOutTimer.Type.MOVE_TIMEOUT, server.getOpponent(this));
				}
				server.timer.cancel(TimeOutTimer.Type.MOVE_TIMEOUT, this);
				break;
				
			case SAY:
				server.broadcast(this, Util.join(args));					// unacknowledged
				break;
				
			case AVAILABLE:
				server.joinChat(this);
				sendCommand(Acknowledgement.OK);
				break;
				
			case LIST:										// unsupported commands can be ignored
				break;
			case LEADERBOARD:
				break;
			case CHALLENGE:
				break;
			case ACCEPT:
				break;
			case DECLINE:
				break;
		}
		
	}
	
	
	/**
	 * Handles an acknowledgement (an answer to a send command) from the client. 
	 * OK's are accepted, other acknowledgements should not be send from the client.
	 * 
	 * @param acknowledgement 			    the received acknowledgment 
	 * @param args						    Possible arguments stating further information.
	 * @throws CommandForbiddenException	if the command is not an acknowledgement.
	 */
	/* @requires acknowledgment != null;
	 *  		 args.length < 2; */
	private void runAcknowledgement(Acknowledgement acknowledgement, String[] args) throws 
		CommandForbiddenException {
		switch (acknowledgement) {
			case OK:
				break;
			case SAY:
				// clients can only send SAY as a command, not as an ack
				throw new CommandForbiddenException();
			case LIST:
				// clients can only send LIST as a command, not as an ack
				throw new CommandForbiddenException();
			case LEADERBOARD:
				// clients can only send LEADERBOARD as a command, not as an ack
				throw new CommandForbiddenException();
		}
	}
	
	
	/**
	 * Handles exit commands from the client on which the server is called to forfeit this game. 
	 * @param exit	exit command
	 * @param args	stating further information
	 * @throws CommandForbiddenException thrown if the client must not send this as an exit command.
	 */
	/* @requires exit != null;
	 *  		 args.length < 2; */
	private void runExit(Exit exit, String[] args) throws 
		CommandForbiddenException {
		if (exit == Exit.FORFEITURE) {
			server.tryForfeitGame(this);		// todo throw forbidden if not in game
		} else {
			throw new CommandForbiddenException();
		}
	}
	
	
	/**
	 * Sends a command without arguments over the outgoing socket connection.
	 * @param command
	 */
	/* @requires command < 2; */
	public synchronized void sendCommand(Command command) {
		try {
			out.write(command.toString() + "\n");
			out.flush();
		} catch (IOException e) { }
	}
	
	
	/**
	 * calls the send command with the string array parameter as one string argument.
	 * @param command
	 * @param arguments
	 */
	/* @requires command != null;
	 *  		 arguments.length > 1;*/
	public synchronized void sendCommand(Command command, String[] arguments) {
		sendCommand(command, Util.join(arguments));
	}
	
	
	/**
	 * Send a command to the outgoing socket if it is not empty. 
	 * Flushes the stream and catches any Input/Output exception.
	 * @param command
	 * @param arguments
	 */
	/* @requires command != null;
	 *  		 arguments != null; */
	public synchronized void sendCommand(Command command, String arguments) {
		String args = Util.join(Util.split(arguments));
		try {
			out.write(command.toString());
			if (args != "") {
				out.write(" " + arguments);
			}
			out.write("\n");
			out.flush();
		} catch (IOException e) { }
	}
	
}