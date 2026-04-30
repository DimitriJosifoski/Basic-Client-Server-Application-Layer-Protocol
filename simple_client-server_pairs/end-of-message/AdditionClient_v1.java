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
   This client uses end-of-stream to signal to
   the server that the client's request is complete.
<p>
   This version of the client, <br>
     1) sends a sequence of integers to the server, <br>
     2) closes its output stream so that the server
        sees an end-of-stream condition, <br>
     3) receives back from the server the sum of the sequence, <br>
     4) closes the connection to the server.
<p>
   Each integer value is sent to the server as a string of
   decimal digits with a single space after the last digit.
   The spaces let the server properly parse the integer values.
<p>
   This client expects the server's response to be a single line
   of text containing one integer value.
<p>
   Since this client closes it output stream to the server
   at the end of its request, this client can send only
   a single request to the server.
<p>
   This client defaults to server port number 5000.
*/
public class AdditionClient_v1
{
   public static final int SERVER_PORT = 5000; // Should be above 1023.

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

      // Get the name and IP address of the local host and
      // print them on the console for information purposes.
      try
      {
         final InetAddress address = InetAddress.getLocalHost();
         System.out.println("CLIENT: Hostname: " + address.getCanonicalHostName() );
         System.out.println("CLIENT: IP address: " +address.getHostAddress() );
      }
      catch (UnknownHostException e)
      {
         System.out.println("Unable to determine this client's address.");
         System.out.println( e );
      }

      // Make a connection to the server.
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


      // Step 9 - The application level protocol.
      // Send the server a (single) request.
      final Scanner stdin = new Scanner(System.in);

      String request = "";   // Used for logging the request.
      while ( stdin.hasNextInt() )
      {
         final int n = stdin.nextInt();
         out.print(n);    // Send each int as a text string.
         out.print(" ");  // Separate all integers with a space.
         out.flush();
         request += n + " ";
      }
      System.out.println("CLIENT: Request is: " + request); // Log the request.
      System.out.println("CLIENT: Request sent to the server.");

      // Tell the server that there's no more data.
      try
      {
         //out.close(); // WRONG!
         socket.shutdownOutput(); // Send end-of-stream to the server.
      }
      catch (IOException e)
      {
         System.out.println("CLIENT: Cannot close output to server.");
         System.out.println( e );
      }

      // Receive the server's response.
      try
      {
         final String response = in.readLine();
         final int sum = Integer.parseInt(response.trim());
         System.out.println("CLIENT: Server response is: sum = " + sum);
      }
      catch(IOException e)
      {
         System.out.println("CLIENT: Cannot receive response from server.");
         System.out.println( e );
      }

      // Disconnect from the server.
      try
      {
         socket.close();
         System.out.println("CLIENT: Closed socket.");
      }
      catch (IOException e)
      {
         System.out.println("CLIENT: Cannot disconnect from server.");
         System.out.println( e );
      }
   }
}
