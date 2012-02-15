import java.io.*;
import java.net.*;
import java.util.*;

public class BankProtocol {
    private int PROTOCOL_VERSION = 1;

    private static final int CREATE = 0x10;
    private static final int DEPOSIT = 0x20;
    private static final int WITHDRAW = 0x30;
    private static final int GETBALANCE = 0x40;
    private static final int CLOSE = 0x50;

    public BankProtocol() {
    }

    public String receiveMessage(BankMessage inputMessage) {
	String output = null;

    int opcode = inputMessage.opcode;
	int firstParam = inputMessage.parameters[0];

	switch (opcode) {
	case CREATE:
		// SUCCESSFUL CREATE
		output = new String 
				"You have created account "+ firstParam;
		break;
	case 0x11:
	    // INVALID INITIAL DEPOSIT
		output = new String 
				"Error: Your deposit amount was not valid";
	    break;
	case 0x12:
	    // ACCOUNT NUMBERS EXHAUSTED
		output = new String
				"Error: Unable to create account";
	    break;
	    
	case DEPOSIT:
	case WITHDRAW:
	    // SUCCESSFUL DEPOSIT OR WITHDRAW - RETURN BALANCE
		double balance = (double) firstParam / 100;
		output = new String.format(
				"Success: Your balance is now $%.2f",balance);
		break;

	case 0x21:
	case 0x31:
	case 0x41:
	case 0x51:
	    // INVALID ACCOUNT NUMBER
		output = new String
				"Error: Your account number was not valid";
	    break;

	case 0x22:
	    // INVALID DEPOSIT AMOUNT
		output = new String
				"Error: Your deposit amount was not valid";
	    break;

	case 0x32:
	    // INVALID WITHDRAW AMOUNT
		output = new String 
				"Error: Your withdrawal amount was not valid";
	    break;

	case GETBALANCE:
		// SUCCESSFUL BALANCE QUERY
		double balance = (double) firstParam / 100;
		output = String.format("Your balance is $%.2f",  balance);
		break;
	case 0x42:
		// UNABLE TO GET BALANCE - MISC ERROR
		output = String
				"Error: Unable to get balance";
		break;
	  
	case CLOSE:
		// SUCCESSFUL ACC CLOSE
		output = new String
				"Success: You closed account #" + firstParam;
		break;
	case 0x52:
		// UNABLE TO CLOSE - MISC ERROR
		output = new String 
				"Error: Unable to close account #" + firstParam;
		break;

	default:
	}

	return output;
    }

    public BankMessage processInput(String userInput) 
    		throws InputMismatchException, NoSuchElementException {
    	BankMessage output = null;

    	// scan userInput string
    	Scanner sc = new Scanner(userInput);
    	Int parameters[];
    	
    	// try to find operation name
    	String opcode = sc.next();

    	if (opcode.equals("create")) {
    		// case CREATE
    		parameters[0] = sc.nextInt();
    		output = buildMessage(0x10, parameters[0]);
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
    		output = buildMessage(0x40, parameters[0]); 
    	    break;
    	}
    	else if (opcode.equals("close")) {
    		// case CLOSE:
    		parameters[0] = sc.nextInt();
    		output = buildMessage(0x50, parameters[0]);
    	}
    	else {
    		throw InputMismatchException;
    	} 
    }


    
    private BankMessage buildMessage(int opcode, int parameter) {
    int[] parameters = new int[1];
    parameters[0] = parameter;

	return new BankMessage(PROTOCOL_VERSION, 
			   opcode, 
			   404, 
			   parameters);
    }


    private BankMessage buildMessage(int opcode, int parameter[]) {
	int[] parameters = new int[1];
	parameters[0] = parameter[0];
	parameters[1] = parameter[1];

	return new BankMessage(PROTOCOL_VERSION, 
			   opcode, 
			   404, 
			   parameters);
    }
    
    
    
}