package game.player;

import game.Board;
import game.Column;
import game.Mark;
import game.player.strategy.*;


public class ComputerPlayer extends Player {

	private Strategy strategy;
	
	public ComputerPlayer(Mark m, Strategy s) {
		super(m);
		strategy = s;
	}

	public Column getMove(Board board) {
		return strategy.getMove(board);
	}
	
	
}
