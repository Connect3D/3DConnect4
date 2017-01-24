package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import util.MessageUI;


public class ServerTui implements Runnable, MessageUI {
	
	public static final String OPEN_COMMAND = "open";
	public static final String CLOSE_COMMAND = "close";
	
	private Server server = null;
	private BufferedReader console;
	private boolean quit = false;
	
	
	public ServerTui() {
		console = new BufferedReader(new InputStreamReader(System.in));
	}

	
	public void run() {
		
		while (!quit) {
			String line = "";
			try {
				line = console.readLine();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
			if (line.equals("exit")) {
				break;
			}
			if (line.trim().length() > 0) {
				runCommand(line.split(" "));
			}
		}
		
		try {
			console.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	private void runCommand(String[] command) {
		switch (command[0]) {
			case OPEN_COMMAND:
				if (command.length == 2) {
					server = new Server(Integer.parseInt(command[1]), this);		// TODO chekc what happens when command[1] is not an int
				}
				// TODO create some sort of manual of open commands
				break;
			case CLOSE_COMMAND:
				if (server != null) {
					quit = true;
					server.shutdown();
				}
				break;
		}
	}

	
	public void addMessage(String msg) {
		System.out.println(msg);
	}
	
}
