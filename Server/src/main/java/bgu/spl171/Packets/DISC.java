package bgu.spl171.Packets;

import bgu.spl171.net.api.bidi.ConnectionsImp;

public class DISC extends Packet{

	final int opCode=10;
	
	public DISC() {
	}

	public int getOpCode() {
		return opCode;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void operate(ConnectionsImp c, int connId) {
		ACK a=new ACK(0);
		a.operate(c, connId);
	}

}
