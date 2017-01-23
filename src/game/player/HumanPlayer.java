package game.player;

import java.util.ArrayList;

import game.Board;
import game.Column;
import game.Mark;
import game.Move;
import util.ProvidesMoves;


public class HumanPlayer extends Player {

	ProvidesMoves input;
	
	public HumanPlayer(String n, Mark m, ProvidesMoves p) {
		super(n, m);
		input = p;
	}


	public Move getMove(Board board) {
		ArrayList<Column> possible = board.possibleColumns();
		while (true) {
			Column choice = input.waitForMove();
			for (int i = 0; i < possible.size(); ++i) {
				if (possible.get(i).equals(choice)) {
					return new Move(choice, mark);
				}
			}
		}
	}
 
}
