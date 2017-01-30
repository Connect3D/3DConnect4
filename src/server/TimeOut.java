package server;

import java.util.Timer;
import java.util.TimerTask;

import protocol.command.Action;



class TimeOut {
	
	private final Server server;
	private final ClientHandler client;
	private final Timer timer;
	
	private SendDisconnect timeOut = null;
	
	public TimeOut(Server s, ClientHandler c) {
		server = s;
		client = c;
		timer = new Timer();
	}
	
	
	public synchronized void schedule(Long delay) {
		cancel();
		timeOut = new SendDisconnect(server, client, timer);
		timer.schedule(timeOut, delay);
	}
	
	
	public synchronized void cancel() {
		if (timeOut != null) {
			timeOut.cancel();
			timeOut = null;
		}
	}
	
	
	private class SendDisconnect extends TimerTask {
		
		private final ClientHandler client;
		private final Server server;
		private final Timer timer;
		
		public SendDisconnect(Server s, ClientHandler c, Timer t) {
			server = s;
			client = c;
			timer = t;
		}
		
		public void run() {
			client.sendCommand(Action.DISCONNECT);
			server.leave(client);
			timer.schedule(new CloseSocket(client), 1000);
		}
		
	}
	
	
	private class CloseSocket extends TimerTask {
		
		private final ClientHandler client;
		
		public CloseSocket(ClientHandler c) {
			client = c;
		}
		
		public void run() {
			//client.closeSocket();
		}
		
	}
	
}