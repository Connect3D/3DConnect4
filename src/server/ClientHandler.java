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


public class ClientHandler implements Runnable, ProvidesMoves {

	private final Server server;
	private final Socket socket;
	private final BufferedReader in;
	private final BufferedWriter out;
	
	private String name = "";
	private Column move = null;
	
	
	public ClientHandler(Server serverArg, Socket sockArg) throws IOException {
		server = serverArg;
		socket = sockArg;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	}
	
	
	public String getName() {
		return name;
	}

	
	public void run() {
		try {
			while (true) {
				server.broadcast(name + ": " + in.readLine());
				// somwhere read input and provide moves
			}
		} catch (IOException e) {
			shutdown();
		}
	}

	
	public void send(String msg) {
		try {
			out.write(msg);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			shutdown();
		}
	}
	
	
	public void terminate() {
		send(ErrorCode.SERVER_SHUTTING_DOWN.toString());
		try {
			socket.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	// implement properly
	private void shutdown() {
		//server.removeHandler(this);
		server.broadcast("[" + name + " has left]");
	}


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
