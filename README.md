Client and server application to play connect 4 in 3D!

<h2>Packages:</h2>

client
client.GUIPanels
game
game.player
game.player.strategy
protocol
protocol.command
server
util
util.container
util.exception
util.test

<h2>External libraries:</h2>
Google Guava, provided in /lib	 https://github.com/google/guava
add to build path in Eclipse.

<h2>Client usage</h2>
run ssProject/src/client/connect4GUI*
On startup of the GUI,
enter name and port number if playing local, else 
also enter the port address (IP, IP4 or IP6)

Now on both of the two GUI’s, the connect button can be pressed.
This will give a connection message on the server and client GUI message box with the name of the connected person(s).
If one is connected, the ready button can be pressed. If both have pressed the ready button, the server will send a game.

*with configuration argument “HUMAN” if a game is to be played between two humans. Else do not provide any argument.

<h2>Server usage</h2>
run server/serverTui
Usage: open <portnr>

<h2>Test cases</h2> 
provided under util.test
system testing done with util/test/debugclient

<h2>Tools required for running</h2>
- JUNit required for unit testing, download at https://github.com/junit-team/junit4/wiki/Download-and-Install

<h2>Contact</h2>
Aart Odding
s1595555
a.v.odding@student.utwente.nl

Richard Kok
s1572431 
r.c.kok@student.utwente.nl
