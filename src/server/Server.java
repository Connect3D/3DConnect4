package server;

import util.MessageUI;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;


public class Server extends Thread {
	private static final String USAGE = "usage: " + Server.class.getName() + " <port>";

	private int port;
	private MessageUI mui;
	private Collection<ClientHandler> threads;


	public Server(int portArg, MessageUI muiArg) {
		mui = muiArg;
		port = portArg;
		threads = new ArrayList<ClientHandler>();
	}

	
	public void run() {
		ServerSocket ssocket = null;
		try {
			ssocket = new ServerSocket(port);
			while (true) {
				Socket socket = ssocket.accept();
				ClientHandler clientHandler = new ClientHandler(this, socket);
				addHandler(clientHandler);
				mui.addMessage("[client no." + threads.size() + " connected.]");
				clientHandler.announce();
				clientHandler.start();
			}
		} catch (IOException e) {
			mui.addMessage("ERROR: serversocket could not be created on port " + port);
			shutdown(ssocket);
			System.exit(0);
		}
	}


	public void shutdown(ServerSocket sock) {
		mui.addMessage("Closing socket connection...");
		try {
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void broadcast(String msg) {
		for (ClientHandler ch : threads) {
			ch.sendMessage(msg);
			mui.addMessage(msg);
		}
	}

	
	public void addHandler(ClientHandler handler) {
		threads.add(handler);
	}

	
	public void removeHandler(ClientHandler handler) {
		threads.remove(handler);
	}

}
