package game;

import game.player.Player;
import java.util.Observable;


// TODO make thread safe !!!!!!!!!!!!!
public class Game extends Observable implements Runnable {

	public static final int CONSECUTIVE_MARKS_TO_WIN = 4;
	
	private Board board = new Board();
	private Player[] players = new Player[2];
	private int currentPlayer = 0;


	public Game(Player _player1, Player _player2) {
		players[0] = _player1;
		players[1] = _player2;
	}
	
	
	public void run() {
		while (board.getEnding() == Ending.NOT_ENDED) {
			doMoveFor(players[currentPlayer]);
		}
	}
	
	
	public Board getBoardState() {
		return new Board(board);
	}
	
	
	public Ending getEnding() {
		return board.getEnding();
	}
	
	//concurrency. synchronize functions with data race.
	//look at thread pull.game thread safe.
	//updates network and GUI with tupil of move and playername
	private void doMoveFor(Player player) {
		if (board.getEnding() == Ending.NOT_ENDED) {
			Move move = player.getMove(board);
			board.doMove(move);
			setChanged();
			notifyObservers(move);
			switchCurrentPlayer();
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
