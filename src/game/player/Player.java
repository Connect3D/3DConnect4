package game.player;

import game.Board;
import game.Column;
import game.Mark;
import game.Move;


public abstract class Player {

	public final Mark mark;
	
	public Player(Mark m) {
		mark = m;
	}
	
	public abstract Move getMove(Board board);
	
}