package server;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;



public class TimeOutTimer {
	

	public static final long ACKNOWLEDGEMENT_TIMEOUT_DURATION = 5000l;        // 5 seconds
	public static final long MOVE_TIMEOUT_DURATION = 600000l;                 // 10 minutes
	
	private final Server server;
	private final Timer timer;
	private final HashMap<ClientHandler, MoveTimeOut> moveTimeOuts;
	//private final HashMap<ClientHandler, MoveTimeOut> acknowledgementTimeOuts;
	
	
	public TimeOutTimer(Server s) {
		server = s;
		timer = new Timer();
		moveTimeOuts = new HashMap<ClientHandler, MoveTimeOut>();
		//acknowledgementTimeOuts = new HashMap<ClientHandler, MoveTimeOut>();
	}
	
	
	public synchronized void start(Type type, ClientHandler client) {
		
		switch (type) {
			case ACKNOWLEDGEMENT_TIMEOUT:
				break;
			case MOVE_TIMEOUT:
				MoveTimeOut timeOut = new MoveTimeOut(server, client);
				moveTimeOuts.put(client, timeOut);
				timer.schedule(timeOut, MOVE_TIMEOUT_DURATION);
				break;
		}
		
	}
	
	
	public synchronized void cancel(Type type, ClientHandler client) {
		
		switch (type) {
			case ACKNOWLEDGEMENT_TIMEOUT:
				break;
			case MOVE_TIMEOUT:
				if (moveTimeOuts.containsKey(client)) {
					moveTimeOuts.get(client).cancel();
					moveTimeOuts.remove(client);
				}
				break;
		}
	}
	
	
	public enum Type {
		ACKNOWLEDGEMENT_TIMEOUT,
		MOVE_TIMEOUT;
	}
	
	
	private class MoveTimeOut extends TimerTask {
		
		private final ClientHandler client;
		private final Server server;
		
		
		public MoveTimeOut(Server s, ClientHandler c)  {
			server = s;
			client = c;
		}
		
		public void run() {
			server.tryForfeitGame(client);
		}		
		
	}
	
	
}
