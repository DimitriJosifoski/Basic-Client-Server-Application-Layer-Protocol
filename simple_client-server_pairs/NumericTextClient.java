/*

*/

import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.util.Scanner;

/**
   This client implements a text protocol that
   is similar to the purely binary protocol
   from NumericClient.java.
<p>
   This client sends to the server an integer value
   as a text string that is terminated with a white
   space character.
<p>
   Then this client expects back from the server that
   many doubles, each double represented as a text string,
   with "white space" characters between the doubles.
<p>
   Then this client closes its connection to the server.
<p>
   Notice that this client sends ASCII encoded text to
   the server and expects UTF-16BE encoded text in return.
<p>
   Try running this client/server pair with mixed up
   character encodings on one of the streams. Try
   "UTF-16" on one end of a stream and "ASCII" on
   the other end of that stream.
<p>
   This client defaults to the localhost IP address with
   port number 5000. The remote host and port numbers
   can be changed using command-line arguments.

@see NumericTextServer
@see NumericClient
*/
public class NumericTextClient
{
   public static final int SERVER_PORT = 5000; // Should be above 1023.

   /**
      @param args  two optional command-line arguments, hostname and port number
   */
   public static void main (String[] args)
   {
      Socket          socket = null;
      Scanner             in = null;
      OutputStreamWriter out = null;

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

         in = new Scanner(                    // Step 9.
                     socket.getInputStream(), // Step 8.
                     "UTF-16BE");             // Try ASCII.

         out = new OutputStreamWriter(          // Step 9.
                      socket.getOutputStream(), // Step 8.
                      "ASCII");                 // Try UTF-16.
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
      // Send the server a request.
      final Scanner stdin = new Scanner(System.in);
      System.out.print("How many doubles do you want from the server: ");
      final int request = stdin.nextInt();
      try
      {
         out.write( Integer.toString(request) ); // Text encoded integer.
         out.write( "\n" ); // Terminate the integer string with white space.
         out.flush(); // Make sure that the request is sent.
      }
      catch (IOException e)
      {
         System.out.println("CLIENT: Cannot send request to server.");
         //socket.close();  // should close the socket
         e.printStackTrace();
         System.exit(-1);
      }

      System.out.println("CLIENT: Request sent to the server.");

      // Receive the doubles from the server.
      for (int i = 0; i < request; ++i)
      {
         try
         {
            final double response = in.nextDouble(); // Text encoded double.
            // Log the double to the client's console.
            System.out.printf("CLIENT: Server response %d is: %f\n", i, response);
         }
         catch (java.util.InputMismatchException e)
         {
            System.out.println("CLIENT: Cannot parse double from server.");
            System.out.println( e );
         }
         catch (java.util.NoSuchElementException e)
         {
            System.out.println("CLIENT: Missing double from server.");
            System.out.println( e );
         }
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
   private NumericTextClient() {
      throw new AssertionError();
   }
}
