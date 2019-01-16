package bgu.spl171.Packets;

import bgu.spl171.net.api.bidi.ConnectionsImp;
import bgu.spl171.net.srv.ConnectionHandler;

public class ACK <T> extends Packet{

	final int opCode=4;
	int blockNum;
	
	public ACK(int num) {
		this.blockNum=num;
	}
	public int getOpCode() {
		return opCode;
	}
	public int getblockNum(){
		return blockNum;
	}
	public String toString(){
		return "ACK "+blockNum;
	}
	@SuppressWarnings({ "hiding", "rawtypes", "unchecked" })
	@Override
	public <Packet> void operate(ConnectionsImp c, int connId) {
		((ConnectionHandler)c.getMyHashy().get(connId)).send(new ACK(blockNum));
	}
	
}
