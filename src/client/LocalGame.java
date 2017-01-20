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
		
		LocalGame testLocalGame = new LocalGame();
		/*
		Player p1 = new HumanPlayer(Mark.X, testLocalGame);
		Player p2 = new ComputerPlayer(Mark.O, new RandomStrategy());
		Game game = new Game(p1, p2, testLocalGame);
		
		Thread thread = new Thread(game);
		thread.start();
		*/
		testLocalGame.run();
		/*
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		
		//System.out.println(game.getEnding());
	}
	
	
	// ====================================================================================
	// class starts here
	
	
	private ReentrantLock printLock = new ReentrantLock();
	private Player player1 = null;
	private Player player2 = null;
	private Game game = null;
	private Column move = null;
	
	
	public void run() {
		

		System.out.println("Starting");
		
		Scanner console = new Scanner(System.in);
		boolean exit = false;
		
		while (!exit) {
			String input = console.nextLine();
			String[] splitted = input.split(" ");
			
			if (splitted[0].equals("exit")) break;
			
			if (game != null && game.getEnding() == Game.Ending.NOT_ENDED) {
				switch (splitted[0]) {
					case "move":
						if (splitted.length == 3) {
							synchronized (this) {
								move = new Column(Integer.parseInt(splitted[1]), Integer.parseInt(splitted[2]));
								notify();
							}
						}
						break;
				}
			}
			else {
				switch (splitted[0]) {
					case "players":
						if (splitted.length == 3) {
							PlayerType p1 = PlayerType.fromString(splitted[1]);
							PlayerType p2 = PlayerType.fromString(splitted[2]);
							if (p1 != null && p2 != null) {
								player1 = makePlayer(PlayerType.fromString(splitted[1]), Mark.X);
								player2 = makePlayer(PlayerType.fromString(splitted[2]), Mark.O);
								System.out.println("Players initialized");
							}
						}
						break;
					case "start":
						if (player1 != null && player2 != null) {
							game = new Game(player1, player2, this);
							Thread thread = new Thread(game);
							thread.start();
						}
						else {
							System.out.println("Players are not initialized");
						}
				}
			}
		}
 		
		console.close();
	}
	
	
	private Player makePlayer(PlayerType playerType, Mark mark) {
		if (playerType == PlayerType.HUMAN) return new HumanPlayer(mark, this);
		if (playerType == PlayerType.RANDOM) return new ComputerPlayer(mark, new RandomStrategy());
		if (playerType == PlayerType.MINIMAX) return null;
		if (playerType == PlayerType.ONLINE) return null;
		return null;
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
	
	
	public void printBoard(Board board) {
		printLock.lock();
		for (int y = Board.DEPTH - 1; y >= 0; --y) {
			System.out.print("\t");
			for (int z = 0; z < Board.HEIGHT; ++z) {
				System.out.print(Board.DELIMETER);
				System.out.print("\t");
			}
			System.out.print("\n");
			System.out.print("      " + y + " ");
			for (int z = 0; z < Board.HEIGHT; ++z) {
				System.out.print(board.getRowAsString(y, z));
				System.out.print("\t");
			}
			System.out.print("\n");
		}
		System.out.print("\t");
		for (int z = 0; z < Board.HEIGHT; ++z) {
			System.out.print(Board.DELIMETER);
			System.out.print("\t");
		}
		System.out.print("\n");
		printLock.unlock();
	}
	
	
}


