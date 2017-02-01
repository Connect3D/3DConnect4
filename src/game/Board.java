package game;

import java.util.ArrayList;
import java.util.Arrays;
import util.exception.ColumnFullException;

/**
 * @author Aart Odding.
 * Board class representing a 3D connect4 board.
 * A board consists of WIDTH * DEPTH columns, each can be filled with HEIGHT marks.
 * When a block has been cascased vertically down the column, which normally happens due to gravity,
 * the board looks in all possible directions to see if there are enough consecutive marks to win.
 */
public class Board {
	
	public static final int WIDTH = 4; 
	public static final int DEPTH = 4; 
	public static final int HEIGHT = 4;
	public static final int N_FIELDS = WIDTH * DEPTH * HEIGHT; //Possible positions to fill a mark.
	
	
	public Mark[] fields;       // all possible fields in width, depth and height of the board.
						 	    //invariant: fields.length == N_FIELDS
	private Game.Ending ending; //Indicates the current state of the game.
							    //invariant: ending != null
	
	
	/**
	 * Creates a board, fills it with empty fields and gives it an ending state of not ended.
	 */
	/* @ensures fields.length = N_FIELDS
	 * 			(\forall int i; 0 <= i && i < fields.length;
 					fields[i] == Mark.EMPTY); 
 				ending == Ending.NOT_ENDED */
		
	public Board() {
		fields = new Mark[N_FIELDS];
		Arrays.fill(fields, Mark.EMPTY);
		ending = Game.Ending.NOT_ENDED;
	}
	
	
	/**
	 * Constructor that copies a board.
	 * @param board. The board to be copied.
	 */
	/* @requires board != null
	 * @ensures fields.length = N_FIELDS
	 * 			(\forall int i; 0 <= i && i < fields.length;
 	 *				fields[i] == Mark.EMPTY); 
     *				ending == Ending.NOT_ENDED */
	public Board(Board board) {
		fields = new Mark[N_FIELDS];
		for (int i = 0; i < N_FIELDS; ++i) {
			fields[i] = board.fields[i];
		}
		ending = board.ending.copy();
	}
	
	
	///////////////////////////////////////////////////////////////
	//                                                           //
	//    					Queries 						     //
	//                                                           //
	///////////////////////////////////////////////////////////////
	
	
	/**
	 * Returns the height of the given column.
	 * @param column. The column for which the height is checked. 
	 * @return height of the column.
	 */
	/* @requires column != null;
	 * @ensures\result >= 0 & /result < HEIGHT. */
	public int getColumnHeight(Column column) {
		return cascade(column).z;
	}

	
	/**
	 * Returns the current state of the game.
	 * @return a copy of the ending state.
	 */
	 /* @ensures \result != null;  */
	public Game.Ending getEnding() {
		return ending.copy();
	}
	
	/**
	 * Checks wether the column is full.
	 * @param column 	the column to be checked.
	 * @return if the column has all positions not empty.
	 */
	/* @require column != null */
	public boolean isColumnFull(Column column) {
		return fields[Position.indexOf(column.x, column.y, HEIGHT - 1)] != Mark.EMPTY;
	}
	
	
	/**
	 * Returns an arraylist of all the columns that are not full.
	 * @return arraylist of non-empty columns.
	 */
	/* @ensures\result >= 0 & \result < WIDTH * DEPTH */
	public ArrayList<Column> possibleColumns() {
		ArrayList<Column> possible = new ArrayList<Column>();
		for (int x = 0; x < WIDTH; ++x) {
			for (int y = 0; y < DEPTH; ++y) {
				Column column = new Column(x, y);
				if (!isColumnFull(column)) {
					possible.add(column);
				}
			}
		}
		return possible;
	}
	
	
	///////////////////////////////////////////////////////////////
	//                                                           //
	//    					Public Commands		     		     //
	//                                                           //
	///////////////////////////////////////////////////////////////
	
	
	/**
	 * Cascades down the column of the move parameter
	 * and fills the lowest empty field with the mark of the move.
	 * @param move 	the move with a column and a mark.
	 */
	/* @ensures notEmptyFields.length >= old.otEmptyFields.length
	 */
	public void doMove(Move move) {
		Position position = cascade(move.column);
		fields[position.index()] = move.mark;
		ending = checkEndingFor(position, move.mark);
	}
	
	
	///////////////////////////////////////////////////////////////
	//                                                           //
	//    				Private Commands					     //
	//                                                           //
	///////////////////////////////////////////////////////////////
	
    /**
     * Returns the ending state of the game by checking 
     * for consecutive marks from the given position.
     * Should be called after every new move since not all previous positions are checked. 
     * @param position with a x, y and a z.
     * @param mark	   either X or O.
     * @return ending state of the game from a given position and mark.
     */
	/* @ensures \result == DRAW ||  \result == X_WINS ||
	 * 		    \result == O_WINS ||  \result == NOT_ENDED */
	private Game.Ending checkEndingFor(Position position, Mark mark) {
		if (isFull()) {
			return Game.Ending.DRAW;
		}
		for (Direction d = Direction.begin(); !d.equals(Direction.center()); d = d.next()) {
			int consecutive = 1 + consecutiveMarks(position, mark, d) 
				+ consecutiveMarks(position, mark, d.opposite());
			if (consecutive >= Game.CONSECUTIVE_MARKS_TO_WIN) {
				if (mark == Mark.X) {
					return Game.Ending.X_WINS;
				}
				if (mark == Mark.O) {
					return Game.Ending.O_WINS;
				}
			}
		}
		return Game.Ending.NOT_ENDED;
	}
	
	/**
	 * Returns wether the board is full.
	 * @return true if board is full, false if board is not full
	 */
	private boolean isFull() {
		for (int x = 0; x < WIDTH; ++x) {
			for (int y = 0; y < DEPTH; ++y) {
				if (!isColumnFull(new Column(x, y))) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Cascades down a given column,
	 * finds the position of the first empty position starting from the bottom.
	 * @param column the column on which the cascading function is performed.
	 * @return the position of the lowest empty position.
	 */
	/* @requires column != null
	 * @ensures \result instanceof position || \result instance of ColumnFullException */
	private Position cascade(Column column) {
		for (int z = 0; z < HEIGHT; ++z) {
			if (fields[Position.indexOf(column.x, column.y, z)] == Mark.EMPTY) {
				return new Position(column.x, column.y, z);
			}
		}
		throw new ColumnFullException(column.x, column.y);
	}
	
	/**
	 * Returns the amount of consecutive marks equal to the passed mark.
	 * Starting from a position and looking in a given direction.
	 * @param position position with a x, y and z. 
	 * @param mark	   	mark enum, either X or O
	 * @param direction direction with a 0 or 1 for x, y and z.
	 * @return amount of equals marks from a position looking into a direction.
	 */
	/* @require position != null
	 *  		mark != null
	 *  		direction != null
	 *  @ensures result >= 0 && ( \result < WIDTH || \result < DEPTH|| \result < HEIGHT)  */
	private int consecutiveMarks(Position position, Mark mark, Direction direction) {
		Position next = position.inDirection(direction);
		int amount = 0;
		while (next != null && fields[next.index()] == mark) {
			next = next.inDirection(direction);
			++amount;
		}
		return amount;
	}
	
	

}