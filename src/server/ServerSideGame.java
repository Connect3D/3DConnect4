package server;

import java.util.Observable;
import java.util.Observer;

import game.Column;
import game.Game;
import game.Mark;
import game.Move;
import game.player.HumanPlayer;
import util.ProvidesMoves;

public class ServerSideGame implements Observer, ProvidesMoves {

	private Game game;
	public final ClientHandler player1;
	public final ClientHandler player2;
	
	
	public ServerSideGame(ClientHandler p1, ClientHandler p2) {
		player1 = p1;
		player2 = p2;
		game = new Game(new HumanPlayer("name", Mark.X, this), new HumanPlayer("name", Mark.O, this));
		game.addObserver(this);
		Thread t = new Thread(game);
		t.start();
	}


	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof Game.Ending) {
			// game has ended
		}
		else if (arg1 instanceof Move) {
			// move was done
		}
	}


	@Override
	public Column waitForMove() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
}
