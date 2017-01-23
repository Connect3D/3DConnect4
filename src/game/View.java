package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import game.Game;
import game.Mark;
import game.player.HumanPlayer;

/**
 * @author RichKok
 * GUI with buttons and text notifying the user of the game's status.
 * An Actionlistener is attached to the buttons, calling the private Controller class which in turn issues the Game class.
 * The view is added as an observer to the game class, therefore changes in Game consequently call the update command.
 * Changes of state in the game class are observed 
 */
public class View implements Observer {

	public View() {
		
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		Game game = new Game(new Humanplayer(Mark.X), new HumanPlayer(Mark.O));
	}

	class Controller implements ActionListener {

		Game game;
		
		public Controller(Game game) {
			this.game = game;
		}
		
		/**
		 * Receives input from GUI buttons, calls an appropriate command of Game.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
				
		}
		
	}
}
