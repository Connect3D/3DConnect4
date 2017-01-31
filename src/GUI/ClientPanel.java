package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import client.Client;
import game.Controller;
import protocol.command.Action;
import util.MessageUI;

public class ClientPanel extends JPanel implements MessageUI {
	
	public final JButton b1Connect = new JButton("Connect");
	public JTextField tfHostname;
	public final JTextField tfPort = new JTextField("2727", 5);
	public final JTextField tfName = new JTextField(" ", 12);
	public final JTextField tfMyMessage = new JTextField(" ", 34);
	public final JTextArea taMessages = new JTextArea("", 10, 34);	
	
	public void build(Controller controller) {
		this.setLayout(new BorderLayout(0, 20));
		JPanel p1 = new JPanel(new FlowLayout());
		JPanel pp = new JPanel(new GridLayout(3, 2));

		JLabel tlHostname = new JLabel("Hostname: ");
		tfHostname = new JTextField(getHostAddress(), 12);
		JLabel tlPort = new JLabel("Port: ");

		JLabel tlName = new JLabel("Name: ");
		tfName.addKeyListener(controller);
		pp.add(tlHostname);
		pp.add(tfHostname);
		pp.add(tlPort);
		pp.add(tfPort);
		pp.add(tlName);
		pp.add(tfName);

		b1Connect.addActionListener(controller);
		b1Connect.setEnabled(false);

		p1.add(pp, BorderLayout.WEST);
		p1.add(b1Connect, BorderLayout.EAST);
		
		BorderLayout p2Layout = new BorderLayout(0, -30);
		JPanel p2 = new JPanel(p2Layout);
		
		JLabel tlMyMessage = new JLabel("MyMessage");
		tfMyMessage.addActionListener(controller);
		tfMyMessage.setEditable(false);

		p2.add(tlMyMessage);
		p2.add(tfMyMessage, BorderLayout.SOUTH);

		JPanel p3 = new JPanel(new BorderLayout(0, 10));

		JLabel tlMessages = new JLabel("Messages:");
		taMessages.setEditable(false);

		p3.add(tlMessages);
		p3.add(taMessages, BorderLayout.SOUTH);

		this.add(p1, BorderLayout.NORTH);
		this.add(p2);
		this.add(p3, BorderLayout.SOUTH);
	}
	
	public String getHostAddress() {
		try {
			InetAddress iaddr = InetAddress.getLocalHost();
			return iaddr.getHostName();
		} catch (UnknownHostException e) {
			return "?unknowns?";
		}
	}
	
	@Override
	public void addMessage(String msg) {
		taMessages.append(msg + "\n");
	}
	
}
