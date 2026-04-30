/*

*/

import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;

/**
   This is an echo server that echoes just one line of text.
<p>
   This server expects one line of input from the client.
   This single line of input is echoed back to the client
   and then the client connection is closed by this server.
   The server then waits for another client connection.
<p>
   Because this server is single threaded, it can only process
   input from one client at a time. While this server is waiting
   for a client's line of input, any new client that connects to
   this server is made to wait on this server's ServerSocket.
<p>
   To observe this server's listening socket queue up new clients,
   start a client, but don't type in any input to the client, and
   then start one or two more clients. Then go back to the first
   client and enter its input.
<p>
   This server defaults to running on port 5000. This server's
   port number can be changed using a command-line argument.

@see EchoClient_v1a
@see EchoClient_v1b
*/
public class EchoServer_v1
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

      // Block the server to test the "backlog" queue.
      //new java.util.Scanner(System.in).nextLine();

      while (true) // Serve multiple clients.
      {
         Socket socket = null;
         BufferedReader in = null;
         PrintWriter out = null;

         // Wait for an incoming client request.
         try
         {
            socket = serverSocket.accept(); // Steps 4, 7.

            // At this point, a client connection has been made.
            in = new BufferedReader(
                     new InputStreamReader(          // Step 9.
                          socket.getInputStream())); // Step 8.

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

         // Step 9.
         // Implement an application layer protocol.
         // Read the client's request and echo it.
         try
         {
            // Wait for the client's request.
            final String request = in.readLine();

            // Log the request to the server's console.
            System.out.println("SERVER: Client " + clientCounter
                             + ": Message received: \"" + request + "\"");

            // Echo the request back to the client.
            out.println("You are client " + clientCounter + ": " + request);
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
   private EchoServer_v1() {
      throw new AssertionError();
   }
}
