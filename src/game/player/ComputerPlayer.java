package game.player;

import game.Board;
import game.Column;
import game.Mark;
import game.Move;
import game.player.strategy.*;


public class ComputerPlayer extends Player {

	private Strategy strategy;
	public static final String NAME = "ComputerPlayer";
	
	public ComputerPlayer(Mark m, Strategy s) {
		this(NAME, m, s);
	}
	
	public ComputerPlayer(String n, Mark m, Strategy s) {
		super(n, m);
		strategy = s;
	}

	public Move getMove(Board board) {
		return new Move(strategy.getMove(board), mark);
	}
	
	
}
