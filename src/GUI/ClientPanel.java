package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import client.Client;
import game.Controller;
import util.MessageUI;

public class ClientPanel extends JPanel implements MessageUI {
	private JButton b1Connect;
	private JTextField tfHostname;
	private JTextField tfPort;
	private JTextField tfName;
	private JTextField tfMyMessage;
	private JTextArea taMessages;
	
	public ClientPanel(Controller controller) {
		this.setLayout(new FlowLayout());
		JPanel p1 = new JPanel(new FlowLayout());
		JPanel pp = new JPanel(new GridLayout(3, 2));

		JLabel tlHostname = new JLabel("Hostname: ");
		tfHostname = new JTextField(Connect4GUI.getHostAddress(), 12);

		JLabel tlPort = new JLabel("Port: ");
		tfPort = new JTextField("2727", 5);

		JLabel tlName = new JLabel("Name: ");
		tfName = new JTextField(" ", 12);
		tfName.addKeyListener(controller);

		pp.add(tlHostname);
		pp.add(tfHostname);
		pp.add(tlPort);
		pp.add(tfPort);
		pp.add(tlName);
		pp.add(tfName);

		b1Connect = new JButton("Connect");
		b1Connect.addActionListener(controller);
		b1Connect.setEnabled(false);

		p1.add(pp, BorderLayout.WEST);
		p1.add(b1Connect, BorderLayout.EAST);

		JPanel p2 = new JPanel(new BorderLayout());
		JPanel p3 = new JPanel(new BorderLayout());

		JLabel tlMyMessage = new JLabel("MyMessage");
		tfMyMessage = new JTextField(" ", 34);
		tfMyMessage.addActionListener(controller);
		tfMyMessage.setEditable(false);

		p2.add(tlMyMessage);
		p2.add(tfMyMessage, BorderLayout.SOUTH);

		JLabel tlMessages = new JLabel("Messages:");
		taMessages = new JTextArea("", 10, 34);
		taMessages.setEditable(false);

		p3.add(tlMessages);
		p3.add(taMessages, BorderLayout.SOUTH);

		this.add(p1);
		this.add(p2);
		this.add(p3);
	}

	public JButton getConnectButton() {
		return b1Connect;
	}

	public String getMyMessageText() {
		return tfMyMessage.getText();
	}
	
	public String getClientName() {
		return tfName.getText();
	}
	
	public JTextField getNameField() {
		return tfName;
	}
	
	public String getPort() {
		return tfPort.getText();
	}
	
	public String getHostName() {
		return tfHostname.getText();
	}
	
	public void enableFields(boolean bool) {
		tfHostname.setEditable(bool);
		tfPort.setEditable(bool);
		tfName.setEditable(bool);
	}
	
	
	public void enableConnectButton(boolean bool) {
		b1Connect.setEnabled(bool);
	}
	
	public void setMyMessageText(String msg) {
		tfMyMessage.setText(msg);
	}

	@Override
	public void addMessage(String msg) {
		taMessages.append(msg + "\n");
	}

	public void setMyMessageFieldEditable(boolean bool) {
		tfMyMessage.setEditable(true);
	
	}
	
}
