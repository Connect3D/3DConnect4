Client and server application to play connect 4 in 3D!

Packages

ssProject
	src
		client
			GUIPanels
		game
		protocol
			command
		server
		util
			container
			exception	
			test

External libraries
	download latest jar release of Guava at https://github.com/google/guava/releases
	add to build path in Eclipse.

Playing Connect4 3D 
between two human players or between two random players

run ssProject/src/server/serverTUI
Usage: open <portnr>

  2 times:
	run ssProject/src/client/connect4GUI*
	On startup of the GUI,
	enter name and port number if playing local, else 
	also enter the port address (IP, IP4 or IP6)


	Now on both of the two GUI’s, the connect button can be pressed.
	This will give a connection message on the server and client GUI message box with the name of the connected person(s).
	If one is connected, the ready button can be pressed. If both have pressed the ready button, the server will send a game.

	*with configuration argument “HUMAN” if a game is to be played between two humans. Else do not provide any argument.


Test cases 

	provided under util.test

Tools
	JUNit required for testing,
	can be downloaded at https://github.com/junit-team/junit4/wiki/Download-and-Install
	

Contact

	Aart Odding
	s1595555
	a.v.odding@student.utwente.nl

	Richard Kok
	s1572431 
	r.c.kok@student.utwente.nl
