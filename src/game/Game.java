package game;

import game.player.Player;


// provides both functionality for running the game to the end or step by step
public class Game implements Runnable {

	public static final int CONSECUTIVE_MARKS_TO_WIN = 4;
	
	private Board board;
	private Player player1;
	private Player player2;
	private Player current_player;
	
	
	public Game(Player _player1, Player _player2) {
		player1 = _player1;
		player2 = _player2;
		current_player = player1;
		board = new Board();
	}
	
	
	public void run() {
		while (board.getEnding() == Ending.NOT_ENDED) {
			doMoveFor(current_player);
			switchCurrentPlayer();
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
		if (current_player == player1) current_player = player2;
		if (current_player == player2) current_player = player1;
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
		
	}

	

}
