package game;

import game.player.Player;
import util.OutputsBoard;


// provides both functionality for running the game to the end or step by step
public class Game implements Runnable {

	public static final int CONSECUTIVE_MARKS_TO_WIN = 4;
	
	private Board board;
	private Player[] players;
	private int currentPlayer;
	private OutputsBoard boardOutput;
	
	public Game(Player _player1, Player _player2, OutputsBoard output) {
		players = new Player[2];
		players[0] = _player1;
		players[1] = _player2;
		currentPlayer = 0;
		board = new Board();
		boardOutput = output;
	}
	
	
	public void run() {
		while (board.getEnding() == Ending.NOT_ENDED) {
			if (boardOutput != null) boardOutput.printBoard();
			doMoveFor(players[currentPlayer]);
			switchCurrentPlayer();
			System.out.println(board.getEnding());		// temporary
		}
	}
	
	
	public Ending getEnding() {
		return board.getEnding();
	}
	
	
	private void doMoveFor(Player player) {
		if (board.getEnding() == Ending.NOT_ENDED) {
			Column move = player.getMove(board);
			board.doMove(move, player.mark);
		}
	}
	
	
	private void switchCurrentPlayer() {
		currentPlayer = currentPlayer == 0 ? 1 : 0;
	}
	
	
	// all possible endings of a game
	public enum Ending {
		
		NOT_ENDED, DRAW, X_WINS, O_WINS;
	
		// way to pass by value (enums and classes and thus normally passed by reference)
		public Ending copy() { 
			if (this == NOT_ENDED) return NOT_ENDED;
			if (this == DRAW) return DRAW;
			if (this == X_WINS) return X_WINS;
			else return O_WINS;
		}
		
		public String toString() {
			if (this == NOT_ENDED) return "NOT_ENDED";
			if (this == DRAW) return "DRAW";
			if (this == X_WINS) return "X_WINS";
			else return "O_WINS";
		}
		
	}

	

}
