package client;

import java.util.Scanner;

import game.*;
import game.player.*;
import game.player.strategy.*;
import util.OutputsBoard;
import util.ProvidesMoves;


public class LocalGame implements Runnable, ProvidesMoves, OutputsBoard {
	
	
	public static void main(String[] args) {
		
		System.out.println("starting game");
		
		LocalGame test = new LocalGame();
		Player p1 = new HumanPlayer(Mark.X, test);
		Player p2 = new ComputerPlayer(Mark.O, new RandomStrategy());
		Game game = new Game(p1, p2, test);
		
		Thread t1 = new Thread(test);
		Thread t2 = new Thread(game);
		
		t1.start();
		t2.start();
		
		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println(game.getEnding());
	}
	
	
	// ====================================================================================
	// class starts here
	
	
	private Column move = null;
	
	
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
						synchronized (this) {
							move = new Column(Integer.parseInt(splitted[1]), Integer.parseInt(splitted[2]));
							notify();
						}
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


	public synchronized void printBoard() {
		
	}
	
	
}


