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
import game.Controller;
import game.Game;
import game.Mark;
import protocol.command.Action;
import util.Vector;

public class GameplayPanel extends JPanel {

	private JButton resetButton;
	private JLabel statusLabel;
	private JRadioButton[][] inputButtons;
	private JButton[][][] outputButtons;
	
	public GameplayPanel(Controller controller) {
		this.setLayout(new BorderLayout());
		
		GridLayout glayout = new GridLayout(3, 1);
		glayout.setVgap(0);		
		JPanel playPanel = new JPanel(glayout);
		playPanel.add(createStatusPanel());
		playPanel.add(createInputPanel(controller));
		playPanel.add(createMenuPanel(controller), BorderLayout.SOUTH);

		JPanel mainOutputPanel = new JPanel(new FlowLayout());
		mainOutputPanel.add(createColumnOutputPanel());

		this.add(playPanel, BorderLayout.WEST);
		this.add(mainOutputPanel, BorderLayout.EAST);
	}
	
	public JButton getResetbutton() {
		return resetButton;
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
	
	void enableResetButton(boolean bool) {
		resetButton.setEnabled(bool);
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
	
	public JPanel createStatusPanel() {
		JPanel statusPanel = new JPanel(new BorderLayout());
		statusLabel = new JLabel("");
		statusPanel.add(statusLabel, BorderLayout.NORTH);
		statusPanel.add(new JButton(Action.UNREADY.toString()), BorderLayout.SOUTH);
		return statusPanel;
	}
	
	
	public JPanel createInputPanel(Controller  controller) {
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

	public JPanel createMenuPanel(Controller  controller) {
		JPanel menuPanel = new JPanel(new FlowLayout());
		resetButton = new JButton("Play again");
		resetButton.addActionListener(controller);
		resetButton.setEnabled(false);
		menuPanel.add(resetButton);
		return menuPanel;
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
