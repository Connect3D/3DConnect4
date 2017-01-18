package client;

import game.*;
import game.player.*;
import game.player.strategy.*;

public class Client {
	
	
	public static void main(String[] args) {
		
		System.out.println("starting game");
		
		Player p1 = new ComputerPlayer(Mark.X, new RandomStrategy());
		Player p2 = new ComputerPlayer(Mark.O, new RandomStrategy());
		Game game = new Game(p1, p2);
		
		game.run();
		
		System.out.println(game.getEnding());
		
		
	}
	
	
}

