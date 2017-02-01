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



public class ClientHandler extends Observable implements Runnable {
	
	public final Socket socket;				// public so the server can close the clienthandler
	
	private final Server server;
	private final BufferedReader in;
	private final BufferedWriter out;
	private final CommandParser parser;
	
	//private Pair<Command, String[]> lastCommandSend = null;
	//private boolean acknowledgementPending = false;
	//private boolean inGame = false;
	
	
	public ClientHandler(Server serverArg, Socket sockArg) throws IOException {
		server = serverArg;
		socket = sockArg;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		parser = new CommandParser(Command.Direction.CLIENT_TO_SERVER);
	}
	
	
	//////////////////////////////////////
	//                                  //
	//    Executing incoming commands   //
	//                                  //
	//////////////////////////////////////
	
	public void run() {	// TODO synchronize where necessary
		
		while (true) {
			try {
				Pair<Command, String[]> command = parser.parse(in.readLine());
				if (command.first instanceof Action) {
					runAction((Action) command.first, command.second);
				} else if (command.first instanceof Exit) {
					runExit((Exit) command.first, command.second);
				} else if (command.first instanceof Acknowledgement) {
					runAcknowledgement((Acknowledgement) command.first, command.second);
				} else if (command.first instanceof Error) {
					runError((Error) command.first, command.second);
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
				
			case LIST:									// unsupported commands can be ignored
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
	
	
	private void runError(Error error, String[] args) {
		// to stuff
		//sendCommand(Action.SAY, "err");
	}
	
	
	private void runExit(Exit exit, String[] args) throws 
		CommandForbiddenException {
		if (exit == Exit.FORFEITURE) {
			server.tryForfeitGame(this);		// todo throw forbidden if not in game
		} else {
			throw new CommandForbiddenException();
		}
	}
	
	
	///////////////////////////////////////
	//                                   //
	//    Sending commands over socket   //
	//                                   //
	///////////////////////////////////////
	
	public synchronized void sendCommand(Command command, String[] arguments) {
		sendCommand(command, Util.join(arguments));
	}
	
	
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
	
	
	public synchronized void sendCommand(Command command) {
		try {
			out.write(command.toString() + "\n");
			out.flush();
		} catch (IOException e) { }
	}
	
}