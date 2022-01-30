import java.net.*;
import java.io.*;
import java.util.*;

public class countServer
{
    public static void main(String[] args) throws IOException {

        //creates server socket
         int PORT = 5001;
        ServerSocket serverSocket = new ServerSocket(PORT);

        System.out.println("Server started...");
        System.out.println("Wating for clients...");

        //creats a cache on server start
        countCache cache = new countCache();

        //while the server is running
        while (true) {

            //accept clients
            Socket clientSocket = serverSocket.accept();

            //creates thread

            Thread t = new Thread() {
                public void run() {

                    try (
                            //creates the output writer, writes to client
                            PrintWriter out =
                                    new PrintWriter(clientSocket.getOutputStream(), true);
                            //creates the reader, reads from client
                            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(clientSocket.getInputStream()));
                    ) {
                        String inputLine, outputLine;

                        // Initiate conversation with client
                        countProtocol cp = new countProtocol();
                        outputLine = cp.processInput(null,cache);
                        out.println(outputLine);

                        //while the client has something to say
                        while ((inputLine = in.readLine()) != null) {
                            //take input and handle conversation
                            outputLine = cp.processInput(inputLine,cache);

                            //send server side coversation to client
                            out.println(outputLine);
                        }

                    } catch (IOException e) { }
                }
            };
            // start threads
            t.start();
        }
    }
}

