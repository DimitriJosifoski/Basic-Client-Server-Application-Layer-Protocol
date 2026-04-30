/*

*/
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client_closes_in_v2
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

      // Close the input stream from the socket.
      try
      {
         //in.close();  // Wrong!
         socket.shutdownInput();
         System.out.println("CLIENT: Client's socket input stream closed.");
      }
      catch (IOException e)
      {
         System.out.println("CLIENT: Cannot close client's socket input stream.");
         System.out.println( e );
      }

      // Send the server multiple requests.
      Scanner stdin = new Scanner(System.in);
      int count = 0;
      while ( stdin.hasNextLine() )
      {
         ++count;
         String rqst = stdin.nextLine();
         out.println( rqst );
         out.flush();
         System.out.println("CLIENT: Message " + count + " sent to server.");
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
