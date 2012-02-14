public class BankMessage {
    public int version;
    public int opcode;
    public int packetLength;
    public int messageId;
    public int[] parameters;

    public BankMessage(int version,
                       int opcode,
                       int messageId,
                       int[] parameters) {
	this.version = version;
	this.opcode = opcode;
	this.packetLength = 8 + parameters.length * 4;
	this.messageId = messageId;
	this.parameters = parameters;
    }

    public BankMessage readMessage(DataInputStream input) {
	int version = input.readByte();
	int opcode = input.readByte();
	int packetLength = input.readShort();
	int messageId = input.readInt();

	int numParameters = (packetLength - 8)/4;
	int[] parameters = new int[numParameters];

	for(int i = 0; i < parameters.length; i++) {
	    parameters[i] = input.readInt();
	}

	return new BankMessage(version,
			       opcode,
			       packetLength,
			       messageId,
			       parameters);
    }

    public void writeMessage(DataOutputStream output) {
	output.writeByte(this.version);
	output.writeByte(this.opcode);
	output.writeShort(this.packetLength);
	output.writeInt(this.messageId);
	
	for(int i = 0; i < parameters.length; i++) {
	    output.writeInt(parameters[i]);
	}
    }
}