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
            var length = in.nextInt();
            System.out.println("SERVER: Client " + clientCounter
                             + ": Expecting sequence of length " + length + ".");
            System.out.print("SERVER: Client " + clientCounter + ": Sequence is: ");
            var sum = 0;
            for (int i = 0; i < length && in.hasNextInt(); ++i) {
               var n = in.nextInt();
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
