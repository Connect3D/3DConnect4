package client;

import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;

import game.*;
import game.player.*;
import game.player.strategy.*;
import util.OutputsBoard;
import util.ProvidesMoves;


public class LocalGame implements Runnable, ProvidesMoves, OutputsBoard {
	
	
	public static void main(String[] args) {
		
		System.out.println("starting game");

		LocalGame testLocalGame = new LocalGame();
		Player p1 = new HumanPlayer(Mark.X, testLocalGame);
		Player p2 = new ComputerPlayer(Mark.O, new RandomStrategy());
		Game game = new Game(p1, p2, testLocalGame);
		
		
		Thread thread = new Thread(game);
		thread.start();
		testLocalGame.run();
		
		
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println(game.getEnding());
	}
	
	
	// ====================================================================================
	// class starts here
	
	
	private Column move = null;
	//private ReentrantLock inputLock = new ReentrantLock();
	private ReentrantLock printLock = new ReentrantLock();
	
	
	public void run() {
		
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
	
	
	public synchronized void printBoard(Board board) {
		printLock.lock();
		for (int y = Board.DEPTH - 1; y >= 0; --y) {
			for (int z = 0; z < Board.HEIGHT; ++z) {
				System.out.print(Board.DELIMETER);
				System.out.print("\t");
			}
			System.out.print("\n");
			for (int z = 0; z < Board.HEIGHT; ++z) {
				System.out.print(board.getRowAsString(y, z));
				System.out.print("\t");
			}
			System.out.print("\n");
		}
		for (int z = 0; z < Board.HEIGHT; ++z) {
			System.out.print(Board.DELIMETER);
			System.out.print("\t");
		}
		System.out.print("\n");
		printLock.unlock();
	}
	
	
}


