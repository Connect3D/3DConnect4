package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import game.Board;
import game.Direction;
import game.Direction.Axis;
import game.Column;
import game.Game;
import game.Mark;
import game.Position;

public class BoardTest {

	 private Board board;
	 
	 /**
     * Sets the instance variable to an initial value.
     * All test methods should be preceded by a call to this method.
     */
    @Before
    public void setUp() {
    	board = new Board();
    }
    
    /**
     * after the first column has not yet been filled, 15 columns remain.
     */
    @Test 
	public void possibleColumnsNoneFull() {
		Direction dir = new Direction(Axis.NEUTRAL, Axis.NEUTRAL, Axis.NEUTRAL);
		fillConsecutiveMarks(new Position(0, 0, 0), Mark.X, dir, 3);
		assertEquals(board.possibleColumns().size() == 16, true);
	}
    
    /**
     * after the first column has been filled, 15 columns remain.
     */
    @Test 
	public void possibleColumnsOneFull() {
		Direction dir = new Direction(Axis.NEUTRAL, Axis.NEUTRAL, Axis.NEUTRAL);
		fillConsecutiveMarks(new Position(0, 0, 0), Mark.X, dir, 4);
		assertEquals(board.possibleColumns().size() == 15, true);
	}
 

    /**
     * A not filled column, is not indicated as full.
     */
	@Test public void notFullColumn(){
		Direction dir = new Direction(Axis.NEUTRAL, Axis.NEUTRAL, Axis.NEUTRAL);
		fillConsecutiveMarks(new Position(0, 0, 0), Mark.X, dir, 3);
		assertEquals(board.isColumnFull(new Column(0, 0)), false);
	}
	
	
    /**
     * After filling up a column, the board states that it is full
     */
	@Test public void fullColumn(){
		Direction dir = new Direction(Axis.NEUTRAL, Axis.NEUTRAL, Axis.NEUTRAL);
		fillConsecutiveMarks(new Position(0, 0, 0), Mark.X, dir, 4);
		assertEquals(board.isColumnFull(new Column(0, 0)), true);
	}
    
    /**
     * Tests if filling up first column gives a not ended game. 
     */
	@Test
	public void incompleteSeqZDir() {
		Direction dir = new Direction(Axis.NEUTRAL, Axis.NEUTRAL, Axis.POSITIVE);
		fillConsecutiveMarks(new Position(0, 0, 0), Mark.X, dir, 3);
		assertEquals(board.getEnding() == Game.Ending.NOT_ENDED, true);
	}
	
	/**
     * Filling up the first column gives an ended game. 
     */
	@Test
	public void completeSeqZDir() {
		Direction dir = new Direction(Axis.NEUTRAL, Axis.NEUTRAL, Axis.NEUTRAL);
		fillConsecutiveMarks(new Position(0, 0, 0), Mark.X, dir, 4);
		assertEquals(board.getEnding() == Game.Ending.X_WINS, true);
	}
	
	@Test
	public void incompleteSeqXDir() {
		Direction dir = new Direction(Axis.POSITIVE, Axis.NEUTRAL, Axis.NEUTRAL);
		fillConsecutiveMarks(new Position(0, 0, 0), Mark.X, dir, 3);
		assertEquals(board.getEnding() == Game.Ending.NOT_ENDED, true);
	}
	@Test
	public void completeSeqXDir() {
		Direction dir = new Direction(Axis.POSITIVE, Axis.NEUTRAL, Axis.NEUTRAL);
		fillConsecutiveMarks(new Position(0, 0, 0), Mark.O, dir, 4);
		assertEquals(board.getEnding() == Game.Ending.O_WINS, true);
	}
	

	@Test
	public void incompleteSeqYDirNotEmptyBoard() {
		Direction dir = new Direction(Axis.NEUTRAL, Axis.POSITIVE, Axis.NEUTRAL);
		fillConsecutiveMarks(new Position(0, 0, 0), Mark.O, dir, 3);
		Direction dir2 = new Direction(Axis.NEUTRAL, Axis.POSITIVE, Axis.NEUTRAL);
		fillConsecutiveMarks(new Position(0, 0, 0), Mark.X, dir, 4);
		assertEquals(board.getEnding() == Game.Ending.X_WINS, false);
	}
	
	@Test
	public void completeSeqYDirNotEmptyBoard() {
		Direction dir = new Direction(Axis.NEUTRAL, Axis.POSITIVE, Axis.NEUTRAL);
		fillConsecutiveMarks(new Position(0, 0, 0), Mark.X, dir, 3);
		Direction dir2 = new Direction(Axis.NEUTRAL, Axis.POSITIVE, Axis.NEUTRAL);
		fillConsecutiveMarks(new Position(0, 0, 0), Mark.X, dir, 4);
		assertEquals(board.getEnding() == Game.Ending.X_WINS, true);
	}
	
    private void fillConsecutiveMarks(Position position, Mark mark, Direction direction, int steps) {
    	Position next = position;
    	int amount = 0;
    	while (next != null && amount < steps){
			board.doMove(new Column(next.x, next.y) , mark);
			next = next.inDirection(direction);
			amount++;
		} 
	}

}
