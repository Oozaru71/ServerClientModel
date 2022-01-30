import java.net.*;
import java.io.*;
import java.util.*;
public class countClient {
    public static void main(String[] args) throws IOException {
        //Intialize some port on localhost
        final String HOST = "127.0.0.1";
        final int PORT = 5001;

        System.out.println("Client started.");

        try (
                //try to create and connect to a socket on the host
                Socket socket = new Socket(HOST, PORT);

                //create writer for the socket sttream

                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                //create reader from server

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
        ) {

            //create input reader

            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String fromServer = " ";
            String fromUser;

            //show options

            System.out.println("--Options--\n"
                    +"----Be sure to follow the following syntax: function yourFileName.txt--- \n"+
                    "----The server does not support spaces in the name of the file.--- \n"+
                    "----For cloning the system follows the follows the same format of this example: clone C:\\\\Users\\\\mike\\\\Desktop\\\\updates.txt--- \n"+
                    "Functions: \n"
                    + " Create  (creates a brand new, empty file of the given name in the datasource). \n" +
                    " Update  (update the contents of the existing file). \n" +
                    " Remove (delete the contents and reference to the existing " +
                    "file). \n" +
                    " List  (a directory lookup command, IE. return all file names " +
                    "present on the data tier). \n" +
                    " Read (read the entire contents of the given file from the data " +
                    "tier)\n"+
                    " Clone (creates a copy of the given file on the datasource)");

            //while the server has somethign to say stay alive


            while (true ) {

                // prints server side of the conversation
                fromServer= in.readLine();


                // exits on server bye
                if(fromServer.equalsIgnoreCase("Bye.")){
                    System.out.println("Server: " + fromServer);
                    System.exit(1);
                }
                else{
                    //this part of the codes makes sure that if a string is passed with these special symbols,
                    //it gets split into an array and then printed one by one insted of one string
                    String strArr[]=fromServer.split("~!@");
                    System.out.println("Server: " + strArr[0]);
                    for(int i=1;i<strArr.length;i++)
                    {
                        if(fromServer.indexOf("~!@") >= 0)
                        {
                            System.out.println("Server: " + strArr[i]);
                        }
                    }
                }


                //takes user input
                fromUser = stdIn.readLine();
                if (fromUser != null) {
                    //shows user input and sends to ouputstream
                    System.out.println("Client: " + fromUser);
                    out.println(fromUser);
                }
            }


        }
    }
}

