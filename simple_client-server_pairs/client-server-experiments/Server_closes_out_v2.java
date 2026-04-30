/*

*/
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Server_closes_out_v2
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

         // Close the output stream from the socket.
         try
         {
            //out.close();  // Wrong!
            socket.shutdownOutput();
            System.out.println("SERVER: Client " + clientCounter + ": server's socket output stream closed.");
         }
         catch (IOException e)
         {
            System.out.println("SERVER: Cannot close server's socket output stream.");
            System.out.println( e );
         }

         // Read the client's request but let stdin block this server.
         Scanner stdin = new Scanner(System.in);
         System.out.println("SERVER: Client " + clientCounter + ": Waiting for client's request message.");
         try
         {
            // Handle the client's request.
            final String request = in.readLine();
            System.out.println("SERVER: Client " + clientCounter + ": Message received: \"" + request + "\"");
            int count = 1;
            boolean eof = false;
            while (!eof)
            {
               System.out.println("Type a response to unblock this server and read 1000 requests:");
               stdin.nextLine();
               for (int i = 0; i < 1000 && !eof; ++i)
               {
                  ++count;
                  final String rqst = in.readLine();
                  System.out.println("SERVER: Client " + clientCounter + ": Message " + count + " Received.");
                  if (null == rqst) eof = true;
               }
            }
            socket.close(); // We are done with the client's request.
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
