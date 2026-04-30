/*

*/

import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.IOException;

/**
   This client uploads a file to the server.
<p>
   This client sends one message to the server and
   that message is all of the data available from
   this client's standard input stream. So this
   client essentially "uploads" data to the server.
<p>
   When this client detects end-of-stream on its
   standard input, it closes its output stream to
   the server so that the server knows that there
   is no more data arriving from this client. Then
   this client waits for a response from the server.
<p>
   When this client detects end-of-stream on its
   input from the server, this client closes its
   socket.
<p>
   Try sending a file to the server. For example,
<p>
   > java UploadClient  <  EchoClient_v1.java
<p>
   This client can also be used to communicate with
   a web server. Try entering the following five
   line into cmd.
<pre>
      >java UploadClient  www.example.com  80
      GET / HTTP/1.1
      Host: www.example.com

      ctrl-z
</pre>
<p>
   The web server should return a short web page.
<p>
   This client defaults to the localhost IP address with
   port number 5000. The remote host and port numbers
   can be changed using command-line arguments.

@see UploadServer
*/
public class UploadClient
{
   public static final int SERVER_PORT = 5000; // Should be above 1023.

   /**
      @param args  two optional command-line arguments, hostname and port number
   */
   public static void main (String[] args)
   {
      Socket socket = null;
      BufferedInputStream   in = null;
      BufferedOutputStream out = null;

      final String hostName;
      if (args.length > 0)
      {
         hostName = args[0];
      }
      else
      {
         hostName = "localhost";
      }

      final int portNumber;
      if (args.length > 1)
      {
         portNumber = Integer.parseInt(args[1]);
      }
      else
      {
         portNumber = SERVER_PORT;
      }

      // Get this client's process id number (PID). This helps
      // to identify the client in TaskManager or TCPView.
      final ProcessHandle handle = ProcessHandle.current();
      final long pid = handle.pid();
      System.out.println("CLIENT: Process ID number (PID): " + pid );

      // Make a connection to the server
      try
      {
         System.out.println("CLIENT: Connecting to server: " + hostName
                          + " on port " + portNumber );
         // Steps 5, 6, 7.
         socket = new Socket(InetAddress.getByName(hostName),
                             portNumber);

         in = new BufferedInputStream(           // Step 9.
                     socket.getInputStream());   // Step 8.

         out = new BufferedOutputStream(         // Step 9.
                      socket.getOutputStream()); // Step 8.
      }
      catch (IOException e)
      {
         System.out.println("CLIENT: Cannot connect to server.");
         e.printStackTrace();
         System.exit(-1);
      }
      System.out.println("CLIENT: Connected to server.");
      // Get this client's local port number and log it to the console.
      // This helps to identify this client in the server's transcript.
      final int port = socket.getLocalPort();
      System.out.println("CLIENT: Local Port: " + port);

      // Step 9.
      // Implement an application layer protocol.
      // Send the server a single message with all the
      // data from this client's standard input stream.
      final BufferedInputStream stdin = new BufferedInputStream(System.in);
      int bytesSent = 0;
      try
      {
         // Since the streams are buffered, its OK
         // to read and write one byte at a time.
         int oneByte;  // Must be an int because of "eof".
         while ((oneByte = stdin.read()) != -1) // Read stdin until "eof".
         {
            out.write(oneByte);
            ++bytesSent;
         }
         out.flush();
      }
      catch (IOException e)
      {
         System.out.println("CLIENT: Cannot send data to server.");
         System.out.println( e );
      }
      // Tell the server that this client is done sending data.
      // We cannot close the socket's out stream. That shuts down the socket!
      // Instead we tell the socket to close its output stream.
      try
      {
         //out.close(); // Wrong!
         socket.shutdownOutput();  // Send "eof" to the server.
      }
      catch (IOException e)
      {
         System.out.println("CLIENT: Cannot close output stream to server.");
         System.out.println( e );
      }
      System.out.println("CLIENT: Sent " + bytesSent + " bytes to server.");

      System.out.println("CLIENT: Waiting for server response.\n");

      // Wait for a possible response from the server.
      int bytesReceived = 0;
      try
      {
         // Since the streams are buffered, its OK
         // to read and write one byte at a time.
         int oneByte; // Must be an int because of "eof".
         while ((oneByte = in.read()) != -1) // Read in until "eof".
         {
            System.out.write(oneByte);
            ++bytesReceived;
         }
         System.out.flush();
      }
      catch (IOException e)
      {
         System.out.println("CLIENT: Cannot receive response from server.");
         System.out.println( e );
      }
      System.out.println("\nCLIENT: Received " + bytesReceived + " bytes from server.");

      // Disconnect from the server.
      try
      {
         socket.close(); // Step 10.
         socket = null;  // Step 11.
         System.out.println("CLIENT: Closed socket.");
      }
      catch (IOException e)
      {
         System.out.println("CLIENT: Cannot disconnect from server.");
         System.out.println( e );
      }
   }


   // Private default constructor to enforce noninstantiable class.
   // See Item 4 in "Effective Java", 3rd Ed, Joshua Bloch.
   private UploadClient() {
      throw new AssertionError();
   }
}
