package client;

import java.util.Scanner;

import game.*;
import game.player.*;
import game.player.strategy.*;
import util.ProvidesMoves;


public class Client implements Runnable, ProvidesMoves {
	
	
	private Column move = null;
	
	
	public static void main(String[] args) {
		
		System.out.println("starting game");
		
		Client client = new Client();
		Player p1 = new HumanPlayer(Mark.X, client);
		Player p2 = new ComputerPlayer(Mark.O, new RandomStrategy());
		Game game = new Game(p1, p2);
		
		Thread t1 = new Thread(client);
		Thread t2 = new Thread(game);
		
		t1.start();
		t2.start();
		/*
		try {
			//Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}*/
		
		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(game.getEnding());
	}
	
	
	public Client() {
	
	}
	
	
	public void run() {
		System.out.println("starting");
		
		Scanner console = new Scanner(System.in);
		boolean exit = false;
		
		while (console.hasNextLine()) {
			String input = console.nextLine();
			String[] splitted = input.split(" ");
			
			switch (splitted[0]) {
				case "exit":
					exit = true;
					break;
				case "move":
					if (splitted.length == 3) {
						move = new Column(Integer.parseInt(splitted[1]), Integer.parseInt(splitted[2]));
						notify();
					}
					break;
			}
			
			if (exit) break;
		}
 		
		console.close();
	}
	
	
	public synchronized Column waitForMove() {
		try {
			while (move == null) {
				wait();
			}
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		Column choice = new Column(move);
		move = null;
		return choice;
	}
	
	
}

