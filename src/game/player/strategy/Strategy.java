package game.player.strategy;

import game.Board;
import game.Column;


public interface Strategy {

	public Column getMove(Board board);
	
}
