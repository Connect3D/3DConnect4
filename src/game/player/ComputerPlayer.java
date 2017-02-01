package game.player;

import java.util.ArrayList;
import client.Controller;
import game.Board;
import game.Column;
import game.Mark;
import game.Move;
import game.player.strategy.*;



public class ComputerPlayer extends Player {

	private Strategy strategy;
	public static final String NAME = "ComputerPlayer";
	private Controller input;

	
	public ComputerPlayer(Mark m, Strategy s, Controller c) {
		this(NAME, m, s, c);
	}
	

	public ComputerPlayer(String n, Mark m, Strategy s, Controller c) {
		super(n, m);
		strategy = s;
		input = c;
	}

	
	public Move getMove(Board board) {
		ArrayList<Column> possible = board.possibleColumns();
		while (true) {
			Move move = new Move(strategy.pickColumn(board), mark);
			input.sendMove(move.column.x, move.column.y);
			input.waitForMove();
			for (Column c : possible) {
				if (c.equals(move.column)) {
					return move;
				}
			}
		}
	}
	
}
