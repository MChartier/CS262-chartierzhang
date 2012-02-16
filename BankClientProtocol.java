import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;


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
			double balance = ((double) firstParam) / 100;
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
			double bal = ((double) firstParam) / 100;
			output = String.format("Your balance is $%.2f",  bal);
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

	public BankMessage makeMessage() {
    
		Scanner stdIn = new Scanner(System.in);
		int userInput;


		int[] opcodes = {CREATE, DEPOSIT, WITHDRAW, 
				GETBALANCE, CLOSE};

		do {
			userInput = stdIn.nextInt();
		} while(!(userInput >=1 && userInput <= 5));

		int opcode = opcodes[userInput - 1];
		int[] parameters = new int[2];
		
		// create a pattern only accepting positive numbers with at most 2 decimal places
		Pattern dollar = Pattern.compile("^\\$?([1-9]{1}[0-9]{0,2}(\\,[0-9]{3})*(\\.[0-9]{0,2})?|[1-9]{1}[0-9]{0,}(\\.[0-9]{0,2})?|0(\\.[0-9]{0,2})?|(\\.[0-9]{1,2})?)$");
		String input = null;
		
		// populate parameters list
		switch(opcode) {
		case CREATE:
			do {
				System.out.println("How much for initial deposit?");
				input = stdIn.next();
			} 
			while ((dollar.matcher(input).matches()) == false);
			parameters[0] = (int) (Double.parseDouble(input) * 100);

			break;

		case DEPOSIT:
			System.out.println("Which account?");
			parameters[0] = stdIn.nextInt();
			
			do {
				System.out.println("How much to deposit?");
				input = stdIn.next();
			} 
			while ((dollar.matcher(input).matches()) == false);
			parameters[1] = (int) (Double.parseDouble(input) * 100);
			break;

		case WITHDRAW:
			System.out.println("Which account?");
			parameters[0] = stdIn.nextInt();
			
			do {
				System.out.println("How much to withdraw?");
				input = stdIn.next();
			} 
			while ((dollar.matcher(input).matches()) == false);
			parameters[1] = (int) (Double.parseDouble(input) * 100);
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
