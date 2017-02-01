package util.test;

import static org.junit.Assert.*;

import org.junit.Test;
import game.Direction;



public class DirectionTest {

	
	@Test
	public void test() {
	}

	
	@Test
	public void testIterating() {
		int n = 0;
		for (Direction d = Direction.begin(); d.next() != null; d = d.next()) {
			System.out.println(n++);
		}
		assertEquals(n, 26);
	}
	
}
