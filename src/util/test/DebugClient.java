package util.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;



public class DebugClient implements Runnable {

	private final Socket socket;
	private final BufferedReader socketIn;
	private final BufferedWriter socketOut;
	private final BufferedReader consoleIn;
	private final BufferedWriter consoleOut;
	
	private boolean stop = false;
	//InetAddress REMOTE_ADRESS = InetAddress.getByName("130.89.95.25");
	
	
	public static void main(String[] args) throws Exception {
		DebugClient client = new DebugClient();
		new Thread(client).start();
		client.readConsole();
	}

	
	public DebugClient() throws Exception {
		/*InetAddress REMOTE_ADDRESS 
			= InetAddress.getByName("2001:67c:2564:a130:350f:4f74:beed:238f");
		InetAddress REMOTE_ADDRESS 
			= InetAddress.getByName("130.89.176.65");
		socket = new Socket(REMOTE_ADDRESS, 2727); */
		socket = new Socket(InetAddress.getLocalHost(), 2727);
		socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		socketOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		consoleIn = new BufferedReader(new InputStreamReader(System.in));
		consoleOut = new BufferedWriter(new OutputStreamWriter(System.out));
	}
	
	
	public void run() {
		while (!stop) {
			try {
				consoleOut.write(socketIn.readLine() + "\n");
				consoleOut.flush();
			} catch (IOException e) {
				stop = true;
				closeStreams();
				break;
			}
		}
	}
	
	
	public void readConsole() throws InterruptedException {
		while (!stop) {
			try {
				socketOut.write(consoleIn.readLine() + "\n");
				socketOut.flush();
			} catch (IOException e) {
				stop = true;
				closeStreams();
				break;
			}
		}
	}
	
	
	private void closeStreams() {
		try {
			stop = true;
			socketIn.close();
			socketOut.close();
			consoleIn.close();
			consoleOut.close();
		} catch (IOException e) { }
	}
	
}
