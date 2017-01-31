package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;


public class DebugClient implements Runnable {

	private final Socket socket;
	private final BufferedReader socket_in;
	private final BufferedWriter socket_out;
	private final BufferedReader console_in;
	private final BufferedWriter console_out;
	
	
	public static void main(String[] args) throws Exception {
	DebugClient client = new DebugClient();
		new Thread(client).start();
		client.readConsole();
	}

	
	public DebugClient() throws Exception {
		socket = new Socket(InetAddress.getLocalHost(), 2727);
		socket_in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		socket_out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		console_in = new BufferedReader(new InputStreamReader(System.in));
		console_out = new BufferedWriter(new OutputStreamWriter(System.out));
	}
	
	
	public void run() {
		while (socket.isConnected()) {
			try {
				console_out.write(socket_in.readLine() + "\n");
				console_out.flush();
			} 
			catch (IOException e) {
				closeStreams();
				break;
			}
		}
	}
	
	
	public void readConsole() {
		while (socket.isConnected()) {
			try {
				socket_out.write(console_in.readLine() + "\n");
				socket_out.flush();
			}
			catch (IOException e) {
				closeStreams();
				break;
			}
		}
	}
	
	
	private void closeStreams() {
		try {
			socket_in.close();
			socket_out.close();
			console_in.close();
			console_out.close();
		} 
		catch (IOException e) { }
	}
	
}
