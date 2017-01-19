package game.player.strategy;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import game.Board;
import game.Column;


public class RandomStrategy implements Strategy {

	public Column getMove(Board board) {
		ArrayList<Column> possible = board.possibleColumns();
		int index = ThreadLocalRandom.current().nextInt(0, possible.size());
		return possible.get(index);
	}
	
}
