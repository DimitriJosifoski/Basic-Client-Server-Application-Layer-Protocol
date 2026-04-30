/*

*/

import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
   This server implements a purely binary protocol that
   expects an int value from the client and then sends
   that many double values back to the client.
<p>
   This server expects a binary, four byte, integer
   from the client.
<p>
   Then this server sends back to the client that many
   binary, eight byte, doubles.
<p>
   Then this server closes its connection to the client.
<p>
   Try connecting to this server using EchoClient_v1b.java.
   That is a text based client but it will be communicating
   with a binary server. The results in the client's console
   will be all garbled.
<p>
   This server defaults to running on port 5000. This server's
   port number can be changed using a command-line argument.

@see NumericClient
*/
public class NumericServer
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
         DataInputStream in = null;
         DataOutputStream out = null;

         // Wait for an incoming client request.
         try
         {
            socket = serverSocket.accept(); // Steps 4, 7.

            // At this point, a client connection has been made.
            in = new DataInputStream(             // Step 9.
                        socket.getInputStream()); // Step 8.

            out = new DataOutputStream(             // Step 9.
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

         // Step 9.
         // Implement an application layer protocol.
         // Read the client's request and echo it.
         try
         {
            // Wait for the client's request.
            final int request = in.readInt();

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
               out.writeDouble(response);
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
   private NumericServer() {
      throw new AssertionError();
   }
}
