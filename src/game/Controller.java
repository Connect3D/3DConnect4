package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import util.ProvidesMoves;

public class Controller implements ActionListener, ProvidesMoves {

	private Column column;
	private View view;

	public Controller() {
	}

	public void setView(View view) {
		this.view = view;
	}

	/**
	 * Receives input from GUI buttons, calls an appropriate command of Game.
	 */
	@Override
	public synchronized void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (src instanceof JButton) {
			JButton button = (JButton) src;
			Vector buttonPos = view.getButtonVector(button);
			column = new Column(buttonPos.x, buttonPos.y);
			notifyAll();
		}
	}

	@Override
	public synchronized Column waitForMove() {
		try {
			while (column == null) {
				wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Column choice = new Column(column);
		column = null;
		return choice;
	}

}