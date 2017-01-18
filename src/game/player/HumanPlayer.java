package game.player;

import java.util.ArrayList;

import game.Board;
import game.Column;
import game.Mark;
import util.ProvidesMoves;


public class HumanPlayer extends Player {

	
	ProvidesMoves input;
	
	public HumanPlayer(Mark m, ProvidesMoves p) {
		super(m);
	}


	public Column getMove(Board board) {
		ArrayList<Column> possible = board.possibleColumns();
		while (true) {
			Column choice = input.waitForMove();
			for (int i = 0; i < possible.size(); ++i) {
				if (possible.get(i).equals(choice)) {
					return choice;
				}
			}
		}
	}

}
