
   Client/Server Experiments


Each of the four programs,

   Client_closes_in.java
   Client_closes_out.java
   Server_closes_in.java
   Server_closes_out.java

closes one of its two streams from its communicating socket.

      Client                               Server
      Socket                               Socket
  +-------------+                      +-------------+
  |             |    tcp connection    |             |
  | IP address  O----------------------O IP address  |
  | Port number |                      | Port number |
  |             |                      |             |
  +-/|\-----\ /-+                      +-/|\-----\ /-+
     |       |                            |       |
     |       |                            |       |
     |       |                            |       |
   out       in                         out       in
stream       stream                  stream       stream


Here is another way to picture a tcp connection.

      Client                               Server
      Socket                               Socket
  +-------------+                      +-------------+
  |             |    tcp connection    |             |
  |    in <-----|----------------------|----< out    |
  |   out >-----|----------------------|----> in     |
  |             |                      |             |
  +-------------+                      +-------------+


Normally we should pair these client server programs in the
followng ways.

   Client_closes_in.java   <==>  Server_closes_out.java
   Client_closes_out.java  <==>  Server_closes_in.java

In the first client/server pair, communication is strictly
from the client to the server. In the second client/server
pair, communication is strictly from the server to the client.

What happens if we pair them in the opposite way?
Explain the results.

   Client_closes_in.java   <==>  Server_closes_in.java
   Client_closes_out.java  <==>  Server_closes_out.java


One interesting thing to note is that it is wrong
to actually call the close() method on a stream
from a socket. If you call the close() method on
either one of the streams from a socket, that closes
the entire socket and the socket can no longer be
used for any communication. Instead of closing a
stream from a socket, you should call one of the
   shutdownInput()
   shutdownOutput()
methods from the Socket class. Each of these methods
closes its associated stream from the socket.


Another interesting thing to note is that when an
input stream from a socket connection is closed, and
the process at the other end of the connection writes
to its socket's output stream, then the data written
to the socket is "silently discarded". Compare that
to writing to a closed pipe, which throws an exception.
This is another example of how socket streams are not
like other streams.


The last interesting thing to note is that when an
output stream from a socket connection is closed, and
the process at the other end of the connection reads
from its socket's input stream, then the read returns
an end-of-stream condition. But if the socket of the
output stream is closed, and the process at the other
end of the connection reads from its socket's input
stream, then the read throws an exception.


The client server pair,

   Client_closes_in_v2.java  <==>  Server_closes_out_v2.java

uses only the stream from the client to the server. This
pair allows an unlimited number of lines of text data to
be sent. Run the client with a large text file directed
into its standard input stream.

   > java Client_closes_in_v2 < big.txt

(Run big_txt.cmd script file to download the large text file.)

This server blocks after it reads 1,000 lines of text. You can
unblock the server by tapping the Enter key. Notice how the client
sends data to the server even while the server is blocked and not
reading any of its data. The data being sent by the client is
filling up buffers in the operating system. When those buffers
fill up, the client will also get blocked. Unblocking the server
drains some of the buffers and so the client can also unblock
(until the buffers fill up again).

While the client is sending data to the server, try terminating
the server (with control-C). How does that affect the client?



The four programs

   Client_reads_first.java
   Client_writes_first.java
   Server_reads_first.java
   Server_writes_first.java

all implement a very simple text protocol. Each program reads
a single string from the other program and writes a single
string to the other. The programs differ in whether they do
their read first or their write first. If a program reads
first, then it echoes the string that it read back to the
other program.

Normally we should pair these client servers this way.

   Client_reads_first.java   <==>  Server_writes_first.java
   Client_writes_first.java  <==>  Server_reads_first.java

What happens of we pair them the opposite way?
Explain the results.

   Client_reads_first.java   <==>  Server_reads_first.java
   Client_writes_first.java  <==>  Server_writes_first.java
