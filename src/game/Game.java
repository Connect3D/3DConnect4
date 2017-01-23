package game;

import game.player.Player;
import java.util.Observable;


// TODO make thread safe !!!!!!!!!!!!!
// TODO implement reset
public class Game extends Observable implements Runnable {

	public static final int CONSECUTIVE_MARKS_TO_WIN = 4;
	
	private Board board = new Board();
	private Player[] players = new Player[2];
	private int currentPlayer = 0;


	public Game(Player _player1, Player _player2) {
		players[0] = _player1;
		players[1] = _player2;
	}
	

	public String getCurrentPlayerName() {
		return players[currentPlayer].getName();
	}
	
	/**
	 * Had to implement this due to move not giving the player but the mark.
	 */
	public String getOtherPlayerName() {
		return players[currentPlayer == 0 ? 1 : 0].getName();
	}
	
	public Mark getCurrentPlayerMark() {
		return players[currentPlayer].mark;
	}

	
	public void run() {
		while (board.getEnding() == Ending.NOT_ENDED) {
			doMoveFor(players[currentPlayer]);
		}
		setChanged();
		notifyObservers(board.getEnding());
	}
	
	
	public synchronized Board getBoardState() {
		return new Board(board);
	}
	
	
	public synchronized Ending getEnding() {
		return board.getEnding();
	}
	
	
	private void doMoveFor(Player player) {
		Move move = player.getMove(board);
		synchronized (this) {
			board.doMove(move);
			switchCurrentPlayer();
		}
		setChanged();
		notifyObservers(move);
	}
	
	private void switchCurrentPlayer() {
		currentPlayer = currentPlayer == 0 ? 1 : 0;
	}
	
	// all possible endings of a game
	public enum Ending {
		
		NOT_ENDED, X_WINS, O_WINS, DRAW;
	
		// way to pass by value (enums are classes and thus normally passed by reference)
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
