/*
   Do not modify this file.
*/

import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Scanner;

/**
   This client will,
     1) Either send the server a negative integer and go to step 6,
        or go to step 2.
     2) Send the server a sequence of positive integers.
     3) Send the server a negative integer to end the sequence.
     4) Receive back from the server the sum of the sequence.
     5) Go to step 1.
     6) Close the connection to the server.
<p>
   Each integer value is sent to the server as a string of
   decimal digits with a single space after the last digit.
   The spaces let the server properly parse the integer values.
<p>
   This client defaults to server port number 5002.
*/
public class AdditionClient_v2_Hw4
{
   public static final int SERVER_PORT = 5002; // Should be above 1023.

   public static void main (String[] args)
   {
      Socket      socket = null;
      Scanner     in = null;
      PrintWriter out = null;

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

      System.out.println("AdditionClient_v2_Hw4");
      // Get this client's process id number (PID). This helps
      // to identify the client in the server's transcript.
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

      // Make a connection to the server
      try
      {
         System.out.println("CLIENT: Connecting to server: " + hostName
                          + " on port " + portNumber );

         socket = new Socket(InetAddress.getByName(hostName), portNumber);

         in = new Scanner(socket.getInputStream());

         out = new PrintWriter(socket.getOutputStream());
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


      // Implement the appropriate client/server application layer protocol.
      final Scanner stdin = new Scanner(System.in);

      int n;  // Data value.
      while ((n = stdin.nextInt()) >= 0)
      {
         String request = n + " ";   // Used for logging the request.
         // Send the server a request.
         out.print(n);    // Send first int as a text string.
         out.print(" ");  // Separate all integers with a space.
         out.flush();
         while ((n = stdin.nextInt()) >= 0)
         {
            out.print(n);    // Send each int as a text string.
            out.print(" ");  // Separate all integers with a space.
            out.flush();
            request += n + " ";
         }
         out.print(n);   // Send a negative sentinel to end the sequence.
         out.print(" ");  // Separate all integers with a space.
         out.flush();
         System.out.println("CLIENT: Request is: " + request + n); // Log the request.

         // Receive the server's response.
         final int sum = in.nextInt();
         System.out.println("CLIENT: Server response is: sum = " + sum);
      }
      out.print(n);    // Send a negaitve sentinel to end the requests.
      out.print(" ");  // Separate all integers with a space.
      out.flush();
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
