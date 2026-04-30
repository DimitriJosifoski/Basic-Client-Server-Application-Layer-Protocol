
    How to denote the "end-of-message"

When a client and a server implement an application layer protocol,
the client process sends the server a "request message" and the
server sends back to the client a "response message".

When the client process sends a request message, and the client
reaches the end of that message, the client needs a way to tell
the server that there is no more data and the server should
switch from reading data from the client to writing its response
message back to the client. Without some kind of signal from the
client that the request message is complete, the server has no way
to know, while it is reading data from the client, if a pause in
the data transmission means the end of the data or if the pause
really is just a pause and the data will continue.

When the server sends its response message back to the client, the
server has a similar problem. It needs a way to signal the client
that the server has finished sending the complete response message.

One solution to this problem is to define, as part of the application
layer protocol, a specific length for the request and response messages.
For example each message may be defined to be a specific number of bytes,
or to be exactly one line of text. In this case, when the server has read
the specified number of bytes (or one text line) from the client, the
server knows that it has read the whole message and the server can switch
to sending a response.

The problem with this strategy is that it does not allow for arbitrarily
sized requests or responses. In most application layer protocols, not every
message will have the same length, some are short and some are long. In this
case, a protocol needs a strategy that allows the client and the server to
send an arbitrary amount of data in a message and for the recipient to be
able to know when it has received all of the (arbitrary) data in a message.

In general, there are three ways in which a client/server pair can send
messages of arbitrary size and still coordinate the exchange of their
read/write roles. They can coordinate,
   1.) using end-of-stream,
   2.) using a counter,
   3.) using a sentinel value.

The three client/server pairs in this folder,

   AdditionClient_v1.java  /  AdditionServer_v1.java
   AdditionClient_v2.java  /  AdditionServer_v2.java
   AdditionClient_v3.java  /  AdditionServer_v3.java

demonstrate these three ways using, respectively,
   1.) end-of-stream,
   2.) a counter,
   3.) a sentinel value.

In all three of these client/server pairs, the client
send a request that is made up of a sequence of integers,
and expextes a response that is the sum of those integers.
The problem is for the client to some how let the server
know how many integers are in one request (we could fix
the number of integers in a request, but that is not
very useful or interesting).

The first pair uses end-of-stream to denote that the client
is done writing its request message and so the server should
stop reading integers and send its response. Since the client
uses end-of-stream to denote it is done writing, the client
can only send one request to the server.

In the second pair, the client begins each request with a count
value to let the server know how many integers are in the request.
When the server has read than many integers, the server switches
over to writing its response. Since the client has not closed its
output stream, the client can send any number of requests to the
server. The client uses end-of-stream to denote the end of its
requests to the server.

In the third pair, the client uses a negative number as a sentinel
value at the end of each request message to let the server know
that it has read the last value in the request. When the server
reads a negative value it should switch from reading integers to
writing its response. Since the client is using a negative number
as a sentinel, it cannot use negative numbers as data values. Since
the client has not closed its output, the client can send any number
of requests to the server. The client uses end-of-stream to denote
the end of its requests to the server.


In all three client/server pairs, the client uses
end-of-stream to denote the end of the client's requests
to the server. But it's also possible for the client to use
a sentinel or a counter to let the server know when the
client no longer has more requests to make. It is left as
an exercise to write client/server pairs that use either a
sentinel or a counter to denote the end of client requests.


NOTE: In these examples, only the client is sending a variable
amount of information. The server always sends back a single
number. It could be the case that the client always sends a
fixed amount of information and the server sends a variable
amount. And it could also be the case that both the client
and the server always send a variable amount of information
in each exchange. Any one of the above three techniques could
be use by either the client or the server to denote the "end
of data" in an exchange of information.


NOTE: These three techniques for exchanging read/write roles
are used extensively in the HTTP protocol for web servers and
clients. We will see several different sentinel values and a
variety of counters being used by the HTTP protocol.


NOTE: The client/server pairs in this folder all use text data
to send integer numbers. They could be rewritten to use a binary
integer data format for sending values back and forth. It is
left as an exercise to rewrite one of the client/server pairs
to use binary streams and a binary data format.
