package server;

import util.MessageUI;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedList;


public class Server implements Runnable {
	
	//private static final String USAGE = "usage: " + Server.class.getName() + " <port>";

	public final int port;
	
	private MessageUI console;
	private Collection<ClientHandler> clients;
	private ServerSocket serverSocket = null;

	
	public Server(int portArg, MessageUI muiArg) {
		port = portArg;
		console = muiArg;
		clients = new LinkedList<ClientHandler>();
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			console.addMessage("ERROR: serversocket could not be created on port " + port);
			//shutdown(serverSocket); 	// not necessary socket doesnt exist yet
			//System.exit(0);			// we can just attempt again
		}
	}
	
	
	public void run() {
		
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				ClientHandler clientHandler = new ClientHandler(this, socket);
				addHandler(clientHandler);
				console.addMessage("[client no." + clients.size() + " connected.]");
				clientHandler.announce();
				clientHandler.start();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	public void shutdown() {
		console.addMessage("Closing socket connection...");
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void broadcast(String msg) {
		for (ClientHandler ch : clients) {
			ch.sendMessage(msg);
			console.addMessage(msg);
		}
	}

	
	public void addHandler(ClientHandler handler) {
		clients.add(handler);
	}

	
	public void removeHandler(ClientHandler handler) {
		clients.remove(handler);
	}

}
