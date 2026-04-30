/*

*/

import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Scanner;

/**
   This client sends user provided lines of text to the
   server and reads the server's response to each line.
<p>
   This client stops sending lines to the server when the
   client detects the eof condition on its standard input
   stream. After sending the last line of text to the server,
   this client closes its output stream to the server, which
   causes the server to see an end-of-file condition. Then
   this client expects one more response back from the server.
<p>
   Try sending a whole file to the server. For example,
<p>
   > java EchoClient_v3  <  EchoClient_v1.java
<p>
   This client defaults to the localhost IP address with
   port number 5000. The remote host and port numbers
   can be changed using command-line arguments.

@see EchoServer_v3
*/
public class EchoClient_v3
{
   public static final int SERVER_PORT = 5000; // Should be above 1023.

   /**
      @param args  two optional command-line arguments, hostname and port number
   */
   public static void main (String[] args)
   {
      Socket          socket = null;
      BufferedReader  in = null;
      PrintWriter     out = null;

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

         in = new BufferedReader(
                  new InputStreamReader(          // Step 9.
                       socket.getInputStream())); // Step 8.

         out = new PrintWriter(                   // Step 9.
                      socket.getOutputStream());  // Step 8.
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
      // Send the server multiple messages.
      final Scanner stdin = new Scanner(System.in);
      System.out.println("Type a message for the server (^z or ^d to end):");
      while ( stdin.hasNextLine() ) // Read stdin until eof.
      {
         final String message = stdin.nextLine();
         out.println(message);
         out.flush(); // Make sure that the message is sent.
         // Receive an echo back from the server.
         try
         {
            final String response = in.readLine();
            System.out.println("CLIENT: Server response is: \"" + response + "\"");
         }
         catch (IOException e)
         {
            System.out.println("CLIENT: Cannot receive response from server.");
            System.out.println( e );
         }
         System.out.println("Type a message for the server (^z or ^d to end):");
      }

      // Tell the server that this client is done sending messages.
      // We cannot close the socket's out stream. That shuts down the socket!
      // Instead we tell the socket to close its output stream.
      try
      {
         //out.close(); // Wrong!
         socket.shutdownOutput();
      }
      catch (IOException e)
      {
         System.out.println("CLIENT: Cannot shut down output stream to server.");
         System.out.println( e );
      }

      // Wait for a single response from the server.
      try
      {
         final String response = in.readLine();
         System.out.println("CLIENT: Server response is: \"" + response + "\"");
      }
      catch (IOException e)
      {
         System.out.println("CLIENT: Cannot receive response from server.");
         System.out.println( e );
      }

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
   private EchoClient_v3() {
      throw new AssertionError();
   }
}
