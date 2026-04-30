var clientCounter = 0
try (var serverSocket = new ServerSocket(5000)) { // Steps 1, 2.
   System.out.println("SERVER online:");
   while (true) {
      try (var socket = serverSocket.accept();                    // Steps 4, 7.
           var in = new Scanner(socket.getInputStream());         // Step 9 and 8.
           var out = new PrintWriter(socket.getOutputStream())) { // Step 9 and 8.
         ++clientCounter;
         var clientIP = socket.getInetAddress();
         var clientPort = socket.getPort();
         System.out.println("SERVER: Client " + clientCounter
                          + ": IP: " +  clientIP.getHostAddress()
                          + ", Port: " + clientPort);
         // Step 8. Read each sequence of integers, sum them, send back the sum.
         while (in.hasNextInt()) {
            System.out.print("SERVER: Client " + clientCounter + ": Sequence is: ");
            var sum = 0;
            var n = -1;
            while (in.hasNextInt() && (n = in.nextInt()) > 0 ) {
               sum += n;
               System.out.print(n + " "); // Log the sequence.
            }
            System.out.println("\nSERVER: Client " + clientCounter + ": Message received: sum = " + sum);
            out.println(sum);
            out.flush();
         }
      } // Step 10.
      System.out.println("SERVER: Client " + clientCounter + ": Closed socket.");
   }
}
