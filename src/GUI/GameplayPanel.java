package GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import game.Board;
import game.Game;
import game.Mark;
import game.View;
import util.Vector;

public class GameplayPanel extends JPanel {

	private JPanel mainPanel;
	private JButton anotherGame;
	private JLabel statusLabel;
	private JRadioButton[][] inputButtons;
	private JButton[][][] outputButtons;
	
	public GameplayPanel(Connect4GUI.Controller controller) {
		JPanel mainPanel = new JPanel(new BorderLayout());
		GridLayout glayout = new GridLayout(3, 1);
		glayout.setVgap(0);

		JPanel playPanel = new JPanel(glayout);
		playPanel.add(createStatusPanel());
		playPanel.add(createInputPanel(controller));

		playPanel.add(createMenuPanel(controller), BorderLayout.SOUTH);

		JPanel mainOutputPanel = new JPanel(new FlowLayout());
		mainOutputPanel.add(createColumnOutputPanel());

		mainPanel.add(playPanel, BorderLayout.WEST);
		mainPanel.add(mainOutputPanel, BorderLayout.EAST);
	}
	
	void selectInputbutton(int x, int y, boolean bool){
		inputButtons[x][y].setSelected(bool);
	}

	void setOutputbuttonText(int x, int y, int z, String text) {
		outputButtons[x][y][z].setText(text);
	}
	
	public void setStatuslabelText(String text) {
		statusLabel.setText(text);
	}
	
	void enableInputbutton(int x, int y, boolean bool) {
		inputButtons[x][y].setEnabled(false);
	}
	
	void enableAnothergameButton(boolean bool) {
		anotherGame.setEnabled(bool);
	}
	
	public JButton getAnothergameButton() {
		return anotherGame;
	}
	
	public Vector getInputbuttonVector(JRadioButton src) {
		for (int x = 0; x < Board.WIDTH; x++) {
			for (int y = 0; y < Board.DEPTH; y++) {
				if (src.equals(inputButtons[x][y])) {
					return new Vector(x, y);
				}
			}
		}
		return null;
	}
	
	public JPanel createInputPanel(Connect4GUI.Controller  controller) {
		JPanel input = new JPanel(new GridLayout(Board.WIDTH, Board.HEIGHT));
		inputButtons = new JRadioButton[Board.WIDTH][Board.DEPTH];
		for (int x = 0; x < Board.WIDTH; x++) {
			for (int y = 0; y < Board.DEPTH; y++) {
				JRadioButton button = new JRadioButton();
				inputButtons[x][y] = button;
				button.addActionListener(controller);
				input.add(button);
			}
		}
		return input;
	}

	public JPanel createMenuPanel(Connect4GUI.Controller  controller) {
		JPanel menuPanel = new JPanel(new FlowLayout());
		anotherGame = new JButton("Play again");
		anotherGame.addActionListener(controller);
		anotherGame.setEnabled(false);
		menuPanel.add(anotherGame);
		return menuPanel;
	}

	public JPanel createStatusPanel() {
		JPanel statusPanel = new JPanel(new FlowLayout());
		statusLabel = new JLabel("");
		statusPanel.add(statusLabel);
		return statusPanel;
	}
	

	public JPanel createColumnOutputPanel() {
		JPanel fpanel = new JPanel(new FlowLayout());
		outputButtons = new JButton[Board.WIDTH][Board.DEPTH][Board.HEIGHT];
		GridLayout gLayout = new GridLayout(Board.HEIGHT, 1);
		gLayout.setVgap(20);
		JPanel mgpanel = new JPanel(gLayout);
		for (int z = Board.HEIGHT - 1; z >= 0; z--) {
			JPanel gpanel = new JPanel(new GridLayout(Board.WIDTH, Board.HEIGHT));
			for (int x = 0; x < Board.WIDTH; x++) {
				for (int y = 0; y < Board.DEPTH; y++) {
					JButton button = new JButton();
					button.setPreferredSize(new Dimension(20, 20));
					outputButtons[x][y][z] = button;
					gpanel.add(button);
				}
			}
			mgpanel.add(gpanel);
		}
		fpanel.add(mgpanel);
		return fpanel;
	}


}
