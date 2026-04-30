/*

*/

import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Scanner;

/**
   This server uses a counter to detect
   the end of the client's request.
<p>
   For each client, this server, <br>
      1) reads a positive integer indicating the
         number of integer values to expect, <br>
      2) reads the specified number of integer values,
         one value per line of text, <br>
      3) sends back the sum of the integers as a text string, <br>
      4) if not end-of-stream, go back to step 1, <br>
      5) closes the connection to the client.
<p>
   Each integer value from the client is read as a string of
   decimal digits followed by white space. This server does
   not make any assumption about what kind of white space
   separates the integers values.
<p>
   This server defaults to port number 5000.
*/
public class AdditionServer_v2
{
   public static final int SERVER_PORT = 5000; // Should be above 1023.

   public static void main (String[] args)
   {
      final int portNumber;
      if (args.length > 0)
      {
         portNumber = Integer.parseInt(args[0]);
      }
      else
      {
         portNumber = SERVER_PORT;
      }

      System.out.println("AdditionServer_v2");

      int clientCounter = 0;

      // Get this server's process id number (PID). This helps
      // to identify the server in TaskManager or TCPView.
      final ProcessHandle handle = ProcessHandle.current();
      final long pid = handle.pid();
      System.out.println("SERVER: Process ID number (PID): " + pid );

      // Get the name and IP address of the local host and
      // print them on the console for information purposes.
      try
      {
         final InetAddress address = InetAddress.getLocalHost();
         System.out.println("SERVER: Hostname: " + address.getCanonicalHostName() );
         System.out.println("SERVER: IP address: " +address.getHostAddress() );
         System.out.println("SERVER: Using port no. " + portNumber);
      }
      catch (UnknownHostException e)
      {
         System.out.println("Unable to determine this host's address.");
         System.out.println( e );
      }

      // Create the server's listening socket.
      ServerSocket serverSocket = null;
      try
      {
         serverSocket = new ServerSocket(portNumber); // Steps 1, 2.
         System.out.println("SERVER online:");
      }
      catch (IOException e)
      {
         System.out.println("SERVER: Error creating network connection.");
         e.printStackTrace();
         System.exit(-1);
      }

      while (true) // Serve multiple clients.
      {
         Socket socket = null;
         Scanner in = null;
         PrintWriter out = null;

         // Wait for an incoming client request.
         try
         {
            socket = serverSocket.accept(); // Steps 4, 7.

            // At this point, a client connection has been made.
            in = new Scanner(                       // Step 9.
                         socket.getInputStream());  // Step 8.

            out = new PrintWriter(                  // Step 9.
                         socket.getOutputStream()); // Step 8.
         }
         catch (IOException e)
         {
            System.out.println("SERVER: Error connecting to client.");
            System.out.println( e );
         }

         ++clientCounter;
         // Get the client's IP address and port and log them to the console.
         final InetAddress clientIP = socket.getInetAddress();
         final int clientPort = socket.getPort();
         System.out.println("SERVER: Client " + clientCounter
                          + ": IP: " +  clientIP.getHostAddress()
                          + ", Port: " + clientPort);


         // Step 9 - The application level protocol.
         // Read all of the client's requests.
         try
         {
            while (in.hasNextInt()) // While not end-of-stream.
            {
               // Read the client's sequence of integer values,
               // sum them, and send back the sum.
               final int seqLength = in.nextInt();
               System.out.println("SERVER: Client " + clientCounter
                                + ": Expecting sequence of length " + seqLength + ".");
               System.out.print("SERVER: Client " + clientCounter + ": Sequence is: "); // Log the sequence.
               int sum = 0;
               for (int i = 0; i < seqLength; ++i)
               {
                  if (! in.hasNextInt())
                  {
                     System.out.print("Premature end-of-stream.");
                     break;
                  }
                  final int n = in.nextInt();
                  sum += n;
                  System.out.print(n + " ");  // Log the sequence.
               }
               System.out.println();
               System.out.println("SERVER: Client " + clientCounter + ": Message received: sum = " + sum);
               out.println(sum); // Send the sum as a text string.
               out.flush();      // Now make sure that the response is sent.
            }
            socket.close();   // We are done with this client's requests.
            System.out.println("SERVER: Client " + clientCounter + ": Closed socket.");
         }
         catch (IOException e)
         {
            System.out.println("SERVER: Error communicating with client (Client no. " + clientCounter + ")");
            System.out.println( e );
         }
      }
   }
}
