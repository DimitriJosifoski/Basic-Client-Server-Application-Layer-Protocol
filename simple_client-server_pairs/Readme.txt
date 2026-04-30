
    TCP Client/Server Communication

This folder contains six pairs of client-server programs
that use sockets and TCP to communicate with each other.

For each client/server pair, compile both the client and the
server program, then start the server program in one command-
line terminal, and then start the client in another command-
line terminal.

Every client/server pair must have a "protocol" that they
agree to use. For example, it must be agreed on, ahead of
time, which program communicates first on their connection.
They need to agree on how many turns of reading and writing
each program will take. They need to agree on how they will
determine when to terminate their connection. They need to
agree on the format of the data that they transmit (for
example, text data in UTF-8, or text data in ASCII, or
binary numeric data, etc).

The first client/server pair,
   EchoServer_v1.java
   EchoClient_v1a.java
implements a very simple protocol. The client communicates first
and sends one line of text to the server and then waits for a
response from the server. The server receives the client's line
of text, echoes it back to the client, and then closes its end of
the connection. The client receives the response from the server
and then closes its end of the connection. The client
   EchoClient_v1b.java
implements the same protocol but it prompts the user for the line
of text to send to the server.

The second client/server pair,
   EchoServer_v2.java
   EchoClient_v2.java
implements a protocol similar to the first. The client communicates
first and sends one line of text to the server and then waits for a
response from the server. The server receives the client's line of
text, echoes it back to the client, and then waits for a second line
of text from the client. The client receives the first response from
the server, sends the server a second line of text, and then waits
for the second response from the server. The server receives the
client's second line of text, echoes it back to the client, and then
closes its end of the connection. The client receives the second
response from the server and then closes its end of the connection.

The third client/server pair,
   EchoServer_v3.java
   EchoClient_v3.java
implements a protocol similar to the second but the client can send
an arbitrary number of text lines to the server. The client tells
the server that it is done sending text by closing its end of the
connection. The server echoes back to the client every line of text
sent by the client. The server closes its end of the connection when
the server detects the end-of-stream condition on its input stream.

The fourth client/server pair
   NumericServer.java
   NumericClient.java
implements a binary protocol. The client communicates first and
sends to the server a 4-byte binary integer and then the client
waits for the server to send it the number of doubles represented
by that integer value. The server receives the integer value from
the client and then sends to the client that many 8-byte binary
doubles. After sending the doubles to the client, the server closes
its end of the connection. After the client receives the appropriate
number of doubles from the serve, the client closes its end of the
connection.

The fifth client/server pair
   NumericTextServer.java
   NumericTextClient.java
implements a text version of the previous protocol. The client
communicates first and sends to the server an integer encoded as
a text string and then the client waits for the server to send it
the number of doubles represented by that integer value. The server
receives the integer value from the client and then sends to the
client that many doubles encoded as text strings. After sending the
doubles to the client, the server closes its end of the connection.
After the client receives the appropriate number of doubles from the
serve, the client closes its end of the connection. The client and
server need to agree on what text encoding to use on each of the two
connection streams. They do not have to use the same encoding on both
streams, but they do need to agree on what encoding is used on each
stream. Also, since the numbers are sent as encoded text, and every
number can have a different number of digits, the streams need to
use white space characters to separate the numbers from each other.

The sixth client/server pair
   UploadServer.java
   UploadClient.java
implements a binary protocol. The client sends to the server all the
data from the client's standard input stream. The server stores the
data it receives from a client in a file in the current directory.
The communication is a binary protocol because the server does not
know, and does not need to know, what kind of data is being sent
to it by the client. The client may upload a binary file or a text
file to the server. So the server must use a binary input stream
from the client and a binary output stream to the saved file.


Notice that each of the servers is programmed to accept an arbitrary
number of connections from clients. After you launch a client and it
communicates with its server, launch another client, then another
client. Notice that each server logs what it is doing to its console
window including a count of each client connection.

Here are several experiments that you can do using these client/server
programs.

Start up EchoServer_v1.java and then run EchoClient_v1b.java but do not
yet type in any input text. Then run an instance of EchoClient_v1a.java
and then run another instance of EchoClient_v1b.java (you will need four
command-line terminals). Then type a line of input to the first instance
of EchoClient_v1b.java. What happens? Why?


Start up EchoServer_v2.java and then run EchoClient_v1a.java. Then
run another instance of EchoClient_v1a.java. What happens? Why?


Start up EchoServer_v3.java and then run EchoClient_v1a.java. Then
run another instance of EchoClient_v1a.java. Then run an instance
of EchoClient_v2.java. What happens? Why?


Start up EchoServer_v1.java and then run EchoClient_v3.java. What
happens? Why?


Start up EchoServer_v3.java and then type in the following URL into
your browser's address bar.
     localhost:5000
What happens? (This experiment works a bit better if you stop the
server from echoing lines back to the client. You need to comment
out two lines of code in the server and then recompile it.)


Start up NumericServer.java and then run EchoClient_v1b.java. Enter
two characters at the prompt, say "xy", and tap the Enter key. What
happens? Why?

Still using NumericServer.java, run EchoClient_v2.java. Tap the Enter
key at the first prompt. What happens? Why? Tap the Enter key again
at the second prompt. What happens? Why? (Hint: Where does the number
538978406 come from?)


Start up EchoServer_v3.java and then run UploadClient.java (without
any file redirection). Type a bunch of input lines followed by ctrl-z.
Why doesn't the server respond to each of the client's input lines?
Why do all of the server's responses come after the client closes
its input to the server?


Try running two instances of a server on the same port number. What
happens? Try running two instances of a server on two different port
numbers (pass the port as a command-line parameter). Run clients that
connect to either running server (give the clients command-line
arguments to tell them which running server to connect to).

Try running a server and its client on two different computers at
the same time, so you are really using a network.

Use the command-line program netstat to get information about server
and client programs that are currently running on your computer.

  > netstat -n -p tcp
  > netstat -f -p tcp
  > netstat /?

https://learn.microsoft.com/en-us/windows-server/administration/windows-commands/netstat
https://man7.org/linux/man-pages/man8/netstat.8.html

Download and run the Windows program TCPView to get information
similar to netstat about running server and client programs.

https://learn.microsoft.com/en-us/sysinternals/downloads/tcpview
