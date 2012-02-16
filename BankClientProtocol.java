import java.io.*;
import java.net.*;
import java.util.*;

public class BankClientProtocol {
    private int PROTOCOL_VERSION = 1;

    private static final int CREATE = 0x10;
    private static final int DEPOSIT = 0x20;
    private static final int WITHDRAW = 0x30;
    private static final int GETBALANCE = 0x40;
    private static final int CLOSE = 0x50;

    public BankClientProtocol() {
    }

    public String receiveMessage(BankMessage inputMessage) {
	String output = null;

    int opcode = inputMessage.opcode;
	int firstParam = inputMessage.parameters[0];
        double balance;
        
	switch (opcode) {
	case CREATE:
		// SUCCESSFUL CREATE
	    output = "You have created account "+ firstParam;
		break;
	case 0x11:
	    // INVALID INITIAL DEPOSIT
		output = "Error: Your deposit amount was not valid";
	    break;
	case 0x12:
	    // ACCOUNT NUMBERS EXHAUSTED
		output = "Error: Unable to create account";
	    break;
	    
	case DEPOSIT:
	case WITHDRAW:
	    // SUCCESSFUL DEPOSIT OR WITHDRAW - RETURN BALANCE
		balance = (double) firstParam / 100;
		output = String.format("Success: Your balance is now $%.2f",balance);
		break;

	case 0x21:
	case 0x31:
	case 0x41:
	case 0x51:
	    // INVALID ACCOUNT NUMBER
		output = "Error: Your account number was not valid";
	    break;

	case 0x22:
	    // INVALID DEPOSIT AMOUNT
		output = "Error: Your deposit amount was not valid";
	    break;

	case 0x32:
	    // INVALID WITHDRAW AMOUNT
		output = "Error: Your withdrawal amount was not valid";
	    break;

	case GETBALANCE:
		// SUCCESSFUL BALANCE QUERY
        balance = (double) firstParam / 100;
		output = String.format("Your balance is $%.2f",  balance);
		break;
	case 0x42:
		// UNABLE TO GET BALANCE - MISC ERROR
		output = "Error: Unable to get balance";
		break;
	  
	case CLOSE:
		// SUCCESSFUL ACC CLOSE
		output = "Success: You closed account #" + firstParam;
		break;
	case 0x52:
		// UNABLE TO CLOSE - MISC ERROR
		output = "Error: Unable to close account #" + firstParam;
		break;

	default:
        System.out.println("We are sorry, the server is having difficulties.");
        System.exit(2);
	}

	return output;
    }

    public BankMessage makeMessage() 
    		throws InputMismatchException, NoSuchElementException {
    	/*BankMessage output = null;

    	// scan userInput string
    	Scanner sc = new Scanner(userInput);
    	int parameters[] = new int[2];
	parameters[1] = 0;    
	
    	// try to find operation name
    	String opcode = sc.next();

    	if (opcode.equals("create")) {
    		// case CREATE
    		parameters[0] = sc.nextInt();
    		output = buildMessage(0x10, parameters);
    	}
    	else if (opcode.equals("deposit")) {
    		// case DEPOSIT:
    		parameters[0] = sc.nextInt();
    		parameters[1] = (int) sc.nextDouble() * 100;
    		output = buildMessage(0x20, parameters);
    	}
    	else if (opcode.equals("withdraw")) {
    		// case WITHDRAW:
    		parameters[0] = sc.nextInt();
    		parameters[1] = (int) sc.nextDouble() * 100;
    		output = buildMessage(0x30, parameters);
    	}
    	else if (opcode.equals("getbalance")) {
    		// case GETBALANCE:
    		parameters[0] = sc.nextInt();
    		output = buildMessage(0x40, parameters); 
    	}
    	else if (opcode.equals("close")) {
    		// case CLOSE:
    		parameters[0] = sc.nextInt();
    		output = buildMessage(0x50, parameters);
    	}
    	else {
    		throw new InputMismatchException();
    	} */
                Scanner stdIn = new Scanner(System.in);
                int userInput;
                
    
                int[] opcodes = {CREATE, DEPOSIT, WITHDRAW, 
                    GETBALANCE, CLOSE};
                
                BankClient.usage();
                
                do {
                    userInput = stdIn.next();
                } while(!(uInput >=1 && uInput <= 5));
                
                int opcode = opcodes[uInput - 1];
                int[] parameters = new int[2];
                
                // populate parameters list
                switch(opcode) {
                    case CREATE:
                        System.out.println("How much for initial deposit?");
                        parameters[0] = (int) stdIn.nextDouble() * 100;
                        break;
                        
                    case DEPOSIT:
                        System.out.println("Which account?");
                        parameters[0] = stdIn.nextInt();
                        System.out.println("How much?");
                        parameters[1] = (int) stdIn.nextDouble() * 100;
                        break;
                        
                    case WITHDRAW:
                        System.out.println("Which account?");
                        parameters[0] = stdIn.nextInt();
                        System.out.println("How much?");
                        parameters[1] = (int) stdIn.nextDouble() * 100;
                        break;
                        
                    case GETBALANCE:
                        System.out.println("Which account?");
                        parameters[0] = stdIn.nextInt();
                        break;
                        
                    case CLOSE:
                        System.out.println("Which account?");
                        parameters[0] = stdIn.nextInt();
                        break;
                        
                    default:
                        System.out.println("Invalid opcode. System failure.");
                        System.exit(-1);
                }

        
                BankMessage output = buildMessage(opcode, parameters);
                return output;
    }



    private BankMessage buildMessage(int opcode, int parameter[]) {
	int[] parameters = new int[2];
	parameters[0] = parameter[0];
	parameters[1] = parameter[1];

	return new BankMessage(PROTOCOL_VERSION, 
			   opcode, 
			   404, 
			   parameters);
    }    
}
