package bgu.spl171.Packets;

import bgu.spl171.net.api.bidi.ConnectionsImp;

public abstract class Packet {
	int opCode;
	@SuppressWarnings("rawtypes")
	public abstract <T> void operate(ConnectionsImp c, int connectionId);
	
	public abstract <T> int getOpCode();
	
}
