package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import util.MessageUI;
import util.Util;


public class ServerTui implements MessageUI {
	
	public static final String OPEN_COMMAND = "open";
	public static final String CLOSE_COMMAND = "close";
	public static final String QUIT_COMMAND = "quit";
	public static final String USAGE = "available commands: open <port>, close, quit";
	
	private final BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
	private Server server = null;
	private boolean quit = false;
	
	
	public static void main(String[] args)  {
		try {
			new ServerTui().run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void run() throws IOException {
		addMessage(USAGE);
		while (!quit) {
			String[] command = console.readLine().split(" ");
			if (command.length > 0) {
				switch (command[0]) {
					case OPEN_COMMAND:
						if (command.length == 2 && Util.isInt(command[1]) && server == null) {
							try {
								server = new Server(Integer.parseInt(command[1]), this);
								new Thread(server).start();
								addMessage("Server opened succesfully on port " + command[1]);
							} catch (IOException e) {
								addMessage(e.getMessage());
							}
						}
						break;
					case CLOSE_COMMAND:
						if (server != null) {
							server.shutDown();
							server = null;
						}
						break;
					case QUIT_COMMAND:
						if (server != null) {
							server.shutDown();
							server = null;
							addMessage("Quitting");
						}
						quit = true;
						break;
					default:
						addMessage(USAGE);
						break;
				}
			} else {
				addMessage(USAGE);
			}
		}
		console.close();
	}
	
	
	public synchronized void addMessage(String msg) {
		System.out.println(msg);
	}
	
}
