package test;

import static org.junit.Assert.*;

import org.junit.Test;

import game.Board;
import game.Column;
import game.Game;
import game.Mark;

public class BoardTest {

<<<<<<< HEAD
	/**
	 * 
	 * Task: Create tests
	 * 
	 */
=======
	
>>>>>>> bc012d043baefea1d23237182a2ea586e77eb2f7
	@Test
	public void test() {
		
		System.out.println("testing of Board class");
		
		Board b1 = new Board();
		
		b1.doMove(new Column(1, 1), Mark.X);
		b1.doMove(new Column(1, 1), Mark.X);
		b1.doMove(new Column(1, 1), Mark.X);
		
		System.out.println(b1.getEnding() == Game.Ending.NOT_ENDED);
		
		b1.doMove(new Column(1, 1), Mark.X);
		
		System.out.println(b1.isColumnFull(new Column(1, 1)));
		System.out.println(b1.getEnding() == Game.Ending.X_WINS);
		
		Board b2 = new Board(b1);
		
		System.out.println(b2.possibleColumns().size() == 15);
		
	}

}
