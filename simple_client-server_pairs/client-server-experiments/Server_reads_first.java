/*

*/
import java.net.*;
import java.io.*;

public class Server_reads_first
{
   public static final int SERVER_PORT = 5000; // Should be above 1023.

   public static void main (String[] args)
   {
      int clientCounter = 0;

      // Get the name and IP address of the local host and
      // print them on the console for information purposes.
      try
      {
         final InetAddress address = InetAddress.getLocalHost();
         System.out.println("SERVER Hostname: " + address.getCanonicalHostName() );
         System.out.println("SERVER IP address: " +address.getHostAddress() );
         System.out.println("SERVER Using port no. " + SERVER_PORT);
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
         serverSocket = new ServerSocket(SERVER_PORT);
         System.out.println("SERVER online:");
      }
      catch (IOException e)
      {
         System.out.println("SERVER: Error creating network connection.");
         System.out.println( e );
         System.exit(-1);
      }

      while (true) // Serve multiple clients.
      {
         Socket         socket = null;
         BufferedReader in = null;
         PrintWriter    out = null;

         // Wait for an incoming client request.
         try
         {
            socket = serverSocket.accept();

            // At this point, a client connection has been made.
            in = new BufferedReader(
                     new InputStreamReader(
                          socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream());
         }
         catch(IOException e)
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

         System.out.println("SERVER: Client " + clientCounter + ": Waiting for client's message.");

         // Read the client's message.
         String message = null;
         try
         {
            message = in.readLine();
            System.out.println("SERVER: Client " + clientCounter + ": Message received: \"" + message + "\"");
         }
         catch (IOException e)
         {
            System.out.println("SERVER: Error communicating with client (Client no. " + clientCounter + ")");
            System.out.println( e );
         }

         // Send the client a response.
         out.println("You are client " + clientCounter + ": " + message);
         out.flush();    // Make sure that the response is sent.
         System.out.println("SERVER: Client " + clientCounter + ": Message sent to client.");

         try
         {
            socket.close(); // We are done with the client's request.
            System.out.println("SERVER: Client " + clientCounter + ": Closed socket.");
         }
         catch (IOException e)
         {
            System.out.println("SERVER: Error closing communicating socket (Client no. " + clientCounter + ")");
            System.out.println( e );
         }
      }
   }
}
