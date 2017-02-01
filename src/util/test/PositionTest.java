package util.test;

import static org.junit.Assert.*;
import org.junit.Test;
import game.Position;


public class PositionTest {

	@Test
	public void test() {
		Position p1 = new Position(0, 0, 0);
		Position p2 = new Position(3, 3, 3);
		Position p3 = new Position(1, 2, 0);
		Position p4 = new Position(3, 1, 2);
		assertTrue(p1.index() == 0);
		assertTrue(p2.index() == 63);
		assertTrue(p3.index() == 24);
		assertTrue(p4.index() == 54);
	}

}
