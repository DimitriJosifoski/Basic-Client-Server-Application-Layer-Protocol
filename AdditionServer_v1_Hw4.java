/*
   Course: CS 33600
   Name: Dimitri Josifoski
   Email: djosifos@purdue.edu
   Assignment: 4
*/

import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * For each client, this server will,
 * 1) Read an integer.
 * If the integer is negative,
 * then close the connection to the client,
 * otherwise read the specified number of integer values.
 * 2) Send back the sum of the sequence as a text string.
 * 3) Go back to step 1.
 * <p>
 * Each integer value from the client is read as a string of
 * decimal digits followed by white space. The server should
 * not make any assumption about what kind of white space
 * separates the integers values.
 * <p>
 * This server defaults to port number 5001.
 */
public class AdditionServer_v1_Hw4 {
   public static final int SERVER_PORT = 5001; // Should be above 1023.

   public static void main(String[] args) {
      final int portNumber;
      if (args.length > 0) {
         portNumber = Integer.parseInt(args[0]);
      } else {
         portNumber = SERVER_PORT;
      }

      int clientCounter = 0;

      System.out.println("AdditionServer_v1_Hw4");
      // Get this server's process id number (PID). This helps
      // to identify the server in TaskManager or TCPView.
      final ProcessHandle handle = ProcessHandle.current();
      final long pid = handle.pid();
      System.out.println("SERVER: Process ID number (PID): " + pid);

      // Get the name and IP address of the local host and
      // print them on the console for information purposes.
      try {
         final InetAddress address = InetAddress.getLocalHost();
         System.out.println("SERVER Hostname: " + address.getCanonicalHostName());
         System.out.println("SERVER IP address: " + address.getHostAddress());
         System.out.println("SERVER Using port no. " + portNumber);
      } catch (UnknownHostException e) {
         System.out.println("Unable to determine this host's address.");
         System.out.println(e);
      }

      // Create the server's listening socket.
      ServerSocket serverSocket = null;
      try {
         serverSocket = new ServerSocket(portNumber);
         System.out.println("SERVER online:");
      } catch (IOException e) {
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
         try {
            socket = serverSocket.accept();

            // At this point, a client connection has been made.
            in = new Scanner(socket.getInputStream());

            out = new PrintWriter(socket.getOutputStream());
         } catch (IOException e) {
            System.out.println("SERVER: Error connecting to client");
            System.out.println(e);
         }

         ++clientCounter;
         // Get the client's IP address and port and log them to the console.
         final InetAddress clientIP = socket.getInetAddress();
         final int clientPort = socket.getPort();
         System.out.println("SERVER: Client " + clientCounter
               + ": IP: " + clientIP.getHostAddress()
               + ", Port: " + clientPort);

         // Implement the appropriate client/server application layer protocol.
         try {
            int seqLength;
            while (in.hasNextInt()
                  && (seqLength = in.nextInt()) >= 0) {
               System.out.println("SERVER: Client " + clientCounter
                     + ": Expecting sequence of length " + seqLength + ".");
               System.out.print("SERVER: Client " + clientCounter + ": Sequence is: ");
               int sum = 0;
               for (int i = 0; i < seqLength; ++i) {
                  final int n = in.nextInt();
                  sum += n;
                  System.out.print(n + " ");
               }
               System.out.println();
               System.out.println("SERVER: Client " + clientCounter + ": Message received: sum = " + sum);
               out.println(sum);
               out.flush();
            }
            socket.close();
            System.out.println("SERVER: Client " + clientCounter + ": Closed socket.");
         } catch (IOException e) {
            System.out.println("SERVER: Error communicating with client (Client no. " + clientCounter + ")");
            System.out.println(e);
         }

      }
   }
}
