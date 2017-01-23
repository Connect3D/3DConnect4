package client;

import game.*;
import game.player.*;
import game.player.strategy.*;
import util.OutputsBoard;
import util.ProvidesMoves;
import util.ThreadedStreamReader;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.locks.ReentrantLock;



public class LocalGame implements Runnable, ProvidesMoves, Observer {
	
	
	public static void main(String[] args) {
		
		LocalGame testLocalGame = new LocalGame();
		testLocalGame.run();
		
	}
	
	
	// ====================================================================================
	// class starts here
	
	
	private ReentrantLock printLock = new ReentrantLock();
	private Column move = null;
	
	
	public void run() {
		
		ThreadedStreamReader input = new ThreadedStreamReader(System.in);
		Thread input_thread = new Thread(input);
		input_thread.start();
		
		Player player1 = null;
		Player player2 = null;
		Game game = null;
		
		System.out.println("Starting local game");
		
		
		while (input_thread.isAlive()) {
			
			if (input.available()) {
				
				String command = input.getLine();
				String[] splitted = command.split(" ");
				
				if (game == null) {
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
								else {
									System.out.println("Playertype not recognized");
								}
							}
							break;
						case "start":
							game = makeNewGame(player1, player2);
					}
				}
				else if (game.getEnding() == Game.Ending.NOT_ENDED) {
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
			}
			if (game != null && game.getEnding() != Game.Ending.NOT_ENDED) {
				finishGame(game);
				game = null;
			}
			try {
				Thread.sleep(40);
			} 
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
	
	
	private void finishGame(Game game) {
		printLock.lock();
		System.out.println("game finished");
		System.out.println(game.getEnding());
		printLock.unlock();
	}
	
	
	private Game makeNewGame(Player p1, Player p2) {		// giving reference argument didnt work for players, mayb add return game
		if (p1 == null || p2 == null) {
			System.out.println("Players are not initialized");
			return null;
		}
		Game new_game = new Game(p1, p2);
		Thread thread = new Thread(new_game);
		thread.start();
		return new_game;
	}
	
	
	private Player makePlayer(PlayerType playerType, Mark mark) {
		if (playerType == PlayerType.HUMAN) return new HumanPlayer("Henk", mark, this);
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


