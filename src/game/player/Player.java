package game.player;

import game.Board;
import game.Mark;
import game.Move;



public abstract class Player {

	public final Mark mark;
	public final String name;
	
	
	public Player(String n, Mark m) {
		name = n;
		mark = m;
	}
	
	
	public String getName() {
		return name;
	}
	
	
	public abstract Move getMove(Board board);
	
}