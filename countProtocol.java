    import java.io.*;
    import java.util.Scanner;
    import java.util.*;

    public class countProtocol {

        // states work as a way to tell the server in what part of the conversation they are in
        private static final int WAITING = 0;
        private static final int ASKEDNAME = 1;
        private static final int WAITUPDATE = 3;
        private static final int LETMECOUNT=4;

        //state begins as waiting
        private int state = WAITING;

        //datastore location
        private String root="C:\\Users\\Micheal\\Desktop\\Datasource\\";



        private String fileName="";

        private String welcome="What would you like to do? ";

        //update is used to make sure an update file gets cachced as a new object

        private Boolean update=false;

        //used to properly count lines
        private int iHoldLines=0;

        //takes the input from the user as well as the cache created by the server

        public String processInput(String theInput, countCache cache) {

        String theOutput="";

        //reaction from server to a client connecting
        if (state==WAITING) {
            theOutput = welcome;
            state=ASKEDNAME;
        }

        //C:\\Users\\Micheal\\Desktop\\updates.txt

        //reaction to client inputting some functon
        else if(state==ASKEDNAME){
            String command="";
            //input verification to make sure command formate is followed
            if (!(theInput.toUpperCase().contains("LIST"))&&(!theInput.toUpperCase().contains("CREATE")&&(!theInput.toUpperCase().contains("READ"))
                   &&(!theInput.toUpperCase().contains("UPDATE"))&&(!theInput.toUpperCase().contains("REMOVE")&&(!theInput.toUpperCase().contains("BYE"))
                    &&(!theInput.toUpperCase().contains("CLONE"))))){
                command="";
            }
            //input verification for special case
            else if(theInput.equalsIgnoreCase("list")){
                command="List";
            }
            //onput verification to bye request
            else if(theInput.equalsIgnoreCase("bye")){
                command="bye";
            }
            else  {
                //if the input follows the function format and it concaints proper file and and function
                //then the input is split in two
                try {
                    String[] input = theInput.split(" ");
                    fileName = input[1];
                    // there was some problem with the input validation when inputting the clone
                    //path so the input validation needs to be a bit more lenient
                    if(!input[0].equalsIgnoreCase("clone")) {
                        if (fileName.contains(".txt")) {
                            command = input[0];
                        } else
                            command = "";
                    }
                    else
                        command = input[0];
                }
                catch (ArrayIndexOutOfBoundsException e)
                {
                    //if there were some case that passes the input verifcation and creates and issue on the array above
                    //then this is the last protection against an error
                    command="";
                }
            } 
            File f;
            //file object is created in java once before any function
            if(command.equalsIgnoreCase("clone"))
            {
                //allows you to create a clone with the same name as the src
                String [] split=fileName.split("\\\\");
                String splitF=split[split.length-1];
                f= new File(root+splitF);
            }
            else {
                f = new File(root + fileName);
            }

            if(command.equalsIgnoreCase("CREATE"))
            {
                try {

                    //file object is created in data store
                    if (f.createNewFile()) {
                        theOutput=fileName+" has been created";
                    } else {
                        //if it already exits then ...
                        theOutput="I already know this file so try updating it instead of storing it";
                    }

                }catch (IOException e){
                    System.out.println("Error");
                    e.printStackTrace();
                }
                theOutput+="  **Would you like to do something else?**";

            }
            else if(command.equalsIgnoreCase("update")){
                //if the file exits then it can be updated
                if((f.exists())) {
                    theOutput = "What would you like to write to the file?";
                    state=WAITUPDATE;

                }
                //if not then let the client know
                else
                    theOutput = "I can't update a file that doesn't exist, try storing it first. ";
            }
            else if(command.equalsIgnoreCase("remove")) {
                try
                {
                    //attempt to remove a file
                    if(f.delete())
                    {
                        theOutput+=fileName+ " deleted";
                    }
                    else
                    {
                        // if it does not exits then tell client
                        theOutput+="This file did not exist or could not be deleted";
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                theOutput+="  **Would you like to do something else?**";

            }

            else if(command.equalsIgnoreCase("list"))
            {
                //creates a new file to reach the data source directory
                File folder = new File(root);
                File[] listOfFiles = folder.listFiles();
                theOutput="The files in the Datasource are: ";
                //finds the names of the files in the directory
                for (int i = 0; i < listOfFiles.length; i++) {
                    if (listOfFiles[i].isFile()) {
                        theOutput += "~!@File:" + listOfFiles[i].getName()+"   ";
                    }
                }
                theOutput+="  **Would you like to do something else?**";

            }

            else if(command.equalsIgnoreCase("read")) {
                if((f.exists())) {
                    //if the file exits then count contents or read the,
                    theOutput+=" **What would you like to see (# of characters,# of lines, # of words, content)?**";
                    state=LETMECOUNT;
                }
                else
                    theOutput = "I can't count  a file that doesn't exist, try storing it first. ";



            }
            else if(command.equalsIgnoreCase("bye")){
                //out server by from client by input
                theOutput="Bye.";
            }
            else if(command.equalsIgnoreCase("clone"))
            {
                try {
                    //file object is created in data store
                    if (f.createNewFile()) {
                        theOutput="A clone of "+fileName+" has been created    ";

                        // temp file for the src

                        File tempFile= new File(fileName);
                        //new reader
                        try {
                            // new reader beings on wanted file
                            FileInputStream in = new FileInputStream(tempFile);
                            FileOutputStream out = new FileOutputStream(f);

                            try {

                                int n;

                                // read() function to read the
                                // byte of data
                                while ((n = in.read()) != -1) {
                                    // write() function to write
                                    // the byte of data
                                    out.write(n);
                                }
                            }
                            finally {
                                if (in != null) {

                                    // close() function to close the
                                    // stream
                                    in.close();
                                }
                                // close() function to close
                                // the stream
                                if (out != null) {
                                    out.close();
                                }
                            }
                        } catch (FileNotFoundException e) {
                            //catch non existent file
                            theOutput+="This file did not exist or could not be read so I left an empty copy in the datastore";
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        //if it already exits then ...
                        theOutput="I already know this file so try updating it instead of storing it";
                    }

                }catch (IOException e){
                    System.out.println("Error");
                    e.printStackTrace();
                }
                theOutput+="  **Would you like to do something else?**";
            }
            else
            {
                theOutput+="That was not a valid input, make sure the file ends in .txt or you used a valid function.Try again.";
            }


        }

        else if(state==LETMECOUNT)
        {
            //state set for counting, usually a state is set when a new input is needed
            //first checks if infor is in cache
            if((cache.get(fileName)!=null)&&(!update))
            {
                //reads from the cache
                theOutput="++ I am taking from the cache ++  ";
                String a=cache.get(fileName);
                String[] b=a.split(",");

                //outputs info from cache based on input string

                if (theInput.equalsIgnoreCase("lines")) {
                //gets lines
                    theOutput+=b[0];
                    state=ASKEDNAME;
                } else if (theInput.equalsIgnoreCase("words")) {
                    //gets words
                    theOutput+=b[1];
                    state=ASKEDNAME;
                } else if (theInput.equalsIgnoreCase("characters")) {
                    //gets characters
                    theOutput+=b[2];;
                    state=ASKEDNAME;
                }
                else if (theInput.equalsIgnoreCase("content")) {
                    //gets the actual input in the file
                    theOutput+=b[3];;
                    state=ASKEDNAME;
                }
                else
                {
                    theOutput+="That was not a valid input. Try inputting 'words','lines', 'characters', or 'content'. ";
                }
            }
            else {
                //if the file is not in the cache, it calls the stats functions and places the data in cache
                cache.put(fileName,stats());

                //completes the same function as if statemnt

                String a=cache.get(fileName);
                String[] b=a.split(",");
                theOutput="++ Placing in cache and taking from the cache ++";



                if (theInput.equalsIgnoreCase("lines")) {
                    theOutput+=b[0];
                    state=ASKEDNAME;
                } else if (theInput.equalsIgnoreCase("words")) {
                    theOutput+=b[1];
                    state=ASKEDNAME;
                } else if (theInput.equalsIgnoreCase("characters")) {
                    theOutput+=b[2];;
                    state=ASKEDNAME;
                }else if (theInput.equalsIgnoreCase("content")) {
                    theOutput+=b[3];;
                    state=ASKEDNAME;
                }
                else{
                    theOutput+="~!@That was not a valid input. Try inputting 'words','lines', 'characters', or 'content'.  ";
                }
                System.out.println(theOutput);
                update=false;

            }

            theOutput+="~!@ **Would you like to do something else?****";

        }

        else if(state==WAITUPDATE)
        {
            //crates new file object
            File file = new File(root+fileName);

            try {

                PrintWriter out = new PrintWriter(file.getAbsoluteFile());
                try {
                    //overrrides current content and updates file
                    out.print(theInput);
                } finally {
                    out.close();
                }
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
            theOutput="You have updated the file to say: "+ theInput;
            theOutput+="    **Would you like to do something else?**";
            update=true;
            state=ASKEDNAME;

        }

            return theOutput;



        }


        public String[]  read()
        {
            // a string array is used in order to pass two versions of the same string, one
            //made for display the other for counting

            //resets line counter
            iHoldLines=0;
            String theOutput[]=new String[2];
            BufferedReader in = null;
            //new reader
            try {
                // new reader beings on wanted file
                in = new BufferedReader(new FileReader(root+fileName));
                String line = in.readLine();
                while(line != null)
                {
                    //places lines in single string
                    theOutput[0]+=line;
                    theOutput[1]+="~!@"+line;
                    //counts lines
                    iHoldLines++;
                    line = in.readLine();
                }
                in.close();
            } catch (FileNotFoundException e) {
                //catch non existent file
                theOutput[0]+="This file did not exist or could not be read";
            } catch (IOException e) {
                e.printStackTrace();
            }


            return theOutput ;
        }

        public String stats()
        {
                String copy = "";
                String[] getR=read();
                String all = getR[0];


                //Counts each character except space
                int cc = 0;
                for (int i = 0; i < all.length(); i++) {
                    if (all.charAt(i) != ' ')
                        cc++;
                }

                //Counts each word
                int wc = 0;
                char ch[] = new char[all.length()];      //Intialize the word Array
                for (int i = 0; i < all.length(); i++) {
                    ch[i] = all.charAt(i);
                    if (((i > 0) && (ch[i] != ' ') && (ch[i - 1] == ' ')) || ((ch[0] != ' ') && (i == 0)))
                        wc++;
                }

                //Counts each line
                int lc;
                if(wc==0&&cc==0) {
                     lc = (iHoldLines-1);
                }
                else
                     lc = (iHoldLines);
                //holds counted information and returns it
                    copy += "For file: " + fileName + " Number of lines is: " + lc+",";
                    copy += "For file: " + fileName + " Number of words is: " + wc+",";
                    copy += "For file: " + fileName + " Number of characters is: " + cc+",";
                    copy += getR[1];


            return copy;
        }

    }
