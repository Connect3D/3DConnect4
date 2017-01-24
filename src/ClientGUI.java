

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import client.Client;
import util.MessageUI;

public class ClientGUI extends JFrame implements ActionListener, KeyListener, MessageUI {
	private JButton b1Connect;
	private JTextField tfHostname;
	private JTextField tfPort;
	private JTextField tfName;
	private JTextField tfMyMessage;
	private JTextArea taMessages;
	private Client client;
	
	public ClientGUI() {
		super("ClientGUI");

		buildGUI();
		setVisible(true);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				e.getWindow().dispose();
			}
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		});

	}

	public void buildGUI() {
		setSize(500, 400);

		JPanel p1 = new JPanel(new FlowLayout());
		JPanel pp = new JPanel(new GridLayout(3, 2));

		JLabel tlHostname = new JLabel("Hostname: ");
		tfHostname = new JTextField(getHostAddress(), 12);

		JLabel tlPort = new JLabel("Port: ");
		tfPort = new JTextField("2727", 5);

		JLabel tlName = new JLabel("Name: ");
		tfName = new JTextField(" ", 12);
		tfName.addKeyListener(this);

		pp.add(tlHostname);
		pp.add(tfHostname);
		pp.add(tlPort);
		pp.add(tfPort);
		pp.add(tlName);
		pp.add(tfName);

		b1Connect = new JButton("Connect");
		b1Connect.addActionListener(this);
		b1Connect.setEnabled(false);

		p1.add(pp, BorderLayout.WEST);
		p1.add(b1Connect, BorderLayout.EAST);

		JPanel p2 = new JPanel(new BorderLayout());
		JPanel p3 = new JPanel(new BorderLayout());

		JLabel tlMyMessage = new JLabel("MyMessage");
		tfMyMessage = new JTextField(" ", 34);
		tfMyMessage.addActionListener(this);
		tfMyMessage.setEditable(false);

		p2.add(tlMyMessage);
		p2.add(tfMyMessage, BorderLayout.SOUTH);

		JLabel tlMessages = new JLabel("Messages:");
		taMessages = new JTextArea("", 10, 34);
		taMessages.setEditable(false);

		p3.add(tlMessages);
		p3.add(taMessages, BorderLayout.SOUTH);

		Container cc = getContentPane();
		cc.setLayout(new FlowLayout());
		cc.add(p1);
		cc.add(p2);
		cc.add(p3);
	}
	
	public String getHostAddress() {
		try {
			InetAddress iaddr = InetAddress.getLocalHost();
			return iaddr.getHostName();
		} catch (UnknownHostException e) {
			return "?unknowns?";
		}
	}

	public void startConnecting() {
		String name = tfName.getText();
		int port = 0;
		InetAddress iaddress = null;
		try {
			port = Integer.parseInt(tfPort.getText());
		} catch (NumberFormatException e) {
			System.out.println("ERROR: port " + tfPort + " is not a valid portnumber");
		}
		try {
			iaddress = InetAddress.getByName(tfHostname.getText());
		} catch (UnknownHostException e) {
			System.out.println("ERROR: host " + tfHostname + " is not a valid hostname");
		}

		tfHostname.setEditable(false);
		tfPort.setEditable(false);
		tfName.setEditable(false);
		try {
			client = new Client(name, iaddress, port, this);
			client.start();
			tfMyMessage.setEditable(true);
		} catch (IOException e) {
			System.out.println("ERROR: Client could not be created");
		}
	}

	@Override
	public void addMessage(String msg) {
		taMessages.append(msg + "\n");
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		Object src = evt.getSource();
		if (src.equals(b1Connect)) {
			startConnecting();
		}
		if (src.equals(tfMyMessage)) {
			client.sendMessage(tfMyMessage.getText());
			tfMyMessage.setText("");
			
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		Object src = e.getSource();
		if (src.equals(tfName)) {
			b1Connect.setEnabled(true);
		}	
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	public static void main(String[] args) {
		new ClientGUI();
	}

}
