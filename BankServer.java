import java.io.*;
import java.net.*;

public class BankServer {
    final static int portNumber = 401;

    public static void main(String[] args) throws IOException {

	// listen on socket for network I/O
	ServerSocket serverSocket = null;
	try {
	    serverSocket = new ServerSocket(portNumber);
	} catch (IOException e) {
	    System.err.println("List on port: " + portNumber + " failed.");
	    System.exit(-1);
	}

	// wait for a client connection
	Socket clientSocket = null;
	try {
	    clientSocket = serverSocket.accept();
	}
	catch (IOException e) {
	    System.out.println("Accept failed: " + portNumber);
	    System.exit(-1);
	}

	// establish communication with client
	PritnWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
	BufferedReader in = 
	    new BufferedReader(
	    new InputStreamReader(
            clientSocket.getInputStream()));
	String inputLine, outputLine = null;

	// initiate conversation with client (one at a time for now)
	BankProtocol bp = new BankProtocol();

	do {
	    outputLine = bp.processInput(inputLine);
	    out.println(outputLine);

	    // if(outputLine.equals("Bye."))
	    // 	break;

	} while((inputLine = in.readLine()) != null);
    }
}