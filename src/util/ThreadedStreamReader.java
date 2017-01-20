package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;


public class ThreadedStreamReader implements Runnable {

	public static final String EXIT_CODE = "exit";
	
	private BufferedReader input;
	
	private LinkedList<String> buffer = new LinkedList<String>();
	private ReentrantLock bufferLock = new ReentrantLock();
	
	private AtomicBoolean available = new AtomicBoolean(false);
	
	
	public ThreadedStreamReader(InputStream _input) {
		input = new BufferedReader(new InputStreamReader(_input));
	}


	public void run() {
		while (true) {
			String line = "";
			try {
				line = input.readLine();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
			if (line.equals(EXIT_CODE)) {
				break;
			}
			if (line.trim().length() > 0) {
				bufferLock.lock();
				buffer.add(line);
				available.set(true);
				bufferLock.unlock();
			}
		}
		try {
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public boolean available() {
		return available.get();
	}
	
	
	public String getLine() {
		if (!available.get()) {
			return null;
		}
		
		String line;
		
		bufferLock.lock();
		
		line = buffer.removeFirst();
		if (buffer.isEmpty()) {
			available.set(false);
		}
		
		bufferLock.unlock();
		
		return line;
	}
	
}
