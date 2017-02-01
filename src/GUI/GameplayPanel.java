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
import javax.swing.JTextField;

import client.States;
import game.Board;
import game.Controller;
import game.Game;
import game.Mark;
import protocol.command.Action;
import util.container.Vector;

public class GameplayPanel extends JPanel {

	public final JButton resetButton = new JButton("Play again");
	public final JButton exitButton = new JButton("Exit");
	public final JLabel statusLabel = new JLabel("");
	public final JButton statusButton = new JButton(States.READY.toString());
	public final JRadioButton[][] inputButtons = new JRadioButton[Board.WIDTH][Board.DEPTH];
	public final JButton[][][] outputButtons = new JButton[Board.WIDTH][Board.DEPTH][Board.HEIGHT];


	public void build(Controller controller) {
		this.setLayout(new BorderLayout());
		GridLayout glayout = new GridLayout(3, 1);
		glayout.setVgap(0);
		JPanel playPanel = new JPanel(glayout);
		playPanel.add(createStatusPanel(controller));
		playPanel.add(createInputPanel(controller));
		playPanel.add(createMenuPanel(controller), BorderLayout.SOUTH);

		JPanel mainOutputPanel = new JPanel(new FlowLayout());
		mainOutputPanel.add(createColumnOutputPanel());

		this.add(playPanel, BorderLayout.WEST);
		this.add(mainOutputPanel, BorderLayout.EAST);
		
		statusButton.setEnabled(false);
		enableInputButtons(false);

	}
	
	public void switchStatusButtonText() {
		statusButton.setText(statusButton.getText() == Action.READY.toString() ? Action.UNREADY.toString() : Action.READY.toString());
	}

	public void selectInputbutton(int x, int y, boolean bool) {
		inputButtons[x][y].setSelected(bool);
	}

	public void setOutputbuttonText(int x, int y, int z, String text) {
		outputButtons[x][y][z].setText(text);
	}

	public void enableInputbutton(int x, int y, boolean bool) {
		inputButtons[x][y].setEnabled(false);
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

	public JPanel createStatusPanel(Controller controller) {
		JPanel statusPanel = new JPanel(new GridLayout(3, 1));
		statusPanel.add(statusLabel);
		statusButton.addActionListener(controller);
		statusPanel.add(statusButton);
		return statusPanel;
	}

	public JPanel createInputPanel(Controller controller) {
		JPanel input = new JPanel(new GridLayout(Board.WIDTH, Board.HEIGHT));
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

	public void enableInputButtons(boolean bool) {
		for (int x = 0; x < Board.WIDTH; x++) {
			for (int y = 0; y < Board.DEPTH; y++) {
				inputButtons[x][y].setEnabled(bool);
			}
		}
	}

	public JPanel createMenuPanel(Controller controller) {
		JPanel menuPanel = new JPanel(new FlowLayout());
		resetButton.addActionListener(controller);
		resetButton.setEnabled(false);
		exitButton.addActionListener(controller);
		exitButton.setEnabled(false);
		menuPanel.add(resetButton);
		menuPanel.add(exitButton);
		return menuPanel;
	}

	public JPanel createColumnOutputPanel() {
		JPanel fpanel = new JPanel(new FlowLayout());
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
