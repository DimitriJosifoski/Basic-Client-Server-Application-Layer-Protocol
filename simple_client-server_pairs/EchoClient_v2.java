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
   This client sends two user provided lines of text
   to the server and reads the server's response to
   each line.
<p>
   If we delay either line, we can block the server,
   since the server is single threaded. What happens
   if more clients connect to the server while it is
   blocked?
<p>
   Also, try connecting this client to EchoServer_v1.java,
   which only expects one line from each client. What happens?
<p>
   This client defaults to the localhost IP address with
   port number 5000. The remote host and port numbers
   can be changed using command-line arguments.

@see EchoServer_v2
*/
public class EchoClient_v2
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
      // Send the server the first request.
      final Scanner stdin = new Scanner(System.in);
      System.out.print("Type your first message for the server: ");
      final String message1 = stdin.nextLine();
      out.println(message1 + "  (from pid = " + pid + ")");
      out.flush(); // Make sure that the request is sent.

      System.out.println("CLIENT: Message sent to the server.");
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

      // Send the server a second request.
      System.out.print("Type your second message for the server: ");
      final String message2 = stdin.nextLine();
      out.println(message2 + "  (from pid = " + pid + ")");
      out.flush(); // Make sure that the request is sent.

      System.out.println("CLIENT: Message sent to the server.");
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
   private EchoClient_v2() {
      throw new AssertionError();
   }
}
