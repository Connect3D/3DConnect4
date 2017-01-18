package game.player;

import game.Board;
import game.Column;
import game.Mark;


public abstract class Player {

	public final Mark mark;
	
	public Player(Mark m) {
		mark = m;
	}
	
	public abstract Column getMove(Board board);
	
}