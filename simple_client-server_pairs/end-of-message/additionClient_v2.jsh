var remoteHost = "localhost"
var remotePort = 5000
var ipAddress = InetAddress.getByName(remoteHost);
System.out.println("CLIENT: Connecting to server: " + remoteHost
                 + " on port " + remotePort );
try (var socket = new Socket(ipAddress, remotePort);        // Steps 5, 6, 7.
     var in = new Scanner(socket.getInputStream());         // Step 9 and 8.
     var out = new PrintWriter(socket.getOutputStream())) { // Step 9 and 8.
   System.out.println("CLIENT: Connected to server.");
   var localPort = socket.getLocalPort();
   System.out.println("CLIENT: Local Port: " + localPort);
   // Step 9. Send the server multiple integer sequences.
   var stdin = new Scanner(System.in);
   while (stdin.hasNextInt()) {
      var length = stdin.nextInt();
      out.print(length + " ");
      out.flush();
      String request = length + " "; // For logging the request.
      for (int i = 0; i < length; ++i) {
         var n = stdin.nextInt();
         out.print(n + " ");
         out.flush();
         request += n + " ";  // For logging the request.
      }
      System.out.println("CLIENT: Request is: " + request);
      System.out.println("CLIENT: Request sent to the server.");
      var response = in.nextInt();
      System.out.println("CLIENT: Server response is: sum = " + response);
   }
} // Step 10.
