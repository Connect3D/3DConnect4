package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import game.Column;
import protocol.ErrorCode;
import util.ProvidesMoves;


// TODO implement disconnect will leave one player and ready the other
// TODO remove all printStacktrace, console is owned by MessageUI

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
	
	
	public void run() {
		
		while (socket.isConnected()) {	// TODO put in synchronized
			
		}
		server.leave(this);		// USE EXCEPTION TO RUN THIS
	}
	
	
	public synchronized void terminate() {
		send(ErrorCode.SERVER_SHUTTING_DOWN.toString());
		try { 
			socket.close();
			in.close();
			out.close();
		} 
		catch (IOException e) { }
	}

	
	public synchronized void send(String msg) {
		try {
			out.write(msg);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	// TODO look at not being synchronized
	public Column waitForMove() {
		try {
			while (move == null) {
				wait();
			}
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		Column choice = new Column(move);
		move = null;
		return choice;
	}
	
}
