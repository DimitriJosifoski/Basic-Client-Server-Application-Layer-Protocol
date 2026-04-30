/*

*/

import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.util.Scanner;

/**
   This server implements a text protocol that
   is similar to the purely binary protocol
   from NumericServer.java.
<p>
   This server expects a text encoded integer from
   the client.
<p>
   Then this server sends back to the client that many
   text encoded doubles with the doubles separated by
   a "white space" character.
<p>
   Then this server closes its connection to the client.
<p>
   Notice that this server sends UTF-16BE encoded text to
   the client and expects ASCII encoded text in return.
<p>
   Try running this client/server pair with mixed up
   character encodings on one of the streams. Try
   "UTF-16" on one end of a stream and "ASCII" on
   the other end of that stream.
<p>
   This server defaults to running on port 5000. This server's
   port number can be changed using a command-line argument.

@see NumericTextClient
@see NumericServer
*/
public class NumericTextServer
{
   public static final int SERVER_PORT = 5000; // Should be above 1023.

   /**
      @param args  one optional command-line argument, the port number
   */
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
         serverSocket = new ServerSocket(portNumber); // Steps 1, 2, 3.
         System.out.println("SERVER: online");
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
         OutputStreamWriter out = null;

         // Wait for an incoming client request.
         try
         {
            socket = serverSocket.accept(); // Steps 4, 7.

            // At this point, a client connection has been made.
            in = new Scanner(                    // Step 9.
                        socket.getInputStream(), // Step 8.
                        "ASCII");                // Try UTF-16.

            out = new OutputStreamWriter(          // Step 9.
                         socket.getOutputStream(), // Step 8.
                         "UTF-16BE");              // Try ASCII.
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

         // Step 9.
         // Implement an application layer protocol.
         // Read the client's request and echo it.
         try
         {
            // Wait for the client's request.
            int request = 0; // default request value
            try
            {
               request = in.nextInt(); // Text encoded integer.
            }
            catch (java.util.InputMismatchException e)
            {
               System.out.println("Server: Cannot parse integer from client.");
               System.out.println( e );
            }
            catch (java.util.NoSuchElementException e)
            {
               System.out.println("Server: Missing integer from client.");
               System.out.println( e );
            }

            // Log the request to the server's console.
            System.out.println("SERVER: Client " + clientCounter
                             + ": Client requested " + request + " doubles.");

            // Send the requested number of doubles back to the client.
            for (int i = 0; i < request; ++i)
            {
               final double response = Math.random();
               // Log the double to the server's console.
               System.out.println(response);
               // Send the double back to the client.
               out.write( Double.toString(response) ); // Text encoded double.
               out.write( " " ); // Put white space between the doubles.
            }
            out.flush(); // Make sure that the response is sent.

            // Step 10.
            // We are done with the client's request.
            socket.close();
            // Step 11.
            socket = null;

            System.out.println("SERVER: Client " + clientCounter
                             + ": Closed socket.");
         }
         catch (IOException e)
         {
            System.out.println("SERVER: Error communicating with client (Client no. " + clientCounter + ")");
            System.out.println( e );
         }
      }// Step 12 (go to Step 4 and accept another client connection).
   }


   // Private default constructor to enforce noninstantiable class.
   // See Item 4 in "Effective Java", 3rd Ed, Joshua Bloch.
   private NumericTextServer() {
      throw new AssertionError();
   }
}
