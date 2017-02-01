package client.GUIPanels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import client.Connect4GUI;
import client.Controller;
import util.MessageUI;



public class ClientPanel extends JPanel implements MessageUI {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final JButton b1Connect = new JButton("Connect");
	public JTextField tfHostname;
	public final JTextField tfPort = new JTextField("2727", 5);
	public final JTextField tfName = new JTextField(" ", 12);
	//public final JTextField tfMyMessage = new JTextField(" ", 34);
	public final JTextArea taMessages = new JTextArea("", 10, 34);	
	public final static String NO_ERROR = "No errors detected.";
	public final JLabel errorField = new JLabel(NO_ERROR);
	
	
	public void build(Controller controller) {
		this.setLayout(new BorderLayout(0, 20));
		JPanel p1 = new JPanel(new FlowLayout());
		JPanel pp = new JPanel(new GridLayout(3, 2));

		JLabel tlHostname = new JLabel("Hostname: ");
		tfHostname = new JTextField(Connect4GUI.getHostAddress(), 12);
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
		
		p2.add(errorField);
		
		JPanel p3 = new JPanel(new BorderLayout());
		p3.setPreferredSize(new Dimension(100, 200));
		taMessages.setEditable(false);

		p3.add(taMessages, BorderLayout.SOUTH);

		this.add(p1, BorderLayout.NORTH);
		this.add(p2);
		this.add(p3, BorderLayout.SOUTH);
	}
	
	
	@Override
	public void addMessage(String msg) {
		taMessages.append(msg + "\n");
	}
	
}
