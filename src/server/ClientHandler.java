package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import protocol.*;
import protocol.command.Acknowledgement;
import protocol.command.Action;
import protocol.command.Command;
import protocol.command.Error;
import protocol.command.Exit;
import util.*;
import util.exception.*;


// TODO implement disconnect will leave one player and ready the other
// TODO remove all printStacktrace, console is owned by MessageUI and server

public class ClientHandler extends Observable implements Runnable {

	public static final long SHUTDOWN_DELAY = 1000l;				// 1 second
	public static final long MAX_ACKNOWLEDGEMENT_DELAY = 5000l;     // 5 seconds
	public static final long MAX_THINKING_TIME = 600000l;           // 10 minutes
	
	private final Timer timer = new Timer();
	private final CommandParser parser = new CommandParser(Command.Direction.CLIENT_TO_SERVER);
	
	private final Server server;
	private final Socket socket;
	private final BufferedReader in;
	private final BufferedWriter out;
	
	//private Pair<Command, String[]> lastCommandSend = null;
	//private boolean acknowledgementPending = false;
	//private boolean inGame = false;
	
	private AtomicBoolean isShuttingDown = new AtomicBoolean(false);
	
	
	
	public ClientHandler(Server serverArg, Socket sockArg) throws IOException {
		server = serverArg;
		socket = sockArg;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	}
	
	
	//////////////////////////////////////
	//                                  //
	//    Executing incoming commands   //
	//                                  //
	//////////////////////////////////////
	
	public void run() {	// TODO synchronize where necessary
		
		while (!isShuttingDown.get()) {
			try {
				Pair<Command, String[]> command = parser.parse(in.readLine());			// Command.parse(in.readLine(), true);
				if (command.first instanceof Action) {
					runAction((Action) command.first, command.second);
				}
				else if (command.first instanceof Exit) {
					runExit((Exit) command.first, command.second);
				}
				else if (command.first instanceof Acknowledgement) {
					runAcknowledgement((Acknowledgement) command.first, command.second);
				}
				else if (command.first instanceof Error) {
					runError((Error) command.first, command.second);
				}
			}
			catch (CommandInvalidException e) {
				sendCommand(Error.COMMAND_INVALID);			// maybe depending on whether waiting for ack or not
			}
			catch (CommandUnsupportedException e) {
				sendCommand(Error.COMMAND_UNSUPPORTED);
			}
			catch (CommandForbiddenException e) {
				sendCommand(Error.FORBIDDEN);
			}
			catch (IOException e) {
				shutDown();
			} 
		}
	}
	
	
	private void runAction(Action action, String[] args) {
		server.console.addMessage("action");
		sendCommand(Action.SAY, "action");
	}
	
	
	private void runAcknowledgement(Acknowledgement acknowledgement, String[] args) {
		server.console.addMessage("action");
		sendCommand(Action.SAY, "ack");
	}
	
	
	private void runError(Error error, String[] args) {
		server.console.addMessage("action");
		sendCommand(Action.SAY, "err");
	}
	
	
	private void runExit(Exit exit, String[] args) {
		server.console.addMessage("action");
		sendCommand(Action.SAY, "exit");
	}
	
	
	///////////////////////////////////////
	//                                   //
	//    Sending commands over socket   //
	//                                   //
	///////////////////////////////////////
	
	public synchronized void sendCommand(Command command, String arguments) {
		String args = Util.join(Util.split(arguments));
		try {
			out.write(command.toString());
			if (args != "") {
				out.write(" " + arguments);
			}
			out.write("\n");
			out.flush();
		} 
		catch (IOException e) { }
	}
	
	
	public synchronized void sendCommand(Command command, String[] arguments) {
		sendCommand(command, Util.join(arguments));
	}
	
	
	public synchronized void sendCommand(Command command) {
		try {
			out.write(command.toString() + "\n");
			out.flush();
		}
		catch (IOException e) { }
	}
	
	
	//////////////////////////////////////////
	//                                      //
	//    functionality for shutting down   //
	//                                      //
	//////////////////////////////////////////
	
	public synchronized void shutDown() {
		sendCommand(Action.DISCONNECT);
		server.leave(this);
		timer.schedule(new Shutdown(this), SHUTDOWN_DELAY);
		isShuttingDown.set(true);;
	}
	
	
	// private shutdown class for delayed shutdowns
	private class Shutdown extends TimerTask {
		
		ClientHandler client;
		
		public Shutdown(ClientHandler c) {
			client = c;
		}
		
		public void run() {
			try {
				synchronized (client) {
					in.close();
					out.close();
					socket.close();
				}
			} 
			catch (IOException e) { }
		}
		
	}
	
}




