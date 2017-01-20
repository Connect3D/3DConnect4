package game;

import game.player.Player;
import util.OutputsBoard;


public class Game implements Runnable {

	public static final int CONSECUTIVE_MARKS_TO_WIN = 4;
	
	private Board board = new Board();
	private Player[] players = new Player[2];
	private int currentPlayer = 0;
	private OutputsBoard boardOutput = null;
	
	
	// constructor for running the game with output(client)
	public Game(Player _player1, Player _player2, OutputsBoard output) {
		players[0] = _player1;
		players[1] = _player2;
		boardOutput = output;
	}
	
	
	// constructor for running the game without output(server)
	public Game(Player _player1, Player _player2) {
		players[0] = _player1;
		players[1] = _player2;
	}
	
	
	public void run() {
		while (board.getEnding() == Ending.NOT_ENDED) {
			if (boardOutput != null) boardOutput.printBoard(board);
			doMoveFor(players[currentPlayer]);
			switchCurrentPlayer();
		}
		if (boardOutput != null) boardOutput.printBoard(board);
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
		
		NOT_ENDED, X_WINS, O_WINS, DRAW;
	
		// way to pass by value (enums and classes and thus normally passed by reference)
		public Ending copy() { 
			if (this == NOT_ENDED) return NOT_ENDED;
			if (this == X_WINS) return X_WINS;
			if (this == O_WINS) return O_WINS;
			else return DRAW;
		}
		
		public String toString() {
			if (this == NOT_ENDED) return "not ended";
			if (this == X_WINS) return "Player X won";
			if (this == O_WINS) return "Player O won";
			else return "It was a draw";
		}
		
	}

	

}
