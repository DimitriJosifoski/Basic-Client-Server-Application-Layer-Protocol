/*

*/
import java.net.*;
import java.io.*;

public class Client_writes_first
{
   public static final int SERVER_PORT = 5000; // Should be above 1023.

   public static void main (String[] args)
   {
      Socket         socket = null;
      BufferedReader in = null;
      PrintWriter    out = null;

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
      // to identify the client in the server's transcrip.
      final ProcessHandle handle = ProcessHandle.current();
      final long pid = handle.pid();
      System.out.println("CLIENT: Process ID number (PID): " + pid );

      // Make a connection to the server.
      try
      {
         System.out.println("CLIENT: Connecting to server: " + hostName + " on port " + portNumber );
         socket = new Socket(InetAddress.getByName(hostName), portNumber);

         in = new BufferedReader(
                  new InputStreamReader(
                       socket.getInputStream()));
         out = new PrintWriter(socket.getOutputStream());
      }
      catch (IOException e)
      {
         System.out.println("CLIENT: Cannot connect to server.");
         System.out.println( e );
         System.exit(-1);
      }
      System.out.println("CLIENT: Connected to server.");
      // Get this client's local port number and log it to the console.
      // This helps to identify this client in the server's transcript.
      final int port = socket.getLocalPort();
      System.out.println("CLIENT: Local Port: " + port);


      // Send the server a message.
      out.println("Message from client with pid = " + pid + ".");
      out.flush();
      System.out.println("CLIENT: Message sent to server.");

      System.out.println("CLIENT: Waiting for server's response.");

      // Read the server's response.
      try
      {
         final String response = in.readLine();
         System.out.println("CLIENT: Server response is: \"" + response + "\"");
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
