import java.io.*;
import java.net.*;
import java.util.*;

public class BankClient {
    final static int portNumber = 2048;
	
    public static void main(String[] args) 
	throws IOException, UnknownHostException, Exception {
    
	// check for hostname
	if (args.length != 2) {
	    System.out.println("Usage: java BankClient hostname");
	    System.exit(1);
	}
    
	String hostname = args[1];
    	
	// set up socket for network I/O
	Socket clientSocket = null;
	
	DataOutputStream out = null;
	DataInputStream in = null;

	try {
	    clientSocket = new Socket(hostname, portNumber);
		
	    // establish communication with server
	    out = new DataOutputStream(clientSocket.getOutputStream());
	    in = new DataInputStream(clientSocket.getInputStream());
	} 
	catch (UnknownHostException e) {
	    System.err.println("Invalid hostname");
	    System.exit(1);
	}
	catch (IOException e) {
	    System.err.println("Couldn't get I/O for connection to host");
	    System.exit(-1);
	}

	// display usage
	usage();
	
	// set up command line input
	BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
	String userInput;
	
	// set up protocol to send/receive server messages
	BankClientProtocol bp = new BankClientProtocol();
	BankMessage inputMessage, outputMessage;
	String responseMessage;
	
	// read user input and send to client server
	while((userInput = stdIn.readLine()) != null) {
	    try {
	    	outputMessage = bp.processInput(userInput);
            outputMessage.writeMessage(out);
            inputMessage = BankMessage.readMessage(in);
            responseMessage = bp.receiveMessage(inputMessage);
            System.out.println(responseMessage);
	    }
	    catch (Exception e) {
	    	usage();
	    }

	}
	
	// clean up
	out.close();
	in.close();
	stdIn.close();
	clientSocket.close();
    }	
    
    public static void usage() {
    	System.out.println("Welcome to DistribCo Banking");
        System.out.println("                              )");
        System.out.println("                         )   __    (");
        System.out.println("                        __  (~(    __");
        System.out.println("                       (~(   \\O\\   )~)");
        System.out.println("                        )O)   )_) (O(");
        System.out.println("                       (_(__ (     )_) )");
        System.out.println("                          )~)__      __");
        System.out.println("                         /O/ )~)  ) (~(");
        System.out.println("                        (_( (O(  __  \\O\\\\");
        System.out.println("                        __)  )_)(~(   \\_\\\\");
        System.out.println("                       (~(       )O)   (            ________");
        System.out.println("                 _      \\O\\   __(_(    __        _-'        `-_");
        System.out.println("          ,-----' |    _ )_)<'~_`)   ) )~)      /             \\\\");
        System.out.println("          | //  : |  -'      )^ \\\\  __(O(   ___| MUHAHAHAAH!!!  |");
        System.out.println("          | //  : |   ---    >__;` (~( )_)  `-.  mmWAHAHA!!!!!  |");
        System.out.println("          | //  : |  -._     /\\_\\   \\O\\        \\    YEAH!!!    /");
        System.out.println("          `-----._|     __  /__( \\|  )_)        `--_________--'");
        System.out.println("           _/___\\_    //)_`/( (| ||]");
        System.out.println("     _____[_______]_[~~-_ (.L)O) ||");
        System.out.println("    [____________________]' (_(,/(~(");
        System.out.println("      ||| /          )~)  ,___,'./\\O\\\\");
        System.out.println("      ||| \\         (O(|,'______|( )_)");
        System.out.println("      ||| /          )_) I==||  __");
        System.out.println("      ||| \\       __/_||  __||__)~)");
        System.out.println("  -----||-/------`-._/||-o-_o__(O(--  __");
        System.out.println("    ~~~~~'   ____     __  /_O_/.\\_\\   \\~\\\\");
        System.out.println("_____________________________________________________");
        
    	System.out.println("Usage:");
    	System.out.println("create initial_deposit_amount");
    	System.out.println("getbalance account_number");
    	System.out.println("deposit account_number amount");
    	System.out.println("withdraw account_number amount");
    	System.out.println("close account_number");
    	System.out.println("------------------------------------");
    	System.out.println("Please include all arguments and format amounts in whole dollars and cents (i.e 110.23)");
    	
    }
}
