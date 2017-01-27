package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import game.Column;
import protocol.CommandTemp;
import protocol.Error;
import util.ProvidesMoves;
import util.Util;


// TODO implement disconnect will leave one player and ready the other
// TODO remove all printStacktrace, console is owned by MessageUI and server

public class ClientHandler implements Runnable, ProvidesMoves {

	private final Server server;
	private final Socket socket;
	private final BufferedReader in;
	private final BufferedWriter out;
	
	private Column move = null;
	
	
	public ClientHandler(Server serverArg, Socket sockArg) throws IOException {
		server = serverArg;
		socket = sockArg;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	}
	
	
	public void run() {	// TODO synchronize where necessary
		
		try {
			while (true) {
				String[] command = Util.arrayTrim(in.readLine().split(" "));
				if (CommandTemp.syntacticallyCorrect(command)) {
					if (CommandTemp.commandSupported(command[0])) {
						runCommand(command);
					}
					else {
						send(Error.COMMAND_UNSUPPORTED.toString());
					}
				}
				else {
					send(Error.COMMAND_INVALID.toString());
				}
			}
		}
		catch (IOException e) {
			server.leave(this);
		}
	}
	
	
	public void runCommand(String[] command) { // TODO synchronize where necessary
		switch (command[0]) {
			case "OK":
				break;
			case "ERROR":
				break;
			case "CONNECT":
				break;
			case "DISCONNECT":
				break;
			case "READY":
				break;
			case "UNREADY":
				break;
			case "START":
				break;
			case "MOVE":
				break;
			case "EXIT":
				break;
			case "SAY":
				break;
			case "AVAILABLE":
				break;
		/*	case "LIST": break;			// so far unsupported
			case "LEADERBOARD": break;
			case "CHALLENGE": break;
			case "ACCEPT": break;
			case "DECLINE": break;	*/
		}
	}
	
	
	public synchronized void terminate() {	// TODO necessary to be sync?
		send(Error.SERVER_SHUTTING_DOWN.toString());
		try { 
			socket.close();
			in.close();
			out.close();
		} 
		catch (IOException e) { }
	}

	
	public synchronized void send(String msg) {
		try {
			out.write(msg + "\n");
			out.flush();
		} 
		catch (IOException e) { }
	}

	
	public synchronized Column waitForMove() {
		try {
			while (move == null) {
				wait();
			}
		} 
		catch (InterruptedException e) { }
		Column choice = new Column(move);
		move = null;
		return choice;
	}
	
}
