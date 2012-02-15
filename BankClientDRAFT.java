import java.io.*;
import java.net.*;
import java.util.Scanner;

public class BankClient {
    private final static int PROTOCOL_VERSION = 1;

    private static final int CREATE = 0x10;
    private static final int DEPOSIT = 0x20;
    private static final int WITHDRAW = 0x30;
    private static final int GETBALANCE = 0x40;
    private static final int CLOSE = 0x50;

    public static void main(String[] args) throws IOException {

	// PUT host here
	String host = "";
	int portNumber = 2048;
	Socket clientSocket = null;
	DataOutputStream out = null;
	DataInputStream in = null;

	try {
	    clientSocket = new Socket(host, portNumber);
	    out = new DataOutputStream(clientSocket.getOutputStream());
	    in = new DataInputStream(clientSocket.getInputStream());
	} catch (UnknownHostException e) {
	    System.err.println("Don't know about host: " + host + ".");
	    System.exit(1);
	} catch (IOException e) {
	    System.err.println("Couldn't get I/O for connection to " + host + ".");
	    System.exit(1);
	}

	BankMessage outMessage, inMessage = null;

	while ((outMessage = makeMessage()) != null) {

	    System.out.println("Sending message to server...");
	    outMessage.writeMessage(out);

	    System.out.println("Awaiting reply from server...");
	    inMessage.readMessage(in);

	    System.out.println("Received opcode: " + inMessage.opcode);
	}

	out.close();
	in.close();
	clientSocket.close();
    }

    public static BankMessage makeMessage() {
	Scanner stdIn = new Scanner(System.in);
	int userInput;

	int[] opcodes = {CREATE, DEPOSIT, WITHDRAW, 
			 GETBALANCE, CLOSE};

        System.out.println("1. Create account");
        System.out.println("2. Deposit Money");
        System.out.println("3. Withdraw Money");
        System.out.println("4. Get Balance");
        System.out.println("5. Close account");

	do {
	    userInput = stdIn.nextInt();
	} while(!(userInput >=1 && userInput <= 5));

	int opcode = opcodes[userInput - 1];
	int[] parameters = null;

	// TODO populate parameters list
	switch(opcode) {
	case CREATE:
	    parameters = null;
	    break;

	case DEPOSIT:
	    parameters = new int[2];
	    System.out.println("Which account?");
	    parameters[0] = stdIn.nextInt();
	    System.out.println("How much?");
	    parameters[1] = stdIn.nextInt();
	    break;

	case WITHDRAW:
	    parameters = new int[2];
	    System.out.println("Which account?");
	    parameters[0] = stdIn.nextInt();
	    System.out.println("How much?");
	    parameters[1] = stdIn.nextInt();
	    break;

	case GETBALANCE:
	    parameters = new int[1];
	    System.out.println("Which account?");
	    parameters[0] = stdIn.nextInt();
	    break;

	case CLOSE:
	    parameters = new int[1];
	    System.out.println("Which account?");
	    parameters[0] = stdIn.nextInt();
	    break;

	default:
	    System.out.println("Invalid opcode. System failure.");
	    System.exit(-1);
	}

	return new BankMessage(PROTOCOL_VERSION, 
			       opcode, 
			       404, 
			       parameters);
    }
}