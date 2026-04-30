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
     1) Send the server a positive integer indicating the
        number of integer sequences that will follow.
     2) Send the server a positive integer indicating
        the length of a sequence of integer values.
     3) Send the server a sequence of integers with the specified length.
     4) Receive back from the server the sum of the sequence.
     5) If not the last sequence, then go back to step 2.
     6) Close the connection to the server.
<p>
   Each integer value is sent to the server as a string of
   decimal digits with a single space after the last digit.
   The spaces let the server properly parse the integer values.
<p>
   This client defaults to server port number 5003.
*/
public class AdditionClient_v3_Hw4
{
   public static final int SERVER_PORT = 5003; // Should be above 1023.

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

      System.out.println("AdditionClient_v3_Hw4");
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

      final int numberOfRequests = stdin.nextInt();
      out.print(numberOfRequests); // Send the number of requests to the server.
      out.print(" ");              // Separate all integers with a space.
      out.flush();
      for (int i = 0; i < numberOfRequests; ++i)
      {
         // Send the server a request.
         final int seqLength = stdin.nextInt(); // Should be a non-negative value.
         out.println(seqLength);                // Send the sequence length to the server.
         out.flush();                           // Separate all integers with a space.
         String request = seqLength + " ";      // Used for logging the request.
         for (int j = 0; j < seqLength; ++j)    // Should have seqLength integers to read.
         {
            final int n = stdin.nextInt();  // Data value.
            out.print(n);    // Send each int as a text string.
            out.print(" ");  // Separate all integers with a space.
            out.flush();
            request += n + " ";
         }
         System.out.println("CLIENT: Request is: " + request); // Log the request.

         // Receive the server's response.
         final int sum = in.nextInt();
         System.out.println("CLIENT: Server response is: sum = " + sum);
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
