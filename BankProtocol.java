import java.io.*;
import java.net.*;

public class BankProtocol {
    private int PROTOCOL_VERSION = 1;

    private static final int CREATE = 0x10;
    private static final int DEPOSIT = 0x20;
    private static final int WITHDRAW = 0x30;
    private static final int GETBALANCE = 0x40;
    private static final int CLOSE = 0x50;

    private static final int MAX_ACCOUNTS = 1024;

    private int[] balance;
    private boolean[] accountState;

    // number of accounts EVER created; does not decrement when account is closed
    // we do not reclaim account numbers
    private int numAccounts;

    public BankProtocol() {
	balance = new int[MAX_ACCOUNTS];
	accountState = new boolean[MAX_ACCOUNTS];
	numAccounts = 0;
    }

    public String processInput(String input) {
	String output = null;

	int opcode;
	int firstParam;
	int secondParam;


	switch (opcode) {
	case CREATE:

	    // INVALID INITIAL DEPOSIT
	    if(firstParam < 0) { 
		output = buildMessage(0x11, {});
	    }

	    // ACCOUNT NUMBERS EXHAUSTED
	    else if(numAccounts >= MAX_ACCOUNTS) {
		output = buildMessage(0x12, {});
	    }

	    // SUCCESSFUL CREATION
	    else {
		accountState[numAccounts] = true;
		balance[numAccounts] = initialDeposit;
		output = buildMessage(0x10, {numAccounts});
		numAccounts++;
	    }
	    break;

	case DEPOSIT:
	    // INVALID ACCOUNT NUMBER
	    if(firstParam < 0 || firstParam >= MAX_ACCOUNTS || !accountState[firstParam]) {
		output = buildMessage(0x21, {});
	    }

	    // INVALID DEPOSIT AMOUNT
	    else if(secondParam < 0) {
		output = buildMessage(0x22, {});
	    }

	    // SUCCESSFUL DEPOSIT
	    else {
		balance[firstParam] += secondParam;
		output = buildMessage(0x20, {balance[firstParam]});
	    }
	    break;

	case WITHDRAW:
	    // INVALID ACCOUNT NUMBER
	    if(firstParam < 0 || firstParam >= MAX_ACCOUNTS || !accountState[firstParam]) {
		output = buildMessage(0x31, {});
	    }

	    // INVALID DEPOSIT AMOUNT
	    else if(secondParam < 0) {
		output = buildMessage(0x32, {});
	    }

	    // SUCCESSFUL DEPOSIT
	    else {
		balance[firstParam] -= secondParam;
		output = buildMessage(0x30, {balance[firstParam]});
	    }
	    break;

	case GETBALANCE:
	    // INVALID ACCOUNT NUMBER
	    if(firstParam < 0 || firstParam >= MAX_ACCOUNTS || !accountState[firstParam]) {
		output = buildMessage(0x41, {});
	    }
	    
	    // SUCCESSFUL BALANCE CHECK
	    else {
		output = buildMessage(0x40, {});
	    }
	    break;

	case CLOSE:
	    // INVALID ACCOUNT NUMBER
	    if(firstParam < 0 || firstParam >= MAX_ACCOUNTS || !accountState[firstParam]) {
		output = buildMessage(0x51, {});
	    }

	    // SUCCESSFUL ACCOUNT CLOSURE
	    else {
		accountState[firstParam] = false;
		output = buildMessage(0x50, {});
	    }

	    break;

	default:
	}

	return output;
    }

    private buildMessage(int opcode, int[] parameters) {
	// message id?
	return BankMessage(version, opcode, 404, parameters);
    }