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


public class ClientHandler extends Thread {

	private Server server;
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;
	private String clientName;

	
	public ClientHandler(Server serverArg, Socket sockArg) throws IOException {
		server = serverArg;
		sock = sockArg;
		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
	}

	
	public void announce() throws IOException {
		clientName = in.readLine();
		server.broadcast("[" + clientName + " has entered]");
	}

	
	public void run() {
		try {
			while (true) {
				server.broadcast(clientName + ": " + in.readLine());
			}
		} catch (IOException e) {
			shutdown();
		}
	}

	
	public void sendMessage(String msg) {
		try {
			out.write(msg);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			shutdown();
		}
	}

	
	private void shutdown() {
		server.removeHandler(this);
		server.broadcast("[" + clientName + " has left]");
	}
	

	static int createPort(String port) {
		int portInt = 0;
		try {
			portInt = Integer.parseInt(port);
		} catch (NumberFormatException e) {
			System.out.println("ERROR: port " + port + " is not an integer.");
			System.exit(0);
		}
		return portInt;
	}

	
	static InetAddress createAddress(String host) {
		InetAddress address = null;
		try {
			address = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			System.out.println("ERROR: Localhost " + host + " not found");
			System.exit(0);
		}
		return address;
	}
	
}
